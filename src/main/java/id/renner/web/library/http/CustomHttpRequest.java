package id.renner.web.library.http;

import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

public class CustomHttpRequest {
    private final HttpExchange exchange;
    private final Map<String, String> pathElements;
    private final Map<String, String> queryParameters;

    public CustomHttpRequest(HttpExchange httpExchange) {
        this.exchange = httpExchange;
        this.pathElements = new HashMap<>();
        this.queryParameters = new HashMap<>();

        init();
    }

    private void init() {
        String queries = exchange.getRequestURI().getQuery();

        if (queries != null) {
            StringTokenizer queryTokens = new StringTokenizer(queries, "&");
            while (queryTokens.hasMoreElements()) {
                String queryToken = queryTokens.nextToken();
                String[] queryTokenKeyValue = queryToken.split("=");
                queryParameters.put(queryTokenKeyValue[0], queryTokenKeyValue[1]);
            }
        }
    }

    public void putPathElement(String key, String value) {
        pathElements.put(key, value);
    }

    public String getPathElement(String key) {
        return pathElements.get(key);
    }

    public String getPath() {
        return exchange.getRequestURI().getPath();
    }

    public String getMethod() {
        return exchange.getRequestMethod();
    }

    public String getQueryParameterOrDefault(String key, String defaultValue) {
        return queryParameters.getOrDefault(key, defaultValue);
    }

    public String getBody() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))) {
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private OutputStream getOutputStream() {
        return exchange.getResponseBody();
    }

    private void sendResponseHeaders(int code, int messageLength) throws IOException {
        exchange.sendResponseHeaders(code, messageLength);
    }

    public void sendResponse(String message, int code) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(getOutputStream()))) {
            sendResponseHeaders(code, message.length());
            writer.append(message);
        } catch (IOException ex) {
            throw new RuntimeException("failed when trying to respond to request");
        }
    }
}