import logging
import sys

def setup_logging(debug: bool = False) -> None:
    """Configure global logging behavior with file path included."""

    logging.basicConfig(
        level=logging.DEBUG if debug else logging.INFO,
        format="%(asctime)s [%(levelname)s] %(name)s (%(pathname)s:%(lineno)d): %(message)s",
        handlers=[
            logging.StreamHandler(sys.stdout)
        ]
    )
