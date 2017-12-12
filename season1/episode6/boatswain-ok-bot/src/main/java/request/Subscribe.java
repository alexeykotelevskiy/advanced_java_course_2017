package request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Subscribe {
    public Subscribe(Object url) {
        this.url = url;
    }

    @SerializedName("url")
    @Expose
    private Object url;

    public Object getUrl() {
        return url;
    }

    public void setUrl(Object url) {
        this.url = url;
    }

}