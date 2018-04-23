package com.br.instersys.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Dados {

	private static Properties getProperties() throws IOException {
		Properties properties = new Properties();
		try {
			FileInputStream stream = new FileInputStream(new File("").getAbsolutePath() + "/dados/config.properties");
			properties.load(stream);
		} catch (Exception e) {
		}
		return properties;
	}

	public static void SalvarDados(String... args) {
		try {

			BufferedWriter write = new BufferedWriter(
					new FileWriter(new File("").getAbsolutePath() + "/dados/config.properties"));

			try {
				write.write("PATH = " + args[0]);
			} catch (Exception e) {
			}
			try {
				write.newLine();
				write.write("TIPO = " + args[1]);
			} catch (Exception e) {
			}

			try {
				write.newLine();
				write.write("IMPRIMIR = " + args[2]);
			} catch (Exception e) {
			}

			try {
				write.newLine();
				write.write("OPEN = " + args[3]);
			} catch (Exception e) {
			}

			write.flush();
			write.close();
		} catch (IOException e) {
		}

	}

	public static Map<String, String> getDados() throws IOException {
		Map<String, String> dados = new HashMap<String, String>();
		Properties propt = getProperties();

		try {
			dados.put("PATH", propt.getProperty("PATH"));
		} catch (Exception e) {
		}
		try {
			dados.put("TIPO", propt.getProperty("TIPO"));
		} catch (Exception e) {
		}
		try {
			dados.put("IMPRIMIR", propt.getProperty("IMPRIMIR"));
		} catch (Exception e) {
		}
		try {
			dados.put("OPEN", propt.getProperty("OPEN"));
		} catch (Exception e) {
		}
		return dados;
	}

}
