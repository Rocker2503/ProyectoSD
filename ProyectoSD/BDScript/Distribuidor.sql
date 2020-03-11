
--
-- Estructura de tabla para la tabla `venta_general`
--

CREATE TABLE `venta` (
  `id_venta` int NOT NULL AUTO_INCREMENT,
  `fecha` date NOT NULL,
  `tipo_combustible` varchar(16) DEFAULT NULL,
  `litros` int DEFAULT NULL,
  `total` int DEFAULT NULL,
  PRIMARY KEY (id_venta)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Estructura de tabla para la tabla `Distribuidores`
--

CREATE TABLE `precio` (
  `id` int NOT NULL ,
  `precio` int NOT NULL,
  PRIMARY KEY (id)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;




