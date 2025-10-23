from typing import TypedDict, Optional, Literal

INTENTS_t = Literal["current page", "cart", "product", "vendor"]

class UserState(TypedDict):
    user_id: str
    session_id: str
    
class ReasoningState(TypedDict, total=False):
    prompt: str
    intent: Optional[INTENTS_t]
    context_summary: Optional[str]
    
class MainState(UserState, ReasoningState):
    answer: Optional[str]
    error: Optional[str]

