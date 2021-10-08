package com.ihrm.demo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class ParseJwtTest {
    public static void main(String[] args) {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI4ODg4Iiwic3ViIjoi5bCP56ulIiwiaWF0IjoxNjI5MTY0MTIwfQ.oHfUnp_nV849meCmwiFLH66P0xaU5JxsnJocY2ni5dk";
        Claims claims = Jwts.parser().setSigningKey("ihrm").parseClaimsJws(token).getBody();
        System.out.println(claims.getId());
        System.out.println(claims.getSubject());
        System.out.println(claims.getIssuedAt());
    }
}
