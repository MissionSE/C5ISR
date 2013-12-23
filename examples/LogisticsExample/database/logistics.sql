-- phpMyAdmin SQL Dump
-- version 4.0.9
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Dec 23, 2013 at 07:53 PM
-- Server version: 5.6.14
-- PHP Version: 5.4.17

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `logistics`
--

-- --------------------------------------------------------

--
-- Table structure for table `inventory_items`
--

CREATE TABLE IF NOT EXISTS `inventory_items` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name_id` int(11) NOT NULL,
  `quantity` double NOT NULL,
  `maximum` int(11) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `inventory_items`
--

INSERT INTO `inventory_items` (`id`, `name_id`, `quantity`, `maximum`, `created_at`, `updated_at`) VALUES
(1, 11, 45.78, 50, '2013-12-17 18:07:59', '0000-00-00 00:00:00');

-- --------------------------------------------------------

--
-- Table structure for table `item_names`
--

CREATE TABLE IF NOT EXISTS `item_names` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=12 ;

--
-- Dumping data for table `item_names`
--

INSERT INTO `item_names` (`id`, `name`, `created_at`, `updated_at`) VALUES
(9, 'Wood', '2013-12-16 20:27:52', '0000-00-00 00:00:00'),
(10, 'Food', '2013-12-16 20:29:08', '0000-00-00 00:00:00'),
(11, 'Fuel', '2013-12-16 20:35:19', '0000-00-00 00:00:00');

-- --------------------------------------------------------

--
-- Table structure for table `orders`
--

CREATE TABLE IF NOT EXISTS `orders` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ordered_at` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `severity_id` int(11) NOT NULL,
  `status_id` int(11) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `orders`
--

INSERT INTO `orders` (`id`, `ordered_at`, `severity_id`, `status_id`, `created_at`, `updated_at`) VALUES
(1, '2013-12-17 17:14:27', 1, 2, '2013-12-17 18:09:28', '0000-00-00 00:00:00');

-- --------------------------------------------------------

--
-- Table structure for table `orders_to_order_items`
--

CREATE TABLE IF NOT EXISTS `orders_to_order_items` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_id` int(11) NOT NULL,
  `item_id` int(11) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `orders_to_order_items`
--

INSERT INTO `orders_to_order_items` (`id`, `order_id`, `item_id`, `created_at`, `updated_at`) VALUES
(1, 1, 1, '2013-12-17 18:12:13', '0000-00-00 00:00:00');

-- --------------------------------------------------------

--
-- Table structure for table `order_items`
--

CREATE TABLE IF NOT EXISTS `order_items` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name_id` int(11) NOT NULL,
  `quantity` double NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `order_items`
--

INSERT INTO `order_items` (`id`, `name_id`, `quantity`, `created_at`, `updated_at`) VALUES
(1, 11, 2.5, '2013-12-17 18:10:51', '0000-00-00 00:00:00');

-- --------------------------------------------------------

--
-- Table structure for table `severity_names`
--

CREATE TABLE IF NOT EXISTS `severity_names` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=4 ;

--
-- Dumping data for table `severity_names`
--

INSERT INTO `severity_names` (`id`, `name`, `created_at`, `updated_at`) VALUES
(1, 'minor', '2013-12-23 19:22:47', '0000-00-00 00:00:00'),
(2, 'normal', '2013-12-23 19:23:02', '0000-00-00 00:00:00'),
(3, 'urgent', '2013-12-23 19:23:09', '0000-00-00 00:00:00');

-- --------------------------------------------------------

--
-- Table structure for table `sites`
--

CREATE TABLE IF NOT EXISTS `sites` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `short_name` varchar(3) NOT NULL,
  `latitude` double NOT NULL,
  `longitude` double NOT NULL,
  `parent_id` int(11) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=11 ;

--
-- Dumping data for table `sites`
--

INSERT INTO `sites` (`id`, `name`, `short_name`, `latitude`, `longitude`, `parent_id`, `created_at`, `updated_at`) VALUES
(1, 'MSE', 'MSE', 39.97475, -74.97672, 0, '2013-12-13 20:19:05', '0000-00-00 00:00:00'),
(4, 'James'' car', 'CAR', 39.9747147, -74.97545, 1, '2013-12-17 20:19:05', '0000-00-00 00:00:00'),
(5, 'Future Parking Lot', 'FPL', 39.97569, -74.9745, 1, '2013-12-17 20:13:14', '0000-00-00 00:00:00'),
(6, 'White Stuff', 'WS', 39.9748455, -74.97654, 0, '2013-12-17 20:15:18', '0000-00-00 00:00:00'),
(7, 'Roberto''s Home', 'RH', 37.5, -75, 0, '2013-12-17 20:16:16', '0000-00-00 00:00:00'),
(8, 'Warehouse', 'WH', 28.192, -81.439, 0, '2013-12-17 20:18:02', '0000-00-00 00:00:00'),
(9, 'Wawa', 'WAW', 33.871, -77.947, 8, '2013-12-17 20:18:43', '0000-00-00 00:00:00'),
(10, 'Bubba Gump Shrimp', 'BGS', 42.6443, 31.1327, 1, '2013-12-18 18:21:12', '0000-00-00 00:00:00');

-- --------------------------------------------------------

--
-- Table structure for table `sites_to_inventory_items`
--

CREATE TABLE IF NOT EXISTS `sites_to_inventory_items` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `site_id` int(11) NOT NULL,
  `item_id` int(11) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `sites_to_inventory_items`
--

INSERT INTO `sites_to_inventory_items` (`id`, `site_id`, `item_id`, `created_at`, `updated_at`) VALUES
(1, 1, 1, '2013-12-17 18:12:38', '0000-00-00 00:00:00');

-- --------------------------------------------------------

--
-- Table structure for table `sites_to_orders`
--

CREATE TABLE IF NOT EXISTS `sites_to_orders` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `site_id` int(11) NOT NULL,
  `order_id` int(11) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `sites_to_orders`
--

INSERT INTO `sites_to_orders` (`id`, `site_id`, `order_id`, `created_at`, `updated_at`) VALUES
(1, 1, 1, '2013-12-17 18:13:04', '0000-00-00 00:00:00');

-- --------------------------------------------------------

--
-- Table structure for table `status_names`
--

CREATE TABLE IF NOT EXISTS `status_names` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=5 ;

--
-- Dumping data for table `status_names`
--

INSERT INTO `status_names` (`id`, `name`, `created_at`, `updated_at`) VALUES
(1, 'processing', '2013-12-23 19:23:54', '0000-00-00 00:00:00'),
(2, 'delivered', '2013-12-23 19:24:01', '0000-00-00 00:00:00'),
(3, 'shipping', '2013-12-23 19:24:29', '0000-00-00 00:00:00'),
(4, 'cancelled', '2013-12-23 19:24:42', '0000-00-00 00:00:00');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
