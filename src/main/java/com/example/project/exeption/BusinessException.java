package com.example.project.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BusinessException extends Exception{
    public BusinessException(String message) {
        super(message);
    }
}