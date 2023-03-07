package nic.testprojet.AccountingSystem.security.JWT;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;


public class JWTConstants {
    public static final long JWT_EXPIRATION = 70000;
    //Разработать функцию генерации и десариализации, скорее всего так делать пипец как не правильно.
    public static final Key JWT_SECRET = Keys.secretKeyFor(SignatureAlgorithm.HS256);
}
