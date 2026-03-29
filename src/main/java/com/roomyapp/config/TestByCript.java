package com.roomyapp.config;

public class TestByCript {
    public static void main(String[] args) {
        System.out.println(new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode("1234"));
    }
}
