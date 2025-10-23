from typing import Dict, get_args
from langgraph_ai.states import MainState, INTENTS_t
from langgraph_ai.llms import gemini
from langgraph_ai.ultils import text_ultil

def intent_classifier(state: MainState) -> Dict[str, INTENTS_t]:
    if "cart" in state["prompt"].lower():
        return {"intent": "cart"}
    if "page" in state["prompt"].lower():
        return {"intent": "current page"}
    if "product" in state["prompt"].lower():
        return {"intent": "product"}
    if "vendor"in state["prompt"].lower() or "shop" in state["prompt"].lower() :
        return {"intent": "vendor"}

    context = f"Previous chat context: {state.get('context_summary')}\n" if state.get("context_summary") else ""

    prompt = f"""
    You are an assistant that determines the user's intent based on their input and previous conversation context if available. The possible intents are:

    1. "Current Page" — the user wants information about the current page.
    2. "User's Cart" — the user is asking about items in their shopping cart.
    3. "Specific Product" — the user wants details about a specific product.

    {context}
    Analyze the following user message and classify it into exactly one of these intents. Respond only in these tags:
    Tags: {list(get_args(INTENTS_t))}

    User message:
    "{state['prompt']}"
    """

    ans = gemini.generate_text(prompt).strip().lower()
    intent = min(list(get_args(INTENTS_t)), key=lambda x: text_ultil.edit_distance(ans, x))
    return {"intent": intent}


# state: MainState = {
#     "user_id": "user_1",
#     "session_id": "sess_1",
#     "prompt": "how much cost is my inventory",
#     "context_summary": "User previously asked about adding items to cart"
# }

# print(intent_classifier(state))
