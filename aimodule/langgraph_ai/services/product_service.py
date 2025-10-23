from typing import Sequence, Dict
from db.backend_db.entities import ProductsColumns, VendorsColumns, VendorsProductsColumns
from db.backend_db.repositories.product_repository import ProductRepository
from db.backend_db.repositories.vendor_repository import VendorRepository
from db.backend_db.repositories.product_vendor_repository import VendorProductRepository

class ProductService:
    def __init__(self) -> None:
        self.product_repository = ProductRepository()
        self.vendor_repository = VendorRepository()
        self.vendor_product_repository = VendorProductRepository()

    def find_product_name_description_brand_category(self) -> Sequence[Dict]:
        fields = "product_id","name", "category","brand", "description"
        return self.product_repository.to_dict(self.product_repository.find_fields(*fields), fields)
    
    def find_product_name_description_brand_category_price_vendor_vendor_description_by_productids(self, product_ids) -> Sequence[Dict]:
        ids_query = ", ".join(["%s"] * len(product_ids))
        find_fields = "p.name", "p.description", "p.brand", "p.category","vp.price", "v.shop_name", "v.description"
        query = f"""SELECT {", ".join(find_fields)} 
        FROM {self.vendor_product_repository.table_name} AS vp
        JOIN {self.product_repository.table_name}  AS p 
        ON vp.product_id = p.product_id
        JOIN {self.vendor_repository.table_name} as v
        ON v.vendor_id = vp.vendor_id
        WHERE
        p.product_id IN ({ids_query})
        """   
        rows= self.product_repository._execute_query(query, *product_ids)
        return self.product_repository.to_dict(rows, find_fields)
