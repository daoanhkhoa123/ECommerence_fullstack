import json
import asyncio
from confluent_kafka import Consumer, KafkaError
from src.allocation.entrypoints.consumer.dispatcher import get_handler_for_topic


def create_kafka_consumer(bootstrap_servers: str, group_id: str, topics: list[str]) -> Consumer:
    """
    Create and configure a Kafka consumer.
    """
    config = {
        "bootstrap.servers": bootstrap_servers,
        "group.id": group_id,
        "auto.offset.reset": "earliest",
        "enable.auto.commit": True,
    }
    consumer = Consumer(config)
    consumer.subscribe(topics)
    return consumer


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
                    print(f"[Kafka Error] {msg.error()}")
                continue

            try:
                topic = msg.topic()
                handler = get_handler_for_topic(topic)
                if not handler:
                    print(f"[WARN] No handler registered for topic: {topic}")
                    continue

                event = json.loads(msg.value().decode("utf-8"))
                await handler(event)
            except Exception as e:
                print(f"[Error] Failed to process message from {msg.topic()}: {e}")
    except asyncio.CancelledError:
        print("[Kafka Consumer] Shutting down gracefully...")
    finally:
        consumer.close()
