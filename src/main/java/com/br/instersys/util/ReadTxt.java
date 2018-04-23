package com.br.instersys.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadTxt {

	public static List<String> preencherViaCartao(String path) {
		List<String> list = new ArrayList<>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));

			while (br.ready()) {
				String linha = br.readLine();
				linha.trim();
				if (linha.substring(0, 3).equals("029")) {
					list.add(linha.substring(linha.indexOf("=") + 3, (linha.length()) - 1).trim());
				}
			}

			br.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return list;
	}
}
