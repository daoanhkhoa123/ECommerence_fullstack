from typing import Any, Dict, List, Sequence, Tuple
from db.ultils.connection_util import get_connection

__all__ = ["RepositoryBase"]


class RepositoryBase:
    def __init__(self, db_url: str, table_name: str) -> None:
        self.get_connection = lambda: get_connection(db_url)
        self.table_name = table_name

    def _execute_query(self, query: str, *params: Any, fetch: bool = True) -> List[Tuple]:
        with self.get_connection() as conn:
            with conn.cursor() as cur:
                cur.execute(query, params)
                if fetch:
                    return cur.fetchall()
                conn.commit()
                return []

    @property
    def columns(self) -> Sequence[str]:
        query = f"SELECT * FROM {self.table_name} LIMIT 0"
        with self.get_connection() as conn:
            with conn.cursor() as cur:
                cur.execute(query)
                return [d[0] for d in cur.description]  # type: ignore

    @staticmethod
    def to_dict(rows: Sequence[Tuple], keys: Sequence[str]) -> Sequence[Dict]:
        return [{k: v for (k, v) in zip(keys, row)} for row in rows]

    # SELECT
    def find_all(self) -> Sequence[Tuple]:
        query = f"SELECT * FROM {self.table_name}"
        return self._execute_query(query)

    def find_fields(self, *fields: str) -> Sequence[Tuple]:
        col_str = ", ".join(fields)
        query = f"SELECT {col_str} FROM {self.table_name}"
        return self._execute_query(query)

    def find_by_filters(self, filters: Dict[str, Any]) -> Sequence[Tuple]:
        where_clause = " AND ".join([f"{col} = %s" for col in filters.keys()])
        query = f"SELECT * FROM {self.table_name} WHERE {where_clause}"
        return self._execute_query(query, *filters.values())

    def find_fields_by_filters(self, fields: Sequence[str], filters: Dict[str, Any]) -> Sequence[Tuple]:
        col_str = ", ".join(fields)
        where_clause = " AND ".join([f"{k} = %s" for k in filters.keys()])
        query = f"SELECT {col_str} FROM {self.table_name} WHERE {where_clause}"
        return self._execute_query(query, *filters.values())

    # INSERT / UPDATE / DELETE
    def create(self, data: Dict[str, Any]) -> None:
        cols = ", ".join(data.keys())
        placeholders = ", ".join(["%s"] * len(data))
        query = f"INSERT INTO {self.table_name} ({cols}) VALUES ({placeholders})"
        self._execute_query(query, *data.values(), fetch=False)

    def update(self, filters: Dict[str, Any], updates: Dict[str, Any]) -> None:
        set_clause = ", ".join([f"{col} = %s" for col in updates.keys()])
        where_clause = " AND ".join([f"{col} = %s" for col in filters.keys()])
        query = f"UPDATE {self.table_name} SET {set_clause} WHERE {where_clause}"
        params = list(updates.values()) + list(filters.values())
        self._execute_query(query, *params, fetch=False)

    def delete(self, filters: Dict[str, Any]) -> None:
        where_clause = " AND ".join([f"{col} = %s" for col in filters.keys()])
        query = f"DELETE FROM {self.table_name} WHERE {where_clause}"
        self._execute_query(query, *filters.values(), fetch=False)
