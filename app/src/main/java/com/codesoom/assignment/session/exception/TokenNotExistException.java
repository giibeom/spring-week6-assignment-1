package com.codesoom.assignment.session.exception;

public class TokenNotExistException extends RuntimeException {
    public TokenNotExistException() {
        super("토큰이 존재하지 않습니다");
    }
}
