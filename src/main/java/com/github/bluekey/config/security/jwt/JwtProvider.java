package com.github.bluekey.config.security.jwt;

import com.github.bluekey.entity.member.MemberRole;
import com.github.bluekey.entity.member.MemberType;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtProvider {

	private static final String AUTHORIZE_TYPE = "Bearer ";
	private static final String AUTHORIZE_HEADER = "Authorization";
	private static final int BEARER_TOKEN_PREFIX = 7;

	@Value("${jwt.secret}")
	private String secret;
	private Key secretKey;
	@Value("${jwt.token-validity-in-seconds}")
	private Long tokenValidMillisecond;
	private final UserDetailsService userDetailsService;

	@PostConstruct
	protected void init() {
		byte[] keyBytes = Decoders.BASE64.decode(secret);
		secretKey = Keys.hmacShaKeyFor(keyBytes);
	}

	public String generateAccessToken(String username, MemberType memberType, MemberRole memberRole) {
		Claims claims = Jwts.claims().setSubject(username);
		claims.put("type", memberType);
		claims.put("role", memberRole);
		return Jwts.builder()
				.setClaims(claims)
				.setExpiration(new Date(System.currentTimeMillis() + tokenValidMillisecond * 1000))
				.signWith(secretKey)
				.compact();
	}

	public Authentication getAuthentication (String token) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(this.getClaims(token).getSubject());
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

	public String resolveToken(HttpServletRequest request) {
		return request.getHeader(AUTHORIZE_HEADER);
	}

	public JwtValidationType validateToken(String token) {
		try {
			getClaims(token.substring(BEARER_TOKEN_PREFIX));
			return JwtValidationType.VALID_JWT;
		} catch (MalformedJwtException e) {
			return JwtValidationType.INVALID_JWT;
		} catch (ExpiredJwtException e) {
			return JwtValidationType.EXPIRED_JWT;
		} catch (UnsupportedJwtException e) {
			return JwtValidationType.UNSUPPORTED_JWT;
		} catch (IllegalArgumentException e) {
			return JwtValidationType.EMPTY_JWT;
		} catch (SignatureException e) {
			return JwtValidationType.INVALID_JWT_SIGNATURE;
		}
	}

	public Claims getClaims(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(secretKey)
				.build()
				.parseClaimsJws(token)
				.getBody();
	}
}
