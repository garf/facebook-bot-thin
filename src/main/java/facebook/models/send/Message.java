package facebook.models.send;

import bot.App;
import com.google.gson.Gson;
import facebook.models.common.Recipient;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.IOException;

public class Message {

    private transient final OkHttpClient HTTP_CLIENT;
    private transient final Gson GSON;

    {
        HTTP_CLIENT = new OkHttpClient();
        GSON = new Gson();
    }

    final private Attachment attachment;
    final private String text;

    private Message(Attachment attachment, String text) {
        this.attachment = attachment;
        this.text = text;
    }

    public static Message Text(String text) {
        return new Message(null, text);
    }

    public static Message Image(String url) {
        return new Message(Attachment.Image(url), null);
    }

    public static Message Button(String text) {
        return new Message(Attachment.Template(Payload.Button(text)), null);
    }

    public static Message Generic() {
        return new Message(Attachment.Template(Payload.Generic()), null);
    }

    public boolean addElement(Element element) {
        return attachment.addElement(element);
    }

    public boolean addButton(Button button) {
        return attachment.addButton(button);
    }

    public Response sendTo(String recipientId) throws IOException {

        RequestBody body = RequestBody.create(App.JSON, GSON.toJson(new MessageWrapper(recipientId, this)));
        Request request = new Request.Builder()
                .url(App.END_POINT + "?access_token=" + App.token)
                .header("Content-Type", "application/json; charset=UTF-8")
                .post(body)
                .build();
        okhttp3.Response response = HTTP_CLIENT.newCall(request).execute();
        final int code = response.code();
        Response returnValue = null;
        if (code == 200) {
            System.out.println("WORK!");
            returnValue = GSON.fromJson(response.body().string(), Response.class);
        }else {
            System.out.println("ERROR: " + response.body().string());
        }
        response.body().close();
        return returnValue;
    }

    class MessageWrapper {
        private final Recipient recipient;
        private final Message message;
        MessageWrapper(String recipientId, Message message) {
            this.recipient = new Recipient(recipientId);
            this.message = message;
        }
    }
}
