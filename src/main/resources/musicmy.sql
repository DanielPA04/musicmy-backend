-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: database:3306
-- Tiempo de generación: 22-03-2025 a las 12:33:34
-- Versión del servidor: 8.4.2
-- Versión de PHP: 8.2.24

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";

--
-- Base de datos: `musicmy`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `album`
--

CREATE TABLE `album` (
  `id` int NOT NULL,
  `nombre` varchar(255) COLLATE utf32_unicode_ci NOT NULL,
  `fecha` date NOT NULL,
  `genero` varchar(255) COLLATE utf32_unicode_ci NOT NULL,
  `descripcion` varchar(255) COLLATE utf32_unicode_ci NOT NULL,
  `discografica` varchar(255) COLLATE utf32_unicode_ci NOT NULL,
  `img` longblob
) ENGINE=InnoDB DEFAULT CHARSET=utf32 COLLATE=utf32_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `artista`
--

CREATE TABLE `artista` (
  `id` int NOT NULL,
  `nombre` varchar(255) COLLATE utf32_unicode_ci DEFAULT NULL,
  `nombreReal` varchar(255) COLLATE utf32_unicode_ci DEFAULT NULL,
  `descripcion` varchar(255) COLLATE utf32_unicode_ci DEFAULT NULL,
  `spotify` varchar(255) CHARACTER SET utf32 COLLATE utf32_unicode_ci DEFAULT NULL,
  `img` blob
) ENGINE=InnoDB DEFAULT CHARSET=utf32 COLLATE=utf32_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `grupoalbumartista`
--

CREATE TABLE `grupoalbumartista` (
  `id` int NOT NULL,
  `idAlbum` int DEFAULT NULL,
  `idArtista` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf32 COLLATE=utf32_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `resenya`
--

CREATE TABLE `resenya` (
  `id` int NOT NULL,
  `nota` int DEFAULT NULL,
  `descripcion` varchar(255) COLLATE utf32_unicode_ci DEFAULT NULL,
  `fecha` date DEFAULT NULL,
  `website` varchar(255) COLLATE utf32_unicode_ci DEFAULT NULL,
  `idAlbum` int DEFAULT NULL,
  `idUsuario` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf32 COLLATE=utf32_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tipousuario`
--

CREATE TABLE `tipousuario` (
  `id` int NOT NULL,
  `nombre` varchar(255) CHARACTER SET utf32 COLLATE utf32_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf32 COLLATE=utf32_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario`
--

CREATE TABLE `usuario` (
  `id` int NOT NULL,
  `username` varchar(255) COLLATE utf32_unicode_ci NOT NULL,
  `nombre` varchar(255) COLLATE utf32_unicode_ci DEFAULT NULL,
  `fecha` date DEFAULT NULL,
  `descripcion` varchar(255) COLLATE utf32_unicode_ci DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf32 COLLATE utf32_unicode_ci NOT NULL,
  `password` varchar(255) COLLATE utf32_unicode_ci DEFAULT NULL,
  `website` varchar(255) COLLATE utf32_unicode_ci DEFAULT NULL,
  `img` longblob,
  `idTipoUsuario` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf32 COLLATE=utf32_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarioverf`
--

CREATE TABLE `usuarioverf` (
  `id` int NOT NULL,
  `email` varchar(255) COLLATE utf32_unicode_ci NOT NULL,
  `code` varchar(255) COLLATE utf32_unicode_ci NOT NULL,
  `exp` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf32 COLLATE=utf32_unicode_ci;

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `album`
--
ALTER TABLE `album`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `artista`
--
ALTER TABLE `artista`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `grupoalbumartista`
--
ALTER TABLE `grupoalbumartista`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `resenya`
--
ALTER TABLE `resenya`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `tipousuario`
--
ALTER TABLE `tipousuario`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `usuario`
--
ALTER TABLE `usuario`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `usuarioverf`
--
ALTER TABLE `usuarioverf`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `album`
--
ALTER TABLE `album`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `artista`
--
ALTER TABLE `artista`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `grupoalbumartista`
--
ALTER TABLE `grupoalbumartista`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `resenya`
--
ALTER TABLE `resenya`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `tipousuario`
--
ALTER TABLE `tipousuario`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `usuario`
--
ALTER TABLE `usuario`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `usuarioverf`
--
ALTER TABLE `usuarioverf`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;
COMMIT;
