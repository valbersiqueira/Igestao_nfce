package com.br.instersys.util;

import java.io.File;

public class CreateDirs {

	public CreateDirs() {

		File pathDados = new File(new File("").getAbsolutePath() + "/dados");
		File pathLogo = new File(new File("").getAbsolutePath() + "/logomarca");
		File pathLogs = new File(new File("").getAbsolutePath() + "/logs");
//		File pathimprimir = new File(new File("").getAbsolutePath() + "/imprimir");
		try {
			pathDados.mkdir();
		} catch (Exception e) {
		}
		try {
			pathLogo.mkdir();
		} catch (Exception e) {
		}
		try {
			pathLogs.mkdir();
		} catch (Exception e) {
		}
//		try {
//			pathimprimir.mkdir();
//		} catch (Exception e) {
//		}
	}

}
