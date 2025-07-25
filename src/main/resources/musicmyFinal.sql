-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: database:3306
-- Tiempo de generación: 25-05-2025 a las 08:37:09
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
  `nombre` varchar(255) CHARACTER SET utf32 COLLATE utf32_unicode_ci NOT NULL,
  `fecha` date NOT NULL,
  `genero` varchar(255) CHARACTER SET utf32 COLLATE utf32_unicode_ci NOT NULL,
  `descripcion` varchar(255) CHARACTER SET utf32 COLLATE utf32_unicode_ci NOT NULL,
  `discografica` varchar(255) CHARACTER SET utf32 COLLATE utf32_unicode_ci NOT NULL,
  `img` longblob
) ENGINE=InnoDB DEFAULT CHARSET=utf32 COLLATE=utf32_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `artista`
--

CREATE TABLE `artista` (
  `id` int NOT NULL,
  `nombre` varchar(255) CHARACTER SET utf32 COLLATE utf32_unicode_ci NOT NULL,
  `nombreReal` varchar(255) CHARACTER SET utf32 COLLATE utf32_unicode_ci NOT NULL,
  `descripcion` varchar(500) CHARACTER SET utf32 COLLATE utf32_unicode_ci NOT NULL,
  `spotify` varchar(255) CHARACTER SET utf32 COLLATE utf32_unicode_ci NOT NULL,
  `img` blob
) ENGINE=InnoDB DEFAULT CHARSET=utf32 COLLATE=utf32_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `grupoalbumartista`
--

CREATE TABLE `grupoalbumartista` (
  `id` int NOT NULL,
  `idAlbum` int NOT NULL,
  `idArtista` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf32 COLLATE=utf32_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `resenya`
--

CREATE TABLE `resenya` (
  `id` int NOT NULL,
  `nota` int NOT NULL,
  `descripcion` varchar(255) CHARACTER SET utf32 COLLATE utf32_unicode_ci NOT NULL,
  `fecha` date NOT NULL,
  `website` varchar(255) CHARACTER SET utf32 COLLATE utf32_unicode_ci DEFAULT NULL,
  `idAlbum` int NOT NULL,
  `idUsuario` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf32 COLLATE=utf32_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `resenya_like`
--

CREATE TABLE `resenya_like` (
  `id` int NOT NULL,
  `id_usuario` int NOT NULL,
  `id_resenya` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf32 COLLATE=utf32_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tipousuario`
--

CREATE TABLE `tipousuario` (
  `id` int NOT NULL,
  `nombre` varchar(255) CHARACTER SET utf32 COLLATE utf32_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf32 COLLATE=utf32_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario`
--

CREATE TABLE `usuario` (
  `id` int NOT NULL,
  `username` varchar(255) CHARACTER SET utf32 COLLATE utf32_unicode_ci NOT NULL,
  `nombre` varchar(255) CHARACTER SET utf32 COLLATE utf32_unicode_ci DEFAULT NULL,
  `fecha` date DEFAULT NULL,
  `descripcion` varchar(255) CHARACTER SET utf32 COLLATE utf32_unicode_ci DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf32 COLLATE utf32_unicode_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf32 COLLATE utf32_unicode_ci NOT NULL,
  `website` varchar(255) CHARACTER SET utf32 COLLATE utf32_unicode_ci DEFAULT NULL,
  `img` longblob,
  `codverf` varchar(300) CHARACTER SET utf32 COLLATE utf32_unicode_ci DEFAULT NULL,
  `codresetpwd` varchar(300) CHARACTER SET utf32 COLLATE utf32_unicode_ci DEFAULT NULL,
  `idTipoUsuario` int NOT NULL
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
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_grupo_album` (`idAlbum`),
  ADD KEY `fk_grupo_artista` (`idArtista`);

--
-- Indices de la tabla `resenya`
--
ALTER TABLE `resenya`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_resenya_usuario` (`idUsuario`),
  ADD KEY `fk_resenya_album` (`idAlbum`);

--
-- Indices de la tabla `resenya_like`
--
ALTER TABLE `resenya_like`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `usuario_resenya_unique` (`id_usuario`,`id_resenya`),
  ADD KEY `id_resenya` (`id_resenya`);

--
-- Indices de la tabla `tipousuario`
--
ALTER TABLE `tipousuario`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `usuario`
--
ALTER TABLE `usuario`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_usuario_tipousuario` (`idTipoUsuario`);

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
-- AUTO_INCREMENT de la tabla `resenya_like`
--
ALTER TABLE `resenya_like`
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
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `grupoalbumartista`
--
ALTER TABLE `grupoalbumartista`
  ADD CONSTRAINT `fk_grupo_album` FOREIGN KEY (`idAlbum`) REFERENCES `album` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_grupo_artista` FOREIGN KEY (`idArtista`) REFERENCES `artista` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `resenya`
--
ALTER TABLE `resenya`
  ADD CONSTRAINT `fk_resenya_album` FOREIGN KEY (`idAlbum`) REFERENCES `album` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_resenya_usuario` FOREIGN KEY (`idUsuario`) REFERENCES `usuario` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `resenya_like`
--
ALTER TABLE `resenya_like`
  ADD CONSTRAINT `resenya_like_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `resenya_like_ibfk_2` FOREIGN KEY (`id_resenya`) REFERENCES `resenya` (`id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `usuario`
--
ALTER TABLE `usuario`
  ADD CONSTRAINT `fk_usuario_tipousuario` FOREIGN KEY (`idTipoUsuario`) REFERENCES `tipousuario` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;
