package com.br.intersys.coleta;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.br.impressora.MontarImpressaoDaruma;
import com.br.impressora.MontarImpressaoElgin;
import com.br.instersys.util.Dados;
import com.br.instersys.xml.LerXmlPaths;

public class ColetaPasta {

	private static Map<String, String> dados;

	private static File file;

	private static ColetaPasta instancia;

	public static synchronized ColetaPasta getInstancia(File fileInput) {
		if (instancia == null) {
			instancia = new ColetaPasta();
		}
		file = fileInput;
		return instancia;
	}

	public static void getXmlVarrer() {

		try {
			dados = Dados.getDados();

			String extensao = file.getName();
			extensao = extensao.substring(extensao.length() - 3);

			String tipo = dados.get("TIPO");

			String xml = file.getAbsolutePath();
			LerXmlPaths paths;
			try {
				paths = new LerXmlPaths(xml);
				String pathLogomarca = "";

				if (dados.get("PATH") != null && dados.get("IMPRIMIR") != null
						&& dados.get("IMPRIMIR").equalsIgnoreCase("true")) {
					pathLogomarca = dados.get("PATH");
				}

				if (tipo != null) {
					String operador = "";
					try {
						operador = paths.getOperador();
					} catch (Exception e) {
					}
					if (tipo.toUpperCase().contains("ELG") || tipo.toUpperCase().contains("EPSON")) {
						new MontarImpressaoElgin(paths.getPathXmlVia(), tipo, pathLogomarca,
								paths.getPath().getPathTxt(), paths.getPath().getNumerovias(), operador).montarFolha();
					} else if (tipo.toUpperCase().contains("DARU")) {
						MontarImpressaoDaruma d = new MontarImpressaoDaruma(paths.getPathXmlVia(), pathLogomarca,
								paths.getPath().getPathTxt(), paths.getPath().getNumerovias(), operador);
						d.montarimpressaoDaruma();
					}

				}
				System.gc();
				removerArquivos(new File(new File("").getAbsolutePath() + "/ENVIAR/"));
				file.delete();
			} catch (ParserConfigurationException | SAXException | IOException e) {
				e.printStackTrace();
			}
		} catch (IOException e1) {
		}
	}

	public static void removerArquivos(File f) {
		if (f.isDirectory()) {
			File[] files = f.listFiles();
			for (File file : files) {
				removerArquivos(file);
			}
		}
		f.delete();
	}

}
