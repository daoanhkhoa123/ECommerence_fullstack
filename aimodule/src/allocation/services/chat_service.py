from datetime import datetime
from typing import Optional
from langgraph.graph import StateGraph, END
from src.allocation.adapters.llm.google_llm import GoogleLLM
from src.allocation.domain.entities.chat_message import ChatMessage
from src.allocation.adapters.persistence.sqlalchemy_unit_of_work import SqlAlchemyUnitOfWork
from src.allocation.services.account_service import find_user_by_account_id


def build_graph() -> StateGraph:
    llm = GoogleLLM()

    def llm_node(state: dict) -> dict:
        messages = [ChatMessage(role=m["role"], content=m["content"]) for m in state["messages"]]

        # Build prompt: combine all messages into a single string
        prompt_lines = []
        for msg in messages:
            if msg.role == "USER":
                prompt_lines.append(f"User: {msg.content}")
            else:
                prompt_lines.append(f"System: {msg.content}")

        prompt_lines.append("System:")  # indicate the model should respond next
        prompt = "\n".join(prompt_lines)

        reply = llm(prompt)  # type: ignore # call the LLM with the prompt

        state["messages"].append({"role": "SYSTEM", "content": reply})
        return state

    graph = StateGraph(state_schema=dict) # type: ignore
    graph.add_node("llm", llm_node) # type: ignore
    graph.set_entry_point("llm")
    graph.add_edge("llm", END)
    return graph.compile() # type: ignore


def handle_user_message(user_id: int, message: str, graph: StateGraph, uow: SqlAlchemyUnitOfWork) -> Optional[str]:
    history = uow.chat_messages.get_by_user_id(user_id)

    user_info = str(find_user_by_account_id(user_id, uow))
    message = f"This is user information: \n{user_info}"

    user_msg = ChatMessage(role="USER", content=message, created_at=datetime.utcnow())
    history.append(user_msg)
    uow.chat_messages.add_message(user_id, user_msg)

    # Build state dict for the graph
    state_dict = {"messages": [{"role": m.role, "content": m.content} for m in history]}
    result_state = graph.invoke(state_dict)  # type: ignore

    bot_msgs = [m for m in result_state["messages"] if m["role"] == "SYSTEM"]
    bot_reply = bot_msgs[-1]["content"] if bot_msgs else None

    if bot_reply:
        bot_msg = ChatMessage(role="SYSTEM", content=bot_reply, created_at=datetime.utcnow())
        uow.chat_messages.add_message(user_id, bot_msg)

    uow.commit()
    return bot_reply
