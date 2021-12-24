package grimorio.t20.struct;

import java.util.ArrayList;
import java.util.List;


import com.google.gson.Gson;

public class Magia {

	private String nome, escola, execucao, alcance, alvo, area, efeito, duracao, resistencia, descricao, componenteMaterial, fonte;
	private int id, nivel;
	private boolean arcana, divina;
	private List<Aprimoramento> listaAprimoramentos;
	
	public Magia() {
		listaAprimoramentos = new ArrayList<Aprimoramento>();
	}

	public int getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public String getEscola() {
		return escola;
	}

	public String getExecucao() {
		return execucao;
	}

	public String getAlcance() {
		return alcance;
	}

	public String getAlvo() {
		return alvo;
	}

	public String getArea() {
		return area;
	}

	public String getEfeito() {
		return efeito;
	}

	public String getDuracao() {
		return duracao;
	}

	public String getResistencia() {
		return resistencia;
	}

	public String getDescricao() {
		return descricao;
	}

	public int getNivel() {
		return nivel;
	}

	public String getComponenteMaterial() {
		return componenteMaterial;
	}
	
	public String getFonte() {
		return fonte;
	}
	
	public boolean isArcana() {
		return arcana;
	}

	public boolean isDivina() {
		return divina;
	}

	public List<Aprimoramento> getListaAprimoramentos() {
		return listaAprimoramentos;
	}

	//////////////

	public void setId(int id) {
		this.id = id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setEscola(String escola) {
		this.escola = escola;
	}

	public void setExecucao(String execucao) {
		this.execucao = execucao;
	}

	public void setAlcance(String alcance) {
		this.alcance = alcance;
	}

	public void setAlvo(String alvo) {
		this.alvo = alvo;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public void setEfeito(String efeito) {
		this.efeito = efeito;
	}

	public void setDuracao(String duracao) {
		this.duracao = duracao;
	}

	public void setResistencia(String resistencia) {
		this.resistencia = resistencia;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public void setNivel(int nivel) {
		this.nivel = nivel;
	}

	public void setArcana(boolean arcana) {
		this.arcana = arcana;
	}

	public void setDivina(boolean divina) {
		this.divina = divina;
	}

	public void setListaAprimoramentos(List<Aprimoramento> listaAprimoramentos) {
		this.listaAprimoramentos = listaAprimoramentos;
	}

	public void setComponenteMaterial(String componenteMaterial) {
		this.componenteMaterial = componenteMaterial;
	}
	
	public void setFonte(String fonte) {
		this.fonte = fonte;
	}

	///////
	
	public void addAprimoramento(Aprimoramento apr) {
		listaAprimoramentos.add(apr);
	}

	public String getTipoMagia() {
		if (isArcana() && isDivina())
			return "Universal";
		if (isArcana())
			return "Arcana";
		return "Divina";
	}

	public String getDescricaoFormatada() {
		String desc = "_" + getTipoMagia() + " "+getNivel()+" ("+getEscola()+")_\n\n" +
				"**Execução**: " + getExecucao() + "; **Alcance**: "+getAlcance() +
				"%s" + // Alvo, Area e Efeito
				"%s" + // Duração e Resistência
				"\n" + getDescricao().replace("%", "%%") + "\n" +
				"%s" + // Componente Material
				"%s";  // Aprimoramentos

		String alvoAreaEfeito = (getAlvo().isEmpty() ?
				"" : getAlvo().equalsIgnoreCase(getArea()) ?
					"**Alvo ou Área**: " + getAlvo() + "; " : "**Alvo**: " + getAlvo() + "; ") +
				(getArea().isEmpty() ?
						"" : getArea().equalsIgnoreCase(getAlvo()) ?
							"" : "**Área**: " + getArea() + "; ") +
				(getEfeito().isEmpty() ?
						"" : "**Efeito**: " + getEfeito() + "; ");
		alvoAreaEfeito = alvoAreaEfeito.isEmpty() ? "" : "\n" + alvoAreaEfeito;

		String duracaoResistencia = (getDuracao().isEmpty() ?
				"" : "**Duração**: " + getDuracao() + "; ") +
				(getResistencia().isEmpty() ?
						"" : "**Resistência**: " + getResistencia() + "; ");
		duracaoResistencia = duracaoResistencia.isEmpty() ? "" : "\n" + duracaoResistencia + "\n";

		String componenteMaterial = getComponenteMaterial().isEmpty() ?
				"" : "\n_Componente Material_: " + getComponenteMaterial() + "\n";

		String aprimoramentos = "";
		for (Aprimoramento apr: getListaAprimoramentos()) {
			aprimoramentos += "\n" + apr.getDescricaoFormatada();
		}

		return String.format(desc, alvoAreaEfeito, duracaoResistencia,componenteMaterial, aprimoramentos).replace("%%", "%");
	}

	public static Magia fromJson(String json) {
		return new Gson().fromJson(json, Magia.class);
	}

	public String toJson() {
		return new Gson().toJson(this);
	}
	
}
