package grimorio.t20.database;

import grimorio.t20.struct.Aprimoramento;
import grimorio.t20.struct.Magia;

public interface IDatabaseGerenciar {

    IDatabaseGerenciar INSTANCE = new SQLiteDataSource();

    // Prefixo
    String getPrefixo(long guildId);
    void setPrefixo(long guildId, String novoPrefixo);

    // Magias
    void addMagia(Magia magia);
    void addAprimoramento(int idMagia, Aprimoramento aprimoramento);
}
