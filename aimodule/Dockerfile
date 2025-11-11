FROM python:3.12-slim-trixie

WORKDIR /app
COPY --from=ghcr.io/astral-sh/uv:latest /uv /uvx /bin/
ADD . /app
RUN uv sync --locked
EXPOSE 8000

ENTRYPOINT ["/app/.venv/bin/uvicorn", "src.allocation.main:app", "--host", "0.0.0.0", "--port", "8000"]
