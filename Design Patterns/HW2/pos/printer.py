from typing import Dict, List, Protocol

from pos.discount import IDiscount
from pos.product import IProduct
from pos.receipt import IReceipt


class IPrinter(Protocol):
    def print_shop_list(self, products: List[IProduct], discounts: IDiscount) -> None:
        pass

    def print_receipt(self, receipt: IReceipt, discounts: IDiscount) -> None:
        pass

    def print_report(self, sold_items: Dict[IProduct, int]) -> None:
        pass

    def print_revenue(self, revenue: Dict[str, float]) -> None:
        pass


class Printer:
    def print_shop_list(self, products: List[IProduct], discounts: IDiscount) -> None:
        print("+{:-<12}+{:-<17}+{:-<12}+{:-<12}+".format("", "", "", ""))
        print(
            "|{:<10} | {:<15} | {:<10} | {:<10} |".format(
                "ID", "Name", "Price", "Discount"
            )
        )
        print("+{:-<12}+{:-<17}+{:-<12}+{:-<12}+".format("", "", "", ""))

        for product in products:
            product_id = product.get_id()
            product_name = product.get_name()
            product_price = product.get_real_price()
            product_discount = discounts.get_product_discount(product_id)

            print(
                "|{:<10} | {:<15} | {:<10} | {:<10} |".format(
                    product_id,
                    product_name,
                    f"${product_price:.2f}",
                    f"{product_discount * 100:.2f}%",
                )
            )

        print("+{:-<12}+{:-<17}+{:-<12}+{:-<12}+".format("", "", "", ""))

    def print_receipt(self, receipt: IReceipt, discounts: IDiscount) -> None:
        print("+" + "-" * 60 + "+")
        print(
            "|{:<20} | {:<10} | {:<10} | {:<10}|".format(
                "Product", "Units", "Price", "Total"
            )
        )
        print("|" + "-" * 60 + "|")
        for product in receipt:
            print(
                "|{:<20} | {:<10} | {:<10} | {:<10}|".format(
                    product.get_name(),
                    product.get_unit(),
                    product.get_price(discounts),
                    product.calculate_price(discounts),
                )
            )
        print("|" + "-" * 60 + "|")
        print("+" + "-" * 60 + "+")

    def print_report(self, sold_items: Dict[IProduct, int]) -> None:
        print("+" + "-" * 30 + "+")
        print("|{:<20} | {:<10}|".format("Product", "Sales"))
        print("|" + "-" * 30 + "|")
        for product, count in sold_items.items():
            print("|{:<20} | {:<10}|".format(product.get_name(), count))
        print("+" + "-" * 30 + "+")

    def print_revenue(self, revenue: Dict[str, float]) -> None:
        print("+" + "-" * 30 + "+")
        print("|{:<20} | {:<10}|".format("Payment", "Revenue"))
        print("|" + "-" * 30 + "|")
        for payment_type, amount in revenue.items():
            print("|{:<20} | {:<10}|".format(payment_type, round(amount, 2)))
        print("+" + "-" * 30 + "+")
