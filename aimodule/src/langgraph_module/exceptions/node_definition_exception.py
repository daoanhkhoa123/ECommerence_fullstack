# from typing import Callable


# class NodeDefinitionException(Exception):
#     def __init__(self, func: Callable, *params: str) -> None:
#         missing = ", ".join(params)
#         if missing:
#             message = (
#                 f"NodeDefinitionException: Function '{func.__name__}' "
#                 f"(module: {func.__module__}) is missing required parameter(s): {missing}"
#             )
#         else:
#             message = (
#             f"NodeDefinitionException: Error in function '{func.__name__}' "
#             f"(module: {func.__module__})"
#             )
            
#         super().__init__(message)
