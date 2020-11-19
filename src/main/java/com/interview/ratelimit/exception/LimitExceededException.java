package com.interview.ratelimit.exception;


public class LimitExceededException extends RuntimeException {
        public LimitExceededException(String errorMessage) {
                super(errorMessage);
        }
}
