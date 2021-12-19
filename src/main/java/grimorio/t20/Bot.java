package grimorio.t20;

import grimorio.t20.configs.Config;
import grimorio.t20.database.SQLiteDataSource;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import javax.security.auth.login.LoginException;
import java.sql.SQLException;

public class Bot {

    private Bot() throws LoginException {
        String token = Config.get("token");
        if (token.length() < 1) {
            System.err.println("Token não informado.");
            return;
        }
        JDABuilder jda = JDABuilder.createDefault(token);
        jda.addEventListeners(new ComandoListener())
                .setActivity(Activity.watching("Prefixo: !@"))
                .build();
    }

    public static void main(String[] args) throws LoginException {
        new Bot();
    }

}
