package com.sb.jwt;

import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.SecretKey;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class JwtApplicationTests {

	@Value("${custom.jwt.secretKey}")
	private String originSecretKey;
	@Test
	@DisplayName("시크릿 키 체크")
	void checkedSecretKey() {
		assertThat(originSecretKey).isNotNull();
	}

	@Test
	@DisplayName("기존 시크릿 키를 암호화 알고리즘을 통해 SecretKey 객체 만들기")
	void test2() {
		String keyBase64Encoded =  Base64.getEncoder().encodeToString(originSecretKey.getBytes());

		SecretKey secretKey = Keys.hmacShaKeyFor(keyBase64Encoded.getBytes());

		assertThat(secretKey).isNotNull();
	}

}
