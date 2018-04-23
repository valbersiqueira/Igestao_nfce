package com.br.instersys.util;

import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class UtilMessage {

	Map<Integer, String> msg;

	public UtilMessage() {
		this.msg = new HashMap<Integer, String>();
	}

	public int getMessage(int idMsg) {
		this.msg.put(0, "Selecione o tipo de Impressora.");
		this.msg.put(1, "Deseja Fechar a tela de configuração.");
		this.msg.put(2,
				"Impressora Desligada ou Cabo não conetado. \n"
						+ "Ligue a Impressora ou conect o cabo para prosseguir. \n "
						+ "Depois que fizer as configurações click(OK)");

		return JOptionPane.showConfirmDialog(null, this.msg.get(idMsg), "Erro ao salvar.", JOptionPane.YES_OPTION, 0,
				new ImageIcon(UtilMessage.class.getClassLoader().getResource("img/warning.png")));
	}

}
