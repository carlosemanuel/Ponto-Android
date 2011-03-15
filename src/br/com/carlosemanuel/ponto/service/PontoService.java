package br.com.carlosemanuel.ponto.service;

import java.util.Date;

import br.com.carlosemanuel.ponto.PontoActivity;
import br.com.carlosemanuel.ponto.beans.Ponto;
import br.com.carlosemanuel.ponto.beans.PontoStatus;
import br.com.carlosemanuel.ponto.repository.PontoRepository;

public class PontoService {

	private PontoRepository pontoRepository;

	public PontoService() {
		pontoRepository = PontoActivity.repository;
	}

	public PontoService(PontoRepository pontoRepository) {
		this.pontoRepository = pontoRepository;
	}

	public Ponto baterPonto() {

		Ponto ponto = pontoRepository.getLast();
		if (ponto == null || ponto.getFinishDate() != null) {
			ponto = new Ponto();
			ponto.setStartDate(new Date());
			pontoRepository.save(ponto);
		} else {
			ponto.setFinishDate(new Date());
			pontoRepository.update(ponto);
		}

		return ponto;
	}

	public PontoStatus getPontoStatus(Ponto ponto) {
		PontoStatus status = PontoStatus.PONTO_ENTRADA;

		if (ponto.getStartDate() != null && ponto.getFinishDate() == null) {
			status = PontoStatus.PONTO_SAIDA;
		}

		return status;
	}

	public Ponto getLastPonto() {
		return pontoRepository.getLast();
	}
}
