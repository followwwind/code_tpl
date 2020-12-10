package com.wind.core.exception;

/**
 * @package com.wind.core.exception
 * @className BusinessException
 * @note 业务异常
 * @author wind
 * @date 2020/12/9 23:40
 */
public class BusinessException extends RuntimeException{

    public BusinessException() {
        super();
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public BusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
