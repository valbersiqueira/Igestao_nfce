package com.br.instersys.to;

public class DestinoTO {

	private String cpf;
	private String nome;

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		String part1 = cpf.substring(0, 3);
		String part2 = cpf.substring(3, 6);
		String part3 = cpf.substring(6, 9);
		String part4 = cpf.substring(9, 11);
		this.cpf = part1 + "." + part2 + "." + part3 + "-" + part4;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

}
