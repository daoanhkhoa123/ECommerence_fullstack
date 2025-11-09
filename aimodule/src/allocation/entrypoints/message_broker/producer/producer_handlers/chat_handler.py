from fastapi import FastAPI
from src.allocation.adapters.persistence.sqlalchemy_unit_of_work import \
    SqlAlchemyUnitOfWork
from src.allocation.entrypoints.message_broker.build_graph import \
    build_chat_graph
from src.allocation.entrypoints.message_broker.dispatcher import register_topic
from src.allocation.entrypoints.message_broker.producer import kafka_producer
from src.allocation.entrypoints.message_broker.producer.schemas.chat_message_event import \
    ChatMessageEvent
from src.allocation.services.chat_service import handle_user_message

chat_graph = build_chat_graph()

@register_topic("chat.message.v1")
async def handle_chat_message(event: dict, app: FastAPI | None = None):
    data = ChatMessageEvent(**event)

    uow = SqlAlchemyUnitOfWork()

    response_text = handle_user_message(
        user_id=data.account_id,
        message=data.message,
        graph=chat_graph,
        uow=uow,
    )

    kafka_producer.send(
        topic="chat.message.v1",
        value={
            "account_id": data.account_id,
            "message": response_text,
            "role": "SYSTEM",
        },
    )
