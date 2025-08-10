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

                if (currentFile.isFile()) {
                    try {
                        // Read full file content (safe for binary files)
                        byte[] data;
                        try (FileInputStream fis = new FileInputStream(currentFile)) {
                            data = fis.readAllBytes();
                        }

                        // Decrypt
                        byte[] decrypted = CryptoUtils.decrypt(data, "1234");

                        // Overwrite with decrypted content
                        try (FileOutputStream fos = new FileOutputStream(currentFile)) {
                            fos.write(decrypted);
                        }

                    } catch (Exception e) {
                        System.out.println("❌ Error decrypting file: " + currentFile.getAbsolutePath());
                        e.printStackTrace();
                    }
                }
            }

            // Move decrypted folder to Desktop/DecryptedFolders (remove .locked suffix)
            File desktop = new File(System.getProperty("user.home"), "Desktop");
            File decryptedFolderBase = new File(desktop, "DecryptedFolders");
            decryptedFolderBase.mkdirs();

            File destinationFolder = new File(decryptedFolderBase,
                    folder.getName().replace(".locked", ""));

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
