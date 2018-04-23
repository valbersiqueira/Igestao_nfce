package com.br.instersys.to;

public class ProdutoTO {

	private String codigo;
	private String item;
	private String descricao;
	private String quantidade;
	private String unidade;
	private String valorUni;
	private String ValorTot;
	private String desconto;

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		switch (item.length()) {
		case 1:
			item = "00" + item;
			break;

		case 2:
			item = "0" + item;
			break;

		default:
			break;
		}
		this.item = item;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getQuantidade() {
		String qtd = quantidade;
		try {
			@SuppressWarnings("unused")
			int num = 0 / Integer.parseInt(qtd.substring(qtd.indexOf(".") + 1));
		} catch (Exception e) {
			qtd = qtd.substring(0, qtd.indexOf("."));
		}

		return qtd;
	}

	public void setQuantidade(String quantidade) {
		this.quantidade = quantidade;
	}

	public String getUnidade() {
		return unidade;
	}

	public void setUnidade(String unidade) {
		this.unidade = unidade;
	}

	public String getValorUni() {
		return valorUni.replace(".", ",");
	}

	public void setValorUni(String valorUni) {
		this.valorUni = valorUni;
	}

	public String getValorTot() {
		return ValorTot.replace(".", ",");
	}

	public void setValorTot(String valorTot) {
		ValorTot = valorTot;
	}

	public String getDesconto() {
		return desconto;
	}

	public void setDesconto(String desconto) {
		this.desconto = desconto;
	}

	@Override
	public String toString() {
		return "ProdutoTO [codigo=" + codigo + ", item=" + item + ", descricao=" + descricao + ", quantidade="
				+ getQuantidade() + ", unidade=" + unidade + ", valorUni=" + valorUni + ", ValorTot=" + ValorTot + "]";
	}

}
