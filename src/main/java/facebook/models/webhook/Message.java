package facebook.models.webhook;

import java.util.List;

public class Message {
    public String mid;
    public int seq;
    public List<Attachment> attachments;
    public String text;
}
