import importlib
import pkgutil
from fastapi import APIRouter

api_router = APIRouter()

# Dynamically find all *_router.py modules in this package
package = __name__
for _, module_name, _ in pkgutil.iter_modules(__path__):
    if module_name.endswith("_router"):
        module = importlib.import_module(f"{package}.{module_name}")
        if hasattr(module, "router"):
            router = getattr(module, "router")
            api_router.include_router(router)
