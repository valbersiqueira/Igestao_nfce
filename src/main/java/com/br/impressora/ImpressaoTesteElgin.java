package com.br.impressora;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import javax.swing.JOptionPane;

import com.br.instersys.util.CarregarImpressora;
import com.br.instersys.util.Constantes;
import com.br.instersys.util.Dados;

public class ImpressaoTesteElgin {

	public static void impressao() {
		try {
			Map<String, String> dados = Dados.getDados();

			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			LocalDateTime data = LocalDateTime.now();
			DateTimeFormatter pattener = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

			stream.write(Constantes.CENTRALIZAR);
			stream.write(new String("INTERSYS SISTEMAS E PERIFERICOS").getBytes());
			stream.write(Constantes.LINHA);
			stream.write(new String("Av C 8, 477 - QUADRA84 LOTE 21").getBytes());
			stream.write(Constantes.LINHA);
			stream.write(new String("Setor Sudoeste - Goiania - GO").getBytes());
			stream.write(Constantes.LINHA);
			stream.write(new String("Fone: (62) 3247-1560").getBytes());
			stream.write(Constantes.LINHA);
			stream.write(new String(data.format(pattener)).getBytes());
			stream.write(Constantes.LINHA);
			stream.write(Constantes.LINHA);
			stream.write(Constantes.LINHA);
			stream.write(Constantes.CORTAR_PAPEL);

			if (dados.get("TIPO") != null) {
				new CarregarImpressora().imprimir(stream, dados.get("TIPO"));
			} else {
				JOptionPane.showMessageDialog(null, "Impressora não encontrada.");
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	public static void main(String[] args) {
		ImpressaoTesteElgin.impressao();
	}

}
