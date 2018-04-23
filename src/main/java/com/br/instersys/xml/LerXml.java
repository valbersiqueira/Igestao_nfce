package com.br.instersys.xml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.br.instersys.to.DescricaoTO;
import com.br.instersys.to.DestinoTO;
import com.br.instersys.to.EmpresaTO;
import com.br.instersys.to.FormaPagamentoTO;
import com.br.instersys.to.HeaderTO;
import com.br.instersys.to.ProdutoTO;
import com.br.instersys.util.UtilTagXml;

public class LerXml {
	private Document doc;

	public LerXml(String path) throws ParserConfigurationException, SAXException, IOException {
		File xmlFile = new File(path);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		doc = (Document) dBuilder.parse(xmlFile);

	}

	public EmpresaTO toEmpresa() {
		EmpresaTO emp = new EmpresaTO();
		NodeList nodeList = doc.getElementsByTagName(UtilTagXml.EMIT);

		for (int i = 0; i < nodeList.getLength(); i++) {
			Node NoPai = nodeList.item(i);
			if (NoPai.getNodeType() == Node.ELEMENT_NODE) {
				Element elementePai = (Element) NoPai;

				NodeList listPai = elementePai.getChildNodes();

				for (int j = 0; j < listPai.getLength(); j++) {
					Node noFilho = listPai.item(j);
					if (noFilho.getNodeType() == Node.ELEMENT_NODE) {

						Element elementeChild = (Element) noFilho;
						switch (elementeChild.getTagName()) {
						case UtilTagXml.CNPJ:
							emp.setCnpj(elementeChild.getTextContent());
							break;

						case UtilTagXml.NOME:

							emp.setNome(elementeChild.getTextContent().toUpperCase());
							break;

						case UtilTagXml.IE:
							emp.setIe(elementeChild.getTextContent().toUpperCase());
							break;

						case UtilTagXml.ENDERECO:
							NodeList listFilhos = noFilho.getChildNodes();
							for (int k = 0; k < listFilhos.getLength(); k++) {

								Node noFilhos = listFilhos.item(k);
								if (noFilhos.getNodeType() == Node.ELEMENT_NODE) {
									Element elementeChilds = (Element) noFilhos;
									switch (elementeChilds.getTagName()) {
									case UtilTagXml.LOGRADOUTO:
										emp.setLogradouro(elementeChilds.getTextContent().toUpperCase());
										break;

									case UtilTagXml.BAIRRO:
										emp.setBairro(elementeChilds.getTextContent().toUpperCase());
										break;

									case UtilTagXml.NUMERO:
										emp.setNumero(elementeChilds.getTextContent().toUpperCase());
										break;

									case UtilTagXml.MUNICIPIO:
										emp.setMunicipio(elementeChilds.getTextContent().toUpperCase());
										break;

									case UtilTagXml.UF:
										emp.setUf(elementeChilds.getTextContent().toUpperCase());
										break;

									case UtilTagXml.CEP:
										emp.setCep(elementeChilds.getTextContent().toUpperCase());
										break;

									default:
										break;
									}
								}
							}
							break;
						default:
							break;
						}
					}
				}
			}
		}
		// System.out.println(emp);
		return emp;
	}

	public List<ProdutoTO> toProduto() {
		List<ProdutoTO> listPro = new ArrayList<>();

		NodeList noList = doc.getElementsByTagName(UtilTagXml.DET);

		for (int i = 0; i < noList.getLength(); i++) {
			Node noPai = noList.item(i);
			if (noPai.getNodeType() == Node.ELEMENT_NODE) {
				ProdutoTO pro = new ProdutoTO();
				Element element = (Element) noPai;
				pro.setItem(element.getAttribute(UtilTagXml.ITEM));
				NodeList listFilho = noPai.getChildNodes();
				for (int j = 0; j < listFilho.getLength(); j++) {
					Node noFilho = listFilho.item(j);
					NodeList listNetos = noFilho.getChildNodes();
					for (int k = 0; k < listNetos.getLength(); k++) {
						Node noNeto = listNetos.item(k);
						if (noNeto.getNodeType() == Node.ELEMENT_NODE) {
							Element elementNeto = (Element) noNeto;

							switch (elementNeto.getTagName()) {
							case UtilTagXml.COD_PRODUTO:
								pro.setCodigo(elementNeto.getTextContent());
								break;

							case UtilTagXml.PRODUTO_NOME:
								pro.setDescricao(elementNeto.getTextContent());
								break;

							case UtilTagXml.UNIDADE:
								pro.setUnidade(elementNeto.getTextContent());
								break;

							case UtilTagXml.QTD:
								pro.setQuantidade(elementNeto.getTextContent());
								break;

							case UtilTagXml.VALOR_UN:
								pro.setValorUni(elementNeto.getTextContent());
								break;

							case UtilTagXml.VALOR_TOT:
								pro.setValorTot(elementNeto.getTextContent());
								break;

							case UtilTagXml.DESCONTO:
								pro.setDesconto(elementNeto.getTextContent());
								break;
							default:
								break;
							}
						}
					}
				}
				listPro.add(pro);
			}
		}
		return listPro;
	}

	@SuppressWarnings("unused")
	public String getQRCode() {
		NodeList noList = doc.getElementsByTagName(UtilTagXml.TAG_QRCODE);

		for (int i = 0; i < noList.getLength(); i++) {
			Node noPai = noList.item(i);
			NodeList listPai = noPai.getChildNodes();
			for (int j = 0; j < listPai.getLength(); j++) {
				Node noFilho = listPai.item(j);
				NodeList listFilho = noFilho.getChildNodes();
				for (int k = 0; k < listFilho.getLength(); k++) {
					Node noNeto = listFilho.item(k);
					return noNeto.getNodeValue();
				}
			}

		}

		return null;
	}

