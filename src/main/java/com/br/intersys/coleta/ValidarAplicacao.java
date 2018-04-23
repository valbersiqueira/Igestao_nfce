package com.br.intersys.coleta;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ValidarAplicacao implements Runnable {

	public ValidarAplicacao() {
		new Thread(this).start();
	}

	@Override
	public void run() {
		validaraplicacao();

	}

	private void validaraplicacao() {
		while (true) {

			BufferedWriter write;
			try {
				write = new BufferedWriter(
						new FileWriter(new File(new File("").getAbsoluteFile() + "/IGESTAO_NFCE.atv")));
				write.close();
				System.gc();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}
