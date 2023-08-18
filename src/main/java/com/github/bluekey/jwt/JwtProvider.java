package com.github.bluekey.jwt;

import com.github.bluekey.exception.AuthenticationException;
import com.github.bluekey.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtProvider {
	@Value("${jwt.secret}")
	private String secret;

	private Key secretKey;

	@Value("${jwt.token-validity-in-seconds}")
	private Long tokenValidMilisecond;

	private final UserDetailsService userDetailsService;

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

	public Authentication getAuthentication (String token) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(this.getAccount(token));
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

	public String resolveToken(HttpServletRequest request) {
		return request.getHeader("Authorization");
	}

	public boolean validateToken(String token) {
		try {
			if (!token.substring(0, "BEARER ".length()).equalsIgnoreCase("BEARER ")) {
				return false;
			} else {
				token = token.split(" ")[1].trim();
			}
			Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
			return !claims.getBody().getExpiration().before(new Date());
		} catch (Exception e) {
			return false;
		}
	}

	public String getAccount(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(secretKey)
				.build()
				.parseClaimsJws(token)
				.getBody()
				.getSubject();
	}
}