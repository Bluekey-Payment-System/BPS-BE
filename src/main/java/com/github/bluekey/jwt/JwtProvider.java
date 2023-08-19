package com.github.bluekey.jwt;

import com.github.bluekey.entity.member.MemberRole;
import com.github.bluekey.entity.member.MemberType;
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

	public String generateAccessToken(String username, MemberType memberType, MemberRole memberRole) {
		Claims claims = Jwts.claims().setSubject(username);
		claims.put("type", memberType);
		claims.put("role", memberRole);
		return Jwts.builder()
				.setClaims(claims)
				.setExpiration(new Date(System.currentTimeMillis() + tokenValidMilisecond * 1000))
				.signWith(secretKey)
				.compact();
	}

	public Authentication getAuthentication (String token) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(this.getClaims(token).getSubject());
		return new UsernamePasswordAuthenticationToken(userDetails.getUsername(), "", userDetails.getAuthorities());
	}

	public String resolveToken(HttpServletRequest request) {
		return request.getHeader("Authorization");
	}

	public boolean validateToken(String token) {
		try {
			if (!token.startsWith("Bearer ")) {
				return false;
			}
			Claims claims = getClaims(token.substring(7)); // Remove Bearer prefix
			return !claims.getExpiration().before(new Date());
		} catch (Exception e) {
			return false;
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
