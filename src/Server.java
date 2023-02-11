import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.rmi.ServerException;

/**
 * The Server class will receive a message from the IntermediateHost, procces it,
 * and then send it back to IntermediateHost.
 *
 */
public class Server {
    private DatagramPacket sendPacket, receivePacket;
    private DatagramSocket sendSocket, receiveSocket;
    private int messageType;

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

            //receiveSocket.setSoTimeout(10000);

        } catch (SocketException se) {
            se.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * This method will receive a message from the IntermediateHost, process it,
     * and then send it back to IntermediateHost.
     */
    public void sendReceive()
    {
        for(int i = 1; i < 12; i++) {

            /* Receive packet form intermediate host */

            // Construct a DatagramPacket for receiving packets up
            byte data[] = new byte[Client.REQUESTSIZE];
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
                    "Length: " + receivePacket.getLength();
            System.out.println(packetInfo);
            if(i == 11) {
                System.out.println(new String(receivePacket.getData(), StandardCharsets.UTF_8));
            }
            else {
                ByteArrayInserter.print(receivePacket.getData()); // print string and byte representation of request
            }
            System.out.println("");

            // Slow things down (wait 5 seconds)
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.exit(1);
            }

            /* Create a new datagram packet containing the string received from the Host
             * process it then send it back to the host  */
            byte[] msg;
            try {
                 messageType = data[1]; //if message doesn't follow the format, it is invalid request
            } catch (IndexOutOfBoundsException e) {
                // We're finished, so close the sockets.
                sendSocket.close();
                receiveSocket.close();
                throw new RuntimeException("Invalid Request");
            }
            if(messageType == 1) { //read request
                msg = createByteArray((byte) 0, (byte) 3, (byte) 0, (byte) 1); //response is 0301

            } else if (messageType == 2) { //write request
                msg = createByteArray((byte) 0, (byte) 4, (byte) 0, (byte) 0); //response is 0400
            }
            else {
                throw new RuntimeException("Invalid Request");
            }

            //create send packet with the msg byte array
            sendPacket = new DatagramPacket(msg, msg.length,
                    receivePacket.getAddress(), 23);

            //print packet info
            packetInfo = "Server Sending packet to Host: " + "\n" +
                    "To host: " + sendPacket.getAddress() + "\n" +
                    "Destination host port: " + sendPacket.getPort() + "\n" +
                    "Length: " + sendPacket.getLength();
            System.out.println(packetInfo);
            System.out.println("Contents: " + ByteArrayInserter.byteArraytoString(sendPacket.getData()));

            // Send the datagram packet to the client via the send socket.
            try {
                sendSocket.send(sendPacket);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }

            System.out.println("Server: packet sent to Host");

        }
        // We're finished, so close the sockets.
        sendSocket.close();
        receiveSocket.close();
    }

    /**
     * creates a byte array of size 4 bytes exactly
     *
     */
    private byte[] createByteArray(byte x, byte y, byte i, byte j) {
        byte[] arr = new byte[4];
        arr[0] = x;
        arr[1] = y;
        arr[2] = i;
        arr[3] = j;
        return arr;
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.sendReceive();
    }
}
