FROM python:3.12-slim-trixie

WORKDIR /app
COPY --from=ghcr.io/astral-sh/uv:latest /uv /uvx /bin/

# Copy only dependency files first
COPY pyproject.toml uv.lock ./
RUN uv sync --locked

# Then copy your code
COPY . /app

EXPOSE 8000
ENTRYPOINT ["/app/.venv/bin/uvicorn", "src.allocation.main:app", "--host", "0.0.0.0", "--port", "8000"]
