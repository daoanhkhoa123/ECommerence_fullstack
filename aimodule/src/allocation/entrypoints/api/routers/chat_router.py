from fastapi import APIRouter, Depends
from contextlib import contextmanager

from src.allocation.entrypoints.api.schemas.chat_message_request import ChatMessageRequest
from src.allocation.services.chat_service import build_graph, handle_user_message
from src.allocation.adapters.persistence.sqlalchemy_unit_of_work import SqlAlchemyUnitOfWork

router = APIRouter(prefix="/chat", tags=["chat"])

graph = build_graph()

def get_uow():
    with SqlAlchemyUnitOfWork() as uow:
        yield uow

@router.post("/", summary="Send a chat message")
def send_message(
    request: ChatMessageRequest,
    uow: SqlAlchemyUnitOfWork = Depends(get_uow)
):
    reply = handle_user_message(request.account_id, request.message, graph, uow)
    return {
        "status": "success",
        "user_message": request.message,
        "reply": reply,
    }

@router.get("/{account_id}", summary="Get all chat messages for a user")
def get_messages(
    account_id: int,
    uow: SqlAlchemyUnitOfWork = Depends(get_uow)
):
    messages = uow.chat_messages.get_by_user_id(account_id)
    return {
        "account_id": account_id,
        "messages": [
            {"role": m.role, "content": m.content, "created_at": m.created_at}
            for m in messages
        ],
    }
