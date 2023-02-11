import java.nio.charset.StandardCharsets;

/**
 * This class allows bytes and strings to be inserted into a byte array by
 * converting the string to bytes. The result array can be returned by invoking
 * one of the methods
 */
public class ByteArrayInserter {

    /**
     * Creates the byte array of the request
     * @param value The values at index 1
     * @param filename The first string to be added to the byte array
     * @param mode The second string to be added to the array
     * @return the byte array that has the format of the request
     */
    public static byte[] create(byte value, String filename, String mode) {
        byte[] filenameBytes = filename.getBytes(StandardCharsets.UTF_8);
        byte[] modeBytes = mode.getBytes(StandardCharsets.UTF_8);
        byte[] byteArray = new byte[2 + filenameBytes.length + 1 + modeBytes.length + 1];
        byteArray[0] = 0;
        byteArray[1] = value;
        System.arraycopy(filenameBytes, 0, byteArray, 2, filenameBytes.length);
        byteArray[filenameBytes.length + 2] = 0;
        System.arraycopy(modeBytes, 0, byteArray, filenameBytes.length + 3, modeBytes.length);
        byteArray[byteArray.length - 1] = 0;
        return byteArray;
    }

    /**
     * Prints the byte and string representation of the byte array.
     * @param byteArray The byte array to be printed as bytes and as a string
     */
    public static void print(byte[] byteArray) {
        String str = ByteArrayInserter.toString(byteArray);
        System.out.println("String representation: " + str);
        System.out.print("Byte representation: ");
        for (byte b : byteArray) {
            System.out.print(b + " ");
        }
        System.out.println();
    }

    /**
     * Returns the string representation of the byte array
     * @param byteArray The byte array to be converted to a string
     * @return The string representation of byterArray
     */
    public static String toString(byte[] byteArray) {
        int i = 0;
        StringBuilder result = new StringBuilder();

        int firstInt = ((byteArray[i++] & 0xff)); //get first int
        result.append(firstInt).append(" ");

        int secondInt = ((byteArray[i++] & 0xff)); //get second int
        result.append(secondInt).append(" ");

        int j = i;
        while (j < byteArray.length && byteArray[j] != 0) { //find the index where the first string ends
            j++;
        }
        String firstString = new String(byteArray, i, j - i, StandardCharsets.UTF_8); //get first text
        result.append(firstString + " ");
        result.append("0 ");
        i = j + 1;

        j = i;
        while (j < byteArray.length && byteArray[j] != 0) { //find the index where the second string ends
            j++;
        }
        String secondString = new String(byteArray, i, j - i, StandardCharsets.UTF_8); //get second text
        result.append(secondString + " ");
        result.append("0");

        return result.toString();
    }

    /**
     * This method return a string representation of a byte array of ints
     * @param arr byte array of integers
     * @return string representation of arr
     */
    public static String byteArraytoString(byte[] arr) {
        String str = "";
        for(byte b : arr) {
            str += b & 0xff;
        }
        return str;
    }
}
