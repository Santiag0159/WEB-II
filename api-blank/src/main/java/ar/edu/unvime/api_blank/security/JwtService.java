package ar.edu.unvime.api_blank.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    //Clave secreta
    public static final String SECRET = "acaVaLaSuperClaveSuperSecretaYSeguraParaTp3WEB2";
    private final SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());

    public String generarToken(Long id, String email, String rol){
        return Jwts.builder()
                .subject(email)
                .claim("id", id)
                .claim("rol", rol)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+ 100 * 60 * 60 * 10))
                .signWith(key)
                .compact();
    }

    public Claims validarToken(String token){
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
