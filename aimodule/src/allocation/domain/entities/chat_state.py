from dataclasses import dataclass, field
from typing import List

from src.allocation.domain.entities.chat_message import ChatMessage, CHAT_ROLE

@dataclass
class ChatState:
    user_id: int
    current_node_id: str = "START"
    history: List[ChatMessage] = field(default_factory=list)

    def to_langgraph_state(self) -> dict:
        # convert ChatMessage objects to dicts for LangGraph
        return {
            "user_id": self.user_id,
            "messages": [{"role": m.role, "content": m.content} for m in self.history],
            "current_node_id": self.current_node_id
        }

    def from_langgraph_state(self, state: dict):
        self.current_node_id = state.get("current_node_id", "START")
        self.history = [
            ChatMessage(role=m["role"], content=m["content"])
            for m in state.get("messages", [])
        ]

    def append_message(self, role: CHAT_ROLE, content: str):
        self.history.append(ChatMessage(role=role, content=content))
