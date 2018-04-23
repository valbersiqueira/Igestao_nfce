package com.br.impressora;

import java.io.ByteArrayOutputStream;
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
import com.br.instersys.util.CarregarImpressora;
import com.br.instersys.util.Constantes;
import com.br.instersys.util.ReadTxt;
import com.br.instersys.xml.LerXml;
import com.br.valber.Log;

public class MontarImpressaoElgin {

	private String impressora;
	private LerXml lerXml;
	private String pathLogo;
	private List<String> pathViaCartao;
	private String vias;
	private String operador;

	public MontarImpressaoElgin(String path, String impressora, String pathLogo, List<String> pathViaCartao,
			String vias, String operador) {
		this.impressora = impressora;
		this.pathLogo = pathLogo;
		this.pathViaCartao = pathViaCartao;
		this.operador = operador;
		this.vias = vias;
		try {
			this.lerXml = new LerXml(path);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
			new Log().montarFileErro(MontarImpressaoElgin.class, "Error ", e.getMessage());
		}
	}

	public void montarFolha() {

		ByteArrayOutputStream streamTest = new ByteArrayOutputStream();
		try {
			streamTest.write(Constantes.STATUS);
			new CarregarImpressora().imprimir(streamTest, this.impressora);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		int qtdTot = 0;
		double valorTotdes = 0.0;
		ByteArrayOutputStream stream = null;
		try {

			EmpresaTO emp = this.lerXml.toEmpresa();

			List<ProdutoTO> listPro = this.lerXml.toProduto();
			FormaPagamentoTO pay = lerXml.getFormaPagamento();
			List<String> descontos = new ArrayList<>();
			HeaderTO headerTO = lerXml.header();
			DescricaoTO descricaoTO = lerXml.descricao();
			DestinoTO destino = lerXml.destino();

			new CarregarImpressora().imprimir(new Logomarca().printImage(new File(pathLogo)), impressora);
			//
			stream = new ByteArrayOutputStream();
			stream.write(Constantes.DELETAR);
			stream.write(espCaracter(2));
			stream.write(tmCaracter(1));
			// stream.write(Constantes.NEGRITO_ON);
			stream.write(Constantes.CENTRALIZAR);
			stream.write(new String((emp.getNome())).getBytes());
			stream.write(Constantes.LINHA);
			stream.write(new String("CNPJ: " + emp.getCnpj()).getBytes());
			stream.write(new String(" IE: " + emp.getIe()).getBytes());
			stream.write(Constantes.LINHA);
			stream.write(
					new String(emp.getLogradouro() + " N:" + emp.getNumero() + " - " + emp.getBairro()).getBytes());

			stream.write(espCaracter(0));
			stream.write(tmCaracter(1));

			stream.write(Constantes.LINHA);
			// stream.write(Constantes.NEGRITO_OFF);
			stream.write(new String("-------------------------------------------------------").getBytes());
			stream.write(Constantes.LINHA);
			stream.write(new String(headerTO.getDataEmissao()).getBytes());
			stream.write(Constantes.LINHA);
			stream.write(new String("DOCUMENTO AUXILIAR DA NOTA FISCAL DE CONSUMIDOR ELETRONICA").getBytes());
			stream.write(Constantes.LINHA);
			stream.write(new String("-------------------------------------------------------").getBytes());
			stream.write(Constantes.LINHA);
			stream.write(Constantes.ALINHA_ESQ);
			stream.write(
					new String("ITEM     CODIGO    DESCRICAO \n QTD.    UN     UN.VL.UNITARIO(R$)     VL ITEM (R$)")
							.getBytes());
			stream.write(Constantes.LINHA);

			stream.write(Constantes.CENTRALIZAR);

			stream.write(new String("-------------------------------------------------------").getBytes());
			stream.write(Constantes.LINHA);

			stream.write(Constantes.ALINHA_ESQ);

			for (int i = 0; i < listPro.size(); i++) {

				ProdutoTO pro = listPro.get(i);
				if (pro.getDesconto() != null) {
					valorTotdes += Double.parseDouble(pro.getDesconto());
					descontos.add("DESCONTO SOBRE ITEM " + pro.getItem() + " " + pro.getDesconto().replace(".", ","));
				}
				qtdTot++;
				stream.write(new String(pro.getItem()).getBytes());
				stream.write(new String(" " + pro.getCodigo()).getBytes());
				stream.write(new String(" " + pro.getDescricao() + " ").getBytes());

				String valores = pro.getQuantidade() + " " + pro.getUnidade() + " " + pro.getValorUni() + " "
						+ pro.getValorTot();

				String space = "";
				int countChar = pro.getDescricao().length() + pro.getItem().length() + pro.getCodigo().length() + 3;
				int count = 0;
				int valorLeng = valores.length();
				count = countChar + valorLeng;
				if (count <= 64) {
					count = 64 - count;
					for (int j = 0; j < count; j++) {
						space += " ";
					}

				} else {
					if (countChar >= 64) {
						int valor = (countChar - 64) + valores.length();
						count = 64 - (valor);
					} else {
						count = ((64 - countChar) + 64 - valorLeng);
					}
					for (int j = 0; j < count; j++) {
						space += " ";
						System.out.println(space);
					}
				}

				stream.write(new String(space + valores).getBytes());

				stream.write(Constantes.LINHA);
			}
			for (String des : descontos) {
				stream.write(Constantes.LINHA);
				stream.write(new String(des).getBytes());
				stream.write(Constantes.LINHA);
			}

			stream.write(Constantes.CENTRALIZAR);

			stream.write(new String("-------------------------------------------------------").getBytes());
			stream.write(Constantes.LINHA);

			stream.write(Constantes.ALINHA_ESQ);

			stream.write(espCaracter(0));
			stream.write(tmCaracter(1));
			stream.write(new String("       QTD. TOTAL DE ITENS             " + qtdTot).getBytes());
			stream.write(Constantes.LINHA);
			stream.write(new String("       VALOR TOTAL (R$)                " + lerXml.valorTotal()).getBytes());
			if (valorTotdes != 0.0) {
				stream.write(Constantes.LINHA);
				stream.write(new String("       DESCONTO (R$)                   " + valorTotdes).getBytes());
			}
			stream.write(Constantes.LINHA);
			stream.write(new String("       FORMA DE PAGAMENTO              " + pay.getCodigo()).getBytes());

			stream.write(Constantes.LINHA);
			stream.write(Constantes.CENTRALIZAR);
			stream.write(new String("-------------------------------------------------------").getBytes());
			stream.write(Constantes.LINHA);
			stream.write(new String("Numero " + headerTO.getNumero() + " Serie " + headerTO.getSerie() + " Emissao "
					+ headerTO.getDataEmissao()).getBytes());
			stream.write(Constantes.LINHA);
			stream.write(new String("Consulte pela Chave de Acesso em:").getBytes());
			stream.write(Constantes.LINHA);
			stream.write(new String("http://homolog.sefaz.go.gov.br/nfeweb/sites/nfce/danfeNFCe").getBytes());
			stream.write(Constantes.LINHA);
			if (descricaoTO.getChave() != null) {
				stream.write(new String("CHAVE DE ACESSO").getBytes());
				stream.write(Constantes.LINHA);
				stream.write(new String(descricaoTO.getChave()).getBytes());
			}
			stream.write(Constantes.LINHA);
			stream.write(new String("-------------------------------------------------------").getBytes());
			stream.write(Constantes.LINHA);

			if (destino.getCpf() != null) {
				stream.write(new String("CONSUMIDOR " + destino.getCpf()).getBytes());
				stream.write(Constantes.LINHA);
				stream.write(new String("NOME " + destino.getNome()).getBytes());
				stream.write(Constantes.LINHA);
			} else {
				stream.write(new String("CONSUMIDOR NAO IDENTIFICADO").getBytes());
				stream.write(Constantes.LINHA);
			}

			stream.write(new String("-------------------------------------------------------").getBytes());
			stream.write(Constantes.LINHA);

			stream.write(montarQRCode().getBytes());
			new CarregarImpressora().imprimir(stream, impressora);

			ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
			stream2.write(Constantes.LINHA);
			if (descricaoTO.getProtocolo() != null) {
				stream2.write(new String("Protocolo de aturizacao").getBytes());
				stream2.write(Constantes.LINHA);
				stream2.write(new String(descricaoTO.getProtocolo() + "  " + descricaoTO.getDataRecibo()).getBytes());
			} else {
				stream2.write(new String("Nota emitida em Contigencia").getBytes());
			}
			stream2.write(Constantes.LINHA);
			stream2.write(new String("-------------------------------------------------------").getBytes());
			stream2.write(Constantes.LINHA);

			try {
				stream2.write(new String(operador).getBytes());
			} catch (Exception e) {
				// TODO: handle exception
			}
			stream2.write(Constantes.LINHA);
			stream2.write(new String("-------------------------------------------------------").getBytes());
			stream2.write(Constantes.LINHA);
			stream2.write(Constantes.CORTAR_PAPEL);

			new CarregarImpressora().imprimir(stream2, impressora);
			ByteArrayOutputStream streamFor = new ByteArrayOutputStream();
			try {
				for (String pathVia : pathViaCartao) {
					for (int i = 1; i <= Integer.parseInt(vias); i++) {
						for (String t : ReadTxt.preencherViaCartao(pathVia)) {
							streamFor.write(new String(t).getBytes());
							streamFor.write(Constantes.LINHA);
						}
						streamFor.write(Constantes.CORTAR_PAPEL);
						new CarregarImpressora().imprimir(streamFor, impressora);
						streamFor = new ByteArrayOutputStream();
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
			}


		} catch (IOException e) {
			System.out.println("Erro ao imprimir!");
			e.printStackTrace();
		}

	}

	private String montarQRCode() throws IOException {
		String QRCode = lerXml.getQRCode();
		int length = QRCode.length() + 3; // string size + 3
		byte b = (byte) (length & 0xff); // parte 1
		byte c = (byte) ((length >> 8) & 0xff);

		String qrcode = new String(new byte[] { 27, 97, 49 }) + new String(new byte[] { 29, 79, 0, 0 })
				+ new String(new byte[] { 29, 40, 107, 4, 0, 49, 65, 50, 0 })
				+ new String(new byte[] { 29, 40, 107, 3, 0, 49, 67, 3 })
				+ new String(new byte[] { 29, 40, 107, 3, 0, 49, 69, 48 })
				+ new String(new byte[] { 29, 40, 107, b, c, 49, 80, 48 }) + QRCode
				+ new String(new byte[] { 29, 40, 107, 3, 0, 49, 81, 48 });

		return qrcode;
	}

	private byte[] espCaracter(int s) {
		return new byte[] { 27, 32, (byte) s };
	}

	private byte[] tmCaracter(int t) {
		// return new byte[] { 29, 33, (byte) t };
		return new byte[] { 27, 77, (byte) t };
	}

	/*
	 * static String IMPRESSORA = "ELGIN i9(USB)"; static String
	 * IMPRESSORA_EPSON = "EPSON TM-T20 Receipt";
	 * 
	 * public static void main(String[] args) { File fileXml = new File(new
	 * File("").getAbsolutePath() + "/imprimir"); try { fileXml.mkdir(); } catch
	 * (Exception e) { }
	 * 
	 * File enviar = new File(new File("").getAbsolutePath() + "/imprimir");
	 * File[] listfile = enviar.listFiles();
	 * 
	 * for (File file : listfile) { String xml = file.getAbsolutePath();
	 * LerXmlPaths paths; try { paths = new LerXmlPaths(xml); // new
	 * MontarImpressaoElgin(paths.getPathXmlVia(), IMPRESSORA, // new
	 * File("").getAbsolutePath() + "\\logomarca\\intersys.bmp",
	 * paths.getPath().getPathTxt(), // paths.getPath().getNumerovias(),
	 * paths.getOperador()).montarFolha(); new
	 * MontarImpressaoElgin(paths.getPathXmlVia(), IMPRESSORA_EPSON, new
	 * File("").getAbsolutePath() + "\\logomarca\\intersys.png",
	 * paths.getPath().getPathTxt(), paths.getPath().getNumerovias(),
	 * paths.getOperador()).montarFolha();
	 * 
	 * System.out.println(xml); } catch (ParserConfigurationException |
	 * SAXException | IOException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); }
	 * 
	 * } }
	 */

}
