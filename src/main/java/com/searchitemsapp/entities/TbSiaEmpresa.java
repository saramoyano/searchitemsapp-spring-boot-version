package com.searchitemsapp.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.stereotype.Component;

import lombok.NoArgsConstructor;

@Entity
@Component
@NoArgsConstructor
@Table(name="tb_sia_empresa", schema = "sia")
@NamedQuery(name="TbSiaEmpresa.findAll", query="SELECT t FROM TbSiaEmpresa t")
public class TbSiaEmpresa implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "did")
	private Integer did;

	@Column(name="bol_activo")
	private Boolean bolActivo;

	@Column(name="des_empresa")
	private String desEmpresa;

	@Column(name="nom_empresa")
	private String nomEmpresa;

	//bi-directional many-to-one association to TbSiaCategoriasEmpresa
	@ManyToOne(cascade=CascadeType.ALL) 
	@JoinColumn(name="id_categoria", referencedColumnName="did", nullable = false)
	private TbSiaCategoriasEmpresa tbSiaCategoriasEmpresa;

	//bi-directional many-to-one association to TbSiaPais
	@ManyToOne(cascade=CascadeType.ALL) 
	@JoinColumn(name="id_pais", referencedColumnName="did", nullable = false)
	private TbSiaPais tbSiaPais;

	//bi-directional many-to-one association to TbSiaUrl
	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(mappedBy="tbSiaEmpresa")
	private List<TbSiaUrl> tbSiaUrls;
	
	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(mappedBy="tbSiaEmpresa")
	private List<TbSiaSelectoresCss> tbSiaSelectoresCsses;	

	@Column(name="bol_dyn_scrap")
	private Boolean bolDynScrap;	

	public Integer getDid() {
		return this.did;
	}

	public void setDid(Integer did) {
		this.did = did;
	}

	public Boolean getBolActivo() {
		return this.bolActivo;
	} 

	public void setBolActivo(Boolean bolActivo) {
		this.bolActivo = bolActivo;
	}

	public String getDesEmpresa() {
		return this.desEmpresa;
	}

	public void setDesEmpresa(String desEmpresa) {
		this.desEmpresa = desEmpresa;
	}

	public String getNomEmpresa() {
		return this.nomEmpresa;
	}

	public void setNomEmpresa(String nomEmpresa) {
		this.nomEmpresa = nomEmpresa;
	}

	public TbSiaCategoriasEmpresa getTbSiaCategoriasEmpresa() {
		return this.tbSiaCategoriasEmpresa;
	}

	public void setTbSiaCategoriasEmpresa(TbSiaCategoriasEmpresa tbSiaCategoriasEmpresa) {
		this.tbSiaCategoriasEmpresa = tbSiaCategoriasEmpresa;
	}

	public TbSiaPais getTbSiaPais() {
		return this.tbSiaPais;
	}

	public void setTbSiaPais(TbSiaPais tbSiaPais) {
		this.tbSiaPais = tbSiaPais;
	}

	public List<TbSiaUrl> getTbSiaUrls() {
		return this.tbSiaUrls;
	}

	public void setTbSiaUrls(List<TbSiaUrl> tbSiaUrls) {
		this.tbSiaUrls = tbSiaUrls;
	}

	public TbSiaUrl addTbSiaUrl(TbSiaUrl tbSiaUrl) {
		getTbSiaUrls().add(tbSiaUrl);
		tbSiaUrl.setTbSiaEmpresa(this);

		return tbSiaUrl;
	}

	public TbSiaUrl removeTbSiaUrl(TbSiaUrl tbSiaUrl) {
		getTbSiaUrls().remove(tbSiaUrl);
		tbSiaUrl.setTbSiaEmpresa(null);

		return tbSiaUrl;
	}

	public Boolean getBolDynScrap() {
		return bolDynScrap;
	}

	public void setBolDynScrap(Boolean bolDynScrap) {
		this.bolDynScrap = bolDynScrap;
	}

	public List<TbSiaSelectoresCss> getTbSiaSelectoresCsses() {
		return tbSiaSelectoresCsses;
	}

	public void setTbSiaSelectoresCsses(List<TbSiaSelectoresCss> tbSiaSelectoresCsses) {
		this.tbSiaSelectoresCsses = tbSiaSelectoresCsses;
	}
}