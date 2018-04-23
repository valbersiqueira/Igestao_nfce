package com.br.intersys.coleta;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.br.impressora.OpenGaveta;
import com.br.instersys.util.CarregarImpressora;
import com.br.instersys.util.Constantes;
import com.br.instersys.util.Dados;

public class VerificadorPasta extends Thread {

	@Override
	public void run() {
		synchronized (this) {
			coletar();
		}

	}

	@SuppressWarnings("static-access")
	private void coletar() {
		while (true) {
			File imprimir = new File(new File("").getAbsolutePath() + "/");
			File[] listfile = imprimir.listFiles();
			for (File file : listfile) {
				try {

					Map<String, String> dados = Dados.getDados();

					try {

						if (file.getName().equalsIgnoreCase("ABRIRGAVETA.txt")) {
							try {
								file.delete();
								if (dados.get("TIPO").toUpperCase().contains("elgin")
										|| dados.get("TIPO").toUpperCase().contains("epson")) {
									OpenGaveta.openElgin(dados.get("TIPO"));
								} else if (dados.get("TIPO").toUpperCase().contains("daruma")) {
									OpenGaveta.openDaruma();
								}
							} catch (Exception e) {
								// TODO: handle exception
							}
						}
					} catch (Exception e) {
						// TODO: handle exception
					}

					String pom = "";
					String extensao = file.getName();
					try {
						pom = extensao.substring(extensao.length() - 7, 7);
					} catch (Exception e) {
					}
					extensao = extensao.substring(extensao.length() - 3);
					String tipo = dados.get("TIPO");
					if (extensao.equalsIgnoreCase("xml") && dados.get("TIPO") != null
							&& !pom.equalsIgnoreCase("pom.xml")) {
						// String xml = file.getAbsolutePath();
						// LerXmlPaths paths = new LerXmlPaths(xml);
						ByteArrayOutputStream stream = new ByteArrayOutputStream();
						stream.write(Constantes.STATUS);
						if (tipo.toUpperCase().contains("ELGIN") || tipo.toUpperCase().contains("EPSON")
								&& new CarregarImpressora().imprimir(stream, tipo) == 1) {
							ColetaPasta.getInstancia(file).getXmlVarrer();
						} else if (tipo.toUpperCase().contains("DARUMA")) {
							ColetaPasta.getInstancia(file).getXmlVarrer();
						}

					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
