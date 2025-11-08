import json
from confluent_kafka import Producer
from typing import Any

class KafkaProducer:
    def __init__(self, bootstrap_servers: str):
        self.producer = Producer({"bootstrap.servers": bootstrap_servers})

    def _delivery_report(self, err, msg):
        if err is not None:
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
            callback=self._delivery_report,
        )
        self.producer.flush()

def create_kafka_producer(bootstrap_servers: str = "localhost:9092") -> Producer:
    return Producer({"bootstrap.servers": bootstrap_servers})

def produce_message(producer: Producer, topic: str, value: dict):
    def delivery_report(err, msg):
        if err is not None:
            print(f"[Kafka] Message delivery failed: {err}")
        else:
            print(f"[Kafka] Message delivered to {msg.topic()} [{msg.partition()}]")

    producer.produce(topic, value=json.dumps(value).encode("utf-8"), callback=delivery_report)
    producer.flush()