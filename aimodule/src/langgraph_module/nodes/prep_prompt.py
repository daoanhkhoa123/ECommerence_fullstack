from src.langgraph_module.llms.cerebras_llm import CerebrasLLM
from src.langgraph_module.llms.google_llm import GoogleLLM
from src.langgraph_module.llms.llm_interface import LLMInterface
from src.langgraph_module.adapters.persistence.sqlalchemy_unit_of_work import SqlAlchemyUnitOfWork

def rewrite_by_history(state, llm:LLMInterface, uow:SqlAlchemyUnitOfWork):
    pass