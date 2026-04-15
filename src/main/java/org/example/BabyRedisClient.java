package org.example;


import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class BabyRedisClient {
    private Socket s;
    private PrintWriter out;
    private BufferedReader reader;

    public BabyRedisClient (String host, int port){
            try {
                s = new Socket(host, port);
                out = new PrintWriter(
                        new OutputStreamWriter(s.getOutputStream(), StandardCharsets.UTF_8), true
                );
                reader = new BufferedReader(
                        new InputStreamReader(s.getInputStream(),StandardCharsets.UTF_8)
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }



    }

    public String set(String key, String value){
        return send(String.format("SET %s %s", key, value));

    }
    public String get(String key){

        return send(String.format("GET %s", key));

    }
    public String delete(String key){
        return send(String.format("DELETE %s", key));


    }
    public String sAdd(String key, String... values){
        String args = String.join(" ", values);
        return send(String.format("SADD %s %s", key, args));


    }
    public String sRem(String key, String... values){
        String args = String.join(" ", values);
        return send(String.format("SREM %s %s", key, args));


    }
    public String sIsMember(String key, String value){
        return send(String.format("SISMEMBER %s %s", key, value));

    }
    public String sMembers(String key){
        return send(String.format("SMEMBERS %s", key));


    }
    public String expire(String key, int seconds){
        return send(String.format("EXPIRE %s %d", key, seconds));


    }
    public String ttl(String key){
        return send(String.format("TTL %s", key));
    }

    public String send(String command){
        out.println(command);
        return read();
    }
    private String read(){
        try {
            return reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void close(){
        try{
            s.close();
            out.close();
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
