package com.github.bluekey.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtProvider {
	@Value("${jwt.secret}")
	private String secret;

	private Key secretKey;

	@Value("${jwt.token-validity-in-seconds}")
	private Long tokenValidMilisecond;

	@PostConstruct
	protected void init() {
		byte[] keyBytes = Decoders.BASE64.decode(secret);
		secretKey = Keys.hmacShaKeyFor(keyBytes);
	}

	public String generateAccessToken(String username) {
		Claims claims = Jwts.claims().setSubject(username);
		claims.put("roles", "USER");
		return Jwts.builder()
				.setClaims(claims)
				.setExpiration(new Date(System.currentTimeMillis() + tokenValidMilisecond))
				.signWith(secretKey)
				.compact();
	}

}
