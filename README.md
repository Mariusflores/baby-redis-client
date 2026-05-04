# Baby Redis Client

A standalone Java library for connecting to and communicating with
[baby-redis](https://github.com/mariusflores/baby-redis) — a from-scratch
Redis-inspired key-value store.

Handles socket connections, request serialization, and response parsing
using baby-redis's custom wire protocol and RESP decoding.

## Recent Changes

- **0.3.0**: Added `flush` and `keys` commands to the client API.
- **0.2.0**: Added RESP decoding and specific return types for methods (e.g., `boolean`, `String[]`, `int`).

## Status

🚧 **In active development.** API may change as the baby-redis protocol evolves.

## Installation

### Prerequisites

- [baby-redis-protocol](https://github.com/mariusflores/baby-redis-protocol) installed locally

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

        System.out.println(client.keys("*")); // → [greeting, fruits]
        client.flush(); // flushes all keys
        client.close();
    }
}
```

## API

| Method                        | Return Type | Description                             |
|-------------------------------|-------------|-----------------------------------------|
| `BabyRedisClient(host, port)` |             | Connects to the server on construction  |
| `set(key, value)`             | `String`    | Store a string value                    |
| `get(key)`                    | `String`    | Retrieve a string value                 |
| `delete(key)`                 | `String`    | Delete a key                            |
| `sAdd(key, values...)`        | `String`    | Add one or more members to a set        |
| `sRem(key, values...)`        | `String`    | Remove one or more members from a set   |
| `sIsMember(key, value)`       | `boolean`   | Check if a value is a member of the set |
| `sMembers(key)`               | `String[]`  | Retrieve all members of the set         |
| `expire(key, seconds)`        | `int`       | Set a TTL on a key                      |
| `ttl(key)`                    | `int`       | Get remaining TTL for a key             |
| `keys(pattern)`               | `String[]`  | List all keys matching a pattern        |
| `flush()`                     | `String`    | Remove all keys from the database       |
| `ping()`                      | `String`    | Ping the server                         |
| `sendRaw(command)`            | `String`    | Send a raw command string (for CLI use) |
| `close()`                     |             | Close the connection                    |

## Requirements

- Java 21+
- A running baby-redis server (see [baby-redis](https://github.com/mariusflores/baby-redis))

## Related

- [baby-redis](https://github.com/mariusflores/baby-redis) — the server this library talks to
- [baby-redis-cli](https://github.com/mariusflores/baby-redis-cli) — Devtool using this library to connect to server and
  perform command line operations
- [baby-redis-protocol](https://github.com/mariusflores/baby-redis-protocol) — shared RESP protocol library
