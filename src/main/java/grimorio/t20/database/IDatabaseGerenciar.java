package grimorio.t20.database;

import grimorio.t20.struct.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface IDatabaseGerenciar {

    IDatabaseGerenciar INSTANCE = new PostgresDataSource();

    // Prefixo
    String getPrefixo(long guildId);
    void setPrefixo(long guildId, String novoPrefixo);

    // Magias
    void addMagia(Magia magia);
    void addAprimoramento(Aprimoramento aprimoramento);

    void addListaMagias(ArrayList<Magia> listaMagias);
    void addListaAprimoramentos(ArrayList<Aprimoramento> listaAprimoramentos);

    void truncateMagias();
    void truncateAprimoramentos();

    Map<Integer, Magia> consultaMagia(String nome);
    Map<Integer, Magia> ListarMagias(List<String> listEscolas, List<Integer> listNiveis, boolean isArcana, boolean isDivina);

    // Condições
    void addListaCondicao(ArrayList<Condicao> listaCondicoes);
    Map<Integer, Condicao> consultaCondicao(String nome);

    // Errata
    Map<Integer, Errata> consultaErrata(String id);

    // Update
    Map<Integer, Update> consultaUpdate(String id);
}
