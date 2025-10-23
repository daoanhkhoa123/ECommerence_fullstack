from typing import Dict, List, Sequence, Tuple
from db.ultils.connection_util import get_connection

__all__ = ["RepositoryBase"]

class RepositoryBase:
    def __init__(self, db_url, table_name) -> None:
        self.get_connection = lambda: get_connection(db_url)
        self.table_name = table_name

    def _execute_query(self, query:str, *fields:str) -> List[Tuple]:
        with self.get_connection() as conn:
            with conn.cursor() as cur:
                cur.execute(query, fields)
                return cur.fetchall()

    @property
    def columns(self) -> Sequence[str]:
        query = f"SELECT * FROM {self.table_name} LIMIT 0"
        with self.get_connection() as conn:
            with conn.cursor() as cur:
                cur.execute(query)
                return [d[0] for d in cur.description] # type: ignore

    @staticmethod
    def to_dict(rows:Sequence[Tuple], keys:Sequence[str]) -> Sequence[Dict]:
        return [{k:v for (k,v) in zip(keys, row)} for row in rows]

    def find_all(self) -> Sequence[Tuple]:
        query = f"SELECT * FROM {self.table_name} WHERE is_active=TRUE"
        return self._execute_query(query)

    def find_fields(self, *fields: str) -> Sequence[Tuple]:
        col_str = ", ".join(fields)
        query = f"SELECT {col_str} FROM {self.table_name} WHERE is_active=TRUE"
        return self._execute_query(query)

    def find_by_filters(self, filters:dict) -> Sequence[Tuple]:
        where_clause = " AND ".join([f"{col}=%s" for col in filters.keys()])
        query = f"SELECT * FROM {self.table_name} WHERE {where_clause} AND is_active=TRUE"
        return self._execute_query(query, *filters.values())

    def find_fields_by_filters(self, fields: Sequence[str], filters: dict) -> Sequence[Tuple]:
        col_str = ", ".join(fields)
        where_clause = " AND ".join([f"{k}=%s" for k in filters.keys()])
        query = f"SELECT {col_str} FROM {self.table_name} WHERE {where_clause} AND is_active=TRUE"
        params = list(filters.values())
        return self._execute_query(query, *params)
