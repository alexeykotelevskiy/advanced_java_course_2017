package response;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubscribesList {

    @SerializedName("subscriptions")
    @Expose
    private List<Subscription> subscriptions = null;

    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(List<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public class Subscription {

        @SerializedName("time")
        @Expose
        private Integer time;
        @SerializedName("url")
        @Expose
        private String url;

        public Integer getTime() {
            return time;
        }

        public void setTime(Integer time) {
            this.time = time;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

    }

}


