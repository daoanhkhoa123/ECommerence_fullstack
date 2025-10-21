import sys
import logging

def setup_loggin(debug:bool=False) -> None:
    """Configure global logging behavior."""

    logging.basicConfig(
        level = logging.DEBUG if debug else logging.INFO,
        format="%(asctime)s [%(levelname)s] %(name)s: %(message)s",
        handlers=[
            logging.StreamHandler(sys.stdout)
        ]
    )

    logging.getLogger("psycopg2").setLevel(logging.WARNING)
    logging.getLogger("urllib3").setLevel(logging.WARNING)
    