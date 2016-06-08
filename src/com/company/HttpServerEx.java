package com.company;

import java.io.*;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class HttpServerEx implements HttpHandler {

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/", new HttpServerEx());
        server.start();
    }

    @Override
    public void handle(HttpExchange exc) throws IOException {
        exc.sendResponseHeaders(200, 0);
        OutputStream out = exc.getResponseBody();
        String a = "C:" + exc.getRequestURI().getPath();
        File myFolder = new File(a);
        out.write("<html><body>".getBytes());


        if (myFolder.isDirectory()) {
            File[] directory = myFolder.listFiles();
            for (int i = 0; i < directory.length; i++) {
                out.write(("<a href=\"http://127.0.0.1:8080//" + directory[i] + "\">" + directory[i] + "</a><br>").getBytes());
            }
            out.write("</body></html>".getBytes());
            out.close();
        } else if (myFolder.isFile()) {
            out.write(("<a href=\"http://127.0.0.1:8080//" + a + "\"download>" + "<b>For download absolute file click me \t&#x2714; </b><br>"+ "</a><br><br>").getBytes());
            out.write("</body></html>".getBytes());
            InputStream fileInput = new BufferedInputStream(new FileInputStream(myFolder));
            byte[] buf = new byte[4096];
            int count;
            out.write("<br><b>Text representation of file: </b><br>".getBytes());
            while ((count = fileInput.read(buf)) >= 0) {
                out.write(buf, 0, count);
            }
            out.close();
        }
        System.out.println(a);
    }
}