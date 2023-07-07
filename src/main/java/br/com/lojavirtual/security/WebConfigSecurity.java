package br.com.lojavirtual.security;

import javax.servlet.http.HttpSessionListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import br.com.lojavirtual.service.ImplementacaoUserDatailsService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebConfigSecurity extends WebSecurityConfigurerAdapter implements HttpSessionListener {

	@Autowired
	private ImplementacaoUserDatailsService implementacaoUserDatailsService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
		.disable().authorizeRequests().antMatchers("/").permitAll()
		.antMatchers("/index").permitAll()
		.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
		//Redireciona ou dá um retorno para index quando desloga
		.anyRequest().authenticated().and().logout().logoutSuccessUrl("/index")
		//Mapeia o logout do sistema
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		//Filtra as requisições para login de JWT
		.and().addFilterAfter(new JWTLoginFIlter("/login", authenticationManager()), UsernamePasswordAuthenticationFilter.class)
		
		.addFilterBefore(new JwtApiAutenticacaoFilter(), UsernamePasswordAuthenticationFilter.class);
	}
	
	// Irá consultar o user no banco com Spring Security
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(implementacaoUserDatailsService).passwordEncoder(new BCryptPasswordEncoder());
	}
	
	// Ignora algumas URLs livre de autenticação
	@Override
	public void configure(WebSecurity web) throws Exception {
		//web.ignoring().antMatchers(HttpMethod.GET, "/salvarAcesso", "/deleteAcesso").
		//antMatchers(HttpMethod.POST, "/salvarAcesso", "/deleteAcesso");
		/* Ignorando URL no momento para não autenticar */
	}
}
