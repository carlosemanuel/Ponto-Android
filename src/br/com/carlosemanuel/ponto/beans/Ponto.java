package br.com.carlosemanuel.ponto.beans;

import java.io.Serializable;
import java.util.Date;

public class Ponto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	
	private Date startDate;
	
	private Date finishDate;
	
	public Ponto() {
		
	}
	
	public Ponto(Date startDate, Date finishDate) {
		this.startDate = startDate;
		this.finishDate = finishDate;
	}
	
	public Ponto(Long id, Date startDate, Date finishDate) {
		this.id = id;
		this.startDate = startDate;
		this.finishDate = finishDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(Date finishDate) {
		this.finishDate = finishDate;
	}
	
}
