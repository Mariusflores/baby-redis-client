# Baby Redis Client
A standalone Java library for connecting to and communicating with 
[baby-redis](https://github.com/mariusflores/baby-redis) — a from-scratch 
Redis-inspired key-value store.

Handles socket connections, request serialization, and response parsing 
using baby-redis's custom wire protocol.

## Status

🚧 **In active development.** API may change as the baby-redis protocol evolves.

## Installation

Not yet published to Maven Central. Build locally and install to your 
local Maven repository:

```bash
git clone https://github.com/mariusflores/baby-redis-client.git
cd baby-redis-client
mvn clean install
```

Then add it as a dependency in your project:

```xml

    io.babyredis
    baby-redis-client
    0.1.0

```
## Usage

```java
import io.babyredis.client.BabyRedisClient;

public class Example {
    public static void main(String[] args) {
        BabyRedisClient client = new BabyRedisClient("localhost", 6379);

        client.set("greeting", "hello");
        System.out.println(client.get("greeting"));  // → hello

        client.sAdd("fruits", "apple", "banana", "cherry");
        System.out.println(client.sMembers("fruits"));

        client.expire("greeting", 60);
        System.out.println(client.ttl("greeting"));

        client.close();
    }
}
```


## API

| Method | Description |
|--------|-------------|
| `BabyRedisClient(host, port)` | Connects to the server on construction |
| `set(key, value)` | Store a string value |
| `get(key)` | Retrieve a string value |
| `delete(key)` | Delete a key |
| `sAdd(key, values...)` | Add one or more members to a set |
| `sRem(key, values...)` | Remove one or more members from a set |
| `sIsMember(key, value)` | Check if a value is a member of the set |
| `sMembers(key)` | Retrieve all members of the set |
| `expire(key, seconds)` | Set a TTL on a key |
| `ttl(key)` | Get remaining TTL for a key |
| `send(command)` | Send a raw command string (for advanced use) |
| `close()` | Close the connection and release resources |

All methods return the server's response as a `String`.


## Requirements

- Java 21+
- A running baby-redis server (see [baby-redis](https://github.com/mariusflores/baby-redis))

## Related

- [baby-redis](https://github.com/mariusflores/baby-redis) — the server this library talks to
- [baby-redis-cli](https://github.com/mariusflores/baby-redis-cli) — Devtool using this library to connect to server and perform command line operations
