import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * The Client class will send a packet to the intermediate host and then
 * waits for a response
 */
public class Client {
    private DatagramPacket sendPacket, receivePacket;
    private DatagramSocket sendReceiveSocket;
    private String packetString;

    public static final int REQUESTSIZE = 17;
    public static final int RESPONSESIZE = 4;

    /**
     * Constructor for class Client that initializes the DatagramSocket
     */
    public Client() {
        try {
            // Construct a datagram socket and bind it to any available
            // port on the local host machine. This socket will be used to
            // send and receive UDP Datagram packets.
            sendReceiveSocket = new DatagramSocket();
        } catch (SocketException se) {   // Can't create the socket.
            se.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * The following method sends a message to the intermediate host then
     * waits for a response 11 times
     */
    public void sendReceive()
    {
        byte[] msg;
        for(int i = 1; i < 12; i++) { //send 11 requests

            System.out.println("                  Client: Sending message number: " + i + "\n");

                          /* Send the message to the intermediate host */
            if(i == 11) { //send invalid request
                packetString = "ErrorMessage!!!!!";
                msg = packetString.getBytes(); //convert the string into bytes

            } else if (i % 2 == 0) { // even number - write
                //create byte array of the write request
                msg = ByteArrayInserter.create((byte) 2, "test.txt", "octet");

            } else {  //odd number - read
                //create byte array of the read request
                msg = ByteArrayInserter.create((byte) 1, "test.txt", "octet");
            }

            // wait for the host and server to finish setting up
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.exit(1);
            }

            /* Create the packet to be sent */
            try {
                sendPacket = new DatagramPacket(msg, msg.length,
                        InetAddress.getLocalHost(), 23);
            } catch (UnknownHostException e) {
                e.printStackTrace();
                System.exit(1);
            }

            /* Print information about packet to be sent */
            String packetInfo = "Client: Sending packet: \n" +
                    "To host: " + sendPacket.getAddress() + "\n" +
                    "Destination host port: " + sendPacket.getPort() + "\n" +
                    "Length: " + sendPacket.getLength();
            System.out.println(packetInfo);
            if (i == 11) {
                System.out.println(new String(msg, StandardCharsets.UTF_8));
            }
            else {
                ByteArrayInserter.print(msg); // print string and byte representation of request
            }

            /* Send the packet */
            try {
                sendReceiveSocket.send(sendPacket);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }

            System.out.println("Client: Packet sent.\n");

            /* Receive the packet sent by the intermediate host */

            if(i == 11) { //invalid request, nothing to be received
                // We're finished, so close the socket.
                sendReceiveSocket.close();
                return;
            }
            // create the received packet
            byte data[] = new byte[Client.RESPONSESIZE];
            receivePacket = new DatagramPacket(data, data.length);

            // wait to receive the packet
            try {
                // Block until a datagram is received via sendReceiveSocket.
                sendReceiveSocket.receive(receivePacket);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }

            // Process the received datagram.
            packetInfo = "Client: Packet received: \n" +
                    "From host: " + receivePacket.getAddress() + "\n" +
                    "Host port: " + receivePacket.getPort() + "\n" +
                    "Length: " + receivePacket.getLength();
            System.out.println(packetInfo);
            System.out.println("Contents: " + ByteArrayInserter.byteArraytoString(receivePacket.getData()));

            System.out.println("Client: Host Packet received.\n");

        }
        // We're finished, so close the socket.
        sendReceiveSocket.close();
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.sendReceive();
    }
}
