from abc import ABC, abstractmethod
from typing import Optional
from src.allocation.domain.entities.vendor import Vendor

class AbstractVendorRepository(ABC):

    @abstractmethod
    def add(self, vendor: Vendor) -> None:
        ...

    @abstractmethod
    def get(self, vendor_id: int) -> Optional[Vendor]:
        ...

    @abstractmethod
    def delete(self, vendor: Vendor) -> None:
        ...
