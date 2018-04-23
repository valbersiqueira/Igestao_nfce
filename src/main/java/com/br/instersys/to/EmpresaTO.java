package com.br.instersys.to;

public class EmpresaTO {

	private String nome;
	private String cnpj;
	private String ie;
	private String logradouro;
	private String bairro;
	private String municipio;
	private String uf;
	private String cep;
	private String numero;

	public EmpresaTO() {
		this.nome = "";
		this.cnpj = "";
		this.ie = "";
		this.logradouro = "";
		this.bairro = "";
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		String part1 = cnpj.substring(0, 2);
		String part2 = cnpj.substring(2, 5);
		String part3 = cnpj.substring(5, 8);
		String part4 = cnpj.substring(8, 12);
		String part5 = cnpj.substring(12, 14);
		cnpj = part1 + "." + part2 + "." + part3 + "/" + part4 + "-" + part5;
		this.cnpj = cnpj;
	}

	public String getIe() {
		return ie;
	}

	public void setIe(String ie) {
		String part1 = ie.substring(0,2);
		String part2 = ie.substring(2,5);
		String part3 = ie.substring(5,8);
		String part4 = ie.substring(8,9);
		ie = part1+"."+part2+"."+part3+"-"+part4;
		this.ie = ie;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getMunicipio() {
		return municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	@Override
	public String toString() {
		return nome + "\n" + cnpj + "  " + ie + "\n" + logradouro + "  " + bairro;
	}

}
