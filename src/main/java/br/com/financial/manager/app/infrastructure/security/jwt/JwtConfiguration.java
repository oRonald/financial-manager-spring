package br.com.financial.manager.app.infrastructure.security.jwt;

import br.com.financial.manager.app.domain.entity.Users;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
public class JwtConfiguration {

    @Value("${api.token.secret}")
    private String secret;

    public String generateToken(Users users){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withSubject(users.getEmail())
                    .withExpiresAt(Instant.now().plus(Duration.ofMinutes(15)))
                    .sign(algorithm);
        } catch (JWTCreationException e){
            throw new RuntimeException("Error generating token");
        }
    }
}
