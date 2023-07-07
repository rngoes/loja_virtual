package br.com.lojavirtual.security;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import br.com.lojavirtual.ApplicationContextLoad;
import br.com.lojavirtual.model.Usuario;
import br.com.lojavirtual.repository.UsuarioRepository;
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
		
		liberacaoCors(response);

		// Usado para ver no Postman para teste
		response.getWriter().write("{\"Authorization\": \"" + token + "\"}");

	}
	
	//Retorna o usuário validado com o token ou caso não seja validado retorna null
	public Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response) {
		
		String token = request.getHeader(HEADER_STRING);
		
		if (token != null) {
			String tokenLimpo = token.replace(TOKEN_PREFIX, "").trim();
			
			// Faz a validação do token do usuário ne requisição e obtem o USER
			String user = Jwts.parser()
					.setSigningKey(SECRET)
					.parseClaimsJws(tokenLimpo)
					.getBody()
					.getSubject();
			
			if (user != null) {
				Usuario usuario = ApplicationContextLoad
						.getApplicationContext()
						.getBean(UsuarioRepository.class)
						.findUserByLong(user);
				
				if (usuario != null) {
					return new UsernamePasswordAuthenticationToken(usuario.getLogin(), usuario.getSenha(), usuario.getAuthorities());
				}
			}
		}
		
		liberacaoCors(response);
		
		return null;
		
	}

	// Fazendo liberação contra erro de COrs no navegador
	private void liberacaoCors(HttpServletResponse response) {
		
		if (response.getHeader("Access-Control-Allow-Origin") == null) {
			response.addHeader("Access-Control-Allow-Origin", "*");
		}
		
		if (response.getHeader("Access-Control-Allow-Headers") == null) {
			response.addHeader("Access-Control-Allow-Headers", "*");
		}
		
		if (response.getHeader("Access-Control-Request-Headers") == null) {
			response.addHeader("Access-Control-Request-Headers", "*");
		}
		
		if (response.getHeader("Access-Control-Allow-Methods") == null) {
			response.addHeader("Access-Control-Allow-Methods", "*");
		}
		
	}
}
