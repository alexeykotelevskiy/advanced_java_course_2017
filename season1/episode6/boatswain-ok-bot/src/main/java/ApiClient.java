import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import request.MessageReq;
import request.Subscribe;
import response.MessageResp;
import response.SubscribeResp;
import response.SubscribesList;

class ApiClient {

    private static final Logger logger = Logger.getLogger(ApiClient.class.getName());
    private Properties props = new Properties();
    private Gson gson = new Gson();
    private final CloseableHttpClient client;
    private CloseableHttpResponse response;

    ApiClient() throws IOException {
        props.load(ApiClient.class.getResourceAsStream("/application.properties"));
        client = HttpClients.custom()
                .setDefaultHeaders(Arrays.asList(
                        new BasicHeader("Agent", "ariphmetic-bot"),
                        new BasicHeader("Accept", "*/*")
                        )
                ).build();
    }

    /**
     * Добаввляет токен к url адресу
     *
     * @param url url адрес
     * @return возвращает адрес url с добавленным токеном
     */
    private String addToken(String url) {
        String res;
        if (url.indexOf('?') == -1) {
            res = url + "?access_token=" + props.getProperty("ok.api.access_token");
        } else {
            res = url + "&access_token=" + props.getProperty("ok.api.access_token");
        }
        return res;

    }


    /**
     * Подписывает бота на сообщения
     *
     * @return - Возвращает ответ сервера
     * @throws IOException
     */
    SubscribeResp subscribe() throws IOException {
        Subscribe subs = new Subscribe(props.getProperty("bot.message.endpoint"));
        String content = post(addToken(props.getProperty("ok.api.endpoint.subscribe")), gson.toJson(subs));
        return gson.fromJson(content, SubscribeResp.class);
    }

    /**
     * Получает список подписанных хуков
     *
     * @return - возвращает список подписанных хуков
     * @throws IOException
     */
    SubscribesList getSubscribesList() throws IOException {
        String content = get(addToken(props.getProperty("ok.api.endpoint.subscriptions")));
        return gson.fromJson(content, SubscribesList.class);
    }


    /**
     * Совершает post-запрос на адрес url с параметрами params
     *
     * @param url
     * @param params
     * @return тело ответа
     * @throws IOException
     */
    public String post(String url, String params) {
        logger.info("post request to " + url + "\nparams:\n" + params);
        HttpPost post = new HttpPost(url);
        post.setEntity(new ByteArrayEntity(params.getBytes(), ContentType.APPLICATION_JSON));
        try {
            response = client.execute(post);
            logger.info(response.getStatusLine().toString());
            return IOUtils.toString(response.getEntity().getContent());
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Cовершает get запрос на адрес url
     *
     * @param url
     * @return тело ответа
     * @throws IOException
     */
    public String get(String url) {
        logger.info("get request to " + url);
        HttpGet get = new HttpGet(url);
        try {
            response = client.execute(get);
            logger.info(response.getStatusLine().toString());
            return IOUtils.toString(response.getEntity().getContent());
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * отсылает новое сообение с ответом
     *
     * @param message объект сооббщения
     * @param answer  текст сообщения
     * @throws IOException
     */
    void writeAnswer(MessageResp message, String answer) throws IOException {
        MessageReq newMess = new MessageReq(message.getRecipient().getChatId(), answer);
        post(addToken(props.getProperty("ok.api.endpoint.send") + "/" + message.getRecipient().getChatId()), gson.toJson(newMess, MessageReq.class));
    }


}
