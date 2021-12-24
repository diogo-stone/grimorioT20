package grimorio.t20;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import grimorio.t20.configs.Config;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import javax.security.auth.login.LoginException;

public class Bot {

    private Bot() throws LoginException {
        String token = Config.get("token");
        if (token.length() < 1) {
            System.err.println("Token não informado.");
            return;
        }
        EventWaiter waiter = new EventWaiter();

        JDABuilder jda = JDABuilder.createDefault(token);
        jda.addEventListeners(new ComandoListener(waiter), waiter)
                .setActivity(Activity.watching("Prefixo: !@"))
                .build();
    }

    public static void main(String[] args) throws LoginException {
        new Bot();
    }

}
