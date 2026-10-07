-- =========================================================
-- Seed data: CATEGORY, SUPPLIER, PRODUCT
-- Run in order (parents before children).
-- =========================================================

-- ---------- CATEGORY (no identity, so IDs are explicit) ----------
INSERT INTO CATEGORY (CATEGORY_ID, CATEGORY_NAME, DESCRIPTION) VALUES (1, 'Electronics',     'Phones, laptops, accessories and gadgets');
INSERT INTO CATEGORY (CATEGORY_ID, CATEGORY_NAME, DESCRIPTION) VALUES (2, 'Home Appliances', 'Kitchen and household appliances');
INSERT INTO CATEGORY (CATEGORY_ID, CATEGORY_NAME, DESCRIPTION) VALUES (3, 'Stationery',      'Office and school supplies');
INSERT INTO CATEGORY (CATEGORY_ID, CATEGORY_NAME, DESCRIPTION) VALUES (4, 'Groceries',       'Packaged food and daily essentials');
INSERT INTO CATEGORY (CATEGORY_ID, CATEGORY_NAME, DESCRIPTION) VALUES (5, 'Furniture',       'Office and home furniture');
INSERT INTO CATEGORY (CATEGORY_ID, CATEGORY_NAME, DESCRIPTION) VALUES (6, 'Personal Care',   'Health, hygiene and grooming products');

-- ---------- SUPPLIER ----------
INSERT INTO SUPPLIER (SUPPLIER_ID, SUPPLIER_NAME, PHONE, EMAIL, ADDRESS) VALUES (1, 'TechWorld Distributors', '9840012345', 'sales@techworld.in',      '12 Mount Road, Chennai, TN 600002');
INSERT INTO SUPPLIER (SUPPLIER_ID, SUPPLIER_NAME, PHONE, EMAIL, ADDRESS) VALUES (2, 'HomeNeeds Traders',      '9841198765', 'orders@homeneeds.in',     '45 Anna Salai, Chennai, TN 600006');
INSERT INTO SUPPLIER (SUPPLIER_ID, SUPPLIER_NAME, PHONE, EMAIL, ADDRESS) VALUES (3, 'PaperPlus Supplies',     '9884455667', 'contact@paperplus.in',    '8 Ranganathan Street, T Nagar, Chennai, TN 600017');
INSERT INTO SUPPLIER (SUPPLIER_ID, SUPPLIER_NAME, PHONE, EMAIL, ADDRESS) VALUES (4, 'FreshMart Wholesale',    '9789012233', 'supply@freshmart.in',     '22 Koyambedu Market, Chennai, TN 600107');
INSERT INTO SUPPLIER (SUPPLIER_ID, SUPPLIER_NAME, PHONE, EMAIL, ADDRESS) VALUES (5, 'WoodCraft Furnishings',  '9962233445', 'info@woodcraft.in',       '67 GST Road, Tambaram, Chennai, TN 600045');
INSERT INTO SUPPLIER (SUPPLIER_ID, SUPPLIER_NAME, PHONE, EMAIL, ADDRESS) VALUES (6, 'CareWell Distributors',  '9500123456', 'sales@carewell.in',       '3 Avinashi Road, Coimbatore, TN 641018');

