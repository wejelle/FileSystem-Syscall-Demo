import java.io.*;
import java.lang.reflect.Field;
import java.nio.channels.FileChannel;

public class FileSyscallDemo {
    public static void main(String[] args) {
        String fileName = "example.txt";
        String content = "Hello, world!\n";
        byte[] buffer = new byte[content.length()];

        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            FileDescriptor fdOut = fos.getFD();
            int fdOutVal = getFdValue(fdOut);
            System.out.println("Opened file for writing. File descriptor: " + fdOutVal);

            fos.write(content.getBytes());
            fos.flush();
            System.out.println("Wrote to file: " + content.trim());
            fos.close();
            System.out.println("Closed file after writing.");

            FileInputStream fis = new FileInputStream(fileName);
            FileDescriptor fdIn = fis.getFD();
            int fdInVal = getFdValue(fdIn);
            System.out.println("Opened file for reading. File descriptor: " + fdInVal);

            int bytesRead = fis.read(buffer);
            String readContent = new String(buffer, 0, bytesRead);
            System.out.println("Read from file: " + readContent.trim());

            fis.close();
            System.out.println("Closed file after reading.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

      private static int getFdValue(FileDescriptor fd) {
        try {
            Field fdField = FileDescriptor.class.getDeclaredField("fd");
            fdField.setAccessible(true);
            return fdField.getInt(fd);
        } catch (Exception e) {
            return -1; 
        }
    }
}
