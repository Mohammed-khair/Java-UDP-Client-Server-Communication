import java.io.*;
import java.net.*;
/**
 * The Client class will send a packet to the intermediate host and then
 * waits for a response
 */
public class Client implements Runnable{
    private DatagramPacket sendPacket, receivePacket;
    private DatagramSocket sendReceiveSocket;
    private String packetString;

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
    public void run()
    {
        for(int i = 1; i < 11; i++) {
            System.out.println(" Client: Sending message number: " + i);

                          /* Send the message to the intermediate host */

            if (i % 2 == 0) { // even number - write
                packetString = "0 2 test.txt 0 octet 0";
            } else {          //odd number - read
                packetString = "0 1 test.txt 0 octet 0";
            }
            byte msg[] = packetString.getBytes(); //convert the string into bytes

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
                    "Length: " + sendPacket.getLength() + "\n" +
                    "Containing: " + new String(sendPacket.getData(), 0, sendPacket.getLength()) + "\n";
            System.out.println(packetInfo);

            /* Send the packet */
            try {
                sendReceiveSocket.send(sendPacket);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }

            System.out.println("Client: Packet sent.\n");

            /* Receive the packet sent by the intermediate host */

            // create the received packet
            byte data[] = new byte[100];
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
                    "Length: " + receivePacket.getLength() + "\n" +
                    "Containing: " + new String(data, 0, receivePacket.getLength()) + "\n";
            System.out.println(packetInfo);
            System.out.println("Client: Host Packet received.\n");

            // We're finished, so close the socket.
            sendReceiveSocket.close();
        }
    }


}
