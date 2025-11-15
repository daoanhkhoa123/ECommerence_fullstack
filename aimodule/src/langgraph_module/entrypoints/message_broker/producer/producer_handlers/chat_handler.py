import logging
from fastapi import FastAPI
from src.allocation.adapters.persistence.sqlalchemy_unit_of_work import SqlAlchemyUnitOfWork
from src.allocation.adapters.message_bus.broker.dispatcher import register_topic
from src.langgraph_module.entrypoints.message_broker.producer import kafka_producer
from src.langgraph_module.entrypoints.message_broker.producer.schemas.chat_message_event import ChatMessageEvent
from src.langgraph_module.services.chat_service import build_graph, handle_user_message

logger = logging.getLogger(__name__)
chat_graph = build_graph()


@register_topic("chat.message.user.v1")
async def handle_chat_message(event: dict, app: FastAPI | None = None):
    """
    Handle chat message events coming from Kafka.
    Process the user message and produce a system reply back to Kafka.
    """
    try:
        # Validate and parse event payload using DTO (with aliases)
        data = ChatMessageEvent(**event)

        # Create a fresh Unit of Work for this message
        with SqlAlchemyUnitOfWork() as uow:
            response_text = handle_user_message(
                user_id=data.account_id,
                message=data.message,
                graph=chat_graph,
                uow=uow,
            )
            uow.commit()

        # Produce reply back to Kafka as SYSTEM role
        payload = {
            "accountId": data.account_id,
            "message": response_text,
            "role": "SYSTEM",
        }

        kafka_producer.send(
            topic="chat.message.system.v1",
            value=payload,
        )

        logger.info(f"[Kafka] Sent system message: {payload}")

    except Exception as e:
        if app and hasattr(app, "logger"):
            app.logger.exception("Error handling chat message")  # type: ignore
        else:
            logger.exception(f"[handle_chat_message] Error: {e}")
