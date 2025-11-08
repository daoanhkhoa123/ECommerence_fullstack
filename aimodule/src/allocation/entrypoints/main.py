import asyncio
from contextlib import asynccontextmanager
from fastapi import FastAPI
from confluent_kafka import Producer
from src.allocation.adapters.message_bus.broker.kafka_consumer import (
    create_kafka_consumer,
    consume_forever,
)
from src.allocation.adapters.message_bus.broker.kafka_producer import (
    create_kafka_producer,
)
from src.allocation.entrypoints.message_broker.dispatcher import list_registered_topics
import json

@asynccontextmanager
async def lifespan(app: FastAPI):
    topics = list_registered_topics()
    consumer = create_kafka_consumer(
        bootstrap_servers="localhost:9092",
        group_id="ecommerce-group",
        topics=topics,
    )
    asyncio.create_task(consume_forever(consumer))

    app.state.kafka_producer = create_kafka_producer("localhost:9092")

    yield

app = FastAPI(title="E-Commerce API", lifespan=lifespan)

@app.get("/")
def root():
    return {"message": "FastAPI + Kafka consumer running!"}
