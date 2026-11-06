-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Oct 13, 2026 at 02:38 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `pawsyoudb`
--

-- --------------------------------------------------------

--
-- Table structure for table `batches`
--

CREATE TABLE `batches` (
  `batch_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `supplier_id` int(11) NOT NULL,
  `purchase_detail_id` int(11) DEFAULT NULL,
  `quantity` decimal(10,2) NOT NULL DEFAULT 0.00,
  `expiration_date` date DEFAULT NULL,
  `received_date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `batches`
--

INSERT INTO `batches` (`batch_id`, `product_id`, `supplier_id`, `purchase_detail_id`, `quantity`, `expiration_date`, `received_date`) VALUES
(1, 1, 1, NULL, 55.00, '2028-10-13', '2026-10-11'),
(2, 2, 2, NULL, 30.00, NULL, '2026-10-11'),
(3, 3, 3, NULL, 1900.00, '2029-10-23', '2016-10-23'),
(4, 4, 4, NULL, 34.00, '2028-10-28', '2026-10-11'),
(5, 5, 5, NULL, 45.00, NULL, '2026-10-11'),
(6, 6, 6, NULL, 0.00, '2027-10-13', '2026-10-13'),
(7, 7, 7, NULL, 10.00, NULL, '2026-10-12'),
(8, 8, 8, NULL, 300.00, NULL, '2026-10-09'),
(9, 1, 9, NULL, 0.00, '2027-10-21', '2025-10-31'),
(10, 1, 10, NULL, 100.00, '2029-12-21', '2026-10-21'),
(11, 1, 10, NULL, 100.00, '2026-12-31', '2026-10-21'),
(12, 1, 6, NULL, 900.00, '2027-10-24', '2026-10-21'),
(13, 1, 11, NULL, 312.00, '2026-10-22', '2026-10-21'),
(14, 1, 12, NULL, 21.00, '2027-10-21', '2026-10-21'),
(15, 1, 5, NULL, 233.00, '2027-10-23', '2026-10-12'),
(16, 1, 5, NULL, 233.00, '2027-10-23', '2026-10-12'),
(17, 1, 8, NULL, 23.00, '2027-10-24', '2026-10-23'),
(18, 1, 7, NULL, 23.00, '2027-10-24', '2026-10-21'),
(19, 1, 11, NULL, 1000.00, '2025-10-20', '2026-10-14');

-- --------------------------------------------------------

--
-- Table structure for table `customers`
--

CREATE TABLE `customers` (
  `customer_id` int(11) NOT NULL,
  `customer_name` varchar(100) NOT NULL,
  `contact_number` varchar(30) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `products`
--

CREATE TABLE `products` (
  `product_id` int(11) NOT NULL,
  `product_name` varchar(100) NOT NULL,
  `brand` varchar(100) NOT NULL DEFAULT '',
  `category` varchar(50) NOT NULL,
  `unit` varchar(30) NOT NULL,
  `unit_price` decimal(10,2) NOT NULL DEFAULT 0.00,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `products`
--

INSERT INTO `products` (`product_id`, `product_name`, `brand`, `category`, `unit`, `unit_price`, `created_at`) VALUES
(1, 'Dog Food', 'Pedigree', 'Food', 'kg', 230.00, '2026-10-11 07:47:24'),
(2, 'Dog House', 'HouseRUs', 'Other', 'pack', 5000.00, '2026-10-11 08:09:13'),
(3, 'Cat Food', 'Whiskas', 'Food', 'pack', 80.00, '2026-10-11 09:04:47'),
(4, 'efdsa', 'asdsa', 'All', 'kg', 3423.00, '2026-10-11 09:42:38'),
(5, 'werfdf', 'sdfds', 'All', 'kg', 34.00, '2026-10-11 09:46:06'),
(6, 'Rosal', 'Rosal123', 'Medicine', 'mg', 1000.00, '2026-10-12 19:56:54'),
(7, 'Dawg House', 'Weliam', 'Accessories', 'pack', 50000.00, '2026-10-12 20:07:07'),
(8, 'wwwwwww', 'wwwwwww', 'All', 'kg', 22.00, '2026-10-12 20:09:47');

-- --------------------------------------------------------

--
-- Table structure for table `purchases`
--

CREATE TABLE `purchases` (
  `purchase_id` int(11) NOT NULL,
  `supplier_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `purchase_date` date NOT NULL,
  `status` varchar(30) NOT NULL DEFAULT 'Completed',
  `notes` varchar(255) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `purchase_details`
--

CREATE TABLE `purchase_details` (
  `purchase_detail_id` int(11) NOT NULL,
  `purchase_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `quantity` decimal(10,2) NOT NULL,
  `unit_cost` decimal(10,2) NOT NULL,
  `subtotal` decimal(12,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `sales`
--

CREATE TABLE `sales` (
  `sale_id` int(11) NOT NULL,
  `customer_id` int(11) DEFAULT NULL,
  `user_id` int(11) NOT NULL,
  `sale_date` datetime NOT NULL DEFAULT current_timestamp(),
  `payment_method` varchar(50) NOT NULL,
  `total_amount` decimal(12,2) NOT NULL DEFAULT 0.00,
  `status` varchar(30) NOT NULL DEFAULT 'Completed'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `sales`
--

INSERT INTO `sales` (`sale_id`, `customer_id`, `user_id`, `sale_date`, `payment_method`, `total_amount`, `status`) VALUES
(6, NULL, 2, '2026-10-13 14:21:29', 'Cash', 259650.00, 'Completed'),
(7, NULL, 2, '2026-10-13 14:51:59', 'Cash', 582000.00, 'Completed'),
(8, NULL, 2, '2026-10-13 14:59:38', 'Bank Transfer', 8946.00, 'Completed');

-- --------------------------------------------------------

--
-- Table structure for table `sale_details`
--

CREATE TABLE `sale_details` (
  `sale_detail_id` int(11) NOT NULL,
  `sale_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `batch_id` int(11) DEFAULT NULL,
  `quantity` decimal(10,2) NOT NULL,
  `unit_price` decimal(10,2) NOT NULL,
  `subtotal` decimal(12,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `sale_details`
--

INSERT INTO `sale_details` (`sale_detail_id`, `sale_id`, `product_id`, `batch_id`, `quantity`, `unit_price`, `subtotal`) VALUES
(1, 6, 3, NULL, 500.00, 80.00, 40000.00),
(2, 6, 1, NULL, 10.00, 230.00, 2300.00),
(3, 6, 1, NULL, 945.00, 230.00, 217350.00),
(4, 7, 3, NULL, 1000.00, 80.00, 80000.00),
(5, 7, 6, NULL, 2.00, 1000.00, 2000.00),
(6, 7, 7, NULL, 10.00, 50000.00, 500000.00),
(7, 8, 3, NULL, 100.00, 80.00, 8000.00),
(8, 8, 8, NULL, 43.00, 22.00, 946.00);

-- --------------------------------------------------------

--
-- Table structure for table `stock_movements`
--

CREATE TABLE `stock_movements` (
  `movement_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `batch_id` int(11) DEFAULT NULL,
  `user_id` int(11) NOT NULL,
  `movement_type` varchar(30) NOT NULL,
  `quantity` decimal(10,2) NOT NULL,
  `reason` varchar(255) DEFAULT NULL,
  `movement_date` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `suppliers`
--

CREATE TABLE `suppliers` (
  `supplier_id` int(11) NOT NULL,
  `supplier_name` varchar(100) NOT NULL,
  `contact_number` varchar(30) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `supplier_name_normalized` varchar(255) GENERATED ALWAYS AS (lcase(trim(`supplier_name`))) STORED
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `suppliers`
--

INSERT INTO `suppliers` (`supplier_id`, `supplier_name`, `contact_number`, `address`) VALUES
(1, 'Weliam Inc.', NULL, NULL),
(2, 'Dawg Solutions', NULL, NULL),
(3, 'CatDespair Trading', NULL, NULL),
(4, 'asdas', NULL, NULL),
(5, 'sdfd', NULL, NULL),
(6, 'COC', NULL, NULL),
(7, 'Rosal Inc.', NULL, NULL),
(8, 'wwwwwww', NULL, NULL),
(9, 'Rosal Inc', NULL, NULL),
(10, 'Doggy Dawg Solutions', NULL, NULL),
(11, 'asdsas', NULL, NULL),
(12, 'Dawg corp', NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` int(11) NOT NULL,
  `full_name` varchar(100) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` varchar(20) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `full_name`, `username`, `password`, `role`, `created_at`) VALUES
(1, 'System Administrator', 'admin', '123', 'ADMIN', '2026-10-13 05:50:14'),
(2, 'Staff Account', 'staff', '123', 'STAFF', '2026-10-13 05:50:14');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `batches`
--
ALTER TABLE `batches`
  ADD PRIMARY KEY (`batch_id`),
  ADD KEY `idx_batches_fifo` (`product_id`,`received_date`,`batch_id`),
  ADD KEY `idx_batches_expiration` (`expiration_date`),
  ADD KEY `idx_batches_supplier` (`supplier_id`),
  ADD KEY `fk_batches_purchase_detail` (`purchase_detail_id`);

--
-- Indexes for table `customers`
--
ALTER TABLE `customers`
  ADD PRIMARY KEY (`customer_id`);

--
-- Indexes for table `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`product_id`),
  ADD KEY `idx_products_name` (`product_name`),
  ADD KEY `idx_products_category` (`category`);

--
-- Indexes for table `purchases`
--
ALTER TABLE `purchases`
  ADD PRIMARY KEY (`purchase_id`),
  ADD KEY `idx_purchases_date` (`purchase_date`),
  ADD KEY `fk_purchases_supplier` (`supplier_id`),
  ADD KEY `fk_purchases_user` (`user_id`);

--
-- Indexes for table `purchase_details`
--
ALTER TABLE `purchase_details`
  ADD PRIMARY KEY (`purchase_detail_id`),
  ADD KEY `idx_purchase_details_product` (`product_id`),
  ADD KEY `fk_purchase_details_purchase` (`purchase_id`);

--
-- Indexes for table `sales`
--
ALTER TABLE `sales`
  ADD PRIMARY KEY (`sale_id`),
  ADD KEY `idx_sales_date` (`sale_date`),
  ADD KEY `idx_sales_status` (`status`),
  ADD KEY `fk_sales_customer` (`customer_id`),
  ADD KEY `fk_sales_user` (`user_id`);

--
-- Indexes for table `sale_details`
--
ALTER TABLE `sale_details`
  ADD PRIMARY KEY (`sale_detail_id`),
  ADD KEY `idx_sale_details_product` (`product_id`),
  ADD KEY `fk_sale_details_sale` (`sale_id`),
  ADD KEY `idx_sale_details_batch_id` (`batch_id`);

--
-- Indexes for table `stock_movements`
--
ALTER TABLE `stock_movements`
  ADD PRIMARY KEY (`movement_id`),
  ADD KEY `idx_movements_product_date` (`product_id`,`movement_date`),
  ADD KEY `idx_movements_batch` (`batch_id`),
  ADD KEY `fk_movements_user` (`user_id`);

--
-- Indexes for table `suppliers`
--
ALTER TABLE `suppliers`
  ADD PRIMARY KEY (`supplier_id`),
  ADD UNIQUE KEY `uq_suppliers_name` (`supplier_name`),
  ADD UNIQUE KEY `uq_supplier_name_normalized` (`supplier_name_normalized`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `uq_users_username` (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `batches`
--
ALTER TABLE `batches`
  MODIFY `batch_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;

--
-- AUTO_INCREMENT for table `customers`
--
ALTER TABLE `customers`
  MODIFY `customer_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `products`
--
ALTER TABLE `products`
  MODIFY `product_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `purchases`
--
ALTER TABLE `purchases`
  MODIFY `purchase_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `purchase_details`
--
ALTER TABLE `purchase_details`
  MODIFY `purchase_detail_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `sales`
--
ALTER TABLE `sales`
  MODIFY `sale_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `sale_details`
--
ALTER TABLE `sale_details`
  MODIFY `sale_detail_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `stock_movements`
--
ALTER TABLE `stock_movements`
  MODIFY `movement_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `suppliers`
--
ALTER TABLE `suppliers`
  MODIFY `supplier_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `batches`
--
ALTER TABLE `batches`
  ADD CONSTRAINT `fk_batches_product` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`) ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_batches_purchase_detail` FOREIGN KEY (`purchase_detail_id`) REFERENCES `purchase_details` (`purchase_detail_id`) ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_batches_supplier` FOREIGN KEY (`supplier_id`) REFERENCES `suppliers` (`supplier_id`) ON UPDATE CASCADE;

--
-- Constraints for table `purchases`
--
ALTER TABLE `purchases`
  ADD CONSTRAINT `fk_purchases_supplier` FOREIGN KEY (`supplier_id`) REFERENCES `suppliers` (`supplier_id`) ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_purchases_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON UPDATE CASCADE;

--
-- Constraints for table `purchase_details`
--
ALTER TABLE `purchase_details`
  ADD CONSTRAINT `fk_purchase_details_product` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`) ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_purchase_details_purchase` FOREIGN KEY (`purchase_id`) REFERENCES `purchases` (`purchase_id`) ON UPDATE CASCADE;

--
-- Constraints for table `sales`
--
ALTER TABLE `sales`
  ADD CONSTRAINT `fk_sales_customer` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`customer_id`) ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_sales_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON UPDATE CASCADE;

--
-- Constraints for table `sale_details`
--
ALTER TABLE `sale_details`
  ADD CONSTRAINT `fk_sale_details_batch` FOREIGN KEY (`batch_id`) REFERENCES `batches` (`batch_id`) ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_sale_details_product` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`) ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_sale_details_sale` FOREIGN KEY (`sale_id`) REFERENCES `sales` (`sale_id`) ON UPDATE CASCADE;

--
-- Constraints for table `stock_movements`
--
ALTER TABLE `stock_movements`
  ADD CONSTRAINT `fk_movements_batch` FOREIGN KEY (`batch_id`) REFERENCES `batches` (`batch_id`) ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_movements_product` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`) ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_movements_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
