-- Seed data for the lessons
INSERT INTO sqltoy_organ_info (ORGAN_ID, ORGAN_NAME, SHOW_INDEX, UPDATE_TIME) VALUES
('100001', 'Head Office', 1, CURRENT_TIMESTAMP),
('100005', 'R&D Center', 2, CURRENT_TIMESTAMP),
('100007', 'Sales Dept', 3, CURRENT_TIMESTAMP);

INSERT INTO sqltoy_dict_detail (DICT_TYPE, DICT_KEY, DICT_NAME, STATUS, SHOW_INDEX, UPDATE_TIME) VALUES
('SEX_TYPE', 'M', 'Male', 1, 1, CURRENT_TIMESTAMP),
('SEX_TYPE', 'F', 'Female', 1, 2, CURRENT_TIMESTAMP),
('DEVICE_TYPE', 'SERVER', 'rack server', 1, 1, CURRENT_TIMESTAMP),
('DEVICE_TYPE', 'LAPTOP', 'laptop', 1, 2, CURRENT_TIMESTAMP),
('DEVICE_TYPE', 'PRINTER', 'printer', 1, 3, CURRENT_TIMESTAMP);

INSERT INTO sqltoy_staff_info (STAFF_ID, STAFF_CODE, STAFF_NAME, ORGAN_ID, SEX_TYPE, ENTRY_DATE, STATUS, EMAIL) VALUES
('S0001', 'S0001', 'Alice Chen', '100005', 'F', DATE '2019-03-11', 1, 'alice.chen@example.com'),
('S0002', 'S0002', 'Bob Zhang', '100005', 'M', DATE '2019-07-22', 1, 'bob.zhang@example.com'),
('S0003', 'S0003', 'Carol Wang', '100007', 'F', DATE '2020-01-15', 1, 'carol.wang@example.com'),
('S0004', 'S0004', 'David Li', '100005', 'M', DATE '2020-05-06', 1, 'david.li@example.com'),
('S0005', 'S0005', 'Emma Liu', '100001', 'F', DATE '2020-09-30', 1, 'emma.liu@example.com'),
('S0006', 'S0006', 'Frank Wu', '100007', 'M', DATE '2021-02-18', 1, 'frank.wu@example.com'),
('S0007', 'S0007', 'Grace Zhao', '100005', 'F', DATE '2021-06-07', 1, 'grace.zhao@example.com'),
('S0008', 'S0008', 'Henry Sun', '100007', 'M', DATE '2021-11-23', 1, 'henry.sun@example.com'),
('S0009', 'S0009', 'Ivy Zhou', '100001', 'F', DATE '2022-03-14', 1, 'ivy.zhou@example.com'),
('S0010', 'S0010', 'Jack Chen', '100005', 'M', DATE '2022-08-02', 1, 'jack.chen@example.com'),
('S0011', 'S0011', 'Karen Yang', '100007', 'F', DATE '2023-01-09', 1, 'karen.yang@example.com'),
('S0012', 'S0012', 'Leo Huang', '100005', 'M', DATE '2023-04-27', 1, 'leo.huang@example.com'),
('S0013', 'S0013', 'Mia Xu', '100001', 'F', DATE '2023-09-11', 1, 'mia.xu@example.com'),
('S0014', 'S0014', 'Noah Zheng', '100007', 'M', DATE '2024-02-29', 1, 'noah.zheng@example.com'),
('S0015', 'S0015', 'Olivia Gao', '100005', 'F', DATE '2024-06-17', 1, 'olivia.gao@example.com'),
('S0016', 'S0016', 'Peter Lin', '100007', 'M', DATE '2024-10-08', 1, 'peter.lin@example.com'),
('S0017', 'S0017', 'Quinn Feng', '100001', 'M', DATE '2025-03-03', 1, 'quinn.feng@example.com'),
('S0018', 'S0018', 'Ruth Han', '100005', 'F', DATE '2025-07-21', 1, 'ruth.han@example.com'),
('S0019', 'S0019', 'Samuel Ma', '100007', 'M', DATE '2026-01-12', 1, 'samuel.ma@example.com'),
('S0020', 'S0020', 'Tina Cao', '100001', 'F', DATE '2026-05-05', 0, 'tina.cao@example.com');

-- pre-existing orders so Lesson 5 aggregates on real data (keys hand-written in the same PO@df(yyMMdd)+sequence shape)
INSERT INTO lesson_device_order (ORDER_ID, BUYER, ORGAN_ID, DEVICE_TYPE, TRANS_DATE, TOTAL_AMT, STATUS, CREATE_TIME) VALUES
('PO26010100000001', 'C00001', '100007', 'SERVER', DATE '2026-01-05', 125000.00, 1, CURRENT_TIMESTAMP),
('PO26020900000001', 'C00002', '100005', 'LAPTOP', DATE '2026-02-09', 38500.00, 1, CURRENT_TIMESTAMP),
('PO26031800000001', 'C00003', '100007', 'PRINTER', DATE '2026-03-18', 6200.00, 1, CURRENT_TIMESTAMP),
('PO26042200000001', 'C00004', '100005', 'SERVER', DATE '2026-04-22', 98000.00, 1, CURRENT_TIMESTAMP),
('PO26053000000001', 'C00005', '100001', 'LAPTOP', DATE '2026-05-30', 26400.00, 1, CURRENT_TIMESTAMP);

-- Lesson 6: one ledger row (1000 units) that concurrent threads will deduct from
INSERT INTO lesson_trans_ledger (ORDER_ID, QUANTITY, AMT, STATUS, CREATE_TIME) VALUES
('LEDGER-01', 1000, 500000.00, 1, CURRENT_TIMESTAMP);

-- Lesson 7: three products over three months
INSERT INTO lesson_monthly_sales (PRODUCT, ORDER_MONTH, SALE_COUNT, TOTAL_AMT, STATUS, CREATE_TIME) VALUES
('Laptop',  '2026-01', 120, 96000.00, 1, CURRENT_TIMESTAMP),
('Laptop',  '2026-02', 135, 108000.00, 1, CURRENT_TIMESTAMP),
('Laptop',  '2026-03', 150, 120000.00, 1, CURRENT_TIMESTAMP),
('Printer', '2026-01', 80,  24000.00, 1, CURRENT_TIMESTAMP),
('Printer', '2026-02', 72,  21600.00, 1, CURRENT_TIMESTAMP),
('Printer', '2026-03', 90,  27000.00, 1, CURRENT_TIMESTAMP),
('Server',  '2026-01', 30,  45000.00, 1, CURRENT_TIMESTAMP),
('Server',  '2026-02', 42,  63000.00, 1, CURRENT_TIMESTAMP),
('Server',  '2026-03', 36,  54000.00, 1, CURRENT_TIMESTAMP);
