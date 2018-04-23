package com.br.instersys.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class WritErro {

	public WritErro(String erro) {
		BufferedWriter write;
		try {
			write = new BufferedWriter(
					new FileWriter(new File(new File("").getAbsoluteFile() + "/imprimir/STATUS.txt")));
			write.write(erro);
			write.close();
			System.gc();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
