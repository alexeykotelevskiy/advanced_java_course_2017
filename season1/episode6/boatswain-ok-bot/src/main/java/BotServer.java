import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import Solver.Solver;
import Solver.ParseExceprion;
import response.MessageResp;



class BotServer {
    private static final Logger logger = Logger.getLogger(BotServer.class.getName());
    private Gson gson = new Gson();
    private final HttpServer httpServer;
    private Solver solver = new Solver();

    BotServer(String endPoint, ApiClient api) throws IOException {
        InetSocketAddress addr = new InetSocketAddress(5555);
        httpServer = HttpServer.create(addr, 0);
        httpServer.createContext(endPoint, httpExchange -> {
            try (InputStream is = httpExchange.getRequestBody()) {
                MessageResp message = new MessageResp();
                message = gson.fromJson(IOUtils.toString(is), MessageResp.class);
                logger.info("message notificated ");
                String answer;
                try {
                    double s = solver.evaluate(message.getMessage().getText());
                    answer = Double.toString(s);

                } catch (ParseExceprion | ArithmeticException e) {
                    answer = e.getMessage();
                }
                writeResponse(httpExchange, 200, "ok");
                api.writeAnswer(message, answer);
            } catch (IOException e) {
                logger.warning("error read message notificated");
                writeResponse(httpExchange, 500, "failure");
            }

        });
    }

    private void writeResponse(HttpExchange exchange, int status, String content) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "text/html; charset=utf-8");
        byte[] response = content.getBytes();
        exchange.sendResponseHeaders(status, response.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response);
        }
    }

    void start() {
        httpServer.start();
    }
}
