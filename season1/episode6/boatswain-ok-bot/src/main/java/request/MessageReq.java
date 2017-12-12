package request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessageReq {

    @SerializedName("recipient")
    @Expose
    private Recipient recipient;
    @SerializedName("message")
    @Expose
    private Message message;

    public MessageReq(String chatid, String message) {
        this.recipient = new Recipient(chatid);
        this.message = new Message(message);
    }


    public Recipient getRecipient() {
        return recipient;
    }

    public void setRecipient(Recipient recipient) {
        this.recipient = recipient;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public class Message {
        Message(String text) {
            this.text = text;
        }

        @SerializedName("text")
        @Expose
        private String text;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

    }

    public class Recipient {
        Recipient(String chatId) {
            this.chatId = chatId;
        }

        @SerializedName("chat_id")
        @Expose
        private String chatId;

        public String getChatId() {
            return chatId;
        }

        public void setChatId(String chatId) {
            this.chatId = chatId;
        }

    }
}