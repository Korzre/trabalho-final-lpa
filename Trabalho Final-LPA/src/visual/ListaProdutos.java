package visual;

import javax.swing.*;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@SuppressWarnings("serial")
public class ListaProdutos extends JFrame {
	private String[][] lista;
	private float valorFinal_= 0.0f;
	private int produtos;
	private int colunas = 3;
	private JTextField nomeProdutoField;
	private JTextField quantidadeField;
	private JTextField valorField;
	private JTextArea produtosTextArea;

	public ListaProdutos(int produtos) {
		this.produtos = produtos;
		lista = new String[produtos][colunas];

		initComponents();
		setLocationRelativeTo(null);
	}

	private void initComponents() {
		setTitle("Lista de Compras");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(580, 300);
		setLayout(new BorderLayout());

		JPanel panel = new JPanel();
		panel.setBackground(Color.decode("#3F3F3F"));
		panel.setBorder(BorderFactory.createDashedBorder(Color.WHITE, 1.0f, 1.0f, 1.0f, false));
		panel.setLayout(new BorderLayout());

		JLabel label = new JLabel("Trabalho final - LPA");
		label.setFont(new Font("Arial", Font.PLAIN, 18));
		label.setForeground(Color.WHITE);
		label.setHorizontalAlignment(SwingConstants.CENTER);

		produtosTextArea = new JTextArea();
		produtosTextArea.setEditable(false);
		produtosTextArea.setBackground(Color.decode("#3F3F3F"));
		produtosTextArea.setForeground(Color.WHITE);

		JScrollPane scrollPane = new JScrollPane(produtosTextArea);

		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(5, 5, 5, 5);

		JLabel nomeProdutoLabel = new JLabel("Nome do produto:");
		nomeProdutoField = new JTextField();
		nomeProdutoField.setPreferredSize(new Dimension(200, 25));
		JLabel quantidadeLabel = new JLabel("Quantidade:");
		quantidadeField = new JTextField();
		quantidadeField.setPreferredSize(new Dimension(200, 25));
		JLabel valorLabel = new JLabel("Valor:");
		valorField = new JTextField();
		valorField.setPreferredSize(new Dimension(200, 25));

		nomeProdutoField.setInputVerifier(new AlphaNumericVerifier());
		quantidadeField.setInputVerifier(new NumericVerifier());
		valorField.setInputVerifier(new NumericVerifier());

		gbc.gridx = 0;
		gbc.gridy = 0;
		inputPanel.add(nomeProdutoLabel, gbc);
		gbc.gridx = 1;
		inputPanel.add(nomeProdutoField, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		inputPanel.add(quantidadeLabel, gbc);
		gbc.gridx = 1;
		inputPanel.add(quantidadeField, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		inputPanel.add(valorLabel, gbc);
		gbc.gridx = 1;
		inputPanel.add(valorField, gbc);

		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		

		JButton inserirButton = new JButton("Inserir");
		inserirButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				inserirProduto();
			}
		});

