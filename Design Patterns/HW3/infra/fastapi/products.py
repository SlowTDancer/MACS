from typing import Any
from uuid import UUID

from fastapi import APIRouter
from fastapi.responses import JSONResponse
from pydantic import BaseModel

from core.errors import DoesNotExistError, ExistsError
from core.products import Product, ProductService
from infra.fastapi.dependables import (
    ProductRepositoryDependable,
    UnitRepositoryDependable,
)

product_api = APIRouter(tags=["Products"])


class CreateProductRequest(BaseModel):
    unit_id: UUID
    name: str
    barcode: str
    price: int


class ProductItem(BaseModel):
    id: UUID
    unit_id: UUID
    name: str
    barcode: str
    price: int


class UpdateProductRequest(BaseModel):
    price: int


class ProductItemEnvelope(BaseModel):
    product: ProductItem


class ProductListEnvelope(BaseModel):
    products: list[ProductItem]


@product_api.post(
    "/products",
    status_code=201,
    response_model=ProductItemEnvelope,
)
def create_product(
    request: CreateProductRequest,
    products: ProductRepositoryDependable,
    units: UnitRepositoryDependable,
) -> dict[str, Product] | JSONResponse:
    product = Product(**request.model_dump())
    product_service = ProductService(products)
    try:
        units.fetch(product.unit_id)
        product_service.create_product(
            product.unit_id, product.name, product.barcode, product.price, product.id
        )

        return {"product": product}
    except ExistsError:
        return JSONResponse(
            status_code=409,
            content={
                "error": {
                    "message": f"Product with barcode<{product.barcode}>"
                    " already exists."
                }
            },
        )
    except DoesNotExistError:
        return JSONResponse(
            status_code=404,
            content={
                "error": {
                    "message": f"Unit with id<{product.unit_id}> does " "not exist."
                }
            },
        )


@product_api.get(
    "/products/{product_id}",
    status_code=200,
    response_model=ProductItemEnvelope,
)
def fetch_product(
    product_id: UUID, products: ProductRepositoryDependable
) -> dict[str, Product] | JSONResponse:
    try:
        product_service = ProductService(products)
        return {"product": product_service.get_product(product_id)}
    except DoesNotExistError:
        return JSONResponse(
            status_code=404,
            content={
                "error": {
                    "message": f"Product with id<{product_id}> " "does not exist."
                }
            },
        )


@product_api.get("/products", response_model=ProductListEnvelope)
def fetch_all_products(
    products: ProductRepositoryDependable,
) -> dict[str, list[Product]]:
    product_service = ProductService(products)
    return {"products": product_service.get_all_products()}


@product_api.patch(
    "/products/{product_id}",
    status_code=200,
    response_model=None,
)
def update_product(
    product_id: UUID,
    request: UpdateProductRequest,
    products: ProductRepositoryDependable,
) -> dict[Any, Any] | JSONResponse:
    try:
        product_service = ProductService(products)
        product_service.update_product_price(product_id, request.price)
        return {}
    except DoesNotExistError:
        return JSONResponse(
            status_code=404,
            content={
                "error": {
                    "message": f"Product with id<{product_id}> " "does not exist."
                }
            },
        )
