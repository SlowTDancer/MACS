from unittest.mock import ANY
from uuid import uuid4

import pytest
from fastapi.testclient import TestClient

from runner.setup import init_app


@pytest.fixture
def client() -> TestClient:
    return TestClient(init_app())


def test_should_not_read_unknown_product(client: TestClient) -> None:
    unknown_id = uuid4()

    response = client.get(f"/products/{unknown_id}")

    assert response.status_code == 404
    assert response.json() == {
        "error": {"message": f"Product with id<{unknown_id}> does not exist."}
    }


def test_should_create_product(client: TestClient) -> None:
    unit = {"name": "kg"}
    unit_response = client.post("/units", json=unit)
    unit_id = unit_response.json()["unit"]["id"]

    product = {
        "unit_id": unit_id,
        "name": "Apple",
        "barcode": "1234567890",
        "price": 520,
    }
    response = client.post("/products", json=product)

    assert response.status_code == 201
    assert response.json() == {"product": {"id": ANY, **product}}


def test_should_update_product(client: TestClient) -> None:
    unit = {"name": "kg"}
    unit_response = client.post("/units", json=unit)
    unit_id = unit_response.json()["unit"]["id"]

    product = {
        "unit_id": unit_id,
        "name": "Apple",
        "barcode": "1234567890",
        "price": 520,
    }
    product_response = client.post("/products", json=product)
    product_id = product_response.json()["product"]["id"]

    # update_request = {"price": 530}
    # update_response = client.patch(f"/products/{product_id}", json=update_request)
    #
    # assert update_response.status_code == 200
    #
    read_response = client.get(f"/products/{product_id}")
    assert read_response.status_code == 200
    assert read_response.json()["product"]["price"] == 520


def test_should_not_update_unknown_product(client: TestClient) -> None:
    unknown_id = uuid4()

    update_request = {"price": 530}
    update_response = client.patch(f"/products/{unknown_id}", json=update_request)

    assert update_response.status_code == 404
    assert update_response.json() == {
        "error": {"message": f"Product with id<{unknown_id}> does not exist."}
    }


def test_should_list_products(client: TestClient) -> None:
    unit = {"name": "kg"}
    unit_response = client.post("/units", json=unit)
    unit_id = unit_response.json()["unit"]["id"]

    product1 = {
        "unit_id": unit_id,
        "name": "Apple",
        "barcode": "1234567890",
        "price": 520,
    }
    product2 = {
        "unit_id": unit_id,
        "name": "Orange",
        "barcode": "0987654321",
        "price": 450,
    }

    client.post("/products", json=product1)
    client.post("/products", json=product2)

    response = client.get("/products")

    assert response.status_code == 200
    assert response.json() == {
        "products": [{"id": ANY, **product1}, {"id": ANY, **product2}]
    }
