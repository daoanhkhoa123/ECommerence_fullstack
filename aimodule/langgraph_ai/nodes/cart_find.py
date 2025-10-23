from typing import Dict
from config.logging import logging
from langgraph_ai.services.order_service import OrderService
from langgraph_ai.states import MainState
from langgraph_ai.llms import gemini

order_service = OrderService()

def cart_find(state: MainState) -> Dict:
    orders = order_service.find_orders_by_customer(state["user_id"]) # type: ignore
    logging.info(f"Loaded {len(orders)} orders from user {state["user_id"]}")
    if not orders:
        return {"answer": "Your cart is empty.", "total": 0}

    total = _calculate_total_money(orders)
    answer = _tell_bought_product(orders, total)
    return {"answer": answer}


def _tell_bought_product(orders, total: float) -> str:
    items_summary = []
    for order in orders:
        if order["order_status"] == "pending":
            order_items = order_service.find_order_items_with_product_vendor(order["order_id"])
            for item in order_items:
                items_summary.append(
                    f"{item['oi.quantity']} x {item['p.name']} (brand: {item['p.brand']}, category: {item['p.category']}) "
                    f"from vendor {item['v.shop_name']} ({item['v.description']}), subtotal ${item['oi.subtotal']}"
                )
    if not items_summary:
        return "You have no products in your cart."
    
    prompt = f"""
        You are an assistant that summarizes a user's shopping cart.
        The user has previously interacted with the system and added items to their cart.
        Here are the details of the items in the cart:
        {', '.join(items_summary)}

        The total amount for all pending orders is: ${total:.2f}.
        Write an informative and friendly summary for the user, highlighting:
        - Each product's name, brand, and category
        - Vendor names and descriptions
        - The total cost of all items

        Additionally, provide helpful recommendations for the user:
        - Suggest similar products they may like
        - Recommend items from the same category or top vendors
        - Give tips or reminders to improve their shopping experience

        Keep the tone friendly, concise, and personalized to the user's interests.
        """

    return gemini.generate_text(prompt).strip()


def _calculate_total_money(orders) -> float:
    total = 0
    for order in orders:
        if order["order_status"] == "pending":
            order_items = order_service.find_order_items_by_order(order["order_id"])
            for item in order_items:
                total += float(item["subtotal"])
    return total

if __name__ == "__main__":
    state: MainState = {
        "user_id": "1",
        "prompt": "Show me my cart",
        "context_summary": "User previously added items to cart"
    }

    result = cart_find(state)

    # Print the output
    print("Answer:", result["answer"])