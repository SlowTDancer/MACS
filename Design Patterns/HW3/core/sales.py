from dataclasses import dataclass

from core.receipts import ReceiptRepository


@dataclass
class SalesService:
    receipts: ReceiptRepository

    def generate_sales_report(self) -> dict[str, int]:
        closed_receipts = [
            receipt
            for receipt in self.receipts.read_all()
            if receipt.status == "closed"
        ]
        n_receipts = len(closed_receipts)
        revenue = sum(receipt.total for receipt in closed_receipts)
        return {"n_receipts": n_receipts, "revenue": revenue}
