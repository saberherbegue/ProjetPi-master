package com.esprit.projetpi.Entities;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Abonnement {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idAbonnement;

	
	private String pays;

	
	private Integer idUser;

	
	private Date dateDebut;
    
	
	private Date dateFin;
	
	@Transient
	private int NbPays;


	public int getNbPays() {
		return NbPays;
	}


	public void setNbPays(int nbPays) {
		NbPays = nbPays;
	}


	public Long getIdAbonnement() {
		return idAbonnement;
	}


	public void setIdAbonnement(Long idAbonnement) {
		this.idAbonnement = idAbonnement;
	}


	public String getPays() {
		return pays;
	}


	public void setPays(String pays) {
		this.pays = pays;
	}


	public Integer getIdUser() {
		return idUser;
	}


	public void setIdUser(Integer idUser) {
		this.idUser = idUser;
	}


	public Date getDateDebut() {
		return dateDebut;
	}


	public void setDateDebut(Date dateDebut) {
		this.dateDebut = dateDebut;
	}


	public Date getDateFin() {
		return dateFin;
	}


	public void setDateFin(Date dateFin) {
		this.dateFin = dateFin;
	}


	public Abonnement(Long idAbonnement, String pays, Integer idUser, Date dateDebut, Date dateFin) {
		super();
		this.idAbonnement = idAbonnement;
		this.pays = pays;
		this.idUser = idUser;
		this.dateDebut = dateDebut;
		this.dateFin = dateFin;
	}


	public Abonnement() {
		super();
	}
	
	
	

}
