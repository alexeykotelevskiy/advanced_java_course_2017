
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;
import response.SubscribeResp;
import response.SubscribesList;

public class AriphmeticBot {
    public static void main(String[] args) throws IOException {
        Logger logger = Logger.getLogger(AriphmeticBot.class.getName());
        logger.info("Start application...");
        Properties props = new Properties();
        ApiClient client = new ApiClient();
        props.load(ApiClient.class.getResourceAsStream("/application.properties"));
        //Получили список подписок
        SubscribesList subscribesList = client.getSubscribesList();
        //Проверим, подписаны мы или нет
        if (!checkSubscribes(subscribesList, props.getProperty("bot.message.endpoint"))) {
            //если не подписаны, то подписываемся
            SubscribeResp resp = client.subscribe();
            if (resp.getSuccess()) {
                logger.info("Success subscribe");
            } else
            {
                logger.warning("Unsuccess subscribe");
            }
        } else {
            logger.info("Already subscribed");
        }

        try {
            BotServer server = new BotServer(props.getProperty("bot.local.endpoint"), client);
            logger.info("Server created");
            server.start();

        } catch (IOException e) {
            logger.warning("Server not created");
        }


    }

    /**
     * Проверяет наличие хука hook в списке list
     *
     * @param list Список хуков
     * @param hook хук, который ищем
     * @return возврщает true если хук hook присутствует в списке list и false в противном случае
     */
    private static boolean checkSubscribes(SubscribesList list, String hook) {
        if (list == null) {
            return false;
        }
        List<SubscribesList.Subscription> listSubscribes = list.getSubscriptions();
        if (listSubscribes.isEmpty()) {
            return false;
        }
        for (SubscribesList.Subscription subscription : listSubscribes) {
            if (subscription.getUrl().equals(hook)) {
                return true;
            }
        }
        return false;
    }

}
