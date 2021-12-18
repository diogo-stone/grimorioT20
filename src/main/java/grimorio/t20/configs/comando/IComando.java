package grimorio.t20.configs.comando;

import java.util.List;

public interface IComando {

    void gerenciar(ComandoContext context);

    String getNome();

    String getAjuda();

    default List<String> getAliases() {
        return List.of();
    }

}
