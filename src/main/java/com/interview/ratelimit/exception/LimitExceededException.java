package com.interview.ratelimit.exception;


public class LimitExceededException extends Exception {
        public LimitExceededException(String errorMessage) {
                super(errorMessage);
        }
}
