import sqlite3

connection = sqlite3.connect("../../store.db")

cursor = connection.cursor()

cursor.execute("DROP TABLE IF EXISTS sold_products")
cursor.execute("DROP TABLE IF EXISTS receipts")
cursor.execute("DROP TABLE IF EXISTS products")
cursor.execute("DROP TABLE IF EXISTS units")

cursor.execute(
    """CREATE TABLE units (
                id TEXT PRIMARY KEY,
                name TEXT UNIQUE
)"""
)

cursor.execute(
    """CREATE TABLE products (
                    id TEXT PRIMARY KEY,
                    unit_id TEXT,
                    name TEXT,
                    barcode TEXT UNIQUE,
                    price INTEGER
                )"""
)

cursor.execute(
    """
    CREATE TABLE receipts (
        id TEXT PRIMARY KEY,
        status TEXT,
        total INTEGER
    )
"""
)

cursor.execute(
    """
    CREATE TABLE sold_products (
        receipt_id TEXT,
        product_id TEXT,
        quantity INTEGER,
        FOREIGN KEY(receipt_id) REFERENCES receipts(id) ON DELETE CASCADE,
        FOREIGN KEY(product_id) REFERENCES products(id),
        PRIMARY KEY (receipt_id, product_id)
    )
"""
)

cursor.execute(
    """
    CREATE TRIGGER IF NOT EXISTS delete_sold_products
    AFTER DELETE ON receipts
    BEGIN
        DELETE FROM sold_products WHERE receipt_id = OLD.id;
    END;
"""
)

connection.commit()

connection.close()
