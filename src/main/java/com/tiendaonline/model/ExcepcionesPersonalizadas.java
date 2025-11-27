package com.tiendaonline.model;

/**
 * Excepciones personalizadas para la tienda online
 * Todas las clases son package-private (sin public) para permitir un solo archivo
 */

class ArticuloExistenteException extends RuntimeException {
    public ArticuloExistenteException(String mensaje) {
        super(mensaje);
    }
}

class ClienteExistenteException extends RuntimeException {
    public ClienteExistenteException(String mensaje) {
        super(mensaje);
    }
}

class PedidoExistenteException extends RuntimeException {
    public PedidoExistenteException(String mensaje) {
        super(mensaje);
    }
}

class ArticuloNoEncontradoException extends RuntimeException {
    public ArticuloNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}

class ClienteNoEncontradoException extends RuntimeException {
    public ClienteNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}

class PedidoNoEncontradoException extends RuntimeException {
    public PedidoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}

class CantidadInvalidaException extends RuntimeException {
    public CantidadInvalidaException(String mensaje) {
        super(mensaje);
    }
}

class PedidoNoCancelableException extends RuntimeException {
    public PedidoNoCancelableException(String mensaje) {
        super(mensaje);
    }
}