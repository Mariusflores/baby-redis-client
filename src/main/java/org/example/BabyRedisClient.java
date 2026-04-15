package org.example;


import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * A simple Redis client that connects to a Baby Redis server and sends commands.
 * It provides methods for common Redis commands like SET, GET, SADD, etc.
 * The client uses a socket connection to communicate with the server and handles the input/output streams for sending commands and reading responses.
 */

public class BabyRedisClient {
    private Socket s;
    private PrintWriter out;
    private BufferedReader reader;

    /**
     * Creates a new BabyRedisClient instance and connects to the Redis server.
     *
     * @param host The hostname or IP address of the Redis server.
     * @param port The port number of the Redis server.
     */
    public BabyRedisClient(String host, int port) {
        try {
            s = new Socket(host, port);
            out = new PrintWriter(
                    new OutputStreamWriter(s.getOutputStream(), StandardCharsets.UTF_8), true
            );
            reader = new BufferedReader(
                    new InputStreamReader(s.getInputStream(), StandardCharsets.UTF_8)
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    /**
     * Sends a SET command to the Redis server to store a value associated with a key.
     *
     * @param key
     * @param value
     * @return
     */
    public String set(String key, String value) {
        return send(String.format("SET %s %s", key, value));

    }

    /**
     * Sends a GET command to the Redis server to retrieve the value associated with a key.
     *
     * @param key
     * @return
     */
    public String get(String key) {

        return send(String.format("GET %s", key));

    }

    /**
     * Sends a DELETE command to the Redis server to remove a key and its associated value.
     *
     * @param key
     * @return
     */
    public String delete(String key) {
        return send(String.format("DELETE %s", key));


    }

    /**
     * Sends a SADD command to the Redis server to add one or more values to a set associated with a key.
     *
     * @param key
     * @param values
     * @return
     */
    public String sAdd(String key, String... values) {
        String args = String.join(" ", values);
        return send(String.format("SADD %s %s", key, args));


    }

    /**
     * Sends a SREM command to the Redis server to remove one or more values from a set associated with a key.
     *
     * @param key
     * @param values
     * @return
     */
    public String sRem(String key, String... values) {
        String args = String.join(" ", values);
        return send(String.format("SREM %s %s", key, args));


    }

    /**
     * Sends a SISMEMBER command to the Redis server to check if a value is a member of a set associated with a key.
     *
     * @param key
     * @param value
     * @return
     */
    public String sIsMember(String key, String value) {
        return send(String.format("SISMEMBER %s %s", key, value));

    }

    /**
     * Sends a SMEMBERS command to the Redis server to retrieve all members of a set associated with a key.
     *
     * @param key
     * @return
     */
    public String sMembers(String key) {
        return send(String.format("SMEMBERS %s", key));


    }

    /**
     * Sends an EXPIRE command to the Redis server to set a timeout on a key, after which the key will be automatically deleted.
     *
     * @param key
     * @param seconds
     * @return
     */
    public String expire(String key, int seconds) {
        return send(String.format("EXPIRE %s %d", key, seconds));


    }

    /**
     * Sends a TTL command to the Redis server to retrieve the remaining time to live of a key that has an expiration set.
     *
     * @param key
     * @return
     */
    public String ttl(String key) {
        return send(String.format("TTL %s", key));
    }

    /**
     * Sends a command to the Redis server and returns the response. This method is used internally by the other command methods to send the appropriate command string to the server and read the response.
     *
     * @param command
     * @return
     */

    public String send(String command) {
        out.println(command);
        return read();
    }

    // Helper method. Returns the Output from the Server
    private String read() {
        try {
            return reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    // Closes open connections
    public void close() {
        try {
            s.close();
            out.close();
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
