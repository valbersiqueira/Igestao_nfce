package com.br.impressora;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.br.instersys.to.DescricaoTO;
import com.br.instersys.to.DestinoTO;
import com.br.instersys.to.EmpresaTO;
import com.br.instersys.to.FormaPagamentoTO;
import com.br.instersys.to.HeaderTO;
import com.br.instersys.to.ProdutoTO;
import com.br.instersys.util.ReadTxt;
import com.br.instersys.util.WritErro;
import com.br.instersys.xml.LerXml;
import com.br.instersys.xml.LerXmlPaths;
import com.br.valber.Log;

import br.com.daruma.jna.DUAL;

public class MontarImpressaoDaruma {
	static {
		try {
			System.load(new File("").getAbsolutePath() + "\\Darumaframework_DLL64\\DarumaFrameWork.dll");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private LerXml lerXml;
	// private String pathLogo;
	private List<String> pathViaCartao;
	private String vias;
	private String operador;

	public MontarImpressaoDaruma(String pathXml, String logomarca, List<String> pathViaCartao, String vias,
			String operador) {
		try {
			this.lerXml = new LerXml(pathXml);
			// this.pathLogo = logomarca;
			this.pathViaCartao = pathViaCartao;
			this.vias = vias;
			this.operador = operador;
		} catch (ParserConfigurationException | SAXException | IOException e) {
			new Log().montarFileErro(MontarImpressaoElgin.class, "Error ", e.getMessage());
		}
	}

	public int montarimpressaoDaruma() {
		EmpresaTO emp = this.lerXml.toEmpresa();
		List<ProdutoTO> listPro = this.lerXml.toProduto();
		FormaPagamentoTO pay = lerXml.getFormaPagamento();
		List<String> descontos = new ArrayList<>();
		HeaderTO headerTO = lerXml.header();
		DescricaoTO descricaoTO = lerXml.descricao();
		DestinoTO destino = lerXml.destino();

		int iRetorno = 0;
		iRetorno = DUAL.eBuscarPortaVelocidade();
		iRetorno = DUAL.rStatusImpressora();

		String pathLog = "C:\\intersys.bmp";
		iRetorno = DUAL.iEnviarBMP(pathLog);

		// iRetorno = DUAL.iImprimirBMP("C:\\intersys2.bmp");
		iRetorno = DUAL.iImprimirTexto("<bmp></bmp>", 0);
		// iRetorno = DUAL.iImprimirTexto("<ibmp>C:\\intersys2.bmp</ibmp>", 0);

		iRetorno = DUAL.iImprimirTexto("<ce><b>" + emp.getNome() + "</b></ce>", 0);
		iRetorno = DUAL.iImprimirTexto("<ce><b>CNPJ:" + emp.getCnpj() + " " + emp.getIe() + "</b></ce>", 0);
		iRetorno = DUAL.iImprimirTexto(
				"<ce><b>" + emp.getLogradouro() + " N:" + emp.getNumero() + " - " + emp.getBairro() + "</b></ce>", 0);

		iRetorno = DUAL.iImprimirTexto("<ce>------------------------------------------</ce>", 0);

		iRetorno = DUAL.iImprimirTexto("<ce>" + headerTO.getDataEmissao() + "</ce>", 0);
		iRetorno = DUAL.iImprimirTexto("<ce><c>DOCUMENTO AUXILIAR DA NOTA FISCAL DE </c></ce>", 0);
		iRetorno = DUAL.iImprimirTexto("<ce><c>CONSUMIDOR ELETRONICA</c></ce>", 0);

		iRetorno = DUAL.iImprimirTexto("<ce>------------------------------------------</ce>", 0);

		iRetorno = DUAL.iImprimirTexto("<c>ITEM    CODIGO    DESCRICACAO</c>", 0);
		iRetorno = DUAL.iImprimirTexto("<c>QTD.    UN        UN.VL.UNITARIO(R$)    VL. ITEM(R$)</c>", 0);

		iRetorno = DUAL.iImprimirTexto("<ce>------------------------------------------</ce>", 0);

		// iRetorno =
		// DUAL.iImprimirTexto("<c>012345678901234567890123456789012345678901234567890123456789</c>",
		// 0);

		int qtdTot = 0;
		double valorTotdes = 0.0;
		for (int i = 0; i < listPro.size(); i++) {

			ProdutoTO pro = listPro.get(i);
			if (pro.getDesconto() != null) {
				valorTotdes += Double.parseDouble(pro.getDesconto());
				descontos.add("DESCONTO SOBRE ITEM " + pro.getItem() + " " + pro.getDesconto().replace(".", ","));
			}
			qtdTot++;

			String valores = pro.getQuantidade() + " " + pro.getUnidade() + " " + pro.getValorUni() + " "
					+ pro.getValorTot();
			String space = "";
			int countChar = pro.getDescricao().length() + pro.getItem().length() + pro.getCodigo().length() + 3;
			int count = 0;
			int valorLeng = valores.length();
			count = countChar + valorLeng;

			if (count <= 57) {
				count = 57 - count;
				for (int j = 0; j < count; j++) {
					space += " ";
				}

			} else {
				if (countChar >= 57) {
					int valor = (countChar - 57) + valores.length();
					count = 57 - (valor);
				} else {
					count = ((57 - countChar) + 57 - valorLeng);
				}
				for (int j = 0; j < count; j++) {
					space += " ";
				}
			}

			iRetorno = DUAL.iImprimirTexto("<c>" + pro.getItem() + " " + pro.getCodigo() + "" + pro.getDescricao() + ""
					+ space + valores + "</c>", 0);

		}

		for (String des : descontos) {
			iRetorno = DUAL.iImprimirTexto("<c>" + des + "</c>", 0);
		}

		iRetorno = DUAL.iImprimirTexto("<ce>------------------------------------------</ce>", 0);

		iRetorno = DUAL.iImprimirTexto("<c>        QTD. TOTAL DE ITENS        " + qtdTot + "</c>", 0);
		iRetorno = DUAL.iImprimirTexto("<c>        VALOR TOTAL (R$)           " + this.lerXml.valorTotal() + "</c>", 0);
		if (valorTotdes != 0.0) {
			iRetorno = DUAL.iImprimirTexto("<c>        DESCONTO (R$)        " + valorTotdes + "</c>", 0);
		}
		iRetorno = DUAL.iImprimirTexto("<c>        FORMA DE PAGEMENTO         " + pay.getCodigo() + "</c>", 0);

		iRetorno = DUAL.iImprimirTexto("<ce>------------------------------------------</ce>", 0);

		iRetorno = DUAL.iImprimirTexto("<ce><c>Numero " + headerTO.getNumero() + " Serie " + headerTO.getSerie()
				+ "\n Emissao " + headerTO.getDataEmissao() + "</c></ce>", 0);
		iRetorno = DUAL.iImprimirTexto("<ce><c>Consulte pela chave de Acesso em:</c></ce>", 0);
		iRetorno = DUAL.iImprimirTexto("<c>http://homolog.sefaz.go.gov.br/nfeweb/sites/nfce/danfeNFCe</c>", 0);

		if (descricaoTO.getChave() != null) {
			iRetorno = DUAL.iImprimirTexto("<ce><c>CHAVE DE ACESSO</c></ce>", 0);
			iRetorno = DUAL.iImprimirTexto("<ce><c>" + descricaoTO.getChave() + "</c></ce>", 0);
			iRetorno = DUAL.iImprimirTexto("<ce><c></c></ce>", 0);
		}

		iRetorno = DUAL.iImprimirTexto("<ce>------------------------------------------</ce>", 0);

		if (destino.getCpf() != null) {

			iRetorno = DUAL.iImprimirTexto("<ce><c>CONSUMIDOR " + destino.getCpf() + "</c></ce>", 0);
			iRetorno = DUAL.iImprimirTexto("<ce><c>" + destino.getNome() + "</c></ce>", 0);

		} else {
			iRetorno = DUAL.iImprimirTexto("<ce><c>CONSUMIDOR NAO IDENTIFICADO</c></ce>", 0);

		}

		iRetorno = DUAL.iImprimirTexto("<ce>------------------------------------------</ce>", 0);

		iRetorno = DUAL
				.iImprimirTexto("<ce><c><qrcode>" + lerXml.getQRCode() + "<lmodulo>1</lmodulo></qrcode></c></ce>", 0);

		if (descricaoTO.getProtocolo() != null) {
			iRetorno = DUAL.iImprimirTexto("<ce><c>Protocolo de aturizacao</c></ce>", 0);
			iRetorno = DUAL.iImprimirTexto(
					"<ce><c>" + descricaoTO.getProtocolo() + " " + descricaoTO.getDataRecibo() + "</c></ce>", 0);

		} else {
			iRetorno = DUAL.iImprimirTexto("<ce><c>Nota emitida em Contigencia</c></ce>", 0);

		}

		iRetorno = DUAL.iImprimirTexto("<ce>------------------------------------------</ce>", 0);
		iRetorno = DUAL.iImprimirTexto("<ce><c>" + operador + "</c></ce>", 0);

		iRetorno = DUAL.iImprimirTexto("<ce>------------------------------------------</ce>", 0);
		iRetorno = DUAL.iImprimirTexto("<l></l>", 0);
		iRetorno = DUAL.iImprimirTexto("<gui></gui> ", 0);

		try {

			for (String pathVias : pathViaCartao) {
				for (int i = 1; i <= Integer.parseInt(vias); i++) {
					for (String t : ReadTxt.preencherViaCartao(pathVias)) {
						iRetorno = DUAL.iImprimirTexto("<ce><c>" + t + "</c></ce>", 0);
					}
					iRetorno = DUAL.iImprimirTexto("<l></l>", 0);
					iRetorno = DUAL.iImprimirTexto("<gui></gui> ", 0);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		switch (iRetorno) {
		case 0:
			new WritErro("Impressora Desligada!");
			break;
		case 1:
			break;
		case -50:
			new WritErro("Impressora OFF-LINE!");
			break;
		case -51:
			new WritErro("Impressora Sem Papel!");
			break;
		case -27:
			new WritErro("Erro Generico!");
			break;
		case -52:
			break;

		default:
			new WritErro("Retorno não esperado!");
		}

		return iRetorno;

	}

	public static void main(String[] args) {
		System.out.println("Exutar");
		File fileXml = new File(new File("").getAbsolutePath() + "/ENVIAR");
		try {
			fileXml.mkdir();
		} catch (Exception e) {
		}

		File enviar = new File(new File("").getAbsolutePath() + "/ENVIAR");
		File[] listfile = enviar.listFiles();
		for (File file : listfile) {
			String xml = file.getAbsolutePath();
			System.out.println(xml);
			try {
				LerXmlPaths paths = new LerXmlPaths(xml);
				MontarImpressaoDaruma d = new MontarImpressaoDaruma(paths.getPathXmlVia(),
						new File("").getAbsolutePath() + "\\logomarca\\intersys.bmp", paths.getPath().getPathTxt(),
						paths.getPath().getNumerovias(), paths.getOperador());
				d.montarimpressaoDaruma();
			} catch (ParserConfigurationException | SAXException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
