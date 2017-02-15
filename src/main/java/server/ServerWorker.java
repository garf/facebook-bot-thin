//package server;
//
//import dto.socket.ClientMessage;
//import libs.helpers.Config;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.utils.URLEncodedUtils;
//
//import java.io.*;
//import java.net.Socket;
//import java.net.URI;
//import java.net.URISyntaxException;
//import java.util.List;
//
//public class ServerWorker implements Runnable {
//    private Socket client = null;
//
//    public ServerWorker(Socket client) {
//        this.client = client;
//        System.out.println("New connection from: " + client.getInetAddress().getHostName());
//    }
//
//    @Override
//    public void run() {
//        ClientMessage clientMessage;
//        OutputStream out;
//        InputStream in;
//        System.out.println("1");
//        while (true) {
//            try {
//                Config config  = new Config("./config.properties");
//                System.out.println("2");
////                FacebookMessengerClient client = new FacebookMessengerClient();
//                System.out.println("3");
//
//                out = this.client.getOutputStream();
//                in = this.client.getInputStream();
//
//                BufferedOutputStream bos = new BufferedOutputStream(out);
//
//                System.out.println("4");
//
//                StringWriter writer = new StringWriter();
//                String encoding = "UTF-8";
//                IOUtils.copy(in, writer, encoding);
//                System.out.println("5");
//                String requestJson = writer.toString();
//                System.out.println(requestJson);
////                Callback fbCallback = client.deserializeCallback(requestJson);
////                System.out.println(fbCallback.toString());
//            } catch (IOException e) {
//                System.out.println(String.format("Read failed: %s", e.getMessage()));
//            }
//        }
//    }
//
//    private String getUrlParam(String url, String key) {
//        List<NameValuePair> params;
//        try {
//            params = URLEncodedUtils.parse(new URI(url), "UTF-8");
//
//            for (NameValuePair param : params) {
//                if (param.getName().equals(key)) {
//                    return param.getValue();
//                }
//            }
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//
//
//        return null;
//    }
//}
