package jminvsm.service;

/**
 * Excecao de regra de negocio usada pela camada de service.
 */
public class ServiceException extends RuntimeException {

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