	public FormaPagamentoTO getFormaPagamento() {
		FormaPagamentoTO pay = new FormaPagamentoTO();
		NodeList noList = doc.getElementsByTagName(UtilTagXml.TAG_PAG);
		for (int i = 0; i < noList.getLength(); i++) {
			Node noPai = noList.item(i);
			NodeList listPai = noPai.getChildNodes();
			for (int j = 0; j < listPai.getLength(); j++) {
				Node noFilho = listPai.item(j);
				if (noFilho.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) noFilho;

					switch (element.getTagName()) {

					case UtilTagXml.FORMA_PAGAMENTO:
						pay.setCodigo(element.getTextContent());
						break;

					default:
						break;
					}
				}
			}
		}

		return pay;
	}

	public String valorTotal() {
		NodeList noList = doc.getElementsByTagName(UtilTagXml.TOTAL);
		for (int i = 0; i < noList.getLength(); i++) {
			Node noPai = noList.item(i);
			NodeList listPai = noPai.getChildNodes();
			for (int j = 0; j < listPai.getLength(); j++) {
				Node noFilho = listPai.item(j);
				NodeList listFilho = noFilho.getChildNodes();
				for (int k = 0; k < listFilho.getLength(); k++) {
					Node noNeto = listFilho.item(k);
					if (noNeto.getNodeType() == Node.ELEMENT_NODE) {
						Element element = (Element) noNeto;

						switch (element.getTagName()) {

						case UtilTagXml.VALOR_TOTAL_VENDA:
							return (element.getTextContent().replace(".", ","));

						default:
							break;
						}
					}
				}

			}
		}
		return null;
	}

	public HeaderTO header() {
		HeaderTO header = new HeaderTO();
		NodeList noList = doc.getElementsByTagName(UtilTagXml.IDE);
		for (int i = 0; i < noList.getLength(); i++) {
			Node noPai = noList.item(i);
			NodeList listPai = noPai.getChildNodes();
			for (int j = 0; j < listPai.getLength(); j++) {
				Node noFilho = listPai.item(j);
				if (noFilho.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) noFilho;
					switch (element.getTagName()) {

					case UtilTagXml.SERIE:
						header.setSerie(element.getTextContent());
						break;

					case UtilTagXml.NUMERO_NF:
						header.setNumero(element.getTextContent());
						break;

					case UtilTagXml.DATA_EMISSAO:
						String data = element.getTextContent();
						String y = data.substring(0, 4);
						String m = data.substring(5, 7);
						String d = data.substring(8, 10);
						String h = data.substring(11, 19);
						header.setDataEmissao(d + "/" + m + "/" + y + " - " + h);
						break;

					default:
						break;
					}
				}
			}
		}

		return header;
	}

	public DescricaoTO descricao() {
		DescricaoTO des = new DescricaoTO();
		try {
			
		
		NodeList noList = doc.getElementsByTagName(UtilTagXml.TAG_PROTNFE);
		for (int i = 0; i < noList.getLength(); i++) {
			Node noPai = noList.item(i);
			NodeList listPai = noPai.getChildNodes();
			for (int j = 0; j < listPai.getLength(); j++) {
				Node noFilho = listPai.item(j);
				NodeList listFilho = noFilho.getChildNodes();
				for (int k = 0; k < listFilho.getLength(); k++) {
					Node noNeto = listFilho.item(k);
					if (noNeto.getNodeType() == Node.ELEMENT_NODE) {
						Element element = (Element) noNeto;
						switch (element.getTagName()) {

						case UtilTagXml.CHAVE:
							des.setChave(element.getTextContent());
							break;

						case UtilTagXml.DATA_RECIBO:
							String data = element.getTextContent();
							String y = data.substring(0, 4);
							String m = data.substring(5, 7);
							String d = data.substring(8, 10);
							String h = data.substring(11, 19);
							des.setDataRecibo(d + "/" + m + "/" + y + " - " + h);
							break;

						case UtilTagXml.PROTOCOLO:
							des.setProtocolo(element.getTextContent());
							break;

						default:
							break;
						}
					}

				}
			}
		}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return des;
	}

	public DestinoTO destino() {
		DestinoTO des = new DestinoTO();
		NodeList noList = doc.getElementsByTagName(UtilTagXml.DEST);
		for (int i = 0; i < noList.getLength(); i++) {
			Node noPai = noList.item(i);
			NodeList listPai = noPai.getChildNodes();
			for (int j = 0; j < listPai.getLength(); j++) {
				Node noFilho = listPai.item(j);
				if (noFilho.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) noFilho;
					switch (element.getTagName()) {

					case UtilTagXml.CPF:
						des.setCpf(element.getTextContent());
						break;

					case UtilTagXml.NOME_DEST:
						des.setNome(element.getTextContent());
						break;

					default:
						break;
					}
				}
			}
		}

		return des;
	}

//	public static void main(String[] args) {
//		try {
//
//			String xml1 = "D:\\nfce.xml";
//			String xml2 = "F:\\workspace-eclipse\\IGestao_nfce\\ENVIAR\\nfce_0_xml.xml";
//			LerXml xml = new LerXml(xml2);
//
//			xml.destino();
//			System.out.println(xml.valorTotal());
//			xml.descricao();
//
//			xml.getFormaPagamento();
//
//			xml.toEmpresa();
//			xml.toProduto().forEach(p -> {
//				System.out.println(p);
//			});
//			String teste = xml.getQRCode();
//			xml.getFormaPagamento();
//			// System.out.println(xml.dataEmissao());
//		} catch (ParserConfigurationException | SAXException | IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

}
