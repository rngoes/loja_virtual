package br.com.lojavirtual.security;

import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

// Criar a autenticação e retornar também a autenticação JWT
@Service
@Component
public class JWTTokenAutenticacaoService {

	// Token de validade de 30 dias
	private static final long EXPIRATION_TIME = 950400000;

	// Chave de senha para juntar com o JWT
	private static final String SECRET = "ArthurNavarro";

	private static final String TOKEN_PREFIX = "Bearer";

	private static final String HEADER_STRING = "Authorization";

	// Gera token e dá a resposta para o cliente com o JWT
	public void addAuthentication(HttpServletResponse response, String username) throws Exception {

		// Montage do Token
		String JWT = Jwts.builder() // Chama o gerador de token
				.setSubject(username) // Adiciona o user
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Tempo de expiração
				.signWith(SignatureAlgorithm.HS512, SECRET).compact();

		String token = TOKEN_PREFIX + " " + JWT;

		// Dá a resposta para a tela e para o cliente
		response.addHeader(HEADER_STRING, token);
		
		// Usado para ver no Postman para teste
		response.getWriter().write("{\"Authorization\": \"" + token + "\"}");

	}
}
