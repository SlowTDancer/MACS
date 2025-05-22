from pos.discount import Discount


def test_discount_get_product_discount() -> None:
    discounts_list = {(1,): 0.1, (2, 3): 0.2, (4, 5, 6): 0.15}
    discount = Discount(discounts_list)

    assert isinstance(discount, Discount)

    product_id_with_discount = 1
    assert discount.get_product_discount(product_id_with_discount) == 0.1

    product_id_with_multiple_discounts = 2
    assert discount.get_product_discount(product_id_with_multiple_discounts) == 0.2

    product_id_with_no_discount = 7
    assert discount.get_product_discount(product_id_with_no_discount) == 0.0
