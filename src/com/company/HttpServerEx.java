package com.company;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.*;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.ssl.internal.ssl.Provider;

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
        String a = "C:" + exc.getRequestURI().getPath();
        File myFolder = new File(a);

        out.write("<html><body>");

        if (myFolder.isDirectory()) {
            File[] directory = myFolder.listFiles();
            for (int i = 0; i < directory.length; i++) {
                out.write("<a href=\"http://127.0.0.1:8080//" + directory[i] + "\">" + directory[i] + "</a><br>");
                out.flush();
            }
            out.write("</body></html>");
        } else if (myFolder.isFile()) {
            File[] files = myFolder.listFiles();
            for (int j = 0; j < files.length; j++) {
                if (a.equals(files[j].getAbsolutePath())) {
                    out.write("<a href=\"http://127.0.0.1:8080//" + a + "\"download>" + a + "</a><br>");
                }
                out.write("<a href=\"http://127.0.0.1:8080//" + files[j] + "\"download>" + files[j] + "</a><br>");
                out.flush();
            }
            out.write("</body></html>");
        }
        System.out.println(a);
        // out.close();
    }
}