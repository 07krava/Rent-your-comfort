package com.example.rent_yourcomfort.configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class JwtServiceTest {

    private static final String SECRET_KEY = "a384b14e6ea8429876401da41a3ed467e0d90d2f0dd1a89e514f6939686a33ea";

    private final JwtService jwtService = new JwtService();

    private Key getSignInKey() {
        byte[] keyBytes = java.util.Base64.getDecoder().decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Test
    void testGenerateToken() {
        UserDetails userDetails = User.withUsername("test@example.com").password("password").roles("USER").build();
        String token = jwtService.generateToken(userDetails);

        assertNotNull(token);
        assertTrue(jwtService.isTokenValid(token, userDetails));
    }

    @Test
    void testExtractUsername() {
        UserDetails userDetails = User.withUsername("test@example.com").password("password").roles("USER").build();
        String token = jwtService.generateToken(userDetails);

        String username = jwtService.extractUsername(token);

        assertEquals("test@example.com", username);
    }

    @Test
    void testIsTokenValid() {
        UserDetails userDetails = User.withUsername("test@example.com").password("password").roles("USER").build();
        String token = jwtService.generateToken(userDetails);

        assertTrue(jwtService.isTokenValid(token, userDetails));
    }

    @Test
    void testIsTokenValid_whenTokenIsNotExpired() {
        UserDetails userDetails = User.withUsername("test@example.com").password("password").roles("USER").build();

        // Генерируем токен с будущим временем истечения
        String token = jwtService.generateToken(userDetails);

        // Проверяем, что токен валидный
        assertTrue(jwtService.isTokenValid(token, userDetails));
    }

    @Test
    void testIsTokenExpired() {
        UserDetails userDetails = User.withUsername("test@example.com").password("password").roles("USER").build();

        // Генерируем токен
        String token = jwtService.generateToken(userDetails);

        // Извлекаем ключ и дату истечения из оригинального токена
        Key key = getSignInKey();
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        // Устанавливаем истекшее время
        Date expiredExpiration = new Date(System.currentTimeMillis() - 1000 * 60 * 60); // Время истечения в прошлом

        // Создаем новый токен с измененной датой истечения
        String expiredToken = Jwts.builder()
                .setClaims(claims)
                .setExpiration(expiredExpiration)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // Проверяем, что токен с истекшим временем недействителен
        try {
            jwtService.extractUsername(expiredToken);
            assertFalse(jwtService.isTokenValid(expiredToken, userDetails));
        } catch (ExpiredJwtException e) {
            // Успешно ловим исключение, значит, токен действительно истек
        }
    }
}
