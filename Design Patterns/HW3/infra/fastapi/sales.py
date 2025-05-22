from fastapi import APIRouter
from pydantic import BaseModel

from core.sales import SalesService
from infra.fastapi.dependables import ReceiptRepositoryDependable

sales_api = APIRouter(tags=["Sales"])


class SalesItem(BaseModel):
    n_receipts: int
    revenue: int


class SalesItemEnvelope(BaseModel):
    sales: SalesItem


@sales_api.get(
    "/sales",
    status_code=200,
    response_model=SalesItemEnvelope,
)
def get_sales(receipts: ReceiptRepositoryDependable) -> dict[str, dict[str, int]]:
    sales_service = SalesService(receipts)

    return {"sales": sales_service.generate_sales_report()}
