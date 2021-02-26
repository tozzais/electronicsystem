package com.gaocheng.baselibrary.http;

/**
 * Created by 32672 on 2019/1/9.
 */

public class NetException extends Exception{

    public String errorMessage;

    public NetException(String message, Throwable cause, String errorMessage) {
        super(message, cause);
        this.errorMessage = errorMessage;
    }
}
