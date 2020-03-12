

--
-- Estructura de tabla para la tabla `venta_general`
--

CREATE TABLE `venta_general` (
  `id_venta` int NOT NULL AUTO_INCREMENT,
  `fecha` date NOT NULL,
  `tipo_combustible` varchar(16) DEFAULT NULL,
  `litros` int DEFAULT NULL,
  `total` int DEFAULT NULL,
  PRIMARY KEY (id_venta),
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Estructura de tabla para la tabla `Distribuidores`
--

CREATE TABLE `precios` (
  `id` int NOT NULL ,
  `tipo_combustible` varchar(16) NOT NULL,
  `precio` int NOT NULL,
  PRIMARY KEY (id),
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

INSERT into precios VALUES(1, "Gas93", 600)
INSERT into precios VALUES(2, "Gas95", 600)
INSERT into precios VALUES(3, "Gas97", 600)
INSERT into precios VALUES(4, "Kerosene", 600)
INSERT into precios VALUES(5, "Diesel", 600)

