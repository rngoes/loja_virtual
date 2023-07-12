package br.com.lojavirtual;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.lojavirtual.controller.AcessoController;
import br.com.lojavirtual.model.Acesso;
import br.com.lojavirtual.repository.AcessoRepository;
import junit.framework.TestCase;

@Profile("test")
@SpringBootTest(classes = LojaVirtualApplication.class)
public class LojaVirtualApplicationTests extends TestCase {

	@Autowired
	private AcessoController acessoController;

	@Autowired
	private AcessoRepository acessoRepository;
	
	@Autowired
	private WebApplicationContext wac;
	
	@Test
	public void testRestApiCadastroAcesso() throws JsonProcessingException, Exception {
		DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
		MockMvc mockMvc = builder.build();
		
		Acesso acesso = new Acesso();
		acesso.setDescricao("ROLE_COMPRADOR");
		ObjectMapper objectMapper = new ObjectMapper();
		ResultActions retornoAPI = mockMvc.perform(MockMvcRequestBuilders.post("/salvarAcesso")
										  .content(objectMapper.writeValueAsString(acesso))
										  .accept(MediaType.APPLICATION_JSON)
										  .contentType(MediaType.APPLICATION_JSON));
		
		// Converter o retorno da API  para um objeto de acesso.
		Acesso objetoRetorno = objectMapper.readValue(retornoAPI.andReturn().getResponse().getContentAsString(), Acesso.class);
		assertEquals(acesso.getDescricao(), objetoRetorno.getDescricao());
	}
	
	@Test
	public void testRestApiDeleteAcesso() throws JsonProcessingException, Exception {
		DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
		MockMvc mockMvc = builder.build();
		
		Acesso acesso = new Acesso();
		acesso.setDescricao("ROLE_DELETE");
		acesso = acessoRepository.save(acesso);
		ObjectMapper objectMapper = new ObjectMapper();
		ResultActions retornoAPI = mockMvc.perform(MockMvcRequestBuilders.post("/deleteAcesso")
										  .content(objectMapper.writeValueAsString(acesso))
										  .accept(MediaType.APPLICATION_JSON)
										  .contentType(MediaType.APPLICATION_JSON));
		
		assertEquals("Acesso Removido", retornoAPI.andReturn().getResponse().getContentAsString());
		assertEquals(200, retornoAPI.andReturn().getResponse().getStatus());
	}
	
	@Test
	public void testRestApiDeletePorIDAcesso() throws JsonProcessingException, Exception {
		DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
		MockMvc mockMvc = builder.build();
		
		Acesso acesso = new Acesso();
		acesso.setDescricao("ROLE_DELETE_ID");
		acesso = acessoRepository.save(acesso);
		ObjectMapper objectMapper = new ObjectMapper();
		ResultActions retornoAPI = mockMvc.perform(MockMvcRequestBuilders.delete("/deleteAcessoPorId/" + acesso.getId())
										  .content(objectMapper.writeValueAsString(acesso))
										  .accept(MediaType.APPLICATION_JSON)
										  .contentType(MediaType.APPLICATION_JSON));
		
		assertEquals("Acesso Removido", retornoAPI.andReturn().getResponse().getContentAsString());
		assertEquals(200, retornoAPI.andReturn().getResponse().getStatus());
	}
	
	@Test
	public void testRestApiObterAcessoID() throws JsonProcessingException, Exception {
		DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
		MockMvc mockMvc = builder.build();
		
		Acesso acesso = new Acesso();
		acesso.setDescricao("ROLE_OBTER_ID");
		acesso = acessoRepository.save(acesso);
		ObjectMapper objectMapper = new ObjectMapper();
		ResultActions retornoAPI = mockMvc.perform(MockMvcRequestBuilders.get("/obterAcesso/" + acesso.getId())
										  .content(objectMapper.writeValueAsString(acesso))
										  .accept(MediaType.APPLICATION_JSON)
										  .contentType(MediaType.APPLICATION_JSON));
		
		assertEquals(200, retornoAPI.andReturn().getResponse().getStatus());
		// Converter o retorno da API  para um objeto de acesso.
		Acesso objetoRetorno = objectMapper.readValue(retornoAPI.andReturn().getResponse().getContentAsString(), Acesso.class);
		assertEquals(acesso.getDescricao(), objetoRetorno.getDescricao());
		assertEquals(acesso.getId(), objetoRetorno.getId());
	}
	
	@Test
	public void testRestApiObterAcessoDesc() throws JsonProcessingException, Exception {
		DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
		MockMvc mockMvc = builder.build();
		
		Acesso acesso = new Acesso();
		acesso.setDescricao("ROLE_OBTER_DESC");
		acesso = acessoRepository.save(acesso);
		ObjectMapper objectMapper = new ObjectMapper();
		ResultActions retornoAPI = mockMvc.perform(MockMvcRequestBuilders.get("/buscarPorDesc/OBTER_DESC")
										  .content(objectMapper.writeValueAsString(acesso))
										  .accept(MediaType.APPLICATION_JSON)
										  .contentType(MediaType.APPLICATION_JSON));
		
		assertEquals(200, retornoAPI.andReturn().getResponse().getStatus());
		// Converter o retorno da API  para um objeto de acesso.
		List<Acesso> objetoRetorno = objectMapper.readValue(retornoAPI.andReturn().getResponse().getContentAsString(), 
															new TypeReference<List<Acesso>>() {});
		assertEquals(1, objetoRetorno.size());
		assertEquals(acesso.getDescricao(), objetoRetorno.get(0).getDescricao());
		
		acessoRepository.deleteById(acesso.getId());
	}

	@Test
	public void testCadastraAcesso() throws ExceptionLojaVirtual {
		Acesso acesso = new Acesso();

		acesso.setDescricao("ROLE_ADMIN");

		assertEquals(true, acesso.getId() == null);

		// Gravou no banco de dados
		acesso = acessoController.salvarAcesso(acesso).getBody();

		assertEquals(true, acesso.getId() > 0);

		// Validar dados salvos da forma correta
		assertEquals("ROLE_ADMIN", acesso.getDescricao());

		// Teste de carregamento
		Acesso acesso2 = acessoRepository.findById(acesso.getId()).get();

		assertEquals(acesso.getId(), acesso2.getId());

		// Teste de deleção

		acessoRepository.deleteById(acesso2.getId());

		acessoRepository.flush(); // Roda esse SQL de delete no banco de dados

		Acesso acesso3 = acessoRepository.findById(acesso2.getId()).orElse(null);

		assertEquals(true, acesso3 == null);

		// Teste de query

		acesso = new Acesso();

		acesso.setDescricao("ROLE_ALUNO");

		acesso = acessoController.salvarAcesso(acesso).getBody();

		List<Acesso> acessos = acessoRepository.buscarAcessoDesc("ALUNO".trim().toUpperCase());

		assertEquals(1, acessos.size());

		acessoRepository.deleteById(acesso.getId());
	}

}
