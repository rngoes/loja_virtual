package br.com.lojavirtual.repository;

import org.springframework.stereotype.Repository;

import br.com.lojavirtual.model.PessoaJuridica;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

@Repository
public interface PessoaRepository extends CrudRepository<PessoaJuridica, Long> {

	@Query(value = "select pj from PessoaJuridica pj where pj.cnpj = ?1")
	public PessoaJuridica existeCNPJCadastrado(String cnpj);
}
