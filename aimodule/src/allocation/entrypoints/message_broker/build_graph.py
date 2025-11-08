from langgraph.graph import StateGraph, END

def build_chat_graph() -> StateGraph:
    graph = StateGraph(dict)  # state type is usually a dict

    # Define node functions
    def start_node(state: dict):
        return {"messages": state.get("messages", []) + [{"role": "system", "content": "Welcome!"}]}

    def end_node(state: dict):
        return {"messages": state.get("messages", []) + [{"role": "system", "content": "Goodbye!"}]}

    # Add nodes
    graph.add_node("START", start_node)
    graph.add_node("END", end_node)

    # Connect nodes
    graph.add_edge("START", "END")
    graph.set_entry_point("START")
    graph.set_finish_point("END")

    return graph
