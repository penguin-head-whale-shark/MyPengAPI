package com.github.pdgs.MyPengAPI.advice.exception;

public class CEmailSignInFailedException extends RuntimeException {

    public CEmailSignInFailedException(String msg, Throwable t) {
        super(msg, t);
    }

    public CEmailSignInFailedException(String msg) {
        super(msg);
    }
    
    public CEmailSignInFailedException() {
        super();
    }

}
