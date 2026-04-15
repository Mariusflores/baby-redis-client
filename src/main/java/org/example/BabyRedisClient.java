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
        send(String.format("SET %s %s", key, value));
        return read();
    }
    public String get(String key){

        send(String.format("GET %s", key));
        return read();
    }
    public String delete(String key){
        send(String.format("DELETE %s", key));
        return read();

    }
    public String sAdd(String key, String... values){
        String args = String.join(" ", values);
        send(String.format("SADD %s %s", key, args));
        return read();

    }
    public String sRem(String key, String... values){
        String args = String.join(" ", values);
        send(String.format("SREM %s %s", key, args));
        return read();

    }
    public String sIsMember(String key, String value){
        send(String.format("SISMEMBER %s %s", key, value));
        return read();
    }
    public String sMembers(String key){
        send(String.format("SMEMBERS %s", key));
        return read();

    }
    public String expire(String key, String seconds){
        send(String.format("EXPIRE %s %s", key, seconds));
        return read();

    }
    public String ttl(String key){
        send(String.format("TTL %s", key));

        return read();
    }

    private void send(String command){
        out.println(command);
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
