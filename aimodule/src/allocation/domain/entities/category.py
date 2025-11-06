from dataclasses import dataclass
from typing import Optional

@dataclass
class Category:
    id: int
    name: str
    description: Optional[str]