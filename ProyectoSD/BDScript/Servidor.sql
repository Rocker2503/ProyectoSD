--
-- Estructura de tabla para la tabla `Distribuidores`
--

CREATE TABLE `distribuidores` (
  `id` int NOT NULL ,
  `nombre` varchar(16) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



--
-- Estructura de tabla para la tabla `venta_general`
--

CREATE TABLE `venta_general` (
  `id_venta` int NOT NULL AUTO_INCREMENT,
  `id_distribuidor` int NOT NULL,
  `fecha` date NOT NULL,
  `tipo_combustible` varchar(16) DEFAULT NULL,
  `litros` int DEFAULT NULL,
  `total` int DEFAULT NULL,
  PRIMARY KEY (id_venta),
  FOREIGN KEY (id_distribuidor) REFERENCES distribuidores(id)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Estructura de tabla para la tabla `Distribuidores`
--

CREATE TABLE `precios` (
  `id` int NOT NULL ,
  `id_dist` int NOT NULL,
  `precio` int NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (id_dist) REFERENCES distribuidores(id)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;




