package com.example.springjwt.jwt;

public class LoginRequest {
  private String username;
  private String password;

  // 생성자 없어도 됨 (Jackson이 기본 생성자 + setter로 값 주입함)

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
