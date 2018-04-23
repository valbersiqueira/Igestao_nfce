package com.br.intersys.view;

import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.br.impressora.ImpressaoTesteDaruma;
import com.br.impressora.ImpressaoTesteElgin;
import com.br.instersys.util.CreateDirs;
import com.br.instersys.util.Dados;
import com.br.instersys.util.UtilMessage;
import com.br.intersys.coleta.ValidarAplicacao;
import com.br.intersys.coleta.VerificadorPasta;
import com.br.intersys.tray.TrayInit;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.imageio.ImageIO;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.Toolkit;
import javax.swing.JCheckBox;

public class ConfiguracaoImpressoraView extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;

	private File imgChoose;
	private JFileChooser choose;

	private JButton btnSalvar, btnSelecionarLogomarca, btnSair;
	private JLabel imagem_lbl;
	private JComboBox<String> comboBox;

	private JCheckBox logoCheck;

	private String tipo;
	private JLabel lblNomeDaImpressora;
	private JLabel lblLogomarca;

	private String path;
	private JButton teste_btn;
	private Map<String, String> dados;
	private PrintService[] services;
	private List<String> nomesImpressoras = new ArrayList<>();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new CreateDirs();

					ConfiguracaoImpressoraView frame = new ConfiguracaoImpressoraView();
					frame.setLocationRelativeTo(null);
					frame.setVisible(false);

					new TrayInit(frame).inicializarTray();

					new ValidarAplicacao();
					//ew ColetaPasta();
					new VerificadorPasta().start();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ConfiguracaoImpressoraView() {
		setIconImage(
				Toolkit.getDefaultToolkit().getImage(ConfiguracaoImpressoraView.class.getResource("/img/printer.png")));

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				int result = JOptionPane.showConfirmDialog(null, "Fechar Tela de configuração?", "Confirmação",
						JOptionPane.YES_OPTION, 0,
						new ImageIcon(UtilMessage.class.getClassLoader().getResource("img/info.png")));

				if (result == JOptionPane.YES_OPTION) {
					setVisible(false);
				}
			}
		});

		setTitle("CONFIGURAÇÃO DE IMPRESSORA");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 491, 388);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBounds(0, 0, 485, 358);
		contentPane.add(panel);

		lblNomeDaImpressora = new JLabel("Impressora:");
		lblNomeDaImpressora.setFont(new Font("SansSerif", Font.PLAIN, 12));

		comboBox = new JComboBox<String>();
		comboBox.setMaximumRowCount(50);

		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tipo = (String) comboBox.getSelectedItem();

			}
		});

		comboBox.setBackground(Color.WHITE);
		comboBox.setFont(new Font("SansSerif", Font.PLAIN, 12));

		// comboBox.setModel(
		// new DefaultComboBoxModel<String>(new String[] { }));

		services = PrintServiceLookup.lookupPrintServices(null, null);

		nomesImpressoras.add("Selecione");
		for (PrintService ps : services) {
			nomesImpressoras.add(ps.getName());
		}
		for (String str : nomesImpressoras) {
			comboBox.addItem(str);
		}
		

		btnSelecionarLogomarca = new JButton("Selecionar Logomarca");
		btnSelecionarLogomarca.setBackground(Color.white);
		btnSelecionarLogomarca.setIcon(
				new ImageIcon(ConfiguracaoImpressoraView.class.getClassLoader().getResource("img/picture.png")));

		btnSelecionarLogomarca.setFont(new Font("SansSerif", Font.PLAIN, 12));
		btnSelecionarLogomarca.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					processarImg();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		lblLogomarca = new JLabel("Logomarca");
		lblLogomarca.setFont(new Font("SansSerif", Font.PLAIN, 12));

		imagem_lbl = new JLabel("");
		imagem_lbl.setVerticalAlignment(SwingConstants.BOTTOM);

		btnSair = new JButton("Sair");
		btnSair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (new UtilMessage().getMessage(1) == JOptionPane.YES_OPTION) {
					setVisible(false);
				}

			}
		});
		btnSair.setBackground(Color.white);
		btnSair.setIcon(new ImageIcon(ConfiguracaoImpressoraView.class.getClassLoader().getResource("img/cancel.png")));
		btnSair.setFont(new Font("SansSerif", Font.PLAIN, 12));

		btnSalvar = new JButton("Salvar");

		btnSalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean isValid = true;
				if (tipo == null || tipo.equalsIgnoreCase("selecione")) {
					if (new UtilMessage().getMessage(0) == JOptionPane.OK_OPTION) {
						comboBox.setFocusable(true);
					}
					isValid = false;
				}

				if (imgChoose != null) {
					try {
						BufferedImage imagem1 = null;
						imagem1 = ImageIO.read(imgChoose);
						ImageIO.write(imagem1, "jpg",
								new File(new File(new File("").getAbsolutePath()) + "\\logomarca\\logomarca.png"));
					} catch (IOException e1) {
						new UtilMessage().getMessage(4);
					}
				}
				if (isValid) {
					path = new File("").getAbsolutePath().replace("\\", "/");
					path += "/logomarca/logomarca.png";
					String[] args = { path, tipo, String.valueOf(logoCheck.isSelected()) };

					Dados.SalvarDados(args);
					if (JOptionPane.showConfirmDialog(null, "Deseja Fechar a tela de configuração.", "Erro ao salvar.",
							JOptionPane.YES_OPTION, 0, new ImageIcon(UtilMessage.class.getClassLoader()
									.getResource("img/info.png"))) == JOptionPane.YES_OPTION) {
						setVisible(false);
					}

				}

			}
		});

		btnSalvar.setBackground(Color.WHITE);
		btnSalvar.setIcon(new ImageIcon(ConfiguracaoImpressoraView.class.getClassLoader().getResource("img/save.png")));
		btnSalvar.setFont(new Font("SansSerif", Font.PLAIN, 12));

		teste_btn = new JButton("Testar impressora");

		teste_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (tipo != null) {
					if (tipo.toUpperCase().contains("ELGIN") || tipo.toUpperCase().contains("EPSON")) {
						ImpressaoTesteElgin.impressao();
					} else if (tipo.toUpperCase().contains("DARUMA")) {
						new ImpressaoTesteDaruma().impressao();
					} else {
						JOptionPane.showMessageDialog(null, "Selecione a Impressora Instalada.");
					}
				} else {
					JOptionPane.showMessageDialog(null, "Selecione a Impressora Instalada.");
				}
			}
		});

		teste_btn.setBackground(Color.WHITE);
		teste_btn.setIcon(new ImageIcon(ConfiguracaoImpressoraView.class.getClassLoader().getResource("img/test.png")));
		teste_btn.setFont(new Font("SansSerif", Font.PLAIN, 12));

		logoCheck = new JCheckBox("Imprimir logo marca");
		logoCheck.setBackground(Color.WHITE);

		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel
						.createSequentialGroup().addContainerGap().addGroup(gl_panel.createParallelGroup(
								Alignment.LEADING)
								.addComponent(imagem_lbl, GroupLayout.PREFERRED_SIZE, 398,
										GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_panel.createParallelGroup(Alignment.LEADING, false)
										.addGroup(gl_panel.createSequentialGroup()
												.addComponent(teste_btn, GroupLayout.DEFAULT_SIZE,
														GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addGap(18).addComponent(lblNomeDaImpressora)
												.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(comboBox,
														GroupLayout.PREFERRED_SIZE, 149, GroupLayout.PREFERRED_SIZE))
										.addGroup(gl_panel.createSequentialGroup().addGroup(gl_panel
												.createParallelGroup(Alignment.LEADING)
												.addComponent(btnSelecionarLogomarca, GroupLayout.PREFERRED_SIZE, 218,
														GroupLayout.PREFERRED_SIZE)
												.addComponent(lblLogomarca))
												.addPreferredGap(ComponentPlacement.RELATED)
												.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
														.addGroup(gl_panel.createSequentialGroup()
																.addComponent(btnSalvar)
																.addPreferredGap(ComponentPlacement.UNRELATED)
																.addComponent(btnSair, GroupLayout.PREFERRED_SIZE, 104,
																		GroupLayout.PREFERRED_SIZE))
														.addComponent(logoCheck, GroupLayout.PREFERRED_SIZE, 133,
																GroupLayout.PREFERRED_SIZE)))))
						.addContainerGap(42, Short.MAX_VALUE)));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup().addGap(25)
						.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE).addComponent(teste_btn)
								.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(lblNomeDaImpressora))
						.addGap(25)
						.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE).addComponent(btnSelecionarLogomarca)
								.addComponent(btnSalvar).addComponent(btnSair))
						.addGap(24)
						.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE).addComponent(lblLogomarca)
								.addComponent(logoCheck))
						.addGap(18)
						.addComponent(imagem_lbl, GroupLayout.PREFERRED_SIZE, 144, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(48, Short.MAX_VALUE)));
		panel.setLayout(gl_panel);

		preencherDadosSalvos();
	}

	private void processarImg() throws IOException {
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Imagens", "jpg", "png");
		choose = new JFileChooser();

		choose.setFileSelectionMode(JFileChooser.FILES_ONLY);
		choose.setAcceptAllFileFilterUsed(false);
		choose.addChoosableFileFilter(filter);

		choose.showOpenDialog(null);
		imgChoose = choose.getSelectedFile();

		try {
			ImageIcon icon = new ImageIcon(imgChoose.getAbsolutePath());
			icon.setImage(icon.getImage().getScaledInstance(400, 125, 100));
			imagem_lbl.setIcon(icon);
			lblLogomarca.setVisible(true);
			logoCheck.setVisible(true);
		} catch (Exception e) {

		}
	}

	private void preencherDadosSalvos() {
		try {
			dados = Dados.getDados();
			try {
				tipo = dados.get("TIPO");
				if(tipo != null){
					comboBox.setSelectedItem(dados.get("TIPO"));					
				}
			} catch (Exception e) {
				lblLogomarca.setVisible(false);
				logoCheck.setVisible(false);
			}

			try {
				ImageIcon icon = new ImageIcon(dados.get("PATH"));
				icon.setImage(icon.getImage().getScaledInstance(400, 125, 100));
				imagem_lbl.setIcon(icon);
				lblLogomarca.setVisible(true);
				logoCheck.setVisible(true);
			} catch (Exception e) {
				lblLogomarca.setVisible(false);
				logoCheck.setVisible(false);
			}

			try {
				if (dados.get("IMPRIMIR").equals("true")) {
					logoCheck.setSelected(true);
				} else {
					logoCheck.setSelected(false);
				}
			} catch (Exception e) {
			}

			try {
				if (dados.get("OPEN").equals("true")) {
					if (new UtilMessage().getMessage(2) == JOptionPane.YES_OPTION) {
						String[] args = { dados.get("PATH"), dados.get("TIPO"), dados.get("IMPRIMIR") };
						Dados.SalvarDados(args);
					}
				}
			} catch (Exception e) {
			}

		} catch (IOException e) {
			lblLogomarca.setVisible(false);
			logoCheck.setVisible(false);
		}
	}

}
