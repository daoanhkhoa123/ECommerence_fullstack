from typing import Awaitable, Callable, Dict

_topic_handlers: Dict[str, Callable[[dict], Awaitable[None]]] = {}


def register_topic(topic: str):
    """
    Decorator to register a Kafka topic to its handler coroutine.
    Usage:
        @register_topic("order-item-events")
        async def handle_order_item_event(event: dict):
            ...
    """
    def decorator(func: Callable[[dict], Awaitable[None]]):
        _topic_handlers[topic] = func
        return func  # return original function so it can still be called directly
    return decorator


def get_handler_for_topic(topic: str) -> Callable[[dict], Awaitable[None]] | None:
    """Get the handler for a given topic name."""
    return _topic_handlers.get(topic)


def list_registered_topics() -> list[str]:
    """Return a list of all topics currently registered."""
    return list(_topic_handlers.keys())
