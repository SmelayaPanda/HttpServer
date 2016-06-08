package com.company;

import java.io.*;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class HttpServerEx implements HttpHandler {
    public static File homePage = new File("C:\\");
    String localAddress = "\"http://192.168.0.102:8080//";//loopAddress = "\"http://127.0.0.1:8080//"
    String downloadMessage = "<b>For download absolute file click me &#x2714; </b><br>";

    private static String getFileSizeKiloBytes(File file) {
        return (double) file.length() / (1024) + " kb";
    }

    public long getFileSize(File folder) {
        long foldersize = 0;
        File[] filelist = folder.listFiles();
        for (int i = 0; i < filelist.length; i++) {
            if (filelist[i].isDirectory()) {
                foldersize += getFileSize(filelist[i]);
            } else {
                foldersize += filelist[i].length();
            }
        }
        return foldersize;
    }

    public static void main(String[] args) throws IOException {
        File startFile = new File("C:\\");
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
        out.write("WELCOME. You are on a Panda PC!=)<br>".getBytes());
        out.write("<img src=\"http://kungfupanda3.ru/sites/default/files/kung-fu-panda-3-online.jpg\" alt=\"Mountain View\" style=\"width:304px;height:228px;\">".getBytes());
        out.write(("<br><a href=" + localAddress + homePage + "\">" + "HOME: <b>&#127968</b>" + "</a><br>").getBytes());
        if (myFolder.isDirectory()) {
            File[] directory = myFolder.listFiles();
            for (int i = 0; i < directory.length; i++) {
                out.write(("<a href=" + localAddress + directory[i] + "\">" + directory[i] + "</a><br>").getBytes());
            }
            out.write("</body></html>".getBytes());
            out.close();
        } else if (myFolder.isFile()) {
            Long lastModified = myFolder.lastModified();
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            out.write(("<a href=" + localAddress + a + "\"download>" + downloadMessage + "  " + getFileSizeKiloBytes(myFolder) + "<br>" + sdf.format(new Date(lastModified)) + "</a><br><br>").getBytes());
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
