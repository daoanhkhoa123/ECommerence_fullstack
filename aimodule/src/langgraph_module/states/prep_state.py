from typing import Literal, Optional, TypedDict

INTENT = Literal[
    "Search Product By Product Information",
    "Search Product By Vendor Information",
]

INTENT_DICT: dict[INTENT, str] = {
    "Search Product By Product Information": "Find products using specific product details such as name, description, or attributes.",
    "Search Product By Vendor Information": "Find products offered by a particular vendor, supplier, or brand.",
}


class PrepState(TypedDict):
    user_prompt: str
    intent: INTENT
    rewrited_prompt: str
    data_cache: Optional[str]