package br.com.leco.cm.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class Campo {
	
	private final int linha;
	private final int coluna;
	
	private boolean aberto = false;  // campo aberto ou fechado
	private boolean minado = false;  // campo minado ou não.
	private boolean marcado = false; // campo marcado.
	
	private List<Campo> vizinhos = new ArrayList<>(); // Vizinhos
	private List<CampoObservador> observadores = new ArrayList<>();

	Campo(final int linha, final int coluna) {
		this.linha = linha;
		this.coluna = coluna;
	}
	
	boolean adicionarVizinho(Campo vizinho) {

		 boolean linhaDiferente = linha != vizinho.linha;
		 boolean colunaDiferente = coluna != vizinho.coluna;
		 boolean diagonal = linhaDiferente && colunaDiferente;

		 int deltaLinha = Math.abs(linha - vizinho.linha);
		 int deltaColuna = Math.abs(coluna - vizinho.coluna);
		 int deltaGeral = deltaColuna + deltaLinha;

		// 1º cenário
		if (deltaGeral == 1 && !diagonal) {
			vizinhos.add(vizinho);
			return true;
		} else if (deltaGeral == 2 && diagonal) { // 2º cenário
			vizinhos.add(vizinho);
			return true;
		} else {
			return false;
		}
	}
	
	public void registraObservador(CampoObservador observador) {
		observadores.add(observador);
	}
	
	/**
	 * Chamar método para avisar sempre que um evento acontecer
	 * @param evento
	 */
	public void notificarObservadores(CampoEvento evento) {
		observadores.stream().forEach(o -> o.eventoOcorreu(this, evento));
	}


	public void alternarMarcacao() {
		if (!aberto) {
			marcado = !marcado;
			
			if (marcado) {
				notificarObservadores(CampoEvento.MARCAR);
			} else {
				notificarObservadores(CampoEvento.DESMARCAR);
			}
		}
	}

	public boolean abrir() {
		if (!aberto && !marcado) {
			// verifica se explodiu
			if (minado) {
				notificarObservadores(CampoEvento.EXPLODIR);
				return true;
			}
			
			setAberto(true);
			
			if (vizinhancaSegura()) {
				vizinhos.forEach(v -> v.abrir());
			}
			return true;
		} else {
			return false;
		}
	}

	public boolean vizinhancaSegura() {
		return vizinhos.stream().noneMatch(v -> v.minado);
	}

	public void minar() {
		minado = true;
	}

	public boolean isMinado() {
		return minado;
	}

	public boolean isMarcado() {
		return marcado;
	}

	public void setAberto(final boolean aberto) {
		this.aberto = aberto;
		
		if (this.aberto) {
			 notificarObservadores(CampoEvento.ABRIR);
		}
	}

	public boolean isAberto() {
		return aberto;
	}

	public boolean isFechado() {
		return !isAberto();
	}

	public int getLinha() {
		return linha;
	}

	public int getColuna() {
		return coluna;
	}

	public boolean objetivoAlcancado() {
		final boolean desvendado = !minado && aberto;
		final boolean protegido = !minado && marcado;
		return desvendado || protegido;
	}
	
	public int minasNaVizinhanca() {
		return (int) vizinhos.stream().filter(v -> v.minado).count();
	}
	
	public void reiniciar() {
		aberto = false;
		minado = false;
		marcado = false;
		notificarObservadores(CampoEvento.REINICIAR);
	}
	
	@Override
	public String toString() {
		if(marcado)
			return "X";
		else if (aberto && minado)
			return "*";
		else if(aberto && minasNaVizinhanca() > 0)
			return Long.toString(minasNaVizinhanca());
		else if(aberto)
			return " ";
		else
			return "?";
			
	}
}
