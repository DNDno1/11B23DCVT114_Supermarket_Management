-- Online Supermarket Management System Database Schema & Sample Data
-- Matches ER Diagram in Design Document (Subject No. 11)

CREATE DATABASE IF NOT EXISTS supermarket_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE supermarket_db;

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS tblSaleInvoiceItem4;
DROP TABLE IF EXISTS tblSaleInvoice4;
DROP TABLE IF EXISTS tblOrderItem4;
DROP TABLE IF EXISTS tblOrder4;
DROP TABLE IF EXISTS tblImportInvoice4;
DROP TABLE IF EXISTS tblItem4;
DROP TABLE IF EXISTS tblSupplier4;
DROP TABLE IF EXISTS tblSaler4;
DROP TABLE IF EXISTS tblWarehouseStaff4;
DROP TABLE IF EXISTS tblManagementStaff4;
DROP TABLE IF EXISTS tblCustomer4;
DROP TABLE IF EXISTS tblStaff4;
DROP TABLE IF EXISTS tblUser4;

SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE tblUser4 (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    birthdate DATE NULL,
    address VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phonenumber VARCHAR(255) NULL,
    note VARCHAR(255) NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE tblStaff4 (
    tblUser4id INT PRIMARY KEY,
    role VARCHAR(255) NOT NULL,
    CONSTRAINT fk_staff_user FOREIGN KEY (tblUser4id) REFERENCES tblUser4(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE tblManagementStaff4 (
    StafftblUser4id INT PRIMARY KEY,
    CONSTRAINT fk_mgmt_staff FOREIGN KEY (StafftblUser4id) REFERENCES tblStaff4(tblUser4id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE tblWarehouseStaff4 (
    StafftblUser4id INT PRIMARY KEY,
    CONSTRAINT fk_warehouse_staff FOREIGN KEY (StafftblUser4id) REFERENCES tblStaff4(tblUser4id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE tblSaler4 (
    StafftblUser4id INT PRIMARY KEY,
    CONSTRAINT fk_saler_staff FOREIGN KEY (StafftblUser4id) REFERENCES tblStaff4(tblUser4id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE tblCustomer4 (
    tblUser4id INT PRIMARY KEY,
    AccCode VARCHAR(255) NOT NULL UNIQUE,
    CONSTRAINT fk_customer_user FOREIGN KEY (tblUser4id) REFERENCES tblUser4(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE tblSupplier4 (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phonenumber VARCHAR(255) NOT NULL,
    note VARCHAR(255) NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE tblItem4 (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    quantity INT NOT NULL DEFAULT 0,
    price INT NOT NULL,
    ImportPrice INT NOT NULL,
    note VARCHAR(255) NULL,
    tblSupplier4id INT NOT NULL,
    CONSTRAINT fk_item_supplier FOREIGN KEY (tblSupplier4id) REFERENCES tblSupplier4(id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE tblImportInvoice4 (
    id INT AUTO_INCREMENT PRIMARY KEY,
    Datetime DATE NOT NULL,
    quantity INT NOT NULL,
    Duetime DATE NULL,
    totalAmount INT NOT NULL,
    tblWarehouseStaff4id INT NOT NULL,
    tblItem4id INT NOT NULL,
    CONSTRAINT fk_import_wstaff FOREIGN KEY (tblWarehouseStaff4id) REFERENCES tblWarehouseStaff4(StafftblUser4id),
    CONSTRAINT fk_import_item FOREIGN KEY (tblItem4id) REFERENCES tblItem4(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE tblOrder4 (
    id INT AUTO_INCREMENT PRIMARY KEY,
    Datetime DATE NOT NULL,
    status VARCHAR(255) NOT NULL,
    totalAmount INT NOT NULL,
    tblCustomer4id INT NOT NULL,
    tblWarehouseStaff4id INT NULL,
    CONSTRAINT fk_order_customer FOREIGN KEY (tblCustomer4id) REFERENCES tblCustomer4(tblUser4id),
    CONSTRAINT fk_order_wstaff FOREIGN KEY (tblWarehouseStaff4id) REFERENCES tblWarehouseStaff4(StafftblUser4id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE tblOrderItem4 (
    id INT AUTO_INCREMENT PRIMARY KEY,
    quantity INT NOT NULL,
    totalAmount INT NOT NULL,
    tblItem4id INT NOT NULL,
    tblOrder4id INT NOT NULL,
    CONSTRAINT fk_orderitem_item FOREIGN KEY (tblItem4id) REFERENCES tblItem4(id) ON DELETE CASCADE,
    CONSTRAINT fk_orderitem_order FOREIGN KEY (tblOrder4id) REFERENCES tblOrder4(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE tblSaleInvoice4 (
    id INT AUTO_INCREMENT PRIMARY KEY,
    Datetime DATE NOT NULL,
    totalAmount INT NOT NULL,
    tblSaler4id INT NOT NULL,
    tblCustomer4id INT NULL,
    CONSTRAINT fk_sale_saler FOREIGN KEY (tblSaler4id) REFERENCES tblSaler4(StafftblUser4id),
    CONSTRAINT fk_sale_customer FOREIGN KEY (tblCustomer4id) REFERENCES tblCustomer4(tblUser4id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE tblSaleInvoiceItem4 (
    id INT AUTO_INCREMENT PRIMARY KEY,
    quantity INT NOT NULL,
    totalAmount INT NOT NULL,
    tblItem4id INT NOT NULL,
    tblSaleInvoice4id INT NOT NULL,
    CONSTRAINT fk_saleitem_item FOREIGN KEY (tblItem4id) REFERENCES tblItem4(id) ON DELETE CASCADE,
    CONSTRAINT fk_saleitem_invoice FOREIGN KEY (tblSaleInvoice4id) REFERENCES tblSaleInvoice4(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- Seed Data
-- Demo credentials (password: 123456 for all demo accounts):
--   manager   / 123456 (ManagementStaff)
--   warehouse / 123456 (WarehouseStaff)
--   saler     / 123456 (Saler)
--   customer1 / 123456 (Customer)
--   customer2 / 123456 (Customer)
-- ============================================================

INSERT INTO tblUser4 (id, name, username, password, birthdate, address, email, phonenumber, note) VALUES
(1, 'Nguyễn Văn Quản Lý', 'manager', '123456', '1988-05-12', 'Hà Nội', 'manager@supermarket.vn', '0901111111', 'Nhân viên quản lý'),
(2, 'Trần Thị Kho', 'warehouse', '123456', '1992-08-20', 'Hà Nội', 'warehouse@supermarket.vn', '0902222222', 'Nhân viên kho'),
(3, 'Lê Minh Bán Hàng', 'saler', '123456', '1996-03-15', 'Hà Nội', 'saler@supermarket.vn', '0903333333', 'Nhân viên bán hàng'),
(4, 'Phạm Khách Hàng', 'customer1', '123456', '1999-11-01', 'Cầu Giấy, Hà Nội', 'customer1@gmail.com', '0914444444', 'Thành viên thân thiết'),
(5, 'Hoàng Thị Mai', 'customer2', '123456', '2001-02-14', 'Đống Đa, Hà Nội', 'customer2@gmail.com', '0915555555', 'Khách hàng online');

INSERT INTO tblStaff4 (tblUser4id, role) VALUES
(1, 'ManagementStaff'),
(2, 'WarehouseStaff'),
(3, 'Saler');

INSERT INTO tblManagementStaff4 (StafftblUser4id) VALUES (1);
INSERT INTO tblWarehouseStaff4 (StafftblUser4id) VALUES (2);
INSERT INTO tblSaler4 (StafftblUser4id) VALUES (3);

INSERT INTO tblCustomer4 (tblUser4id, AccCode) VALUES
(4, 'CUS-0004'),
(5, 'CUS-0005');

INSERT INTO tblSupplier4 (id, name, address, email, phonenumber, note) VALUES
(1, 'ABCchop', 'Hà Nội', 'contact@abcchop.vn', '0123456789', 'Chuyên cung cấp thịt tươi và thịt chế biến'),
(2, 'BBCnut', 'Thái Nguyên', 'sales@bbcnut.vn', '0676767676', 'Chuyên cung cấp hạt dinh dưỡng và đồ khô'),
(3, 'Vinamilk Fresh', 'TP. Hồ Chí Minh', 'partner@vinamilk.vn', '0285415555', 'Sữa tươi, sữa chua và chế phẩm sữa'),
(4, 'GreenFarm', 'Đà Lạt', 'farm@greenfarm.vn', '0263382211', 'Rau củ quả hữu cơ sạch');

INSERT INTO tblItem4 (id, name, quantity, price, ImportPrice, note, tblSupplier4id) VALUES
(1, 'Thịt ba chỉ A', 45, 23000, 18000, 'Mỗi gói nặng 0.5 kg, đóng khay hút chân không', 1),
(2, 'Thịt hộp ABCchop', 30, 24000, 17000, 'Hộp 200g tiện lợi, bảo quản nơi khô ráo', 1),
(3, 'Thịt bít tết bò Úc', 24, 145000, 110000, 'Phần thăn ngoại 300g chuẩn nhà hàng', 1),
(4, 'Hạt điều rang muối BBC', 60, 85000, 60000, 'Hũ 500g hạt điều Bình Phước loại 1', 2),
(5, 'Hạnh nhân sấy bơ', 40, 110000, 80000, 'Túi zip 400g nhập khẩu', 2),
(6, 'Sữa tươi tiệt trùng 1L', 120, 36000, 28000, 'Hộp 1 lít ít đường giàu canxi', 3),
(7, 'Sữa chua có đường (Lốc 4)', 80, 28000, 21000, 'Lốc 4 hộp x 100g lên men tự nhiên', 3),
(8, 'Cà chua bi hữu cơ', 35, 32000, 22000, 'Hộp 500g thu hoạch trong ngày', 4),
(9, 'Xà lách thủy canh', 0, 25000, 16000, 'Túi 300g sạch chuẩn VietGAP', 4);

INSERT INTO tblImportInvoice4 (id, Datetime, quantity, Duetime, totalAmount, tblWarehouseStaff4id, tblItem4id) VALUES
(1, '2026-06-07', 60, '2027-06-07', 9000000, 2, 3),
(2, '2026-07-25', 60, '2027-07-25', 1080000, 2, 1),
(3, '2026-08-10', 50, '2027-08-10', 850000, 2, 2),
(4, '2026-08-15', 80, '2027-02-15', 4800000, 2, 4),
(5, '2026-09-01', 150, '2027-03-01', 4200000, 2, 6),
(6, '2026-09-16', 60, '2026-09-20', 1260000, 2, 1);

INSERT INTO tblOrder4 (id, Datetime, status, totalAmount, tblCustomer4id, tblWarehouseStaff4id) VALUES
(1, '2026-06-20', 'Completed', 5220000, 4, 2),
(2, '2026-08-05', 'Completed', 1936000, 5, 2),
(3, '2026-09-10', 'Pending', 330000, 4, NULL),
(4, '2026-09-18', 'Pending', 355000, 5, NULL);

INSERT INTO tblOrderItem4 (quantity, totalAmount, tblItem4id, tblOrder4id) VALUES
(36, 5220000, 3, 1),
(32, 736000, 1, 2),
(20, 600000, 4, 2),
(10, 600000, 5, 2),
(5, 180000, 6, 3),
(3, 150000, 4, 3),
(2, 290000, 3, 4),
(2, 32500, 8, 4);

INSERT INTO tblSaleInvoice4 (id, Datetime, totalAmount, tblSaler4id, tblCustomer4id) VALUES
(1, '2026-07-12', 410000, 3, 4),
(2, '2026-08-22', 640000, 3, NULL),
(3, '2026-09-15', 518000, 3, 5);

INSERT INTO tblSaleInvoiceItem4 (quantity, totalAmount, tblItem4id, tblSaleInvoice4id) VALUES
(5, 115000, 1, 1),
(2, 290000, 3, 1),
(10, 240000, 2, 2),
(4, 400000, 4, 2),
(8, 288000, 6, 3),
(5, 140000, 7, 3),
(3, 90000, 8, 3);
