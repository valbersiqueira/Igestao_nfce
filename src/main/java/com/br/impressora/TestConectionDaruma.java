package com.br.impressora;

import java.io.File;
import java.util.Map;

import com.br.instersys.util.Dados;
import com.br.instersys.util.WritErro;
import com.br.intersys.coleta.VerrificarErroDaruma;

import br.com.daruma.jna.DUAL;

public class TestConectionDaruma {
	static {

	}

	public TestConectionDaruma() {

		try {
			Map<String, String> dados = Dados.getDados();
			String[] args = { dados.get("PATH"), dados.get("TIPO"), dados.get("IMPRIMIR"), "true" };
			Dados.SalvarDados(args);
			new VerrificarErroDaruma().start();

			System.load(new File("").getAbsolutePath() + "\\Darumaframework_DLL64\\DarumaFrameWork.dll");

			String[] args2 = { dados.get("PATH"), dados.get("TIPO"), dados.get("IMPRIMIR"), "false" };
			Dados.SalvarDados(args2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int testar() {
		int iRetorno = 0;

		iRetorno = DUAL.eBuscarPortaVelocidade();
		iRetorno = DUAL.rStatusImpressora();
		iRetorno = DUAL.iImprimirTexto("<l></l>", 0);
		switch (iRetorno) {
		case 0:
			new WritErro("Impressora Desligada!");
			break;
		case 1:
			break;
		case -50:
			new WritErro("Impressora OFF-LINE!");
			break;
		case -51:
			new WritErro("Impressora Sem Papel!");
			break;
		case -27:
			new WritErro("Erro Generico!");
			break;
		case -52:
			break;

		default:
			new WritErro("Retorno não esperado!");
		}
		return iRetorno;
	}

	// public static void main(String[] args) throws IOException {
	// System.out.println(new File("").getAbsolutePath() + "/BACKUP_FILE.exe");
	// Runtime.getRuntime().exec("cmd.exe /c start " + new
	// File("").getAbsolutePath() + "\\ BACKUP_FILE.exe");
	// System.out.println("excut");
	//
	// }
}
