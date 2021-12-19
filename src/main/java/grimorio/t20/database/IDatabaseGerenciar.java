package grimorio.t20.database;

import grimorio.t20.struct.Aprimoramento;
import grimorio.t20.struct.Magia;

import java.util.ArrayList;
import java.util.Map;

public interface IDatabaseGerenciar {

    IDatabaseGerenciar INSTANCE = new SQLiteDataSource();

    // Prefixo
    String getPrefixo(long guildId);
    void setPrefixo(long guildId, String novoPrefixo);

    // Magias
    void addMagia(Magia magia);
    void addAprimoramento(Aprimoramento aprimoramento);

    void addListaMagias(ArrayList<Magia> listaMagias);
    void addListaAprimoramentos(ArrayList<Aprimoramento> listaAprimoramentos);

    Map<Integer, Magia> consultaMagia(String nome);
}
