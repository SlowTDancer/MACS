from dataclasses import dataclass, field
from uuid import UUID

from core.errors import ClosedError, DoesNotExistError, ExistsError
from core.receipts import Receipt


@dataclass
class ReceiptInMemory:
    receipts: dict[UUID, Receipt] = field(default_factory=dict)

    def create(self, receipt: Receipt) -> None:
        if receipt.id in self.receipts:
            raise ExistsError

        self.receipts[receipt.id] = receipt

    def read(self, receipt_id: UUID) -> Receipt:
        try:
            return self.receipts[receipt_id]

        except KeyError:
            raise DoesNotExistError(receipt_id)

    def read_all(self) -> list[Receipt]:
        return list(self.receipts.values())

    def update(self, receipt: Receipt) -> None:
        self.receipts[receipt.id] = receipt

    def delete(self, receipt_id: UUID) -> None:
        if receipt_id not in self.receipts:
            raise DoesNotExistError

        if self.receipts[receipt_id].status == "closed":
            raise ClosedError

        self.receipts.pop(receipt_id)
