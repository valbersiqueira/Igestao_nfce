package com.br.instersys.util;

import java.awt.print.PrinterJob;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.JobName;
import javax.print.attribute.standard.OrientationRequested;

public class CarregarImpressora {

	private int valor = 0;
	public boolean isCarregar = true;
	private InputStream stream;
	private DocPrintJob docPrint;
	private PrintService printer;

	public CarregarImpressora() {
		new Thread() {
			@Override
			public void run() {
				try {
					sleep(4000);
					if (valor == 0) {
						printer = null;
						imprimir(null, null);
						return;

					} else {
						interrupt();
						return;
					}
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}

		}.start();
	}

	public int imprimir(ByteArrayOutputStream toPrint, String printerName) {

		PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
		aset.add(OrientationRequested.PORTRAIT);
		aset.add(new JobName("Impressão Etiqueta", null));

		// PROCURA A IMPRESSORA
		PrintService[] impressoras = PrinterJob.lookupPrintServices();
		for (int i = 0; i < impressoras.length; i++) {
			PrintService p = impressoras[i];
			if (p.getName().equalsIgnoreCase(printerName)) {
				printer = p;
				break;
			}
		}
		// ENVIA COMO ARRAY DE BYTES PARA IMPRESSORA DESEJADA
		try {
			docPrint = printer.createPrintJob();
		} catch (Exception e) {
			Map<String, String> dados;
			try {
				dados = Dados.getDados();
				String[] args2 = { dados.get("PATH"), dados.get("TIPO"), dados.get("IMPRIMIR"), "false" };
				Dados.SalvarDados(args2);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			new WritErro("IPRESSORA NÃO CONECTADA OU DESLIGADA");
			return 0;
			// JOptionPane.showMessageDialog(null,
			// "Impressora salva nas configurações não instalada ou desligada.\n
			// Verifique o cabo ou faça a instalação.");
		}
		try {
			try {
				stream = new ByteArrayInputStream(toPrint.toByteArray());
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
			DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
			SimpleDoc doc = new SimpleDoc(stream, flavor, null);

			docPrint.print(doc, aset);
			valor = 1;
		} catch (PrintException e) {

			// e.printStackTrace();
		}
		return valor;
	}

}
