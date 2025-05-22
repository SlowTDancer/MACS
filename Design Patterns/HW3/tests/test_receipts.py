from dataclasses import dataclass, field
from typing import Any
from unittest.mock import ANY
from uuid import uuid4

import pytest
from faker import Faker
from fastapi.testclient import TestClient

from runner.setup import init_app


@pytest.fixture
def client() -> TestClient:
    return TestClient(init_app())


@dataclass
class Fake:
    faker: Faker = field(default_factory=Faker)

    def product(self, unit_id: str) -> dict[str, Any]:
        return {
            "unit_id": unit_id,
            "name": self.faker.name(),
            "barcode": self.faker.name(),
            "price": 1,
        }


def test_should_not_read_unknown_receipt(client: TestClient) -> None:
    unknown_id = uuid4()

    response = client.get(f"/receipts/{unknown_id}")

    assert response.status_code == 404
    assert response.json() == {
        "error": {"message": f"Receipt with id<{unknown_id}> does not exist."}
    }


def test_should_create_receipt(client: TestClient) -> None:
    response = client.post("/receipts")

    assert response.status_code == 201
    assert response.json() == {
        "receipt": {"id": ANY, "status": "open", "products": [], "total": 0}
    }


def test_add_product_to_receipt(client: TestClient) -> None:
    unit = {"name": "kg"}

    response = client.post("/units", json=unit)

    product = Fake().product(response.json()["unit"]["id"])

    product_response = client.post("/products", json=product)
    product_id = product_response.json()["product"]["id"]

    receipt_response = client.post("/receipts")
    receipt_id = receipt_response.json()["receipt"]["id"]

    add_product_request = {"id": product_id, "quantity": 2}
    response = client.post(f"/receipts/{receipt_id}/products", json=add_product_request)

    assert response.status_code == 201
    assert response.json()["receipt"]["products"] == [
        {"id": product_id, "price": ANY, "quantity": 2, "total": 2 * product["price"]}
    ]


def test_should_close_receipt(client: TestClient) -> None:
    response = client.post("/receipts")
    receipt_id = response.json()["receipt"]["id"]

    close_request = {"status": "closed"}
    close_response = client.patch(f"/receipts/{receipt_id}", json=close_request)

    response = client.get(f"/receipts/{receipt_id}")
    print(response.json())

    assert close_response.status_code == 200
    unit = {"name": "kg"}

    response = client.post("/units", json=unit)
    product = Fake().product(response.json()["unit"]["id"])
    product_response = client.post("/products", json=product)
    product_id = product_response.json()["product"]["id"]

    add_product_request = {"id": product_id, "quantity": 2}
    client.post(f"/receipts/{receipt_id}/products", json=add_product_request)

    delete_response = client.delete(f"/receipts/{receipt_id}")

    assert delete_response.status_code == 403
    assert delete_response.json() == {
        "error": {"message": f"Receipt with id<{receipt_id}> is closed."}
    }


def test_should_delete_receipt(client: TestClient) -> None:
    response = client.post("/receipts")
    receipt_id = response.json()["receipt"]["id"]

    delete_response = client.delete(f"/receipts/{receipt_id}")

    assert delete_response.status_code == 200

    read_response = client.get(f"/receipts/{receipt_id}")
    assert read_response.status_code == 404
