package bot;

import com.google.gson.Gson;
import facebook.models.common.Action;
import facebook.models.send.Message;
import facebook.models.webhook.Messaging;
import facebook.models.webhook.ReceivedMessage;
import libs.helpers.Config;
import okhttp3.MediaType;
import org.fusesource.jansi.AnsiConsole;
import spark.Request;
import spark.Response;

import java.util.List;
import java.util.Random;

import static org.fusesource.jansi.Ansi.Color.*;
import static org.fusesource.jansi.Ansi.ansi;
import static spark.Spark.*;

public class App
{
    public static String token;
    public static int webhookPort;
    public static String validationToken;
    public static final String END_POINT;
    public static final MediaType JSON;
    public static final Random sRandom;
    public static final Gson GSON;

    static {
        Config config = new Config("./config.properties");

        JSON = MediaType.parse("application/json; charset=utf-8");
        END_POINT = "https://graph.facebook.com/v2.6/me/messages";
        GSON = new Gson();
        sRandom = new Random();
        token = config.get("facebook.token");
        validationToken = config.get("facebook.validation_token");
        webhookPort = Integer.parseInt(config.get("facebook.webhook.port"));
    }

    public static void main( String[] args ) {
        AnsiConsole.systemInstall();

        Boolean isTokenSet = token != null && !token.equals("SetYourToken");

        if (!isTokenSet) {
            System.out.println("Token: " + ansi().bg(RED).fg(BLACK).a("NOT SET").reset());
            System.out.println("Please set your facebook token");
            return;
        }

        System.out.println("Token: " + ansi().fg(GREEN).bold().a(token).reset());

        System.out.println(ansi().bg(GREEN).fg(BLACK).a("Bot started").reset());
        port(webhookPort);

//        FacebookRoutes.register();

//        Connect mother = new Connect(config.get("mother.host"), Integer.parseInt(config.get("mother.port")));

        path("/fb", () -> {
            get("/webhook", (Request request, Response response) -> {

                // TODO: move to one unified place
                Config config = new Config("./config.properties");
                String token = config.get("facebook.token");
                String validationToken = config.get("facebook.validation_token");
                int webhookPort = Integer.parseInt(config.get("facebook.webhook.port"));

                if (request.queryMap("hub.verify_token").value().equals(validationToken)) {
                    return request.queryMap("hub.challenge").value();
                }

                return "Error, wrong validation token";
            });

            post("/webhook", (Request request, Response response) -> {
                ReceivedMessage receivedMessage = GSON.fromJson(request.body(), ReceivedMessage.class);
                List<Messaging> messagings = receivedMessage.entry.get(0).messaging;

                for (Messaging messaging : messagings) {
                    String senderId = messaging.sender.id;
                    if (messaging.message != null) {
                        System.out.println(messaging.message.text);
                        // Receiving text message
                        Message.Text(messaging.message.text).sendTo(senderId);

                    } else if (messaging.postback != null) {
                        // Receiving postback message
                        if (messaging.postback.payload == Action.ACTION_A) {
                            Message.Text("Action A").sendTo(senderId);
                        }else {
                            Message.Text("Action B").sendTo(senderId);
                        }
                    } else if (messaging.delivery != null) {
                        // when the message is delivered, this webhook will be triggered.
                    } else {
                        // sticker may not be supported for now.
                        System.out.println(request.body());
                    }
                }
                return "";
            });
        });
    }
}
