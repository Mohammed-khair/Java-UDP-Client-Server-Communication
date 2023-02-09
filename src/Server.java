import java.io.*;
import java.net.*;
/**
 * The Server class will receive a message from the IntermediateHost, procces it,
 * and then send it back to IntermediateHost.
 *
 */
public class Server implements Runnable{
    private DatagramPacket sendPacket, receivePacket;
    private DatagramSocket sendSocket, receiveSocket;

    /**
     * Constructor for class Server that initializes the DatagramSocket
     */
    public Server()
    {
        try {
            // Construct a datagram socket and bind it to any available
            // port on the local host machine. This socket will be used to
            // send UDP Datagram packets.
            sendSocket = new DatagramSocket();

            // Construct a datagram socket and bind it to port 5000
            // on the local host machine. This socket will be used to
            // receive UDP Datagram packets.
            receiveSocket = new DatagramSocket(69);

        } catch (SocketException se) {
            se.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * This method will receive a message from the IntermediateHost, process it,
     * and then send it back to IntermediateHost.
     */
    public void run()
    {
                                      /* Receive packet form intermediate host */

        // Construct a DatagramPacket for receiving packets up
        // to 100 bytes long (the length of the byte array).

        byte data[] = new byte[100];
        receivePacket = new DatagramPacket(data, data.length);
        System.out.println("Server: Waiting for Host Packet.\n");

        // Block until a datagram packet is received from receiveSocket.
        try {
            System.out.println("Server Waiting for Host Packet..."); // so we know we're waiting
            receiveSocket.receive(receivePacket);
        } catch (IOException e) {
            System.out.print("IO Exception: likely:");
            System.out.println("Server Receive Socket Timed Out.\n" + e);
            e.printStackTrace();
            System.exit(1);
        }

        // Process the received datagram.
        String packetInfo = "Server: Host Packet received:" + "\n" +
                "From host: " + receivePacket.getAddress() + "\n" +
                "Host port: " + receivePacket.getPort() + "\n" +
                "Length: " + receivePacket.getLength() + "\n" +
                "Containing: " + new String(data,0, receivePacket.getLength()) + "\n";
        System.out.println(packetInfo);

        // Slow things down (wait 5 seconds)
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e ) {
            e.printStackTrace();
            System.exit(1);
        }

                                  /* Create a new datagram packet containing the string received from the Host
                                               * process it then send it back to the host  */

        sendPacket = new DatagramPacket(data, receivePacket.getLength(),
                receivePacket.getAddress(), 23);

        packetInfo = "Server: Sending packet to Host: " + "\n" +
                "To host: " + sendPacket.getAddress() + "\n" +
                "Destination host port: " + sendPacket.getPort() + "\n" +
                "Length: " + sendPacket.getLength() + "\n" +
                "Containing: " + new String(sendPacket.getData(),0, sendPacket.getLength()) + "\n";
        System.out.println(packetInfo);

        // Send the datagram packet to the client via the send socket.
        try {
            sendSocket.send(sendPacket);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        System.out.println("Server: packet sent to Host");

        // We're finished, so close the sockets.
        sendSocket.close();
        receiveSocket.close();
    }

}
