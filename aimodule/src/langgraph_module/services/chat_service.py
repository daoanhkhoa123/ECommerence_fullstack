from datetime import datetime

from langgraph.graph.state import CompiledStateGraph

from src.langgraph_module.adapters.persistence.sqlalchemy_unit_of_work import \
    SqlAlchemyUnitOfWork
from src.langgraph_module.domain.entities.chat_message import ChatMessage
from src.langgraph_module.states.input_state import InputState

# def build_graph() -> StateGraph:
#     llm = GoogleLLM()

#     def llm_node(state: dict) -> dict:
#         messages = [ChatMessage(role=m["role"], content=m["content"]) for m in state["messages"]]

#         # Build prompt: combine all messages into a single string
#         prompt_lines = []
#         for msg in messages:
#             if msg.role == "USER":
#                 prompt_lines.append(f"User: {msg.content}")
#             else:
#                 prompt_lines.append(f"System: {msg.content}")

#         prompt_lines.append("System:")  # indicate the model should respond next
#         prompt = "\n".join(prompt_lines)

#         reply = llm(prompt)  # type: ignore # call the LLM with the prompt

#         state["messages"].append({"role": "SYSTEM", "content": reply})
#         return state

#     graph = StateGraph(state_schema=dict) # type: ignore
#     graph.add_node("llm", llm_node) # type: ignore
#     graph.set_entry_point("llm")
#     graph.add_edge("llm", END)
#     return graph.compile() # type: ignore

def save_message(user_id: int, chat_message:ChatMessage, uow:SqlAlchemyUnitOfWork) -> ChatMessage:
    uow.chat_messages.add_message(user_id, chat_message)
    return chat_message

def handle_user_message(user_id: int, message: str, graph: CompiledStateGraph, uow: SqlAlchemyUnitOfWork) -> ChatMessage:
    user_message = ChatMessage(message, "USER", datetime.now())
    save_message(user_id, user_message, uow)

    input_state :InputState = {"user_id": user_id, "user_prompt":message}
    reply = graph.invoke(input_state) # type: ignore
    
    system_message = ChatMessage(reply["answer"], "SYSTEM", datetime.now())
    save_message(user_id, system_message, uow)

    uow.commit()
    return system_message
