package grimorio.t20.struct;

public class Errata {

    private int id;
    private String data, descricao, versao;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getData() { return data; }
    public void setData(String data) { this.data = data; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getVersao() { return versao; }
    public void setVersao(String versao) { this.versao = versao; }

    @Override
    public String toString() {
        return "Errata{" +
                "id=" + id +
                ", data='" + data + '\'' +
                ", descricao='" + descricao + '\'' +
                ", versao='" + versao + '\'' +
                '}';
    }
}
