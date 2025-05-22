import pytest
from fastapi.testclient import TestClient

from runner.setup import init_app


@pytest.fixture
def client() -> TestClient:
    return TestClient(init_app())


def test_generate_sales_report_empty(client: TestClient) -> None:
    response = client.get("/sales")

    assert response.status_code == 200
    assert response.json() == {"sales": {"n_receipts": 0, "revenue": 0}}
