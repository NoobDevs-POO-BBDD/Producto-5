-- DROP DATABASE tienda_online;

-- =============================================
-- BASE DE DATOS: TIENDA ONLINE
-- Parte 1: Crear base de datos y tablas
-- =============================================

-- Crear la base de datos
CREATE DATABASE tienda_online;
USE tienda_online;

-- =============================================
-- TABLA: ARTICULOS (con ID autonumérico)
-- =============================================
CREATE TABLE articulos (
    id_articulo INT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(255) NOT NULL,
    precio_venta DECIMAL(10,2) NOT NULL CHECK (precio_venta >= 0),
    gastos_envio DECIMAL(10,2) NOT NULL CHECK (gastos_envio >= 0),
    tiempo_preparacion INT NOT NULL CHECK (tiempo_preparacion >= 0),
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- =============================================
-- TABLA: CLIENTES (con ID autonumérico)
-- =============================================
CREATE TABLE clientes (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    domicilio VARCHAR(255) NOT NULL,
    nif VARCHAR(20) NOT NULL UNIQUE,
    tipo ENUM('ESTANDAR', 'PREMIUM') NOT NULL,
    descuento_envio DECIMAL(5,2) DEFAULT 0.0 CHECK (descuento_envio >= 0 AND descuento_envio <= 100),
    cuota_anual DECIMAL(10,2) DEFAULT 0.0 CHECK (cuota_anual >= 0),
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- =============================================
-- TABLA: PEDIDOS (con ID autonumérico)
-- =============================================
CREATE TABLE pedidos (
    id_pedido INT AUTO_INCREMENT PRIMARY KEY,
    numero_pedido VARCHAR(50) NOT NULL UNIQUE,
    id_cliente INT NOT NULL,
    id_articulo INT NOT NULL,
    cantidad INT NOT NULL CHECK (cantidad > 0),
    fecha_hora DATETIME NOT NULL,
    estado BOOLEAN DEFAULT FALSE COMMENT 'FALSE = Pendiente, TRUE = Enviado',
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- Claves foráneas
    FOREIGN KEY (id_cliente) REFERENCES clientes(id_cliente) 
        ON DELETE RESTRICT 
        ON UPDATE CASCADE,
    FOREIGN KEY (id_articulo) REFERENCES articulos(id_articulo) 
        ON DELETE RESTRICT 
        ON UPDATE CASCADE
);

-- =============================================
-- ÍNDICES PARA MEJORAR EL RENDIMIENTO
-- =============================================

-- Índices para la tabla articulos
CREATE INDEX idx_articulos_codigo ON articulos(codigo);
CREATE INDEX idx_articulos_descripcion ON articulos(descripcion);

-- Índices para la tabla clientes
CREATE INDEX idx_clientes_email ON clientes(email);
CREATE INDEX idx_clientes_nif ON clientes(nif);
CREATE INDEX idx_clientes_tipo ON clientes(tipo);
CREATE INDEX idx_clientes_nombre ON clientes(nombre);

-- Índices para la tabla pedidos
CREATE INDEX idx_pedidos_numero ON pedidos(numero_pedido);
CREATE INDEX idx_pedidos_cliente ON pedidos(id_cliente);
CREATE INDEX idx_pedidos_articulo ON pedidos(id_articulo);
CREATE INDEX idx_pedidos_estado ON pedidos(estado);
CREATE INDEX idx_pedidos_fecha ON pedidos(fecha_hora);
CREATE INDEX idx_pedidos_cliente_estado ON pedidos(id_cliente, estado);

-- =============================================
-- PROCEDIMIENTOS ALMACENADOS - GESTIÓN DE ARTÍCULOS
-- =============================================

-- =============================================
-- PROCEDIMIENTO: sp_addArticulo
-- =============================================
DELIMITER //

CREATE PROCEDURE sp_addArticulo(
    IN p_codigo VARCHAR(50),
    IN p_descripcion VARCHAR(255),
    IN p_precio_venta DECIMAL(10,2),
    IN p_gastos_envio DECIMAL(10,2),
    IN p_tiempo_preparacion INT
)
BEGIN
    DECLARE articulo_count INT;
    
    SELECT COUNT(*) INTO articulo_count 
    FROM articulos 
    WHERE codigo = p_codigo;
    
    IF articulo_count > 0 THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Ya existe un artículo con el mismo código';
    ELSE
        INSERT INTO articulos (codigo, descripcion, precio_venta, gastos_envio, tiempo_preparacion)
        VALUES (p_codigo, p_descripcion, p_precio_venta, p_gastos_envio, p_tiempo_preparacion);
    END IF;
END //

DELIMITER ;

-- =============================================
-- PROCEDIMIENTO: sp_getAllArticulos
-- =============================================
DELIMITER //

CREATE PROCEDURE sp_getAllArticulos()
BEGIN
    SELECT id_articulo, codigo, descripcion, precio_venta, gastos_envio, tiempo_preparacion
    FROM articulos
    ORDER BY descripcion;
END //

DELIMITER ;

-- =============================================
-- PROCEDIMIENTO: sp_getArticuloByCodigo
-- =============================================
DELIMITER //

CREATE PROCEDURE sp_getArticuloByCodigo(IN p_codigo VARCHAR(50))
BEGIN
    SELECT id_articulo, codigo, descripcion, precio_venta, gastos_envio, tiempo_preparacion
    FROM articulos 
    WHERE codigo = p_codigo;
END //

DELIMITER ;

-- =============================================
-- PROCEDIMIENTOS ALMACENADOS - GESTIÓN DE CLIENTES
-- =============================================

-- =============================================
-- PROCEDIMIENTO: sp_addCliente
-- =============================================
DELIMITER //

CREATE PROCEDURE sp_addCliente(
    IN p_email VARCHAR(100),
    IN p_nombre VARCHAR(100),
    IN p_domicilio VARCHAR(255),
    IN p_nif VARCHAR(20),
    IN p_tipo ENUM('ESTANDAR', 'PREMIUM'),
    IN p_descuento_envio DECIMAL(5,2),
    IN p_cuota_anual DECIMAL(10,2)
)
BEGIN
    DECLARE cliente_count INT;
    
    SELECT COUNT(*) INTO cliente_count 
    FROM clientes 
    WHERE email = p_email;
    
    IF cliente_count > 0 THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Ya existe un cliente con el mismo email';
    ELSE
        INSERT INTO clientes (email, nombre, domicilio, nif, tipo, descuento_envio, cuota_anual)
        VALUES (p_email, p_nombre, p_domicilio, p_nif, p_tipo, p_descuento_envio, p_cuota_anual);
    END IF;
END //

DELIMITER ;

-- =============================================
-- PROCEDIMIENTO: sp_getAllClientes
-- =============================================
DELIMITER //

CREATE PROCEDURE sp_getAllClientes()
BEGIN
    SELECT id_cliente, email, nombre, domicilio, nif, tipo, descuento_envio, cuota_anual
    FROM clientes
    ORDER BY nombre;
END //

DELIMITER ;

-- =============================================
-- PROCEDIMIENTO: sp_getClientesEstandar
-- =============================================
DELIMITER //

CREATE PROCEDURE sp_getClientesEstandar()
BEGIN
    SELECT id_cliente, email, nombre, domicilio, nif, descuento_envio
    FROM clientes 
    WHERE tipo = 'ESTANDAR'
    ORDER BY nombre;
END //

DELIMITER ;

-- =============================================
-- PROCEDIMIENTO: sp_getClientesPremium
-- =============================================
DELIMITER //

CREATE PROCEDURE sp_getClientesPremium()
BEGIN
    SELECT id_cliente, email, nombre, domicilio, nif, descuento_envio, cuota_anual
    FROM clientes 
    WHERE tipo = 'PREMIUM'
    ORDER BY nombre;
END //

DELIMITER ;

-- =============================================
-- PROCEDIMIENTO: sp_getClienteByEmail
-- =============================================
DELIMITER //

CREATE PROCEDURE sp_getClienteByEmail(IN p_email VARCHAR(100))
BEGIN
    SELECT id_cliente, email, nombre, domicilio, nif, tipo, descuento_envio, cuota_anual
    FROM clientes 
    WHERE email = p_email;
END //

DELIMITER ;

-- =============================================
-- PROCEDIMIENTO: sp_getClienteByNIF
-- =============================================
DELIMITER //

CREATE PROCEDURE sp_getClienteByNIF(IN p_nif VARCHAR(20))
BEGIN
    SELECT id_cliente, email, nombre, domicilio, nif, tipo, descuento_envio, cuota_anual
    FROM clientes 
    WHERE nif = p_nif;
END //

DELIMITER ;

-- =============================================
-- PROCEDIMIENTOS ALMACENADOS - GESTIÓN DE PEDIDOS
-- =============================================

-- =============================================
-- PROCEDIMIENTO: sp_addPedido
-- =============================================
DELIMITER //

CREATE PROCEDURE sp_addPedido(
    IN p_numero_pedido VARCHAR(50),
    IN p_email_cliente VARCHAR(100),
    IN p_codigo_articulo VARCHAR(50),
    IN p_cantidad INT
)
BEGIN
    DECLARE v_id_cliente INT;
    DECLARE v_id_articulo INT;
    DECLARE pedido_count INT;
    
    SELECT COUNT(*) INTO pedido_count 
    FROM pedidos 
    WHERE numero_pedido = p_numero_pedido;
    
    IF pedido_count > 0 THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Ya existe un pedido con el mismo número';
    END IF;
    
    SELECT id_cliente INTO v_id_cliente 
    FROM clientes 
    WHERE email = p_email_cliente;
    
    IF v_id_cliente IS NULL THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'No existe el cliente con el email especificado';
    END IF;
    
    SELECT id_articulo INTO v_id_articulo 
    FROM articulos 
    WHERE codigo = p_codigo_articulo;
    
    IF v_id_articulo IS NULL THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'No existe el artículo con el código especificado';
    END IF;
    
    IF p_cantidad <= 0 THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'La cantidad debe ser mayor a 0';
    END IF;
    
    INSERT INTO pedidos (numero_pedido, id_cliente, id_articulo, cantidad, fecha_hora)
    VALUES (p_numero_pedido, v_id_cliente, v_id_articulo, p_cantidad, NOW());
END //

DELIMITER ;

-- =============================================
-- PROCEDIMIENTO: sp_deletePedido
-- =============================================
DELIMITER //

CREATE PROCEDURE sp_deletePedido(IN p_numero_pedido VARCHAR(50))
BEGIN
    DECLARE v_estado BOOLEAN;
    DECLARE v_fecha_hora DATETIME;
    DECLARE v_tiempo_preparacion INT;
    DECLARE v_id_pedido INT;
    
    SELECT p.id_pedido, p.estado, p.fecha_hora, a.tiempo_preparacion 
    INTO v_id_pedido, v_estado, v_fecha_hora, v_tiempo_preparacion
    FROM pedidos p
    JOIN articulos a ON p.id_articulo = a.id_articulo
    WHERE p.numero_pedido = p_numero_pedido;
    
    IF v_id_pedido IS NULL THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'No existe el pedido especificado';
    END IF;
    
    IF v_estado = TRUE THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'No se puede eliminar un pedido ya enviado';
    END IF;
    
    IF TIMESTAMPDIFF(MINUTE, v_fecha_hora, NOW()) >= v_tiempo_preparacion THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'El pedido no puede ser cancelado, ha superado el tiempo de preparación';
    END IF;
    
    DELETE FROM pedidos WHERE id_pedido = v_id_pedido;
END //

DELIMITER ;

-- =============================================
-- PROCEDIMIENTO: sp_getPedidosPendientes
-- =============================================
DELIMITER //

CREATE PROCEDURE sp_getPedidosPendientes()
BEGIN
    SELECT p.id_pedido, p.numero_pedido, c.email, c.nombre as nombre_cliente,
           a.codigo, a.descripcion as descripcion_articulo,
           p.cantidad, p.fecha_hora, p.estado
    FROM pedidos p
    JOIN clientes c ON p.id_cliente = c.id_cliente
    JOIN articulos a ON p.id_articulo = a.id_articulo
    WHERE p.estado = FALSE
    ORDER BY p.fecha_hora;
END //

DELIMITER ;

-- =============================================
-- PROCEDIMIENTO: sp_getPedidosPendientesByCliente
-- =============================================
DELIMITER //

CREATE PROCEDURE sp_getPedidosPendientesByCliente(IN p_email_cliente VARCHAR(100))
BEGIN
    SELECT p.id_pedido, p.numero_pedido, c.email, c.nombre as nombre_cliente,
           a.codigo, a.descripcion as descripcion_articulo,
           p.cantidad, p.fecha_hora, p.estado
    FROM pedidos p
    JOIN clientes c ON p.id_cliente = c.id_cliente
    JOIN articulos a ON p.id_articulo = a.id_articulo
    WHERE p.estado = FALSE AND c.email = p_email_cliente
    ORDER BY p.fecha_hora;
END //

DELIMITER ;

-- =============================================
-- PROCEDIMIENTO: sp_getPedidosEnviadosByCliente
-- =============================================

DELIMITER //

CREATE PROCEDURE sp_getPedidosEnviadosByCliente(IN p_email_cliente VARCHAR(100))
BEGIN
    SELECT p.id_pedido, p.numero_pedido, c.email, c.nombre as nombre_cliente,
           a.codigo, a.descripcion as descripcion_articulo,
           p.cantidad, p.fecha_hora, p.estado
    FROM pedidos p
    JOIN clientes c ON p.id_cliente = c.id_cliente
    JOIN articulos a ON p.id_articulo = a.id_articulo
    WHERE p.estado = TRUE AND c.email = p_email_cliente
    ORDER BY p.fecha_hora DESC;
END //

DELIMITER ;

-- =============================================
-- PROCEDIMIENTO: sp_getPedidosEnviados
-- =============================================
DELIMITER //

CREATE PROCEDURE sp_getPedidosEnviados()
BEGIN
    SELECT p.id_pedido, p.numero_pedido, c.email, c.nombre as nombre_cliente,
           a.codigo, a.descripcion as descripcion_articulo,
           p.cantidad, p.fecha_hora, p.estado
    FROM pedidos p
    JOIN clientes c ON p.id_cliente = c.id_cliente
    JOIN articulos a ON p.id_articulo = a.id_articulo
    WHERE p.estado = TRUE
    ORDER BY p.fecha_hora DESC;
END //

DELIMITER ;

-- =============================================
-- PROCEDIMIENTO: sp_marcarPedidoEnviado
-- =============================================
DELIMITER //

CREATE PROCEDURE sp_marcarPedidoEnviado(IN p_numero_pedido VARCHAR(50))
BEGIN
    DECLARE v_id_pedido INT;
    
    SELECT id_pedido INTO v_id_pedido 
    FROM pedidos 
    WHERE numero_pedido = p_numero_pedido;
    
    IF v_id_pedido IS NULL THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'No existe el pedido especificado';
    END IF;
    
    UPDATE pedidos 
    SET estado = TRUE, fecha_actualizacion = CURRENT_TIMESTAMP
    WHERE id_pedido = v_id_pedido;
END //

DELIMITER ;

-- =============================================
-- PROCEDIMIENTO: sp_calcularPrecioPedido
-- =============================================
DELIMITER //

CREATE PROCEDURE sp_calcularPrecioPedido(IN p_numero_pedido VARCHAR(50))
BEGIN
    DECLARE v_precio_base DECIMAL(10,2);
    DECLARE v_gastos_envio DECIMAL(10,2);
    DECLARE v_descuento_envio DECIMAL(5,2);
    DECLARE v_cantidad INT;
    DECLARE v_precio_total DECIMAL(10,2);
    
    SELECT a.precio_venta, a.gastos_envio, c.descuento_envio, p.cantidad
    INTO v_precio_base, v_gastos_envio, v_descuento_envio, v_cantidad
    FROM pedidos p
    JOIN articulos a ON p.id_articulo = a.id_articulo
    JOIN clientes c ON p.id_cliente = c.id_cliente
    WHERE p.numero_pedido = p_numero_pedido;
    
    IF v_precio_base IS NULL THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'No existe el pedido especificado';
    END IF;
    
    SET v_precio_total = (v_precio_base * v_cantidad) + (v_gastos_envio * (1 - v_descuento_envio / 100));
    
    SELECT v_precio_total as precio_total;
END //

DELIMITER ;

-- ============================================= ÚLTIMOS AÑADIDOS

-- =============================================
-- PROCEDIMIENTO: sp_getPedidosEnviadosByCliente
-- =============================================
CREATE PROCEDURE sp_getPedidosEnviadosByCliente(IN p_email_cliente VARCHAR(100))
BEGIN
    SELECT p.id_pedido, p.numero_pedido, c.email, c.nombre as nombre_cliente,
           a.codigo, a.descripcion as descripcion_articulo,
           p.cantidad, p.fecha_hora, p.estado
    FROM pedidos p
    JOIN clientes c ON p.id_cliente = c.id_cliente
    JOIN articulos a ON p.id_articulo = a.id_articulo
    WHERE p.estado = TRUE AND c.email = p_email_cliente
    ORDER BY p.fecha_hora DESC;
END //

-- =============================================
-- PROCEDIMIENTO:sp_getAllPedidosByCliente
-- =============================================
CREATE PROCEDURE sp_getAllPedidosByCliente(IN p_email_cliente VARCHAR(100))
BEGIN
    SELECT p.id_pedido, p.numero_pedido, c.email, c.nombre as nombre_cliente,
           a.codigo, a.descripcion as descripcion_articulo,
           p.cantidad, p.fecha_hora, p.estado
    FROM pedidos p
    JOIN clientes c ON p.id_cliente = c.id_cliente
    JOIN articulos a ON p.id_articulo = a.id_articulo
    WHERE c.email = p_email_cliente
    ORDER BY p.fecha_hora DESC;
END //

-- =============================================
-- PROCEDIMIENTO: sp_getPedidoByNumero
-- =============================================
CREATE PROCEDURE sp_getPedidoByNumero(IN p_numero_pedido VARCHAR(50))
BEGIN
    SELECT p.id_pedido, p.numero_pedido, c.email, c.nombre as nombre_cliente,
           a.codigo, a.descripcion as descripcion_articulo,
           p.cantidad, p.fecha_hora, p.estado
    FROM pedidos p
    JOIN clientes c ON p.id_cliente = c.id_cliente
    JOIN articulos a ON p.id_articulo = a.id_articulo
    WHERE p.numero_pedido = p_numero_pedido;
END //

-- =============================================
-- PROCEDIMIENTO:sp_getAllPedidos()
-- =============================================
CREATE PROCEDURE sp_getAllPedidos()
BEGIN
    SELECT p.id_pedido, p.numero_pedido, c.email, c.nombre as nombre_cliente,
           a.codigo, a.descripcion as descripcion_articulo,
           p.cantidad, p.fecha_hora, p.estado
    FROM pedidos p
    JOIN clientes c ON p.id_cliente = c.id_cliente
    JOIN articulos a ON p.id_articulo = a.id_articulo
    ORDER BY p.fecha_hora DESC;
END //



-- =============================================
-- PROCEDIMIENTOS ALMACENADOS - ESTADÍSTICAS
-- =============================================

-- =============================================
-- PROCEDIMIENTO: sp_getEstadisticasTienda
-- =============================================
DELIMITER //

CREATE PROCEDURE sp_getEstadisticasTienda()
BEGIN
    SELECT 
        (SELECT COUNT(*) FROM articulos) as total_articulos,
        (SELECT COUNT(*) FROM clientes) as total_clientes,
        (SELECT COUNT(*) FROM clientes WHERE tipo = 'ESTANDAR') as clientes_estandar,
        (SELECT COUNT(*) FROM clientes WHERE tipo = 'PREMIUM') as clientes_premium,
        (SELECT COUNT(*) FROM pedidos) as total_pedidos,
        (SELECT COUNT(*) FROM pedidos WHERE estado = FALSE) as pedidos_pendientes,
        (SELECT COUNT(*) FROM pedidos WHERE estado = TRUE) as pedidos_enviados;
END //

DELIMITER ;

-- =============================================
-- VERIFICACIÓN Y PRUEBAS
-- =============================================

-- Probar los procedimientos
CALL sp_getAllArticulos();
CALL sp_getAllClientes();
CALL sp_getEstadisticasTienda();

-- Ver todos los procedimientos creados
SHOW PROCEDURE STATUS WHERE Db = 'tienda_online';