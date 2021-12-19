package grimorio.t20.struct;

import com.google.gson.Gson;

public class Aprimoramento {
	
	private String descricao, custo, exclusivo;
	private int id;
	
	public Aprimoramento() {}
	
	public Aprimoramento(int id, String custo, String descricao, String exclusivo) {
		setId(id);
		setCusto(custo);
		setDescricao(descricao);
		setExclusivo(exclusivo);
	}

	public int getId() {
		return id;
	}

	public String getCusto() {
		return custo;
	}

	public String getDescricao() {
		return descricao;
	}
	
	public String getExclusivo() {
		return exclusivo;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setCusto(String custo) {
		this.custo = custo;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public void setExclusivo(String exclusivo) {
		this.exclusivo = exclusivo;
	}

	public static Aprimoramento fromJson(String json) {
		return new Gson().fromJson(json, Aprimoramento.class);
	}

	public String toJson() {
		return new Gson().toJson(this);
	}

	/////////////
	public String getCustoFormatado() {
		if (getCusto().isEmpty())
			return "Truque";
		
		String retorno = "+" + getCusto();
		
		if (!getExclusivo().isEmpty())
			retorno += " " + exclusivo;
		
		return retorno;
	}

}
