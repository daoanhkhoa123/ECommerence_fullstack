from pathlib import Path

from pydantic_settings import BaseSettings, SettingsConfigDict

_ENV_FILE = Path(__file__).parent.parent / ".env"
_ENV_FILE_ENCODING = "utf-8"

DEFAULT_CONSUMER_HANDLERS = "src.allocation.entrypoints.message_broker.consumer.consumer_handlers"
DEFAULT_PRODUCER_HANDLERS = "src.allocation.entrypoints.message_broker.producer.producer_handlers"


class DatabaseSettings(BaseSettings):
    database_url: str  
    database_embedding_dim:int

    model_config = SettingsConfigDict(
        env_file=_ENV_FILE,
        env_file_encoding=_ENV_FILE_ENCODING,
        extra="ignore"
    )


class KafkaSettings(BaseSettings):
    kafka_url: str  
    kafka_group: str

    consumer_handlers: str = DEFAULT_CONSUMER_HANDLERS
    producer_handlers: str = DEFAULT_PRODUCER_HANDLERS
    kafka_retries: int = 3
    kafka_retry_backoff_ms: int = 1000
    kafka_acks: str = "all"


    model_config = SettingsConfigDict(
        env_file=_ENV_FILE,
        env_file_encoding=_ENV_FILE_ENCODING,
        extra="ignore"
    )


class LLMSettings(BaseSettings):
    embedding_model: str

    model_config = SettingsConfigDict(
        env_file=_ENV_FILE,
        env_file_encoding=_ENV_FILE_ENCODING,
        extra="ignore"
    )

class AppSettings(BaseSettings):
    src_path: str = "allocation"

    model_config = SettingsConfigDict(
        env_file=_ENV_FILE,
        env_file_encoding=_ENV_FILE_ENCODING,
        extra="ignore"
    )