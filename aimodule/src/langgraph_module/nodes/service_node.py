from src.langgraph_module.nodes.decorator import ContextSchema, inject_context
from src.langgraph_module.services import similarity_search_service
from src.langgraph_module.states.output_state import OutputState
from src.langgraph_module.states.prep_state import PrepState


@inject_context
def answer_by_data(state:PrepState, context: ContextSchema) -> OutputState:
    data = similarity_search_service.get_product_by_similarity(
        str(state["data_cache"]), context.uow)

    prompt = f"""
        You are a helpful assistant. Use the following product data to answer the user query.

        Product data:
        {data}

        User query:
        {state['rewrited_prompt']}

        Instructions:
        - Ground your answer in the product data above.
        - Be clear, concise, and directly address the query.
        - If the data is insufficient, say so explicitly instead of guessing.
        - Provide the answer in natural language suitable for the user.
    """

    answer = context.dry_llm
    (prompt)
    return {"answer": answer}