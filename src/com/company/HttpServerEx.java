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
        System.out.println("Server started\nPress any key to stop...");
        System.in.read();
        server.stop(0);
        System.out.println("Server stoped");
    }

    @Override
    public void handle(HttpExchange exc) throws IOException {
        exc.sendResponseHeaders(200, 0);
        //ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStream out = exc.getResponseBody();
        //PrintWriter out = new PrintWriter(exc.getResponseBody());
        String a = "C:" + exc.getRequestURI().getPath();
        File myFolder = new File(a);
        out.write("<html><body>".getBytes());

        if (myFolder.isDirectory()) {
            File[] directory = myFolder.listFiles();
            for (int i = 0; i < directory.length; i++) {
                out.write(("<a href=\"http://127.0.0.1:8080//" + directory[i] + "\">" + directory[i] + "</a><br>").getBytes());
                out.flush();
            }
            out.write("</body></html>".getBytes());
            out.close();
        } else if (myFolder.isFile()) {
            out.write(("<a href=\"http://127.0.0.1:8080//" + a + "\"download>" + a + "</a><br>").getBytes());
            out.flush();
            out.write("</body></html>".getBytes());
            InputStream fileInput = new BufferedInputStream(new FileInputStream(myFolder));
            byte[] buf = new byte[4096];
            int count;
            DataOutputStream dos = new DataOutputStream(out);
            while ((count = fileInput.read(buf)) >= 0) {
                dos.write(buf, 0, count);
            }
            out.flush();
            out.close();


        }
        System.out.println(a);
    }
}