from typing import Any
from uuid import UUID

from fastapi import APIRouter
from fastapi.responses import JSONResponse
from pydantic import BaseModel

from core.errors import ClosedError, DoesNotExistError
from core.products import ProductService
from core.receipts import Receipt, ReceiptService
from infra.fastapi.dependables import (
    ProductRepositoryDependable,
    ReceiptRepositoryDependable,
)

receipt_api = APIRouter(tags=["Receipts"])


class ProductItem(BaseModel):
    id: UUID
    quantity: int
    price: int
    total: int


class ReceiptItem(BaseModel):
    id: UUID
    status: str
    products: list[ProductItem]
    total: int


class UpdateReceiptRequest(BaseModel):
    id: UUID
    quantity: int


class UpdateStatusRequest(BaseModel):
    status: str


class ReceiptItemEnvelope(BaseModel):
    receipt: ReceiptItem


class ReceiptListEnvelope(BaseModel):
    receipts: list[ReceiptItem]


@receipt_api.post(
    "/receipts",
    status_code=201,
    response_model=ReceiptItemEnvelope,
)
def create_receipt(receipts: ReceiptRepositoryDependable) -> dict[str, Receipt]:
    receipt_service = ReceiptService(receipts)
    receipt = receipt_service.create_receipt()

    return {"receipt": receipt}


@receipt_api.post(
    "/receipts/{receipt_id}/products",
    status_code=201,
    response_model=ReceiptItemEnvelope,
)
def add_product(
    receipt_id: UUID,
    request: UpdateReceiptRequest,
    products: ProductRepositoryDependable,
    receipts: ReceiptRepositoryDependable,
) -> dict[str, Receipt] | JSONResponse:
    receipt_service, product_service = ReceiptService(receipts), ProductService(
        products
    )
    product_id, quantity = request.id, request.quantity
    try:
        product = product_service.get_product(product_id)
    except DoesNotExistError:
        return JSONResponse(
            status_code=404,
            content={
                "error": {"message": f"Product with id<{product_id}> does not exist."}
            },
        )
    try:
        receipt_service.add_product_to_receipt(receipt_id, product, quantity)
        return {"receipt": receipt_service.get_receipt(receipt_id)}
    except DoesNotExistError:
        return JSONResponse(
            status_code=404,
            content={
                "error": {"message": f"Receipt with id<{receipt_id}> does not exist."}
            },
        )
    except ClosedError:
        return JSONResponse(
            status_code=403,
            content={"message": f"Receipt with id<{receipt_id}> is closed."},
        )


@receipt_api.get(
    "/receipts/{receipt_id}", status_code=200, response_model=ReceiptItemEnvelope
)
def get_receipt_by_id(
    receipt_id: UUID, receipts: ReceiptRepositoryDependable
) -> dict[str, Receipt] | JSONResponse:
    try:
        receipt_service = ReceiptService(receipts)
        return {"receipt": receipt_service.get_receipt(receipt_id)}
    except DoesNotExistError:
        return JSONResponse(
            status_code=404,
            content={
                "error": {"message": f"Receipt with id<{receipt_id}> does not exist."}
            },
        )


@receipt_api.patch(
    "/receipts/{receipt_id}",
    status_code=200,
    response_model=None,
)
def close_receipt(
    receipt_id: UUID,
    request: UpdateStatusRequest,
    receipts: ReceiptRepositoryDependable,
) -> dict[Any, Any] | JSONResponse:
    try:
        status = request.status
        receipt_service = ReceiptService(receipts)
        if status == "closed":
            receipt_service.close_receipt(receipt_id)
        return {}
    except DoesNotExistError:
        return JSONResponse(
            status_code=404,
            content={
                "error": {"message": f"Receipt with id<{receipt_id}> does not exist."}
            },
        )


@receipt_api.delete(
    "/receipts/{receipt_id}",
    status_code=200,
    response_model=None,
)
def delete_receipt(
    receipt_id: UUID,
    receipts: ReceiptRepositoryDependable,
) -> dict[Any, Any] | JSONResponse:
    try:
        receipt_service = ReceiptService(receipts)
        receipt_service.delete_receipt(receipt_id)
        return {}
    except DoesNotExistError:
        return JSONResponse(
            status_code=404,
            content={
                "error": {"message": f"Receipt with id<{receipt_id}> does not exist."}
            },
        )
    except ClosedError:
        return JSONResponse(
            status_code=403,
            content={"error": {"message": f"Receipt with id<{receipt_id}> is closed."}},
        )