-- ---------- PRODUCT (30 rows) ----------
-- Electronics (cat 1, supplier 1)
INSERT INTO PRODUCT (PRODUCT_ID, PRODUCT_NAME, DESCRIPTION, CATEGORY_ID, SUPPLIER_ID, PRICE, QUANTITY, REORDER_LEVEL, STATUS) VALUES (1,  'Wireless Mouse',          '2.4GHz optical wireless mouse',             1, 1,   599.00, 120, 20, 'ACTIVE');
INSERT INTO PRODUCT (PRODUCT_ID, PRODUCT_NAME, DESCRIPTION, CATEGORY_ID, SUPPLIER_ID, PRICE, QUANTITY, REORDER_LEVEL, STATUS) VALUES (2,  'Mechanical Keyboard',     'RGB backlit mechanical keyboard, blue switches', 1, 1, 2499.00,  35, 10, 'ACTIVE');
INSERT INTO PRODUCT (PRODUCT_ID, PRODUCT_NAME, DESCRIPTION, CATEGORY_ID, SUPPLIER_ID, PRICE, QUANTITY, REORDER_LEVEL, STATUS) VALUES (3,  'USB-C Charger 65W',       'Fast charger for laptops and phones',       1, 1,  1799.00,   4, 10, 'ACTIVE');
INSERT INTO PRODUCT (PRODUCT_ID, PRODUCT_NAME, DESCRIPTION, CATEGORY_ID, SUPPLIER_ID, PRICE, QUANTITY, REORDER_LEVEL, STATUS) VALUES (4,  'Bluetooth Earbuds',       'True wireless earbuds with charging case',  1, 1,  1999.00,  60, 15, 'ACTIVE');
INSERT INTO PRODUCT (PRODUCT_ID, PRODUCT_NAME, DESCRIPTION, CATEGORY_ID, SUPPLIER_ID, PRICE, QUANTITY, REORDER_LEVEL, STATUS) VALUES (5,  '24-inch LED Monitor',     'Full HD IPS monitor with HDMI',             1, 1,  9499.00,  12,  5, 'ACTIVE');
INSERT INTO PRODUCT (PRODUCT_ID, PRODUCT_NAME, DESCRIPTION, CATEGORY_ID, SUPPLIER_ID, PRICE, QUANTITY, REORDER_LEVEL, STATUS) VALUES (6,  'Pen Drive 64GB',          'USB 3.0 flash drive',                       1, 1,   549.00, 200, 30, 'ACTIVE');

-- Home Appliances (cat 2, supplier 2)
INSERT INTO PRODUCT (PRODUCT_ID, PRODUCT_NAME, DESCRIPTION, CATEGORY_ID, SUPPLIER_ID, PRICE, QUANTITY, REORDER_LEVEL, STATUS) VALUES (7,  'Electric Kettle 1.5L',    'Stainless steel auto shut-off kettle',      2, 2,  1299.00,  40, 10, 'ACTIVE');
INSERT INTO PRODUCT (PRODUCT_ID, PRODUCT_NAME, DESCRIPTION, CATEGORY_ID, SUPPLIER_ID, PRICE, QUANTITY, REORDER_LEVEL, STATUS) VALUES (8,  'Mixer Grinder 750W',      '3-jar mixer grinder',                       2, 2,  3899.00,  18,  5, 'ACTIVE');
INSERT INTO PRODUCT (PRODUCT_ID, PRODUCT_NAME, DESCRIPTION, CATEGORY_ID, SUPPLIER_ID, PRICE, QUANTITY, REORDER_LEVEL, STATUS) VALUES (9,  'Steam Iron',              '1600W steam iron with non-stick soleplate', 2, 2,  1499.00,   3,  5, 'ACTIVE');
INSERT INTO PRODUCT (PRODUCT_ID, PRODUCT_NAME, DESCRIPTION, CATEGORY_ID, SUPPLIER_ID, PRICE, QUANTITY, REORDER_LEVEL, STATUS) VALUES (10, 'Table Fan',               '400mm high-speed table fan',                2, 2,  2199.00,  25,  8, 'ACTIVE');
INSERT INTO PRODUCT (PRODUCT_ID, PRODUCT_NAME, DESCRIPTION, CATEGORY_ID, SUPPLIER_ID, PRICE, QUANTITY, REORDER_LEVEL, STATUS) VALUES (11, 'Induction Cooktop',       '2000W touch-control induction stove',       2, 2,  2799.00,  15,  5, 'ACTIVE');
INSERT INTO PRODUCT (PRODUCT_ID, PRODUCT_NAME, DESCRIPTION, CATEGORY_ID, SUPPLIER_ID, PRICE, QUANTITY, REORDER_LEVEL, STATUS) VALUES (12, 'Pop-up Toaster',          '2-slice toaster (discontinued model)',      2, 2,  1199.00,   0,  5, 'INACTIVE');

