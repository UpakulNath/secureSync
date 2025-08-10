import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javax.swing.*;

public class Encryptor {
    public static void encryptLogic(File folderName) {
        if (!folderName.getName().endsWith(".locked")) {
            String[] files = folderName.list();
            if (files != null && files.length > 0) {
                int confirm = JOptionPane.showConfirmDialog(
                        null,
                        "Do you confirm to encrypt this folder?",
                        "Confirm Encryption",
                        JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        // Encrypt all files first
                        for (String file : files) {
                            File currentFile = new File(folderName, file);
                            if (currentFile.isFile()) {
                                try (FileInputStream fis = new FileInputStream(currentFile)) {
                                    byte[] data = fis.readAllBytes();
                                    byte[] encrypted = CryptoUtils.encrypt(data, "1234");

                                    try (FileOutputStream fos = new FileOutputStream(currentFile)) {
                                        fos.write(encrypted);
                                    }
                                }
                            }
                        }

                        // Rename folder AFTER encrypting all files
                        File lockedFolder = new File(folderName.getParent(),
                                folderName.getName() + ".locked");
                        boolean renamed = folderName.renameTo(lockedFolder);

                        if (renamed) {
                            // Move to EncryptedFolders
                            File desktop = new File(System.getProperty("user.home"), "Desktop");
                            File encryptedFolderBase = new File(desktop, "EncryptedFolders");
                            encryptedFolderBase.mkdirs();
                            File destinationFolder = new File(encryptedFolderBase, lockedFolder.getName());

                            Files.move(lockedFolder.toPath(), destinationFolder.toPath(),
                                    StandardCopyOption.REPLACE_EXISTING);

                            JOptionPane.showMessageDialog(null,
                                    "Encryption completed. Folder locked and moved to: "
                                            + destinationFolder.getAbsolutePath());
                        } else {
                            JOptionPane.showMessageDialog(null,
                                    "Encryption done, but failed to rename folder.");
                        }
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(null, "Error during encryption: " + e.getMessage());
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Encryption error: " + e.getMessage());
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Encryption cancelled by user.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "This folder is already locked.");
        }
    }
}
