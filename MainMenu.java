
import java.awt.Component;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
public class MainMenu {
    static JFrame menuFrame;
    static JPanel menuPanel;
    static JButton encryptButton;  
    static JButton decryptButton;
    static JPanel row1;
    static JPanel row2;
    public static void menuScreen() {
        menuFrame = new JFrame("Menu Screen");
        menuFrame.setSize(400, 300);
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));


        row1 = new JPanel();
        row1.setLayout(new BoxLayout(row1, BoxLayout.X_AXIS));
        row1.add(Box.createHorizontalGlue());
        
        
        encryptButton = new JButton("Encrypt");
        decryptButton = new JButton("Decrypt");
        
        
        row1.add(encryptButton);
        row1.add(Box.createHorizontalStrut(20));
        row1.add(decryptButton);
        row1.add(Box.createHorizontalGlue());
        row1.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        
        menuPanel.add(Box.createVerticalGlue());
        menuPanel.add(row1);
        menuPanel.add(Box.createVerticalGlue());

        menuFrame.add(menuPanel);
        menuFrame.setVisible(true);

        encryptButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                menuFrame.dispose();
                JFrame frame = new JFrame("File chooser.");
                frame.setSize(400, 200);
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); // only folders
                int result = chooser.showOpenDialog(frame);

                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFolder = chooser.getSelectedFile();
                    Encryptor.encryptLogic(selectedFolder);
                    frame.dispose();
                }
            }
        });


        decryptButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                menuFrame.dispose();
                File encryptedDir = new File("C:/Users/Upakul/Desktop/EncryptedFolders");

                // Ensure the folder exists
                if (!encryptedDir.exists()) {
                    JOptionPane.showMessageDialog(null, "No encrypted folders found on Desktop.");
                    return;
                }

                // Create a file chooser that starts in the encrypted folder directory
                JFileChooser chooser = new JFileChooser(encryptedDir);
                chooser.setDialogTitle("Select a folder to decrypt");
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); // only folders

                int result = chooser.showOpenDialog(null);

                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedEncryptedFolder = chooser.getSelectedFile();
                    // Now call your decryption logic
                    Decryptor.decryptLogic(selectedEncryptedFolder);
                }
            }
        });

    }

}
