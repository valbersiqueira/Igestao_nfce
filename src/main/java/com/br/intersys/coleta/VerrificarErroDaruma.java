package com.br.intersys.coleta;

import java.io.File;
import java.io.IOException;
import java.util.Map;


import com.br.instersys.util.Dados;

public class VerrificarErroDaruma extends Thread {
	@Override
	public void run() {
		synchronized (this) {
			verificar();
		}
	}

	private void verificar() {
		try {
			sleep(8000);

			try {
				Map<String, String> dados = Dados.getDados();
				if (dados.get("OPEN") != null && dados.get("OPEN").equals("true")) {
					System.out.println("passou");
					String[] args = { dados.get("PATH"), dados.get("TIPO"), dados.get("IMPRIMIR"), "false" };
					Dados.SalvarDados(args);
					Runtime.getRuntime()
							.exec("cmd.exe /c start " + new File("").getAbsolutePath() + "\\BACKUP_FILE.exe");

					Runtime.getRuntime().exit(0);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			// Runtime.getRuntime().exec("start " + new
			// File("").getAbsolutePath() + "/IGESTAO_NFCE.exe");
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
