package com.example.springjwt.exception;

import org.springframework.http.HttpStatus;

public class UsernameNotFoundException extends RuntimeException {
  private String message;
  private HttpStatus httpStatus;
  private  String title;
  public UsernameNotFoundException(String message, String title){
    this(message , title, HttpStatus.UNAUTHORIZED);//
  }
  public UsernameNotFoundException(String message, String title, HttpStatus httpStatus){
    this.message=message;
    this.title=title;
    this.httpStatus=httpStatus;
  }
}

