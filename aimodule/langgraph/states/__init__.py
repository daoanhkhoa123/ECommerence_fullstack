from typing import TypedDict, Optional, Annotated, Literal, List, Dict

INTENTS_t = Literal["Current Page", "User's Cart", "Specific Product"]

class MainState(TypedDict, total=False):
    prompt: Annotated[str, "The latest message from the user"]
    intent: Annotated[INTENTS_t, "Detected intent of the user: Current Page, User's Cart, or Specific Product"]
    query: Annotated[Optional[str], "Refined or processed query extracted from the user's prompt"]
    fetched_data: Annotated[Optional[str], "Data retrieved from the selected source based on the intent"]
    answer: Annotated[Optional[str], "The final response generated for the user"]
    chat_history: Annotated[Optional[List[Dict[str, str]]], 
                            "List of previous chat messages, each with 'role' (user/assistant) and 'content'"]