-- Stationery (cat 3, supplier 3)
INSERT INTO PRODUCT (PRODUCT_ID, PRODUCT_NAME, DESCRIPTION, CATEGORY_ID, SUPPLIER_ID, PRICE, QUANTITY, REORDER_LEVEL, STATUS) VALUES (13, 'A4 Paper Ream',           '500 sheets, 75 GSM',                        3, 3,   289.00, 300, 50, 'ACTIVE');
INSERT INTO PRODUCT (PRODUCT_ID, PRODUCT_NAME, DESCRIPTION, CATEGORY_ID, SUPPLIER_ID, PRICE, QUANTITY, REORDER_LEVEL, STATUS) VALUES (14, 'Ball Pen Pack (10)',      'Blue ink ball pens, pack of 10',            3, 3,    60.00, 500, 100,'ACTIVE');
INSERT INTO PRODUCT (PRODUCT_ID, PRODUCT_NAME, DESCRIPTION, CATEGORY_ID, SUPPLIER_ID, PRICE, QUANTITY, REORDER_LEVEL, STATUS) VALUES (15, 'Spiral Notebook',         '200 pages, ruled, A5',                      3, 3,    85.00, 250, 40, 'ACTIVE');
INSERT INTO PRODUCT (PRODUCT_ID, PRODUCT_NAME, DESCRIPTION, CATEGORY_ID, SUPPLIER_ID, PRICE, QUANTITY, REORDER_LEVEL, STATUS) VALUES (16, 'Stapler',                 'Heavy-duty stapler with 1000 pins',         3, 3,   175.00,   8, 15, 'ACTIVE');
INSERT INTO PRODUCT (PRODUCT_ID, PRODUCT_NAME, DESCRIPTION, CATEGORY_ID, SUPPLIER_ID, PRICE, QUANTITY, REORDER_LEVEL, STATUS) VALUES (17, 'Whiteboard Marker Set',   '4 colours, refillable',                     3, 3,   120.00,  90, 20, 'ACTIVE');

-- Groceries (cat 4, supplier 4)
INSERT INTO PRODUCT (PRODUCT_ID, PRODUCT_NAME, DESCRIPTION, CATEGORY_ID, SUPPLIER_ID, PRICE, QUANTITY, REORDER_LEVEL, STATUS) VALUES (18, 'Basmati Rice 5kg',        'Premium long-grain basmati rice',           4, 4,   649.00,  80, 20, 'ACTIVE');
INSERT INTO PRODUCT (PRODUCT_ID, PRODUCT_NAME, DESCRIPTION, CATEGORY_ID, SUPPLIER_ID, PRICE, QUANTITY, REORDER_LEVEL, STATUS) VALUES (19, 'Sunflower Oil 1L',        'Refined sunflower oil',                     4, 4,   165.00, 150, 30, 'ACTIVE');
INSERT INTO PRODUCT (PRODUCT_ID, PRODUCT_NAME, DESCRIPTION, CATEGORY_ID, SUPPLIER_ID, PRICE, QUANTITY, REORDER_LEVEL, STATUS) VALUES (20, 'Filter Coffee Powder 500g','South Indian filter coffee blend',         4, 4,   320.00,  10, 25, 'ACTIVE');
INSERT INTO PRODUCT (PRODUCT_ID, PRODUCT_NAME, DESCRIPTION, CATEGORY_ID, SUPPLIER_ID, PRICE, QUANTITY, REORDER_LEVEL, STATUS) VALUES (21, 'Toor Dal 1kg',            'Unpolished toor dal',                       4, 4,   175.00, 110, 25, 'ACTIVE');
INSERT INTO PRODUCT (PRODUCT_ID, PRODUCT_NAME, DESCRIPTION, CATEGORY_ID, SUPPLIER_ID, PRICE, QUANTITY, REORDER_LEVEL, STATUS) VALUES (22, 'Green Tea (100 bags)',    'Natural green tea bags',                    4, 4,   399.00,  45, 10, 'ACTIVE');

