package com.company;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.*;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class HttpServerEx implements HttpHandler {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/", new HttpServerEx());
        server.start();
        System.out.println("Server started\nPress any key to stop...");
        System.in.read();
        server.stop(0);
        System.out.println("Server stoped");
    }

    @Override
    public void handle(HttpExchange exc) throws IOException {

        exc.sendResponseHeaders(200, 0);
        PrintWriter out = new PrintWriter(exc.getResponseBody());
        File myFolder = new File("C:\\Program Files\\Notepad++");
        File[] files = myFolder.listFiles();
        for (int i = 0; i < files.length; i++) {
            out.println("<html><body>" +
                    "<a href=\"http://127.0.0.1:8080/\\" + files[i] + "\">" + files[i] + "</a>" +
                    "</body></html>" + "<br>");
            out.flush();
        }
        for (Map.Entry<String, List<String>> o : exc.getRequestHeaders().entrySet()) {
            if (o.getKey().equals("Referer")) {
                System.out.println(o.getValue());
            }
        }
        out.close();
        exc.close();

    }
}