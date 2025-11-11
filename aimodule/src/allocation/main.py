# src/allocation/main.py
from fastapi import FastAPI
from src.allocation.entrypoints.startup import lifespan
from src.allocation.entrypoints.api.routers import api_router

app = FastAPI(title="E-Commerce API", lifespan=lifespan)
app.include_router(api_router)
