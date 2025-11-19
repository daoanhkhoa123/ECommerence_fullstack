from langgraph.graph import StateGraph
from langgraph.graph.state import CompiledStateGraph

from src.langgraph_module.nodes.prep_node import (intent_classifier,
                                                  rewrite_by_history,
                                                  rewrite_by_intent)
from src.langgraph_module.nodes.service_node import answer_by_data
from src.langgraph_module.states.input_state import InputState
from src.langgraph_module.states.output_state import OutputState
from src.langgraph_module.states.prep_state import PrepState


def build_chat_graph() -> CompiledStateGraph:
    graph = StateGraph(state_schema=PrepState, input_schema=InputState, output_schema=OutputState)
    graph.add_node("rewrite_by_history", rewrite_by_history)
    graph.add_node("intent_classifier", intent_classifier)
    graph.add_node("rewrite_by_intent", rewrite_by_intent)
    graph.add_node("answer_by_data", answer_by_data)

    graph.add_edge("rewrite_by_history", "intent_classifier")
    graph.add_edge("intent_classifier", "rewrite_by_intent")
    graph.add_edge("rewrite_by_intent", "answer_by_data")
    graph.set_entry_point("rewrite_by_history")
    graph.set_finish_point("answer_by_data")

    return graph.compile()
