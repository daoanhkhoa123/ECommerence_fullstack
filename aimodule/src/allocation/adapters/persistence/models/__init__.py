import os
import importlib

def load_all_models():
    for filename in os.listdir(os.path.dirname(__file__)):
        if filename.endswith(".py") and filename != "__init__.py":
            importlib.import_module(f"{__package__}.{filename[:-3]}")
