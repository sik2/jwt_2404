package com.sb.jwt;

import com.sb.jwt.global.jwt.JwtProvider;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class JwtApplicationTests {

	@Autowired
	private JwtProvider jwtProvider;

	@Value("${custom.jwt.secretKey}")
	private String originSecretKey;
	@Test
	@DisplayName("시크릿 키 체크")
	void checkedSecretKey () {
		assertThat(originSecretKey).isNotNull();
	}

	@Test
	@DisplayName("기존 시크릿 키를 암호화 알고리즘을 통해 SecretKey 객체 만들기")
	void test2 () {
		String keyBase64Encoded =  Base64.getEncoder().encodeToString(originSecretKey.getBytes());

		SecretKey secretKey = Keys.hmacShaKeyFor(keyBase64Encoded.getBytes());

		assertThat(secretKey).isNotNull();
	}

	@Test
	@DisplayName("jwtProvider 활용 SecretKey 객체 생성")
	void test4 () {
		SecretKey secretKey = jwtProvider.getSecretKey();
		assertThat(secretKey).isNotNull();

	}

	@Test
	@DisplayName("jwtProvider 객체 생성 1번만 하기")
	void test5 () {
		SecretKey secretKey1 = jwtProvider.getSecretKey();
		SecretKey secretKey2 = jwtProvider.getSecretKey();
		assertThat(secretKey1 == secretKey2).isTrue();
	}

	@Test
	@DisplayName("access Token 발급")
	void test6 () {
		Map<String, Object> claims = new HashMap<>();
		claims.put("id", 1L);
		claims.put("username", "user1");

		String accessToken = jwtProvider.genToken(claims, 60 * 60 * 3);

		System.out.println("accessToken : " + accessToken);

		assertThat(accessToken).isNotNull();
	}

	@Test
	@DisplayName("만료된 토큰 유효하지 않은지")
	void test7() {
		Map<String, Object> claims = new HashMap<>();
		claims.put("id", 2L);
		claims.put("username", "admin");

		// 유효성 시간 x
		String accessToken = jwtProvider.genToken(claims, -1);

		System.out.println("accessToken :" + accessToken);

		assertThat(jwtProvider.verify(accessToken)).isFalse();
	}
	@Test
	@DisplayName("access Token을 이용하여 claims 정보 가져오기")
	void test8() {
		Map<String, Object> claims = new HashMap<>();
		claims.put("id", 2L);
		claims.put("username", "admin");

		// 10분
		String accessToken = jwtProvider.genToken(claims, 60 * 10);

		System.out.println("accessToken :" + accessToken);

		assertThat(jwtProvider.verify(accessToken)).isTrue();

		Map<String, Object> claimsFromToken = jwtProvider.getClaims(accessToken);
		System.out.println("claimsFromToken : " + claimsFromToken);
	}
}
