package com.br.impressora;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import com.br.instersys.util.CarregarImpressora;
import com.br.instersys.util.Constantes;

import br.com.daruma.jna.DUAL;

public class OpenGaveta {

	public static void openElgin(String printerName) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			stream.write(Constantes.ABRIR_GAVETA);
			new CarregarImpressora().imprimir(stream, printerName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	static {
		try {
			System.load(new File("").getAbsolutePath() + "\\Darumaframework_DLL64\\DarumaFrameWork.dll");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void openDaruma() {
		int iRetorno = 0;
		iRetorno = DUAL.eBuscarPortaVelocidade();
		iRetorno = DUAL.rStatusImpressora();
		iRetorno = DUAL.iImprimirArquivo("<g></g>");
		
		switch (iRetorno) {
		case 0:
			System.out.println("0(zero) - Impressora Desligada!");
			break;
		case 1:
			System.out.println("1(um) - Impressora OK!");
			break;
		case -50:
			System.out.println("-50 - Impressora OFF-LINE!");
			break;
		case -51:
			System.out.println("-51 - Impressora Sem Papel!");
			break;
		case -27:
			System.out.println("-27 - Erro Generico!");
			break;
		case -52:
			System.out.println("-52 - Impressora inicializando!");
			break;

		default:
			System.out.println("Retorno não esperado!");
		}
	}
	
	public static void main(String[] args) {
//		OpenGaveta.openElgin("ELGIN i9(USB)");
		OpenGaveta.openDaruma();
	}
}
