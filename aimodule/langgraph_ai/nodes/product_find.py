from typing import Dict
from config.logging import logging
from langgraph_ai.services.product_service import ProductService
from langgraph_ai.states import MainState
from langgraph_ai.llms import gemini
from langgraph_ai.ultils import text_ultil


product_service = ProductService()
def product_find(state:MainState) -> Dict:
    products = product_service.find_product_name_description_brand_category()
    info = _extract_product_info(state["prompt"], str(products[:2]))
    
    best_k_product_idx = text_ultil.top_k_matches_editdistance(info, products, 10) # type: ignore
    best_k_ids = [p["product_id"] for p in [products[i] for i in best_k_product_idx]]
    logging.info(f"Retrived {len(best_k_ids)} products: {[p["name"] for p in [products[i] for i in best_k_product_idx]]}")
    extra_k_infos = product_service.find_product_name_description_brand_category_price_vendor_vendor_description_by_productids(best_k_ids)
    ans = _generate_product_answer(state["prompt"], str(extra_k_infos))
    return {"answer":ans}

def _extract_product_info(prompt: str, sample_product: str) -> str:
    prompt = f"""
    You are an assistant that extracts product-related information from user messages.
    Use the sample product as a reference to understand the kind of product details to extract.
    Extract the relevant product name, category, or description from the following user input.
    If there is no product mentioned, respond with your understanding.

    Sample product: "{sample_product}"

    User message:
    "{prompt}"
    """
    return prompt

    
def _generate_product_answer(user_prompt: str, products: str) -> str:
    prompt = f"""
    You are an assistant that provides information about products based on the user's query.
    The user asked: "{user_prompt}"
    You found these top matching products: {products}
    Write a concise and helpful answer to the user mentioning the most relevant products.
    """
    return gemini.generate_text(prompt).strip()


# state = {"prompt": "I want to buy a powerful laptop"}
# print(product_find(state))