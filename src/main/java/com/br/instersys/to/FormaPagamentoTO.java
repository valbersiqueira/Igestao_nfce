package com.br.instersys.to;

public class FormaPagamentoTO {

	private String codigo;
	private String valorPago;

	public String getCodigo() {
		switch (codigo) {
		case "01":
			codigo = "Dinheiro";
			break;
		case "02":
			codigo = "Cheque";
			break;
		case "03":
			codigo = "Cartao de Credito";
			break;
		case "04":
			codigo = "Cartao de Debito";
			break;
		case "05":
			codigo = "Credito Loja";
			break;
		case "10":
			codigo = "Vale Alimentacao";
			break;
		case "11":
			codigo = "Vale Refeicao";
			break;
		case "12":
			codigo = "Vale Presente";
			break;
		case "13":
			codigo = "Vale Combustivel";
			break;
		case "99":
			codigo = "Outros";
			break;
		default:
			break;
		}

		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo.replace(".", ",");
	}

	public String getValorPago() {
		return valorPago;
	}

	public void setValorPago(String valorPago) {
		this.valorPago = valorPago;
	}

	@Override
	public String toString() {
		return "FormaPagamentoTO [codigo=" + getCodigo() + ", valorPago=" + valorPago + "]";
	}

}
