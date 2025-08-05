import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javax.swing.*;



public class Encryptor {
    public static void encryptLogic(File folderName) {
        if (!folderName.getName().endsWith(".locked")) {
            // proceed with encryption
            String[] files = folderName.list();
            if (files != null && files.length > 0) {
                int confirm = JOptionPane.showConfirmDialog(
                        null,
                        "Do you confirm to encrypt this folder?",
                        "Confirm Encryption",
                        JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    // proceed with encryption
                    for (String file : files) {
                        File currentFile = new File(folderName, file);
                        long size = currentFile.length();
                        if (currentFile.isFile()) {
                            try {
                                FileInputStream fis = new FileInputStream(currentFile);
                                byte[] data = new byte[(int) size];
                                int bytesRead = 0;
                                int offset = 0;
                                while (offset < data.length
                                        && (bytesRead = fis.read(data, offset, data.length - offset)) >= 0) {
                                    offset += bytesRead;
                                }
                                fis.close();

                                byte[] encrypted = CryptoUtils.encrypt(data,"1234");

                                File newFile = currentFile;
                                FileOutputStream fos = new FileOutputStream(newFile);
                                fos.write(encrypted);
                                fos.close();
                                File lockedFolder = new File(folderName.getParent(),
                                        folderName.getName() + ".locked");
                                boolean renamed = folderName.renameTo(lockedFolder);
                                if (renamed) {
                                    JOptionPane.showMessageDialog(null,
                                            "Encryption completed. Folder locked as: "
                                                    + lockedFolder.getName());
                                    File desktop = new File(System.getProperty("user.home"), "Desktop");
                                    File encryptedFolderBase = new File(desktop, "EncryptedFolders");
                                    File destinationFolder = new File(encryptedFolderBase,
                                            lockedFolder.getName());
                                    try {
                                        Files.move(lockedFolder.toPath(), destinationFolder.toPath(),
                                                StandardCopyOption.REPLACE_EXISTING);
                                        System.out.println("Moved encrypted folder to: "
                                                + destinationFolder.getAbsolutePath());
                                    } catch (IOException w) {
                                        w.printStackTrace();
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(null,
                                            "Encryption done, but failed to rename folder.");
                                }

                            } catch (FileNotFoundException z) {
                                JOptionPane.showMessageDialog(null,
                                        "File not found." + currentFile.getAbsolutePath());
                            } catch (IOException z) {

                                JOptionPane.showMessageDialog(null,
                                        "Error reading file" + currentFile.getAbsolutePath());
                            } catch (Exception z) {
                                JOptionPane.showMessageDialog(null,
                                        "Error encrypting file" + currentFile.getAbsolutePath());
                            }

                        } else {
                            JOptionPane.showMessageDialog(null, "Encryption cancelled by user.");
                            
                            return;
                        }

                    }
                } else {
                    JOptionPane.showMessageDialog(null, "This folder is already locked.");
                    return;
                }
            }
        }
    }
}