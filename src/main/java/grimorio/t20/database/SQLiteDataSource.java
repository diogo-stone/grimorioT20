package grimorio.t20.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import grimorio.t20.configs.Config;
import grimorio.t20.struct.Aprimoramento;
import grimorio.t20.struct.Magia;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.List;

public class SQLiteDataSource implements IDatabaseGerenciar {

    private final Logger LOGGER = LoggerFactory.getLogger(SQLiteDataSource.class);
    private final HikariDataSource ds;

    public SQLiteDataSource() {
        try {
            final File dbFile = new File("database.db");
            if (!dbFile.exists())
                if (dbFile.createNewFile())
                    LOGGER.info("Database criado.");
                else
                    LOGGER.info("N�o foi poss�vel criar o arquivo do Database.");
        } catch (IOException er) {
            er.printStackTrace();
        }

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:sqlite:database.db");
        config.setConnectionTestQuery("SELECT 1");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        ds = new HikariDataSource(config);

        try (final Statement statement = getConexao().createStatement()) {
            String prefixoPadrao = Config.get("prefixo");

            statement.execute("CREATE TABLE IF NOT EXISTS config_servidor (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "id_servidor VARCHAR(20) NOT NULL," +
                    "prefixo VARCHAR(255) NOT NULL DEFAULT '" + prefixoPadrao + "'" +
                    ");");

            statement.execute("CREATE TABLE IF NOT EXISTS magia (" +
                    "id INTEGER PRIMARY KEY," +
                    "nome VARCHAR(255) NOT NULL," +
                    "escola VARCHAR(255) NOT NULL," +
                    "nivel INTEGER NOT NULL," +
                    "arcana BOOLEAN NOT NULL," +
                    "divina BOOLEAN NOT NULL," +
                    "execucao VARCHAR(255) NOT NULL," +
                    "alcance VARCHAR(255) NOT NULL," +
                    "alvo VARCHAR(255) NOT NULL," +
                    "area VARCHAR(255) NOT NULL," +
                    "efeito VARCHAR(255) NOT NULL," +
                    "duracao VARCHAR(255) NOT NULL," +
                    "resistencia VARCHAR(255) NOT NULL," +
                    "descricao TEXT NOT NULL," +
                    "componente_material TEXT NOT NULL," +
                    "fonte VARCHAR(255) NOT NULL" +
                    ");");

            statement.execute("CREATE TABLE IF NOT EXISTS aprimoramento (" +
                    "id INTEGER PRIMARY KEY," +
                    "id_magia INTEGER NOT NULL," +
                    "custo VARCHAR(255) NOT NULL," +
                    "descricao TEXT NOT NULL," +
                    "exclusivo VARCHAR(255) NOT NULL," +
                    "FOREIGN KEY (id_magia) REFERENCES magia(id)" +
                    ");");
        } catch(SQLException er) {
            er.printStackTrace();
        }
    }

    @Override
    public String getPrefixo(long guildId) {
        try (final PreparedStatement preparedStatement = getConexao()
                // language=SQLite
                .prepareStatement("SELECT prefixo FROM config_servidor WHERE id_servidor = ?")) {

            preparedStatement.setString(1, String.valueOf(guildId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("prefixo");
                }
            }

            try (final PreparedStatement insertStatement = getConexao()
                    // language=SQLite
                    .prepareStatement("INSERT INTO config_servidor(id_servidor) VALUES(?)")) {

                insertStatement.setString(1, String.valueOf(guildId));

                insertStatement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Config.get("prefixo");
    }

    @Override
    public void setPrefixo(long guildId, String novoPrefixo) {
        try (PreparedStatement preparedStatement = getConexao()
                // SQLite
                .prepareStatement("UPDATE config_servidor SET prefixo = ? WHERE id_servidor = ?")) {
            preparedStatement.setString(1, novoPrefixo);
            preparedStatement.setString(2, String.valueOf(guildId));
            preparedStatement.executeUpdate();
        } catch (SQLException er) {
            er.printStackTrace();
        }
    }

    @Override
    public void addMagia(Magia magia) {
        try (final PreparedStatement insertStatement = getConexao()
                // SQLite
                .prepareStatement("INSERT OR REPLACE INTO magia(id, nome, nivel, arcana, divina, escola, execucao, " +
                        "alcance, alvo, area, efeito, duracao, resistencia, descricao, componente_material, " +
                        "fonte) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

            LOGGER.info(String.format("Magia %d: %s", magia.getId(), magia.getNome()));

            int i = 1;
            insertStatement.setInt(i++, magia.getId());
            insertStatement.setString(i++, magia.getNome());
            insertStatement.setInt(i++, magia.getNivel());
            insertStatement.setBoolean(i++, magia.isArcana());
            insertStatement.setBoolean(i++, magia.isDivina());
            insertStatement.setString(i++, magia.getEscola());
            insertStatement.setString(i++, magia.getExecucao());
            insertStatement.setString(i++, magia.getAlcance());
            insertStatement.setString(i++, magia.getAlvo());
            insertStatement.setString(i++, magia.getArea());
            insertStatement.setString(i++, magia.getEfeito());
            insertStatement.setString(i++, magia.getDuracao());
            insertStatement.setString(i++, magia.getResistencia());
            insertStatement.setString(i++, magia.getDescricao());
            insertStatement.setString(i++, magia.getComponenteMaterial());
            insertStatement.setString(i++, magia.getFonte());

            insertStatement.execute();

        } catch (SQLException er) {
            er.printStackTrace();
        }
    }

    @Override
    public void addAprimoramento(int idMagia, Aprimoramento aprimoramento) {
        try (final PreparedStatement insertStatement = getConexao()
                // SQLite
                .prepareStatement("INSERT INTO aprimoramento(id, id_magia, custo, descricao, exclusivo) " +
                        "VALUES(?, ?, ?, ?, ?)")) {

            LOGGER.info(String.format("Aprimoramento > Magia %d: %s", idMagia, aprimoramento.getCustoFormatado()));

            insertStatement.setInt(1, aprimoramento.getId());
            insertStatement.setInt(2, idMagia);
            insertStatement.setString(3, aprimoramento.getCusto());
            insertStatement.setString(4, aprimoramento.getDescricao());
            insertStatement.setString(5, aprimoramento.getExclusivo());

            insertStatement.execute();
        } catch (SQLException er) {
            er.printStackTrace();
        }
    }

    private Connection getConexao() throws SQLException {
        return ds.getConnection();
    }
}
