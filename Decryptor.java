import java.util.Scanner;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class Decryptor {
    public static void main(String[] args) {
        System.out.println("Enter the folder path:");
        Scanner scanner = new Scanner(System.in);
        String folderPath = scanner.nextLine();
        System.out.println("Enter the password to unlock the files:");
        String password = scanner.nextLine();
        scanner.close();

        File folder = new File(folderPath);
        if (folder.exists() && folder.isDirectory()) {
            String[] files = folder.list();
            if (files != null && files.length > 0) {
                for (String file : files) {
                    File currentFile = new File(folder, file);
                    long size = currentFile.length();
                    if (currentFile.isFile()) {
                        try {
                            // Read all bytes safely
                            FileInputStream fis = new FileInputStream(currentFile);
                            byte[] data = new byte[(int) size];
                            int bytesRead = 0;
                            int offset = 0;
                            while (offset < data.length
                                    && (bytesRead = fis.read(data, offset, data.length - offset)) >= 0) {
                                offset += bytesRead;
                            }
                            fis.close();

                            // Decrypt
                            byte[] decrypted = CryptoUtils.decrypt(data, password);

                            // Overwrite
                            FileOutputStream fos = new FileOutputStream(currentFile);
                            fos.write(decrypted);
                            fos.close();

                            System.out.println("Decrypted file: " + currentFile.getName());
                        } catch (Exception e) {
                            System.out.println("Error decrypting file: " + currentFile.getAbsolutePath());
                            e.printStackTrace();
                        }
                    }
                }
            }
        } else {
            System.out.println("Invalid folder path.");
        }
    }
}
