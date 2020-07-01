package br.com.leco.cm.visao;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import br.com.leco.cm.modelo.Tabuleiro;

@SuppressWarnings("serial")
public class PainelTabuleiro extends JPanel {
	
	public PainelTabuleiro(Tabuleiro tabuleiro) {
		setLayout(new GridLayout(tabuleiro.getLinhas(), tabuleiro.getColunas()));
		
		tabuleiro.paraCada(c-> add(new BotaoCampo(c)));
		
		tabuleiro.registraObservador(e -> {

			SwingUtilities.invokeLater(()->{
				if(e.isGanhou()){
					JOptionPane.showMessageDialog(this, "Você Ganhou :) ");
				} else {
					JOptionPane.showMessageDialog(this, "Você Perdeu :( ");	
				}
				tabuleiro.reiniciar();
			});

			
			
			
		});
	}
}
