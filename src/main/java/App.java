import libs.helpers.Config;
import dto.socket.MotherMessage;
import facebook.Formatter;
import mother.Connect;
import dto.socket.ClientMessage;
import org.fusesource.jansi.AnsiConsole;
import server.ServerWorker;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static org.fusesource.jansi.Ansi.*;
import static org.fusesource.jansi.Ansi.Color.*;

public class App
{
    public static void main( String[] args ) {
        AnsiConsole.systemInstall();

        Config config = new Config("./config.properties");

        String token = config.get("facebook.token");
        int webhookPort = Integer.parseInt(config.get("facebook.webhook.port"));

        Boolean isTokenSet = token != null && !token.equals("SetYourToken");

        if (!isTokenSet) {
            System.out.println("Token: " + ansi().bg(RED).fg(BLACK).a("NOT SET").reset());
            System.out.println("Please set your facebook token");
            return;
        }

        System.out.println("Token: " + ansi().fg(GREEN).bold().a(token).reset());

        System.out.println(ansi().bg(GREEN).fg(BLACK).a("Bot started").reset());

//        Connect mother = new Connect(config.get("mother.host"), Integer.parseInt(config.get("mother.port")));

        ServerSocket server = null;
        try {
            server = new ServerSocket(webhookPort);
        } catch (IOException e) {
            System.out.println("Could not listen on port" + webhookPort);
            System.exit(-1);
        }

        while (true) {
            try {
                Socket accept = server.accept();

                ServerWorker worker = new ServerWorker(accept);
                Thread t = new Thread(worker);
                t.start();
            } catch (IOException e) {
                System.out.println("Accept failed: " + webhookPort);
                System.exit(-1);
            }
        }

//        bot.setUpdatesListener(updates -> {
//            String newMessageText = updates.get(0).message().text();
//            String newMessageSender = updates.get(0).message().from().id().toString();
//            Long chatId = updates.get(0).message().chat().id();
//
//            Send send = new Send(bot);
//
//            ClientMessage clientMessage = new ClientMessage();
//
//            clientMessage.setText(newMessageText)
//                    .setSenderId(newMessageSender)
//                    .setChatId(chatId.toString());
//
//            MotherMessage motherResponse = mother.send(clientMessage);
//
//            send.message(Formatter.format(motherResponse.getText()), chatId);
//
//            send.message("", App.makeKeyboard(motherResponse.getKeyboard()), chatId);
//
//            return UpdatesListener.CONFIRMED_UPDATES_ALL;
//        });
    }
//
//    private static ReplyKeyboardMarkup makeKeyboard(String[] keys) {
//        List<KeyboardButton> buttons = new ArrayList<>();
//
//        for (String key: keys) {
//            buttons.add(new KeyboardButton(key));
//        }
//
//        KeyboardButton[] buttonsArray = buttons.toArray(new KeyboardButton[buttons.size()]);
//
//        return new ReplyKeyboardMarkup(buttonsArray)
//        .oneTimeKeyboard(true)
//        .resizeKeyboard(true);
//    }
}
