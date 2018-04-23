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

import com.br.instersys.to.PathsVindosXmlTO;
import com.br.instersys.util.UtilTagXml;
import com.br.valber.Log;

public class LerXmlPaths {

	private Document doc;

	public LerXmlPaths(String path) throws ParserConfigurationException, SAXException, IOException {
		try {

			File xmlFile = new File(path);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			doc = (Document) dBuilder.parse(xmlFile);
		} catch (Exception e) {
			e.printStackTrace();
			new Log().montarFileErro(getClass(), "Erro", e.getMessage());
		}
	}

	public PathsVindosXmlTO getPath() {
		PathsVindosXmlTO to = new PathsVindosXmlTO();
		List<String> list = new ArrayList<>();

		NodeList noList = doc.getElementsByTagName(UtilTagXml.COMPROVANTES);
		for (int i = 0; i < noList.getLength(); i++) {
			Node noPai = noList.item(i);
			if (noPai.getNodeType() == Node.ELEMENT_NODE) {
				Element elementePai = (Element) noPai;
				NodeList listPai = elementePai.getChildNodes();
				for (int j = 0; j < listPai.getLength(); j++) {
					Node noFilho = listPai.item(j);
					if (noFilho.getNodeType() == Node.ELEMENT_NODE) {
						Element elementFilho = (Element) noFilho;
						switch (elementFilho.getTagName()) {

						case UtilTagXml.IMPRIMIR_VIAS:
							to.setNumerovias(elementFilho.getTextContent());
							break;

						case UtilTagXml.PATH_TXT:
							list.add(elementFilho.getTextContent());
							to.setPathTxt(list);
							break;
						default:
							break;
						}
					}
				}
			}

		}
		return to;
	}

	public String getPathXmlVia() {

		NodeList noList = doc.getElementsByTagName(UtilTagXml.PATH_XML_VINDO);
		for (int i = 0; i < noList.getLength(); i++) {
			Node noPai = noList.item(i);
			if (noPai.getNodeType() == Node.ELEMENT_NODE) {
				Element elementePai = (Element) noPai;
				return (elementePai.getTextContent());
			}
		}
		return null;
	}

	public String getOperador() {

		try {
			NodeList noList = doc.getElementsByTagName(UtilTagXml.OPERADOR);
			for (int i = 0; i < noList.getLength(); i++) {
				Node noPai = noList.item(i);
				if (noPai.getNodeType() == Node.ELEMENT_NODE) {
					Element elementePai = (Element) noPai;
					return (elementePai.getTextContent());
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	// public static void main(String[] args) {
	// String path = "C:\\Users\\valber siqueira\\Documents\\via do
	// pdv\\nfce_0.xml";
	// String path2 =
	// "D:\\workspace-eclipse\\IGestao_nfce\\imprimir\\nfce_3.xml";
	//
	// try {
	// LerXmlPaths paths = new LerXmlPaths(path2);
	//
	// System.out.println(paths.getPathXmlVia());
	// System.out.println("\n");
	//
	// System.out.println("\n");
	//
	// System.out.println(paths.getOperador());
	//
	// try {
	// for (String p : paths.getPath().getPathTxt()) {
	// System.out.println(p);
	// BufferedReader br = new BufferedReader(new FileReader(p));
	// List<String> list = new ArrayList<>();
	//
	// while (br.ready()) {
	// String linha = br.readLine();
	// linha.trim();
	// if (linha.substring(0, 3).equals("029")) {
	// list.add(linha.substring(linha.indexOf("=") + 3, (linha.length()) -
	// 1).trim());
	//
	// }
	// System.out.println(linha);
	// }
	//
	// br.close();
	// }
	// } catch (IOException ioe) {
	// ioe.printStackTrace();
	// }
	//
	// } catch (ParserConfigurationException | SAXException | IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }

}
