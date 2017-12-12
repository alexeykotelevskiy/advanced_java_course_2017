import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import request.Subscribe;
import response.SubscribeResp;
import response.SubscribesList;

import static org.junit.Assert.*;

public class ApiClientTest {
    private static ApiClient apiClient;

    @BeforeClass
    public static void createClient() throws IOException
    {
        apiClient = new ApiClient();
    }
    @Test
    public void subscribe() throws Exception {
        SubscribeResp subscribe = apiClient.subscribe();
        assertTrue(subscribe.getSuccess());
    }

    @Test
    public void getSubscribesList() throws Exception {
        SubscribesList list = apiClient.getSubscribesList();
        assertNotNull(list);
    }


}