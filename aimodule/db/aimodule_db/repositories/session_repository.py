from typing import Any, Dict, Sequence, Tuple
from db.aimodule_db import Repository
from db.aimodule_db.entities import ChatSessionsColumns


class SessionRepository(Repository):
    def __init__(self) -> None:
        super().__init__("chat_sessions")

    def find_by_accountid(self, account_id: int) -> Sequence[Tuple]:
        return super().find_by_filters({"account_id": account_id})

    def find_by_session_id(self, session_id: str) -> Sequence[Tuple]:
        return super().find_by_filters({"session_id": session_id})

    def create(self, data: Dict[ChatSessionsColumns, Any]) -> None: # type: ignore
        super().create(data) # type: ignore

    def update( # type: ignore
        self,
        filters: Dict[ChatSessionsColumns, Any],
        updates: Dict[ChatSessionsColumns, Any],
    ) -> None:
        super().update(filters, updates) # type: ignore

    def delete(self, filters: Dict[ChatSessionsColumns, Any]) -> None: # type: ignore
        super().delete(filters) # type: ignore
