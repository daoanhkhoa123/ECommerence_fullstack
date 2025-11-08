from langgraph.graph import StateGraph  
from src.allocation.domain.entities.chat_state import ChatState
from src.allocation.adapters.persistence.sqlalchemy_unit_of_work import SqlAlchemyUnitOfWork
from typing import Any

def handle_user_message(
    user_id: int,
    message: str,
    graph: StateGraph,
    uow: SqlAlchemyUnitOfWork,
) -> str:

    state_entity = uow.chat_state.get_by_user(user_id)
    if state_entity:
        chat_state = state_entity.to_langgraph_state()  # returns dict
        state = ChatState(user_id=user_id)
        state.from_langgraph_state(chat_state)
    else:
        state = ChatState(user_id=user_id)

    state.append_message(role="USER", content=message)

    new_state_dict = graph.run(state.to_langgraph_state())
    state.from_langgraph_state(new_state_dict)

    # persist updated state
    state_entity = state_entity or uow.chat_state.create_entity(user_id)
    state_entity.from_langgraph_state(new_state_dict)
    uow.chat_state.add_or_update(state_entity)
    uow.commit()

    # return last bot message
    bot_msgs = [m for m in state.history if m.role == "SYSTEM"]
    return bot_msgs[-1].content if bot_msgs else ""
