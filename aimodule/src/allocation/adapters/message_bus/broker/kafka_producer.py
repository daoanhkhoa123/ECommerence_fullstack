import json

from confluent_kafka import Producer
from src.configs.settings import KafkaSettings

settings = KafkaSettings()  # type: ignore

class KafkaProducer:
    def __init__(self):
        self.producer = Producer({
            "bootstrap.servers": settings.kafka_url,
            "retries": settings.kafka_retries,
            "retry.backoff.ms": settings.kafka_retry_backoff_ms,
            "acks": settings.kafka_acks
        })

    def _delivery_report(self, err, msg):
        if err:
            print(f"[Kafka] Message delivery failed: {err}")
        else:
            print(f"[Kafka] Message delivered to {msg.topic()} [{msg.partition()}]")

    def send(self, topic: str, value: dict):
        """
        Produce a message to Kafka.
        value: dict that will be JSON-serialized
        """
        self.producer.produce(
            topic=topic,
            value=json.dumps(value).encode("utf-8"),
            callback=self._delivery_report
        )
        self.producer.flush()


# Optional convenience function if you prefer using functions instead of class
def create_kafka_producer() -> KafkaProducer:
    return KafkaProducer()


def produce_message(producer: KafkaProducer, topic: str, value: dict):
    producer.send(topic, value)
