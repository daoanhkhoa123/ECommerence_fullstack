import logging

from fastapi import FastAPI

from src.allocation.entrypoints.startup import lifespan
from src.configs.logging import setup_logging
from src.langgraph_module.entrypoints.api.routers import api_router

# Configure logging
setup_logging(debug=True)
logger = logging.getLogger(__name__)

app = FastAPI(title="E-Commerce API", lifespan=lifespan)
app.include_router(api_router)

logger.info(
    "\n"
    "  ███████╗ █████╗ ███████╗████████╗    █████╗ ██████╗ ██╗\n"
    "  ██╔════╝██╔══██╗██╔════╝╚══██╔══╝   ██╔══██╗██╔══██╗██║\n"
    "  █████╗  ███████║███████╗   ██║      ███████║██████╔╝██║\n"
    "  ██╔══╝  ██╔══██║╚════██║   ██║      ██╔══██║██╔═══╝ ██║\n"
    "  ██║     ██║  ██║███████║   ██║      ██║  ██║██║     ██║\n"
    "  ╚═╝     ╚═╝  ╚═╝╚══════╝   ╚═╝      ╚═╝  ╚═╝╚═╝     ╚═╝\n"
    "                                                        \n"
    " :: FastAPI ::                (v1.x)\n"
)
