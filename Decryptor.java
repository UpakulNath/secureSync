import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.*;

public class Decryptor {
    public static void decryptLogic(File folder) {
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
                        byte[] decrypted = CryptoUtils.decrypt(data, "1234");

                        // Overwrite original file with decrypted data
                        FileOutputStream fos = new FileOutputStream(currentFile);
                        fos.write(decrypted);
                        fos.close();

                    } catch (Exception e) {
                        System.out.println("Error decrypting file: " + currentFile.getAbsolutePath());
                        e.printStackTrace();
                    }
                }
            }

            // Step 1: Move decrypted folder to Desktop/DecryptedFolders
            File desktop = new File(System.getProperty("user.home"), "Desktop");
            File decryptedFolderBase = new File(desktop, "DecryptedFolders");

            if (!decryptedFolderBase.exists()) {
                decryptedFolderBase.mkdir();
            }

            File destinationFolder = new File(decryptedFolderBase,
                    folder.getName().replace(".locked", "")); // remove ".locked" suffix

            try {
                Files.move(folder.toPath(), destinationFolder.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("✅ Decrypted folder moved to: " + destinationFolder.getAbsolutePath());
            } catch (IOException e) {
                System.out.println("⚠️ Failed to move decrypted folder.");
                e.printStackTrace();
            }
        }
    }
}
