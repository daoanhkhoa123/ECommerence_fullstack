import logging
import sys


def setup_logging(debug:bool=False) -> None:
    """Configure global logging behavior."""

    logging.basicConfig(
        level = logging.DEBUG if debug else logging.INFO,
        format="%(asctime)s [%(levelname)s] %(name)s: %(message)s",
        handlers=[
            logging.StreamHandler(sys.stdout)
        ]
    )