-- Furniture (cat 5, supplier 5)
INSERT INTO PRODUCT (PRODUCT_ID, PRODUCT_NAME, DESCRIPTION, CATEGORY_ID, SUPPLIER_ID, PRICE, QUANTITY, REORDER_LEVEL, STATUS) VALUES (23, 'Ergonomic Office Chair',  'Mesh back chair with lumbar support',       5, 5,  7499.00,  14,  5, 'ACTIVE');
INSERT INTO PRODUCT (PRODUCT_ID, PRODUCT_NAME, DESCRIPTION, CATEGORY_ID, SUPPLIER_ID, PRICE, QUANTITY, REORDER_LEVEL, STATUS) VALUES (24, 'Study Table',             'Engineered wood table with drawer',         5, 5,  5999.00,   6,  3, 'ACTIVE');
INSERT INTO PRODUCT (PRODUCT_ID, PRODUCT_NAME, DESCRIPTION, CATEGORY_ID, SUPPLIER_ID, PRICE, QUANTITY, REORDER_LEVEL, STATUS) VALUES (25, 'Bookshelf 5-Tier',        'Open bookshelf, walnut finish',             5, 5,  4299.00,   2,  3, 'ACTIVE');
INSERT INTO PRODUCT (PRODUCT_ID, PRODUCT_NAME, DESCRIPTION, CATEGORY_ID, SUPPLIER_ID, PRICE, QUANTITY, REORDER_LEVEL, STATUS) VALUES (26, 'Plastic Stool',           'Stackable plastic stool (old stock)',       5, 5,   399.00,   0,  5, 'INACTIVE');

-- Personal Care (cat 6, supplier 6)
INSERT INTO PRODUCT (PRODUCT_ID, PRODUCT_NAME, DESCRIPTION, CATEGORY_ID, SUPPLIER_ID, PRICE, QUANTITY, REORDER_LEVEL, STATUS) VALUES (27, 'Herbal Shampoo 340ml',    'Paraben-free herbal shampoo',               6, 6,   245.00,  95, 20, 'ACTIVE');
INSERT INTO PRODUCT (PRODUCT_ID, PRODUCT_NAME, DESCRIPTION, CATEGORY_ID, SUPPLIER_ID, PRICE, QUANTITY, REORDER_LEVEL, STATUS) VALUES (28, 'Toothpaste 150g',         'Fluoride toothpaste',                       6, 6,   110.00, 180, 40, 'ACTIVE');
INSERT INTO PRODUCT (PRODUCT_ID, PRODUCT_NAME, DESCRIPTION, CATEGORY_ID, SUPPLIER_ID, PRICE, QUANTITY, REORDER_LEVEL, STATUS) VALUES (29, 'Hand Sanitizer 500ml',    '70% alcohol hand sanitizer',                6, 6,   199.00,  12, 20, 'ACTIVE');
INSERT INTO PRODUCT (PRODUCT_ID, PRODUCT_NAME, DESCRIPTION, CATEGORY_ID, SUPPLIER_ID, PRICE, QUANTITY, REORDER_LEVEL, STATUS) VALUES (30, 'Electric Trimmer',        'Rechargeable beard trimmer',                6, 6,  1349.00,  22,  5, 'ACTIVE');

COMMIT;

-- ---------- Resync identity columns ----------
-- Because explicit IDs were inserted, move each identity generator past
-- the current max so new rows from the app don't hit ORA-00001.
ALTER TABLE SUPPLIER MODIFY SUPPLIER_ID GENERATED BY DEFAULT AS IDENTITY (START WITH LIMIT VALUE);
ALTER TABLE PRODUCT  MODIFY PRODUCT_ID  GENERATED BY DEFAULT AS IDENTITY (START WITH LIMIT VALUE);

-- ---------- Quick checks ----------
-- SELECT COUNT(*) FROM PRODUCT;   -- expect 30
-- Low-stock items (QUANTITY <= REORDER_LEVEL):
-- SELECT PRODUCT_NAME, QUANTITY, REORDER_LEVEL FROM PRODUCT
--  WHERE STATUS = 'ACTIVE' AND QUANTITY <= REORDER_LEVEL;