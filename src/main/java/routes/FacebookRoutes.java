//package routes;
//
//import facebook.models.common.Action;
//import facebook.models.send.Message;
//import facebook.models.webhook.Messaging;
//import facebook.models.webhook.ReceivedMessage;
//import libs.helpers.Config;
//import spark.Request;
//import spark.Response;
//
//import java.util.List;
//
//import static spark.Spark.*;
//import static bot.App.*;
//
//public class FacebookRoutes {
//    public static void register() {
//        path("/fb", () -> {
//            get("/webhook", (Request request, Response response) -> {
//
//                // TODO: move to one unified place
//                Config config = new Config("./config.properties");
//                String token = config.get("facebook.token");
//                String validationToken = config.get("facebook.validation_token");
//                int webhookPort = Integer.parseInt(config.get("facebook.webhook.port"));
//
//                if (request.queryMap("hub.verify_token").value().equals(validationToken)) {
//                    return request.queryMap("hub.challenge").value();
//                }
//
//                return "Error, wrong validation token";
//            });
//
//            post("/webhook", (Request request, Response response) -> {
//                ReceivedMessage receivedMessage = GSON.fromJson(request.body(), ReceivedMessage.class);
//                List<Messaging> messagings = receivedMessage.entry.get(0).messaging;
//                for (Messaging messaging : messagings) {
//                    String senderId = messaging.sender.id;
//                    if (messaging.message !=null) {
//                        // Receiving text message
//                        switch (sRandom.nextInt(4)){
//                            case 0:
//                                if (messaging.message.text != null)
//                                    Message.Text(messaging.message.text).sendTo(senderId);
//                                else
//                                    sendSampleGenericMessage(senderId);
//                                break;
//                            case 1:
//                                Message.Image("https://unsplash.it/764/400?image=200").sendTo(senderId);
//                                break;
//                            case 2:
//                                sendSampleGenericMessage(senderId);
//                                break;
//                            default:
//                                sendSamplePostBackMessage(senderId);
//                                break;
//                        }
//
//                    } else if (messaging.postback != null) {
//                        // Receiving postback message
//                        if (messaging.postback.payload == Action.ACTION_A) {
//                            Message.Text("Action A").sendTo(senderId);
//                        }else {
//                            Message.Text("Action B").sendTo(senderId);
//                        }
//                    } else if (messaging.delivery != null) {
//                        // when the message is delivered, this webhook will be triggered.
//                    } else {
//                        // sticker may not be supported for now.
//                        System.out.println(request.body());
//                    }
//                }
//                return "";
//            });
//        });
//    }
//}
