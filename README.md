# UDP-IP
# UDP Client-Server Communication

This project demonstrates a simple UDP client-server communication using three Java classes: `Client`, `IntermediateHost`, and `Server`. The communication involves sending a message from the client to the intermediate host, then from the intermediate host to the server, and finally, the server sends a response back to the intermediate host and subsequently to the client.

## Client

The `Client` class represents the client-side of the communication. It sends a message to the intermediate host and waits for a response. The `Client` class implements the `Runnable` interface to be executed as a separate thread.

## IntermediateHost

The `IntermediateHost` class acts as an intermediate node between the client and the server. It receives a message from the client, passes it to the server, waits for the response from the server, and then sends the response back to the client. Similar to the `Client`, the `IntermediateHost` class also implements the `Runnable` interface.

## Server

The `Server` class is responsible for handling the final destination of the communication. It receives a message from the intermediate host, processes it, and then sends a response back to the intermediate host. The `Server` class also implements the `Runnable` interface to be executed in a separate thread.

## How the Communication Works

1. The `Client` sends a message "Anyone there?" to the intermediate host.

2. The `IntermediateHost` receives the message from the client, prints the received data, and then sends the message to the server.

3. The `Server` receives the message from the intermediate host, processes it (simulated with a 5-second delay), and then sends a response back to the intermediate host.

4. The `IntermediateHost` receives the response from the server, prints the received data, and then sends it back to the client.

5. The `Client` receives the response from the intermediate host and prints the received data.

## Execution

To run the communication process, follow these steps:

1. Run the `Server` class.
2. Run the `IntermediateHost` class.
3. Run the `Client` class.

## Note

This is a simplified example to demonstrate UDP communication using Java. In a real-world scenario, error handling, packet loss, and other considerations would need to be addressed.
