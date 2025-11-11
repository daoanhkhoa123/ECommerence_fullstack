import asyncio
from contextlib import asynccontextmanager
from fastapi import FastAPI

from src.allocation.entrypoints.message_broker.loader import load_all_handlers
from src.allocation.entrypoints.message_broker.dispatcher import list_registered_topics
from src.allocation.adapters.message_bus.broker.kafka_consumer import (
    consume_forever, create_kafka_consumer
)
from src.allocation.adapters.message_bus.broker.kafka_producer import (
    create_kafka_producer
)


@asynccontextmanager
async def lifespan(app: FastAPI):
    load_all_handlers()
    print(f"[Kafka] Topics registered: {list_registered_topics()}")

    consumer = create_kafka_consumer(topics=list_registered_topics())
    asyncio.create_task(consume_forever(consumer))

    app.state.kafka_producer = create_kafka_producer()
    yield
