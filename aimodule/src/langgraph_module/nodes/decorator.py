
from dataclasses import asdict, dataclass
from functools import wraps
from typing import Callable

from src.langgraph_module.adapters.persistence.sqlalchemy_unit_of_work import \
    SqlAlchemyUnitOfWork
from src.langgraph_module.llms.llm_interface import LLMInterface
from src.langgraph_module.llms.cerebras_llm import CerebrasLLM
from src.langgraph_module.llms.google_llm import GoogleLLM
@dataclass
class ContextSchema:
    uow: SqlAlchemyUnitOfWork
    creative_llm: LLMInterface
    dry_llm: LLMInterface
    # normal_llm:  LLMInterface

    @property
    def dict(self) -> dict:
        return asdict(self)

def inject_context(func: Callable):
    @wraps(func)
    def wrapper(*args, **kwargs):
        context = kwargs.get("context", None)
        # print(kwargs)
        # if not context:
        #     raise NodeDefinitionException(func, "context")
        
        context = ContextSchema(SqlAlchemyUnitOfWork(), creative_llm=CerebrasLLM("qwen-3-235b-a22b-instruct-2507", 0.8),
                                dry_llm=CerebrasLLM("qwen-3-235b-a22b-instruct-2507", 0.2))
        return func(*args,  **kwargs, context=context)
    
    return wrapper