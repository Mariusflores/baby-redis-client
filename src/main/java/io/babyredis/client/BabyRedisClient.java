package io.babyredis.client;

import io.babyredis.error.BabyRedisException;
import io.babyredis.protocol.RespDecoder;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * A simple Redis client that connects to a Baby Redis server and sends commands.
 * It provides methods for common Redis commands like SET, GET, SADD, etc.
 * The client uses a socket connection to communicate with the server and handles the input/output streams for sending commands and reading responses.
 */

public class BabyRedisClient implements AutoCloseable {
    private final Socket s;
    private final PrintWriter out;
    private final BufferedReader reader;

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
            throw new BabyRedisException("Failed to connect to " + host + ":" + port);
        }


    }

    /**
     * Sends a SET command to the Redis server to store a value associated with a key.
     *
     * @param key   key to store the value under
     * @param value value to be stored
     * @return OK if the command was successful, or an error message if the command failed
     */
    public String set(String key, String value) {

        send(String.format("SET %s %s", key, value));
        try {
            return RespDecoder.decodeString(reader);
        } catch (IOException e) {
            throw new BabyRedisException("Error reading server response");
        }
    }

    /**
     * Sends a GET command to the Redis server to retrieve the value associated with a key.
     *
     * @param key key to retrieve the value for
     * @return the value associated with the key
     * @throws BabyRedisException if the key does not exist
     */
    public String get(String key) {

        send(String.format("GET %s", key));
        try {
            return RespDecoder.decodeBulkString(reader);
        } catch (IOException e) {
            throw new BabyRedisException("Error reading server response");
        }
    }

    /**
     * Sends a DELETE command to the Redis server to remove a key and its associated value.
     *
     * @param key key to remove
     * @return OK if the command was successful, or an error message if the key does not exist
     */
    public String delete(String key) {
        send(String.format("DELETE %s", key));

        try {
            return RespDecoder.decodeString(reader);
        } catch (IOException e) {
            throw new BabyRedisException("Error reading server response");
        }
    }

    /**
     * Sends a SADD command to the Redis server to add one or more values to a set associated with a key.
     *
     * @param key    key to which the set is associated
     * @param values values to be added to the set
     * @return OK if the command was successful, or an error message if the key does not exist or is not a set
     */
    public String sAdd(String key, String... values) {
        String args = String.join(" ", values);
        send(String.format("SADD %s %s", key, args));

        try {
            return RespDecoder.decodeString(reader);
        } catch (IOException e) {
            throw new BabyRedisException("Error reading server response");
        }
    }

    /**
     * Sends a SREM command to the Redis server to remove one or more values from a set associated with a key.
     *
     * @param key    key to which the set is associated
     * @param values values to be removed from the set
     * @return OK if the command was successful, or an error message if the key does not exist or is not a set
     */
    public String sRem(String key, String... values) {
        String args = String.join(" ", values);
        send(String.format("SREM %s %s", key, args));

        try {
            return RespDecoder.decodeString(reader);
        } catch (IOException e) {
            throw new BabyRedisException("Error reading server response");
        }
    }

    /**
     * Sends a SISMEMBER command to the Redis server to check if a value is a member of a set associated with a key.
     *
     * @param key   key to which the set is associated
     * @param value value to check for membership in the set
     * @return True if the value is a member of the set, false otherwise
     */
    public boolean sIsMember(String key, String value) {
        send(String.format("SISMEMBER %s %s", key, value));
        try {
            return RespDecoder.decodeInteger(reader) == 1;
        } catch (IOException e) {
            throw new BabyRedisException("Error reading server response");
        }
    }

    /**
     * Sends a SMEMBERS command to the Redis server to retrieve all members of a set associated with a key.
     *
     * @param key key to which the set is associated
     * @return an array of all members in the set, or an empty array if the set does not exist or is empty
     */
    public String[] sMembers(String key) {
        send(String.format("SMEMBERS %s", key));

        try {
            return RespDecoder.decodeArray(reader);
        } catch (IOException e) {
            throw new BabyRedisException("Error reading server response");
        }

    }

    /**
     * Sends an EXPIRE command to the Redis server to set a timeout on a key, after which the key will be automatically deleted.
     *
     * @param key     key to set the expiration on
     * @param seconds number of seconds until the key expires
     * @return 1 if the timeout was set successfully
     */
    public int expire(String key, int seconds) {
        send(String.format("EXPIRE %s %d", key, seconds));

        try {
            return RespDecoder.decodeInteger(reader);
        } catch (IOException e) {
            throw new BabyRedisException("Error reading server response");
        }
    }

    /**
     * Sends a TTL command to the Redis server to retrieve the remaining time to live of a key that has an expiration set.
     *
     * @param key key to check the TTL for
     * @return the remaining time to live in seconds, or -1 if the key does not
     * have an expiration, or -2 if the key does not exist
     */
    public int ttl(String key) {
        send(String.format("TTL %s", key));

        try {
            return RespDecoder.decodeInteger(reader);
        } catch (IOException e) {
            throw new BabyRedisException("Error reading server response");
        }
    }

    public String ping() {
        send("PING");

        try {
            return RespDecoder.decodeString(reader);
        } catch (IOException e) {
            throw new BabyRedisException("Error reading server response");
        }
    }

    /**
     * Sends a command to the Redis server and returns the response.
     * This method is used internally by the other command methods to send the appropriate command string to the server
     * and read the response.
     *
     * @param command the command string to send to the server
     */
    private void send(String command) {
        out.println(command);
    }

    // Method for sending Raw strings for CLI
    public String sendRaw(String command) {
        send(command);
        try {
            return reader.readLine();
        } catch (IOException e) {
            throw new BabyRedisException("Error reading server response");
        }
    }

    // Closes the connection to the Redis server and releases any resources associated with the client. This method should be called when the client is no longer needed to ensure that the socket and streams are properly closed.
    @Override
    public void close() {
        try {
            reader.close();
            out.close();
            s.close();
        } catch (IOException e) {
            throw new BabyRedisException("Error closing connection");
        }

    }
}
