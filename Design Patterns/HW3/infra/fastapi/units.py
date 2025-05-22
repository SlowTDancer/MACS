from uuid import UUID

from fastapi import APIRouter
from fastapi.responses import JSONResponse
from pydantic import BaseModel

from core.errors import DoesNotExistError, ExistsError
from core.units import Unit
from infra.fastapi.dependables import UnitRepositoryDependable

unit_api = APIRouter(tags=["Units"])


class CreateUnitRequest(BaseModel):
    name: str


class UnitItem(BaseModel):
    id: UUID
    name: str


class UnitItemEnvelope(BaseModel):
    unit: UnitItem


class UnitListEnvelope(BaseModel):
    units: list[UnitItem]


@unit_api.post(
    "/units",
    status_code=201,
    response_model=UnitItemEnvelope,
)
def create_unit(
    request: CreateUnitRequest, units: UnitRepositoryDependable
) -> dict[str, Unit] | JSONResponse:
    unit = Unit(**request.model_dump())
    try:
        units.create(unit)
        return {"unit": unit}
    except ExistsError:
        return JSONResponse(
            status_code=409,
            content={
                "error": {"message": f"Unit with name<{unit.name}> already exists."}
            },
        )


@unit_api.get(
    "/units/{unit_id}",
    status_code=200,
    response_model=UnitItemEnvelope,
)
def fetch_unit(
    unit_id: UUID, units: UnitRepositoryDependable
) -> dict[str, Unit] | JSONResponse:
    try:
        return {"unit": units.fetch(unit_id)}
    except DoesNotExistError:
        return JSONResponse(
            status_code=404,
            content={
                "error": {"message": f"Unit with id<{unit_id}> does " "not exist."}
            },
        )


@unit_api.get("/units", response_model=UnitListEnvelope)
def fetch_all_units(units: UnitRepositoryDependable) -> dict[str, list[Unit]]:
    return {"units": units.fetch_all()}
