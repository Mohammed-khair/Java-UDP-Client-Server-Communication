import java.io.*;
import java.net.*;
/**
 * The IntermediateHost class will receive a message from the client and
 * pass it to the server. Later, the IntermediateHost  waits for a response from
 * the server and then send it back to the client
 *
 */
public class IntermediateHost implements Runnable{
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

            // to test socket timeout (2 seconds) ---------------------- delete later
            //receiveSocket.setSoTimeout(2000);
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
    public void run()
    {
        /* Receive packet from the client */

        // Construct a DatagramPacket for receiving packets up
        // to 100 bytes long (the length of the byte array).

        byte data[] = new byte[100];
        receiveClientPacket = new DatagramPacket(data, data.length);
        System.out.println("Host: Waiting for Client Packet.\n");

        // Block until a datagram packet is received from receiveSocket.
        try {
            System.out.println("Host: Waiting for Client packet..."); // so we know we're waiting
            receiveSocket.receive(receiveClientPacket);
        } catch (IOException e) {
            System.out.print("IO Exception: likely:");
            System.out.println("Host Receive Socket Timed Out.\n" + e);
            e.printStackTrace();
            System.exit(1);
        }

        // Process the received datagram.
        System.out.println("Host: Client Packet received:");
        System.out.println("From Client: " + receiveClientPacket.getAddress());
        System.out.println("Client port: " + receiveClientPacket.getPort());
        int len = receiveClientPacket.getLength();
        System.out.println("Length: " + len);
        System.out.print("Containing: " );

        // Form a String from the byte array.
        String received = new String(data,0,len);
        System.out.println(received + "\n");

        // Slow things down (wait 2 seconds)
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e ) {
            e.printStackTrace();
            System.exit(1);
        }

        /* Create a new datagram packet containing the string received from the client.
        *   and send it to the server */

        sendServerPacket = new DatagramPacket(data, receiveClientPacket.getLength(),
            receiveClientPacket.getAddress(), 69);

        System.out.println( "Host: Sending packet to server:");
        System.out.println("To Server: " + sendServerPacket.getAddress());
        System.out.println("Destination Server port: " + sendServerPacket.getPort());
        len = sendServerPacket.getLength();
        System.out.println("Length: " + len);
        System.out.print("Containing: ");
        System.out.println(new String(sendServerPacket.getData(),0,len));

        try {
            sendSocket.send(sendServerPacket);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        System.out.println("Host: packet sent to server");

        /* Receive packet from server */

        // Construct a DatagramPacket for receiving packets up
        // to 100 bytes long (the length of the byte array).

        data = new byte[100];
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
        System.out.println("Host: Server Packet received:");
        System.out.println("From Server: " + receiveServerPacket.getAddress());
        System.out.println("Server port: " + receiveServerPacket.getPort());
        len = receiveServerPacket.getLength();
        System.out.println("Length: " + len);
        System.out.print("Containing: " );

        // Form a String from the byte array.
        received = new String(data,0,len);
        System.out.println(received + "\n");

        // Slow things down (wait 2 seconds)
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e ) {
            e.printStackTrace();
            System.exit(1);
        }

        /* Create a new datagram packet containing the string received from the Server.
         *   and send it to the Client */

        sendClientPacket = new DatagramPacket(data, receiveServerPacket.getLength(),
                receiveClientPacket.getAddress(), receiveClientPacket.getPort());

        System.out.println( "Host: Sending packet to Client:");
        System.out.println("To Client: " + sendClientPacket.getAddress());
        System.out.println("Destination Client port: " + sendClientPacket.getPort());
        len = sendClientPacket.getLength();
        System.out.println("Length: " + len);
        System.out.print("Containing: ");
        System.out.println(new String(sendClientPacket.getData(),0,len));

        try {
            sendSocket.send(sendClientPacket);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        System.out.println("Host: packet sent to client");

        // We're finished, so close the sockets.
        sendSocket.close();
        receiveSocket.close();
    }

}
