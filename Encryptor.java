import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javax.swing.*;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Encryptor {
    public static void encryptLogic(File folderName) {
        if (!folderName.getName().endsWith(".locked")) {
            // proceed with encryption
            String[] files = folderName.list();
            if (files != null && files.length > 0) {
                // use new JFrame here and dispose the menu frame from MainMenu.
                JFrame frame = new JFrame("Encryption password");
                frame.setSize(400, 300);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                JLabel passwordLabel = new JLabel("Enter the password to encrypt: ");
                JPasswordField passwordField = new JPasswordField();
                passwordField.setPreferredSize(new Dimension(200, 25));
                passwordField.setMaximumSize(new Dimension(200, 25));
                JPanel passwordPanel = new JPanel();
                passwordPanel.setLayout(new BoxLayout(passwordPanel, BoxLayout.Y_AXIS));
                JPanel row1 = new JPanel();
                row1.setLayout(new BoxLayout(row1, BoxLayout.X_AXIS));

                row1.add(Box.createHorizontalGlue());
                JButton encryptButton = new JButton("Encrypt");
                row1.add(passwordLabel);
                row1.add(passwordField);
                row1.add(Box.createHorizontalStrut(20));
                row1.add(Box.createHorizontalGlue());
                row1.setAlignmentX(Component.CENTER_ALIGNMENT);

                JPanel row2 = new JPanel();
                row2.setLayout(new BoxLayout(row2, BoxLayout.X_AXIS));
                row2.add(Box.createHorizontalGlue());
                row2.add(encryptButton);
                row2.add(Box.createHorizontalStrut(20));
                row2.add(Box.createHorizontalGlue());
                row2.setAlignmentX(Component.CENTER_ALIGNMENT);

                passwordPanel.add(Box.createVerticalGlue());
                passwordPanel.add(row1);
                passwordPanel.add(row2);

                passwordPanel.add(Box.createVerticalGlue());

                frame.add(passwordPanel);
                frame.setVisible(true);

                encryptButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (files != null && files.length > 0) {
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

                                        byte[] encrypted = CryptoUtils.encrypt(data,
                                                new String(passwordField.getPassword()));

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
                                            frame.dispose(); // close the encryption window
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
                                }
                            }
                        }

                    }
                });

            }

        } else {
            JOptionPane.showMessageDialog(null, "This folder is already locked.");
            return;
        }
    }
}
