package com.br.instersys.to;

public class DescricaoTO {

	private String protocolo;
	private String chave;
	private String dataRecibo;

	public String getProtocolo() {
		return protocolo;
	}

	public void setProtocolo(String protocolo) {
		int qtd = 9 - protocolo.length();
		String zeros = "";
		for (int i = 0; i < qtd; i++) {
			zeros += "0";
		}
		this.protocolo = zeros + protocolo;
	}

	public String getChave() {
		return chave;
	}

	public void setChave(String chave) {
		String part;
		String indent = "";
		int count = 8;
		String part1= chave.substring(0,4);
		String partFim = chave.substring(40,44);
		for (int i = 0; i < 44; i++) {
			if (i == count ) {
				part = chave.substring(i-4, count);
				indent += part + ".";
				count += 4;
			}
		}
		this.chave = part1+"."+indent+""+partFim;
	}

	public String getDataRecibo() {
		return dataRecibo;
	}

	public void setDataRecibo(String dataRecibo) {
		this.dataRecibo = dataRecibo;
	}

}
