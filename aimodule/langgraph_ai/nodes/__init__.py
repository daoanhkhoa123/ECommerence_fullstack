from typing import Dict
from config.logging import logging
from langgraph_ai.states import MainState
from langgraph_ai.llms import gemini

def save(t):
    ...

def load(user_id, seesion_id):
    ...

def LoadNode(state:MainState) -> Dict:
    ...

def SaveNode(state:MainState) -> None:
    prompt = f"""
    You are maintaining a running summary of a conversation.

    Current summary:
    {state["context_summary"]}

    New user prompt:
    {state["prompt"]}

    New answer:
    {state["answer"]}

    Task: Update the context summary so it concisely includes the new prompt–answer pair while preserving the important information from the existing summary.
    """
    state["context_summary"] =gemini.generate_text(prompt)
    save(state)
    save(state["prompt"])
    save(state["answer"])
