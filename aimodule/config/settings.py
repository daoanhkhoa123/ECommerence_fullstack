from pydantic_settings import BaseSettings, SettingsConfigDict

_ENV_FILE = ".env"
_ENV_FILE_ENCODING = "utf-8"

class DatabaseSettings(BaseSettings):
    backend_db_url: str | None = None
    analytics_db_url: str | None = None
    minconn: int = 1
    maxconn: int = 3

    model_config = SettingsConfigDict(env_file=_ENV_FILE, env_file_encoding=_ENV_FILE_ENCODING)


class LLMSettings(BaseSettings):
    google_api_key: str | None = None
    openai_api_key: str | None = None
    google_llm:str| None = None
    google_embedding:str |None = None

    model_config = SettingsConfigDict(env_file=_ENV_FILE, env_file_encoding=_ENV_FILE_ENCODING)


class AppSettings(BaseSettings):
    environment: str = "development"
    debug: bool = True

    model_config = SettingsConfigDict(env_file=_ENV_FILE, env_file_encoding=_ENV_FILE_ENCODING)
