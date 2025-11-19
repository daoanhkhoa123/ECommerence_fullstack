import json
import logging

from confluent_kafka import KafkaException, Producer

from src.configs.settings import KafkaSettings

logger = logging.getLogger(__name__)
settings = KafkaSettings()  # type: ignore

class KafkaProducer:
    def __init__(self):
        try:
            self.producer = Producer({
                "bootstrap.servers": settings.kafka_url,
                "retries": settings.kafka_retries,
                "retry.backoff.ms": settings.kafka_retry_backoff_ms,
                "acks": settings.kafka_acks
            })
            self.producer.list_topics(timeout=5.0)  # synchronous broker check
        except KafkaException as e:
            logger.error(f"Kafka broker unavailable, producer not created: {e}")
            self.producer = None

    def _delivery_report(self, err, msg):
        if err:
            logger.error(f"[Kafka] Message delivery failed: {err}")
        else:
            logger.info(f"[Kafka] Message delivered to {msg.topic()} [{msg.partition()}]")

    def send(self, topic: str, value: dict):
        if not self.producer:
            logger.warning(f"Cannot send message; Kafka producer not available for topic {topic}")
            return
        try:
            self.producer.produce(
                topic=topic,
                value=json.dumps(value).encode("utf-8"),
                callback=self._delivery_report
            )
            self.producer.flush()
            logger.debug(f"Flushed Kafka producer after sending message to topic: {topic}")
        except Exception as e:
            logger.exception(f"Failed to send message to Kafka topic '{topic}': {e}")

def create_kafka_producer() -> KafkaProducer:
    return KafkaProducer()

def produce_message(producer: KafkaProducer, topic: str, value: dict):
    producer.send(topic, value)
