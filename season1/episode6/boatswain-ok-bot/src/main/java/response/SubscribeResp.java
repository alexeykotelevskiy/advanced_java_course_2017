package response;

public class SubscribeResp {
    public boolean getSuccess() {
        return success;
    }

    public SubscribeResp(boolean success) {
        this.success = success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    boolean success;
}
