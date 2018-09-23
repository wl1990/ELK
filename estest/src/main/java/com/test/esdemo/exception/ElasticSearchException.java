package com.test.esdemo.exception;

/**
 * @author : jingma2
 * @date : 2018/7/21
 * @description
 */
public class ElasticSearchException extends  RuntimeException{

    public ElasticSearchException(String message) {
        super(message);
    }

    public ElasticSearchException(Throwable cause) {
        super(cause);
    }

    public ElasticSearchException(String message, Throwable cause) {
        super(message, cause);
    }
}
