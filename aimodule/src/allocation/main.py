from fastapi import FastAPI
from src.allocation.entrypoints.startup import lifespan
from src.allocation.entrypoints.api.routers import api_router
from src.allocation.configs.logging import setup_logging
import logging

# Configure logging
setup_logging(debug=True)
logger = logging.getLogger(__name__)

# Create FastAPI app
app = FastAPI(title="E-Commerce API", lifespan=lifespan)
app.include_router(api_router)

# Print a custom banner when app starts
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
