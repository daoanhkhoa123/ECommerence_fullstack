from db.backend_db.entities import AccountsColumns ,CustomersColumns, VendorsColumns
from typing import Literal

ChatSessionsColumns = Literal[
    "session_id",
    "account_id",
    "context_summary",
    "intent"
]

ChatHistoryColumns = Literal[
    "message_id",
    "session_id",
    "role",
    "message",
    "created_at"
]