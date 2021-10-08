package com.ihrm.demo;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class CreateJwtTest {
    public static void main(String[] args) {
        JwtBuilder jwtBuilder = Jwts.builder().setId("8888")
                .setSubject("小童")
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, "ihrm");
        System.out.println(jwtBuilder.compact());
    }
}
