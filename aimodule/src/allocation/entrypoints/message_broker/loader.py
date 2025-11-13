import importlib
import pathlib
from src.configs.settings import AppSettings, KafkaSettings

kafka_settings = KafkaSettings()  # type: ignore
app_settings = AppSettings()


def load_all_handlers():
    for folder_str in [kafka_settings.consumer_handlers, kafka_settings.producer_handlers]:
        # Convert dotted module path → filesystem path
        folder_path = pathlib.Path(folder_str.replace(".", "/"))
        print(f"[Loader] Checking folder: {folder_path.resolve()}")

        if not folder_path.exists():
            print(f"[Loader] Folder does not exist: {folder_path.resolve()}")
            continue

        for file in folder_path.glob("*.py"):
            if file.name.startswith("__"):
                continue

            module_path = ".".join(folder_str.split(".") + [file.stem])
            importlib.import_module(module_path)
            print(f"[Loader] Imported handler: {module_path}")
