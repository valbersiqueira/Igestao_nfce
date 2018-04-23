package com.br.impressora;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import com.br.instersys.util.Dados;
import com.br.instersys.util.WritErro;
import br.com.daruma.jna.DUAL;

public class ImpressaoTesteDaruma {

	static {
		System.load(new File("").getAbsolutePath() + "\\Darumaframework_DLL64\\DarumaFrameWork.dll");
	}

	public ImpressaoTesteDaruma() {
		/*try {
			Map<String, String> dados = Dados.getDados();
			String[] args = { dados.get("PATH"), dados.get("TIPO"), dados.get("IMPRIMIR"), "true" };

			Dados.SalvarDados(args);

			System.load(new File("").getAbsolutePath() + "\\Darumaframework_DLL64\\DarumaFrameWork.dll");

			String[] args2 = { dados.get("PATH"), dados.get("TIPO"), dados.get("IMPRIMIR"), "false" };
			Dados.SalvarDados(args2);
		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}

	public int impressao() {
		System.out.println("execute");
		DUAL.regPortaComunicacao("COM9");//cabo usb: USB COM4 -115200 //cabo serial: lpt1 -9600
		DUAL.regVelocidade("115200");

		int iRetorno = 0;

		iRetorno = DUAL.eBuscarPortaVelocidade();
		iRetorno = DUAL.rStatusImpressora();

		LocalDateTime data = LocalDateTime.now();
		DateTimeFormatter pattener = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

		iRetorno = DUAL.iImprimirTexto("<ce>INTERSYS SISTEMAS E PERIFERICOS</ce>", 0);
		iRetorno = DUAL.iImprimirTexto("<ce>Av C 8, 477 - QUADRA84 LOTE 21</ce>", 0);
		iRetorno = DUAL.iImprimirTexto("<ce>Setor Sudoeste - Goiania - GO</ce>", 0);
		iRetorno = DUAL.iImprimirTexto("<ce>Fone: (62) 3247-1560</ce>", 0);
		iRetorno = DUAL.iImprimirTexto("<l></l>", 0);
		iRetorno = DUAL.iImprimirTexto("<ce>" + data.format(pattener) + "</ce>", 0);
		iRetorno = DUAL.iImprimirTexto("<l></l>", 0);
		iRetorno = DUAL.iImprimirTexto("<gui></gui>", 0);

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
	
	public static void main(String[] args) {
		System.out.println("exute");
		ImpressaoTesteDaruma t = new ImpressaoTesteDaruma();
		t.impressao();
	}

}
