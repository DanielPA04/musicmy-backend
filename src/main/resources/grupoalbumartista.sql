-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: database:3306
-- Tiempo de generación: 03-01-2025 a las 11:37:06
-- Versión del servidor: 8.4.2
-- Versión de PHP: 8.2.24

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `musicmy`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `grupoalbumartista`
--

CREATE TABLE `grupoalbumartista` (
  `id` int NOT NULL,
  `idAlbum` int DEFAULT NULL,
  `idArtista` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf32 COLLATE=utf32_unicode_ci;

--
-- Volcado de datos para la tabla `grupoalbumartista`
--

INSERT INTO `grupoalbumartista` (`id`, `idAlbum`, `idArtista`) VALUES
(9, 52, 53),
(16, 52, 56),
(1, 53, 49),
(12, 54, 55),
(2, 58, 55),
(3, 65, 42),
(5, 71, 56),
(10, 75, 50),
(19, 79, 54),
(17, 83, 46),
(11, 83, 48),
(13, 84, 55),
(7, 84, 58),
(4, 87, 56),
(14, 88, 53),
(15, 89, 60),
(18, 95, 48),
(8, 95, 52),
(20, 97, 45),
(6, 98, 45);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `grupoalbumartista`
--
ALTER TABLE `grupoalbumartista`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `idAlbum` (`idAlbum`,`idArtista`),
  ADD KEY `idArtista` (`idArtista`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `grupoalbumartista`
--
ALTER TABLE `grupoalbumartista`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `grupoalbumartista`
--
ALTER TABLE `grupoalbumartista`
  ADD CONSTRAINT `grupoalbumartista_ibfk_1` FOREIGN KEY (`idAlbum`) REFERENCES `album` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `grupoalbumartista_ibfk_2` FOREIGN KEY (`idArtista`) REFERENCES `artista` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
