import java.nio.ByteBuffer;

import static java.lang.Byte.parseByte;

public class Main {
    public static void main(String[] args) {

        /*
        Server server = new Server();
        IntermediateHost host = new IntermediateHost();
        Client client = new Client();

        Thread serverThread = new Thread(server, "Server");
        Thread hostThread = new Thread(host, "Host");
        Thread clientThread = new Thread(client, "Client");

        serverThread.start();
        hostThread.start();
        clientThread.start();



        byte data[] = new byte[100];
        data[0] = 0;
        data[1] = 1;
        String filename = "test.txt";
        data[2] = filename.getBytes(); //filename
        String mode = "octet";
        data[3] = mode.getBytes(); //mode
        data[4] = 0;

        for (int i = 0; i < 5; i++) {
            System.out.println(data[i] + "");
        }
        */
        String filename = "test.txt";
        String mode = "octet";
        byte[] arr[] = new byte[6][];
        arr[0] = ByteBuffer.allocate(4).putInt(0).array();
        arr[1] = ByteBuffer.allocate(4).putInt(1).array();
        arr[2] = filename.getBytes(); //filename
        arr[3] = ByteBuffer.allocate(4).putInt(0).array();
        arr[4] = mode.getBytes(); //mode
        arr[5] = ByteBuffer.allocate(4).putInt(0).array();

        for (int i = 0; i < 5; i++) {
            if (i != 2 && i != 4) {
                System.out.println(ByteBuffer.wrap(arr[i]).getInt());
            } else {
                System.out.println(new String(arr[i]));
            }
        }

    }
}