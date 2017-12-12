package response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessageResp {

    @SerializedName("sender")
    @Expose
    private Sender sender;
    @SerializedName("recipient")
    @Expose
    private Recipient recipient;
    @SerializedName("message")
    @Expose
    private Message message;
    @SerializedName("timestamp")
    @Expose
    private Integer timestamp;

    public Sender getSender() {
        return sender;
    }

    public void setSender(Sender sender) {
        this.sender = sender;
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

    public Integer getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }

    public class Recipient {

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

    public class Sender {

        @SerializedName("user_id")
        @Expose
        private String userId;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

    }

    public class Message {

        @SerializedName("text")
        @Expose
        private String text;
        @SerializedName("seq")
        @Expose
        private Integer seq;
        @SerializedName("mid")
        @Expose
        private String mid;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public Integer getSeq() {
            return seq;
        }

        public void setSeq(Integer seq) {
            this.seq = seq;
        }

        public String getMid() {
            return mid;
        }

        public void setMid(String mid) {
            this.mid = mid;
        }

    }

}