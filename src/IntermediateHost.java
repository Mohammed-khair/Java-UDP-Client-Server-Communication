import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * The IntermediateHost class will receive a message from the client and
 * pass it to the server. Later, the IntermediateHost  waits for a response from
 * the server and then send it back to the client
 *
 */
public class IntermediateHost {
    private DatagramPacket sendClientPacket, receiveClientPacket, sendServerPacket, receiveServerPacket;
    private DatagramSocket sendSocket, receiveSocket;

    /**
     * Constructor for class IntermediateHost that initializes the DatagramSocket
     */
    public IntermediateHost()
    {
        try {
            // Construct a datagram socket and bind it to any available
            // port on the local host machine. This socket will be used to
            // send UDP Datagram packets.
            sendSocket = new DatagramSocket();

            // Construct a datagram socket and bind it to port 5000
            // on the local host machine. This socket will be used to
            // receive UDP Datagram packets.
            receiveSocket = new DatagramSocket(23);

            //receiveSocket.setSoTimeout(10000);

        } catch (SocketException se) {
            se.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * This method will receive a message from the client and
     * pass it to the server. Later, the IntermediateHost  waits for a response from
     * the server and then send it back to the client
     */
    public void sendReceive()
    {
        for(int i = 1; i < 12; i++) {

            /* Receive packet from the client */

            // Construct a DatagramPacket for receiving packets up from client

            byte data[] = new byte[Client.REQUESTSIZE];
            receiveClientPacket = new DatagramPacket(data, data.length);

            System.out.println("Host: Waiting for Client Packet.\n");

            // Block until a datagram packet is received from receiveSocket.
            try {
                System.out.println("Host: Waiting for Client packet...\n"); // so we know we're waiting
                receiveSocket.receive(receiveClientPacket);
            } catch (IOException e) {
                System.out.print("IO Exception: likely:");
                System.out.println("Host Receive Socket Timed Out.\n" + e);
                e.printStackTrace();
                System.exit(1);
            }

            // Process the received datagram.
            String packetInfo = "Host: Client Packet received:" + "\n" +
                    "From Client: " + receiveClientPacket.getAddress() + "\n" +
                    "Client port: " + receiveClientPacket.getPort() + "\n" +
                    "Length: " + receiveClientPacket.getLength();
            System.out.println(packetInfo);
            if (i == 11) {
                System.out.println(new String(sendServerPacket.getData(), StandardCharsets.UTF_8));
            }
            else {
                ByteArrayInserter.print(data); // print string and byte representation of request
            }
            System.out.println("");

            // Slow things down (wait 2 seconds)
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.exit(1);
            }


            /* Create a new datagram packet containing the string received from the client.
             *   and send it to the server */

            //create packet to be sent
            sendServerPacket = new DatagramPacket(data, receiveClientPacket.getLength(),
                    receiveClientPacket.getAddress(), 69);

            //print packet info
            packetInfo = "Host: Sending packet to server:" + "\n" +
                    "To Server: " + sendServerPacket.getAddress() + "\n" +
                    "Destination Server port: " + sendServerPacket.getPort() + "\n" +
                    "Length: " + sendServerPacket.getLength();
            System.out.println(packetInfo);
            if (i == 11) {
                System.out.println(new String(sendServerPacket.getData(), StandardCharsets.UTF_8));
            }
            else {
                ByteArrayInserter.print(sendServerPacket.getData()); // print string and byte representation of request
            }

            //send packet to server
            try {
                sendSocket.send(sendServerPacket);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }

            System.out.println("Host: packet sent to server" + "\n");

            if(i == 11) { //don't send wait for response from server (invalid request)
                // We're finished, so close the socket.
                sendSocket.close();
                receiveSocket.close();
                return;
            }

            /* ----Receive packet from server---- */


            // Construct a DatagramPacket for receiving packets up
            data = new byte[Client.RESPONSESIZE];
            receiveServerPacket = new DatagramPacket(data, data.length);
            System.out.println("Host: Waiting for Server Packet.\n");

            // Block until a datagram packet is received from receiveSocket.
            try {
                System.out.println("Host: Waiting for Server packet..."); // so we know we're waiting
                receiveSocket.receive(receiveServerPacket);
            } catch (IOException e) {
                System.out.print("IO Exception: likely:");
                System.out.println("Host Receive Socket Timed Out.\n" + e);
                e.printStackTrace();
                System.exit(1);
            }

            // Process the received datagram.
            packetInfo = "Host: Server Packet received:" + "\n" +
                    "From Server: " + receiveServerPacket.getAddress() + "\n" +
                    "Server port: " + receiveServerPacket.getPort() + "\n" +
                    "Length: " + receiveServerPacket.getLength();
            System.out.println(packetInfo);
            System.out.println("Contents: " + ByteArrayInserter.byteArraytoString(receiveServerPacket.getData()) + "\n");

            // Slow things down (wait 2 seconds)
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.exit(1);
            }

            /* Create a new datagram packet containing the string received from the Server.
             *   and send it to the Client */

            //create packet to send to client
            sendClientPacket = new DatagramPacket(data, receiveServerPacket.getLength(),
                    receiveClientPacket.getAddress(), receiveClientPacket.getPort());

            // print packet info
            packetInfo = "Host: Sending packet to Client:" + "\n" +
                    "To Client: " + sendClientPacket.getAddress() + "\n" +
                    "Destination Client port: " + sendClientPacket.getPort() + "\n" +
                    "Length: " + sendClientPacket.getLength();
            System.out.println(packetInfo);
            System.out.println("Contents: " + ByteArrayInserter.byteArraytoString(sendClientPacket.getData()));

            //send packet to client
            try {
                sendSocket.send(sendClientPacket);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }

            System.out.println("Host: packet sent to client");


        }
        // We're finished, so close the sockets.
        sendSocket.close();
        receiveSocket.close();
    }

    public static void main(String[] args) {
        IntermediateHost host = new IntermediateHost();
        host.sendReceive();
    }
}
