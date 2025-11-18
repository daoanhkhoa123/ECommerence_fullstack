from src.langgraph_module.nodes.decorator import ContextSchema, inject_context
from src.langgraph_module.states.input_state import InputState
from src.langgraph_module.states.prep_state import INTENT_DICT, PrepState


@inject_context
def rewrite_by_history(state: InputState, context:ContextSchema) -> PrepState:
    with context.uow as uow:
        history = uow.chat_messages.get_last_k_by_user_id(state["user_id"])

    prompt = f"""
    You are an AI assistant. Rewrite the following user prompt based on the user's previous chat history.
        
    User prompt: "{state['user_prompt']}"
    Chat history: {history}

    Provide a concise, clear, and improved version of the user prompt.
    """

    rewrite_prompt = context.creative_llm(prompt)

    return {"rewrited_prompt": rewrite_prompt.strip()}  # type: ignore

@inject_context
def intent_classifier(state: PrepState, context:ContextSchema) -> PrepState:
    prompt = f"""
    You are an AI assistant. Determine the user's intent based on the following prompt
    and a dictionary of possible intents.

    User prompt: "{state['rewrited_prompt']}"
    Intent dictionary: {INTENT_DICT}

    Return only the *key* of the intent that best matches the user prompt.
    """

    intent = context.creative_llm(prompt)

    intent = intent.strip().replace('"', '')

    return {"intent": intent}  # type: ignore


@inject_context
def rewrite_by_intent(state: PrepState, context:ContextSchema) -> PrepState:
    # Select dataform + instructions
    if state["intent"] == "Search Product By Product Information":
        dataform = {
            "product categories": None,
            "product price": None,
            "product name": None,
            "product description": None,
        }
        instruction = "Extract product info based on the user's product details or preferences."

    elif state["intent"] == "Search Product By Vendor Information":
        dataform = {
            "vendor name": None,
            "vendor description": None,
        }
        instruction = "Extract product info that belongs to the specified vendor in the user prompt."

    else:
        dataform = {}
        instruction = "No structured data to extract. Treat the user prompt as a normal chat message."


    prompt = f"""
    You are an assistant that reads a user's prompt and fills a structured Product data form.
    Instruction: {instruction}

    User prompt: "{state['user_prompt']}"
    Current dataform template: {dataform}

    Return ONLY the completed dataform as a valid JSON-like dict (Python dict format).
    """

    llm_output = context.creative_llm(prompt)

    return {
        "data_cache": llm_output.strip()
    }  # type: ignore
