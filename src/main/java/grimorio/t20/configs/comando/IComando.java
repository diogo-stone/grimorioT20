package grimorio.t20.configs.comando;

import java.util.List;

public interface IComando {

    void gerenciar(IComandoContext context);

    String getNome();

    String getAjuda(boolean mostrarAliases);

    String getResumoComando();

    boolean isAdministrativo();

    boolean isRestritoDesenvolvedor();

    default List<String> getAliases() {
        return List.of();
    }

    default String getAliasesToString() {
        StringBuilder builder = new StringBuilder("");
        getAliases().forEach((it) -> { builder.append("`%s").append(it).append("` "); });
        return builder.toString();
    }

}
