package com.company;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.Stack;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class HttpServerEx implements HttpHandler {
    public static Stack<File> directoryStack = new Stack<File>();
    public static File homePage = new File("C:\\");
    String loopAddress = "\"http://127.0.0.1:8080//";
    String localAddress = "\"http://192.168.0.102:8080//";
    String downloadMessage = "<b>For download absolute file click me \\t&#x2714; </b><br>";


    public static void main(String[] args) throws IOException {
        File startFile = new File("C:\\");
        directoryStack.push(startFile);
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
        out.write(("<a href=" + loopAddress + homePage + "\">" + "<b>&#127968</b>" + "</a><br>").getBytes());
        if (myFolder.isDirectory()) {
            File[] directory = myFolder.listFiles();
            for (int i = 0; i < directory.length; i++) {
                out.write(("<a href=" + loopAddress + directory[i] + "\">" + directory[i] + "</a><br>").getBytes());
            }
            out.write("</body></html>".getBytes());
            out.close();
            directoryStack.push(myFolder);

        } else if (myFolder.isFile()) {
            out.write(("<a href=" + loopAddress + a + "\"download>" + downloadMessage + "</a><br><br>").getBytes());
            out.write("</body></html>".getBytes());
            InputStream fileInput = new BufferedInputStream(new FileInputStream(myFolder));
            byte[] buf = new byte[4096];
            int count;
            out.write("<br><b>Text representation of file: </b><br>".getBytes());
            while ((count = fileInput.read(buf)) >= 0) {
                out.write(buf, 0, count);
            }
            out.close();
            //directoryStack.push(myFolder);
        }
        System.out.println(a);
    }
}
