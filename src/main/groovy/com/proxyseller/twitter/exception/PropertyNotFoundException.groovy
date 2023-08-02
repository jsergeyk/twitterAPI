package com.proxyseller.twitter.exception

class PropertyNotFoundException  extends RuntimeException {

    PropertyNotFoundException(String msg) {
        super(msg)
    }

    PropertyNotFoundException(String msg, Throwable cause) {
        super(msg, cause)
    }
}
