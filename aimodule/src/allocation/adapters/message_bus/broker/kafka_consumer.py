import asyncio
import json
import logging

from confluent_kafka import Consumer, KafkaError

from src.allocation.adapters.message_bus.broker.dispatcher import \
    get_handler_for_topic
import time
from src.configs.settings import KafkaSettings

logger = logging.getLogger(__name__)

settings = KafkaSettings()  # type: ignore


def _create_kafka_consumer(topics: list[str]) -> Consumer:
    """
    Create and configure a Kafka consumer with fixed group ID from settings.
    """
    config = {
        "bootstrap.servers": settings.kafka_url,
        "group.id": settings.kafka_group,
        "auto.offset.reset": "earliest",
        "enable.auto.commit": True,
    }
    consumer = Consumer(config)
    consumer.subscribe(topics)
    return consumer

def create_kafka_consumer_with_retries(topics: list[str], max_retries: int = 3, retry_interval: float = 2.0) -> Consumer | None:
    for attempt in range(1, max_retries + 1):
        try:
            consumer = _create_kafka_consumer(topics) 
            consumer.list_topics(timeout=5.0)  
            return consumer
        except Exception as e:
            logger.error(f"Kafka connection attempt {attempt}/{max_retries} failed: {e}")
            consumer.close() if 'consumer' in locals() else None # type: ignore
            if attempt < max_retries:
                time.sleep(retry_interval)
            else:
                logger.error("Failed to connect to Kafka after max retries")
                return None
            

async def consume_forever(consumer: Consumer, poll_interval: float = 1.0):
    """
    Asynchronous Kafka consumer loop that dispatches messages to the correct handler
    based on their topic.
    """

    try:
        while True:
            msg = consumer.poll(timeout=poll_interval)
            if msg is None:
                await asyncio.sleep(0)
                continue

            if msg.error():
                if msg.error().code() != KafkaError._PARTITION_EOF:
                    logger.error(f"[Kafka Error] {msg.error()}")
                continue

            try:
                topic = msg.topic()
                handler = get_handler_for_topic(topic)
                if not handler:
                    logger.warning(f"No handler registered for topic: {topic}")
                    continue

                event = json.loads(msg.value().decode("utf-8"))
                await handler(event)
            except Exception as e:
                logger.exception(f"Failed to process message from {msg.topic()}: {e}")
    except asyncio.CancelledError:
        logger.info("Kafka Consumer shutting down gracefully...")
    finally:
        consumer.close()
