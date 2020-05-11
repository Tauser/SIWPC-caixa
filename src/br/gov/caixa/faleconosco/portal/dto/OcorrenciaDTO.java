package br.gov.caixa.faleconosco.portal.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import br.gov.caixa.faleconosco.portal.util.Util;

public class OcorrenciaDTO implements Serializable{

	private Long protocolo;
	private Date dataEnvio;
	
	//indica se a pessoal que esta preenchendo o formulario tem ou nao conta na caixa
	private String icRelacionamento; 
	
	//indica qual o tipo do formulario 
	private Integer tipo;
	
	private Integer naturezaOcor;
	
	private String nome;
	
	private String noMae;
	
	private String dtNascimento;
	
	private String cpf;
	
	private String cpfCnpj;
	
	private String agencia;
	
	private String conta;
	
	private String operacao;
	
	private String ddd;
	
	private String tel;
	
	private String noCidade;
	
	private String categoria;
	
	private String mensagem;
	
	private String email;
	
	private String sgUf;
	
	
	//adcionados na criacao da avaliacao
	
	private String noSituacaoOcorrencia;
	
	private String noCategoria;
	
	private String noNatureza;
	
	private Timestamp dhOcorrencia;
	
	private String dhOcorenciaStr;
	
	private String numeroFase;
	
	private String numeroTipoOcorrencia;
	
	private String origem;

	private String nuMovimentacao;
	
	
	//fim
	
	
	public Timestamp getDhOcorrencia() {
		return dhOcorrencia;
	}
	public String getNoMae() {
		return noMae;
	}
	public void setNoMae(String noMae) {
		this.noMae = noMae;
	}
	public String getDtNascimento() {
		return dtNascimento;
	}
	public void setDtNascimento(String dtNascimento) {
		this.dtNascimento = dtNascimento;
	}
	public void setDhOcorrencia(Timestamp dhOcorrencia) {
		this.dhOcorrencia = dhOcorrencia;
	}
	public String getNoCategoria() {
		return noCategoria;
	}
	public void setNoCategoria(String noCategoria) {
		this.noCategoria = noCategoria;
	}
	public String getNoSituacaoOcorrencia() {
		return noSituacaoOcorrencia;
	}
	public void setNoSituacaoOcorrencia(String noSituacaoOcorrencia) {
		this.noSituacaoOcorrencia = noSituacaoOcorrencia;
	}
	public String getSgUf() {
		return sgUf;
	}
	public void setSgUf(String sgUf) {
		this.sgUf = sgUf;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public String getAgencia() {
		return agencia;
	}
	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}
	public String getConta() {
		return conta;
	}
	public void setConta(String conta) {
		this.conta = conta;
	}
	public String getOperacao() {
		return operacao;
	}
	public void setOperacao(String operacao) {
		this.operacao = operacao;
	}
	public String getDdd() {
		return ddd;
	}
	public void setDdd(String ddd) {
		this.ddd = ddd;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getNoCidade() {
		return noCidade;
	}
	public void setNoCidade(String noCidade) {
		this.noCidade = noCidade;
	}
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	public String getMensagem() {
		return mensagem;
	}
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	public String getIcRelacionamento() {
		return icRelacionamento;
	}
	public void setIcRelacionamento(String icRelacionamento) {
		this.icRelacionamento = icRelacionamento;
	}
	public Integer getTipo() {
		return tipo;
	}
	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}
	public Integer getNaturezaOcor() {
		return naturezaOcor;
	}
	public void setNaturezaOcor(Integer naturezaOcor) {
		this.naturezaOcor = naturezaOcor;
	}

	public String getOperacaoConta(){
		String retorno = null;
		if(!Util.isEmpty(operacao)&&!Util.isEmpty(conta)){
			retorno = operacao+"/"+conta;
		}
		return retorno;
	}
	public Long getProtocolo() {
		return protocolo;
	}
	public void setProtocolo(Long protocolo) {
		this.protocolo = protocolo;
	}
	public Date getDataEnvio() {
		return dataEnvio;
	}
	public void setDataEnvio(Date dataEnvio) {
		this.dataEnvio = dataEnvio;
	}
	public String getNoNatureza() {
		return noNatureza;
	}
	public void setNoNatureza(String noNatureza) {
		this.noNatureza = noNatureza;
	}
	public String getDhOcorenciaStr() {
		return dhOcorenciaStr;
	}
	public void setDhOcorenciaStr(String dhOcorenciaStr) {
		this.dhOcorenciaStr = dhOcorenciaStr;
	}
	public String getOrigem() {
		return origem;
	}
	public void setOrigem(String origem) {
		this.origem = origem;
	}
	public String getNumeroFase() {
		return numeroFase;
	}
	public void setNumeroFase(String numeroFase) {
		this.numeroFase = numeroFase;
	}
	public String getNumeroTipoOcorrencia() {
		return numeroTipoOcorrencia;
	}
	public void setNumeroTipoOcorrencia(String numeroTipoOcorrencia) {
		this.numeroTipoOcorrencia = numeroTipoOcorrencia;
	}
	public String getCpfCnpj() {
		return cpfCnpj;
	}
	public void setCpfCnpj(String cpfCnpj) {
		this.cpfCnpj = cpfCnpj;
	}
	public String getNuMovimentacao() {
		return nuMovimentacao;
	}
	public void setNuMovimentacao(String nuMovimentacao) {
		this.nuMovimentacao = nuMovimentacao;
	}

	
	
	
	
	
}