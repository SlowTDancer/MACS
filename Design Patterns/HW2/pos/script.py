import sqlite3

from pos.constants import DATABASE_PATH

connection = sqlite3.connect(DATABASE_PATH)

cursor = connection.cursor()

cursor.execute("DROP TABLE IF EXISTS products")

cursor.execute(
    """CREATE TABLE products (
                id INTEGER PRIMARY KEY,
                name TEXT,
                price REAL,
                unit INTEGER
)"""
)

cursor.execute("DROP TABLE IF EXISTS discounts")

cursor.execute(
    """CREATE TABLE discounts (
                    id INTEGER PRIMARY KEY,
                    applicable_product_ids TEXT,
                    value REAL
                )"""
)

cursor.execute("DROP TABLE IF EXISTS sold_products")

cursor.execute(
    """CREATE TABLE sold_products (
                    product_id INTEGER,
                    count INTEGER
                )"""
)

cursor.execute("DROP TABLE IF EXISTS sold_products")

cursor.execute(
    """CREATE TABLE sold_products (
                    product_id INTEGER PRIMARY KEY,
                    count INTEGER
                )"""
)

cursor.execute("DROP TABLE IF EXISTS all_time")

cursor.execute(
    """CREATE TABLE all_time (
                    payment_type TEXT,
                    revenue REAL
                )"""
)

product_data = [
    (1, "Milk", 2.99, 1),
    (2, "Bread Pack", 1.99, 6),
    (3, "Bread", 1.99, 1),
    (4, "Toothpaste", 2.49, 1),
    (5, "Shampoo", 4.99, 1),
    (6, "Apples", 0.79, 1),
    (7, "Chicken Breast", 5.99, 1),
    (8, "Cereal", 3.29, 1),
    (9, "Coffee", 6.99, 1),
    (10, "Water Pack", 0.49, 12),
]

discount_data = [(1, "2,9", 0.2), (2, "3", 0.1), (3, "4,5", 0.3), (4, "10", 0.1)]

all_time_data = [("Cash", 0.0), ("Card", 0.0)]

cursor.executemany(
    """INSERT INTO products (id, name, price, unit) VALUES (?, ?, ?, ?)""",
    product_data,
)

cursor.executemany(
    """INSERT INTO discounts (id, applicable_product_ids, value) VALUES (?, ?, ?)""",
    discount_data,
)

cursor.executemany(
    """INSERT INTO all_time (payment_type, revenue) VALUES (?, ?)""",
    all_time_data,
)

connection.commit()

connection.close()
