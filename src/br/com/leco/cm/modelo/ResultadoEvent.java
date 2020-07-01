package br.com.leco.cm.modelo;

public class ResultadoEvent {
	
	private final boolean ganhou;

	public ResultadoEvent(boolean ganhou) {
			this.ganhou = ganhou;
	}
	
	public boolean isGanhou() {
		return ganhou;
	}

}
