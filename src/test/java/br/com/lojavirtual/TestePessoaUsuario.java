package br.com.lojavirtual;

import java.util.Calendar;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import br.com.lojavirtual.controller.PessoaController;
import br.com.lojavirtual.model.PessoaJuridica;
import junit.framework.TestCase;

@Profile("test")
@SpringBootTest(classes = LojaVirtualApplication.class)
public class TestePessoaUsuario extends TestCase {

	
	@Autowired
	private PessoaController pessoaController;
	
	@Test
	public void testCadPessoaFisica() throws ExceptionLojaVirtual {
		
		PessoaJuridica pessoaJuridica = new PessoaJuridica();
		pessoaJuridica.setCnpj("" + Calendar.getInstance().getTimeInMillis());
		pessoaJuridica.setNome("Empresa Teste");
		pessoaJuridica.setEmail("empteste@gmail.com");
		pessoaJuridica.setTelefone("81998223993");
		pessoaJuridica.setInscEstadual("1234567890");
		pessoaJuridica.setInscMunicipal("1234567890");
		pessoaJuridica.setNomeFantasia("Empresa Teste");
		pessoaJuridica.setRazaoSocial("Empresa Teste LTDA");
		
		pessoaController.salvarPj(pessoaJuridica);
		
		/*PessoaFisica pessoaFisica = new PessoaFisica();
		
		pessoaFisica.setCpf("02909360474");
		pessoaFisica.setNome("Rodrigo Navarro");
		pessoaFisica.setEmail("rngoes@gmail.com");
		pessoaFisica.setTelefone("81998223993");
		pessoaFisica.setEmpresa(pessoaFisica);*/
	}
}
