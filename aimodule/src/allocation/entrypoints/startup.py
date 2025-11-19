import asyncio
import logging
from contextlib import asynccontextmanager

from fastapi import FastAPI

from src.allocation.adapters.message_bus.broker.dispatcher import \
    list_registered_topics
from src.allocation.adapters.message_bus.broker.kafka_consumer import (
    consume_forever, create_kafka_consumer_with_retries)
from src.allocation.adapters.message_bus.broker.kafka_producer import \
    create_kafka_producer
from src.allocation.entrypoints.message_broker.loader import load_all_handlers

logger = logging.getLogger(__name__)

@asynccontextmanager
async def lifespan(app: FastAPI):
    load_all_handlers()
    logger.info(f"[Kafka] Topics registered: {list_registered_topics()}")

    consumer = create_kafka_consumer_with_retries(topics=list_registered_topics())
    if consumer:
        asyncio.create_task(consume_forever(consumer))

    app.state.kafka_producer = create_kafka_producer()
    yield
