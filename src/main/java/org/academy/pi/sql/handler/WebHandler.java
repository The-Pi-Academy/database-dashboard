package org.academy.pi.sql.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class WebHandler implements HttpHandler {

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    String path = exchange.getRequestURI().getPath();
    if (path.equals("/")) {
      path = "/index.html";
    }

    try {
      // Adjust path to your web directory
      String filePath = "src/main/web" + path;
      byte[] content = Files.readAllBytes(Paths.get(filePath));

      // Set content type
      String contentType = getContentType(path);
      exchange.getResponseHeaders().set("Content-Type", contentType);

      exchange.sendResponseHeaders(200, content.length);
      exchange.getResponseBody().write(content);
      exchange.getResponseBody().close();
    } catch (IOException e) {
      String response = "404 Not Found";
      exchange.sendResponseHeaders(404, response.length());
      exchange.getResponseBody().write(response.getBytes());
      exchange.getResponseBody().close();
    }
  }

  private String getContentType(String path) {
    if (path.endsWith(".html")) {
      return "text/html";
    }
    if (path.endsWith(".css")) {
      return "text/css";
    }
    if (path.endsWith(".js")) {
      return "application/javascript";
    }
    return "text/plain";
  }
}