		JButton editarButton = new JButton("Editar");
		editarButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editarProduto();
			}
		});

		JButton calcularButton = new JButton("Calcular valor");
		calcularButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calcularValor();
			}
		});

		JButton removerButton = new JButton("Remover produto");
		removerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removerProduto();
			}
		});
		
		JButton imprimirButton = new JButton("Imprimir");
        imprimirButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	imprimirLista();
            }
        });

		buttonsPanel.add(inserirButton);
		buttonsPanel.add(editarButton);
		buttonsPanel.add(calcularButton);
		buttonsPanel.add(removerButton);
		buttonsPanel.add(imprimirButton);
		

		panel.add(label, BorderLayout.NORTH);
		panel.add(scrollPane, BorderLayout.CENTER);
		panel.add(inputPanel, BorderLayout.WEST);
		panel.add(buttonsPanel, BorderLayout.SOUTH);

		add(panel, BorderLayout.CENTER);

		setVisible(true);
	}
	
	

	private void inserirProduto() {
		String nomeProduto = nomeProdutoField.getText();
		String quantidade = quantidadeField.getText();
		String valor = valorField.getText();

		if (nomeProduto.isEmpty() || quantidade.isEmpty() || valor.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Preencha todos os campos.", "Erro", JOptionPane.ERROR_MESSAGE);
			return;
		}

		int linha = produtosTextArea.getLineCount();

		if (linha > produtos) {
			JOptionPane.showMessageDialog(this, "Não há mais linhas disponíveis na lista de compras.", "Erro",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		lista[linha - 1][0] = nomeProduto;
		lista[linha - 1][1] = quantidade;
		lista[linha - 1][2] = valor;

		String produtoString = String.format("%d - %s | %s | %s", linha, nomeProduto, quantidade, valor);
		produtosTextArea.append(produtoString + "\n");

		nomeProdutoField.setText("");
		quantidadeField.setText("");
		valorField.setText("");

		JOptionPane.showMessageDialog(this, "Produto adicionado!");
	}

	private void editarProduto() {
		String nomeProduto = JOptionPane.showInputDialog(this, "Insira o nome do produto a ser editado:");

		boolean encontrado = false;

		for (int i = 0; i < produtos; i++) {
			if (lista[i][0] != null && lista[i][0].equals(nomeProduto)) {
				encontrado = true;

				String novoNomeProduto = JOptionPane.showInputDialog(this, "Insira o novo nome do produto:");
				String novaQuantidade = JOptionPane.showInputDialog(this, "Insira a nova quantidade:");
				String novoValor = JOptionPane.showInputDialog(this, "Insira o novo valor:");

				lista[i][0] = novoNomeProduto;
				lista[i][1] = novaQuantidade;
				lista[i][2] = novoValor;

				JOptionPane.showMessageDialog(this, "Produto editado.");

				atualizarLista();
				break;
			}
		}

		if (!encontrado) {
			JOptionPane.showMessageDialog(this, "Produto não encontrado ");
		}
	}

	public void calcularValor() {		

		for (int i = 0; i < produtos; i++) {
			if (lista[i][0] != null) {
				float quantidade = Float.parseFloat(lista[i][1]);
				float valor = Float.parseFloat(lista[i][2]);
				valorFinal_ += quantidade * valor;
			}
		}
		

		JOptionPane.showMessageDialog(this, "Valor final da lista de compras: R$" + valorFinal_ + " ");
	}
	
	public void imprimirLista() {
		for (int i = 0; i < produtos; i++) {
			if (lista[i][0] != null) {
				float quantidade = Float.parseFloat(lista[i][1]);
				float valor = Float.parseFloat(lista[i][2]);
				valorFinal_ += quantidade * valor;
			}
		}
		
        StringBuilder conteudoArquivo = new StringBuilder();
        conteudoArquivo.append("Lista de Compras:\n");

        for (int i = 0; i < produtos; i++) {
            if (lista[i][0] != null) {
                String produtoString = String.format("%d - %s | %s | %s", (i + 1), lista[i][0], lista[i][1], lista[i][2]);
                conteudoArquivo.append(produtoString + "\n");
            }
        }

        conteudoArquivo.append("Valor final da lista de compras: R$" + valorFinal_);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("lista_compras.txt"))) {
            writer.write(conteudoArquivo.toString());
            JOptionPane.showMessageDialog(this, "Arquivo 'lista_compras.txt' gerado com sucesso!");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao gerar o arquivo 'lista_compras.txt'.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
        
        File arquivo = new File("lista_compras.txt");
        if (arquivo.exists()) {
            try {
                Desktop.getDesktop().open(arquivo);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao abrir o arquivo 'lista_compras.txt'.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
        
    }

	private void removerProduto() {
		String nomeProduto = JOptionPane.showInputDialog(this, "Insira o produto (nome) a ser removido:");

		boolean encontrado = false;

		for (int i = 0; i < produtos; i++) {
			if (lista[i][0] != null && lista[i][0].equals(nomeProduto)) {
				encontrado = true;
				lista[i][0] = null;
				lista[i][1] = null;
				lista[i][2] = null;
			}
		}

		if (encontrado) {
			JOptionPane.showMessageDialog(this, "Produto removido.");
			atualizarLista();
		} else {
			JOptionPane.showMessageDialog(this, "Produto não encontrado.");
		}
	}

	private void atualizarLista() {
		produtosTextArea.setText("");
		for (int i = 0; i < produtos; i++) {
			if (lista[i][0] != null) {
				String produtoString = String.format("%d - %s | %s | %s", (i + 1), lista[i][0], lista[i][1],
						lista[i][2]);
				produtosTextArea.append(produtoString + "\n");
			}
		}
	}

	class AlphaNumericVerifier extends InputVerifier {
        public boolean verify(JComponent input) {
            JTextField textField = (JTextField) input;
            String text = textField.getText();
            if (!text.matches("^[a-zA-Z0-9\\s]+$")) {
                JOptionPane.showMessageDialog(ListaProdutos.this, "Digite apenas letras e números.", "Erro", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            return true;
        }
    }

	class NumericVerifier extends InputVerifier {
		public boolean verify(JComponent input) {
			JTextField textField = (JTextField) input;
			String text = textField.getText();
			if (!text.matches("[0-9]+")) {
				JOptionPane.showMessageDialog(ListaProdutos.this, "Digite apenas números.", "Erro",
						JOptionPane.ERROR_MESSAGE);
				return false;
			}
			return true;
		}
	}

	private static int obterQuantidadeProdutos() {
		int quantidade = 0;
		boolean entradaValida = false;

		while (!entradaValida) {
			String input = JOptionPane
					.showInputDialog("Insira a quantidade de produtos que serão inseridos na sua lista de compras:");

			try {
				quantidade = Integer.parseInt(input);
				entradaValida = true;
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "Digite um valor numérico válido.", "Erro",
						JOptionPane.ERROR_MESSAGE);
			}
		}

		return quantidade;
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				int produtos = obterQuantidadeProdutos();
				new ListaProdutos(produtos);
			}
		});
	}
}
