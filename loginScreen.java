import java.awt.Component;
import java.awt.Dimension;
import javax.swing.*;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// ...existing code...

public class loginScreen {
    static JFrame frame;
    static JPanel panel;
    static JLabel userNameLabel;
    static JTextField userField;
    static JLabel userPasswordLabel;
    static JPasswordField passwordField;
    static JButton loginButton;

    public static void createScreen() {
        frame = new JFrame("Login Screen");
        frame.setSize(300, 200);


        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));


        Font font = new Font("Arial", Font.PLAIN, 18);


        JPanel row1 = new JPanel();
        row1.setLayout(new BoxLayout(row1, BoxLayout.X_AXIS));


        userNameLabel = new JLabel("Username: ");
        userNameLabel.setFont(font);
        userField = new JTextField(15);
        userField.setPreferredSize(new Dimension(200, 25));
        userField.setMaximumSize(new Dimension(200, 25));
        userField.setFont(font);

        row1.add(userNameLabel);
        row1.add(userField);
        row1.setAlignmentX(Component.CENTER_ALIGNMENT);


        JPanel row2 = new JPanel();
        row2.setLayout(new BoxLayout(row2, BoxLayout.X_AXIS));


        userPasswordLabel = new JLabel("Password: ");
        userPasswordLabel.setFont(font);
        passwordField = new JPasswordField(15);
        passwordField.setPreferredSize(new Dimension(200, 25));
        passwordField.setMaximumSize(new Dimension(200, 25));

        row2.add(userPasswordLabel);
        row2.add(passwordField);
        row2.setAlignmentX(Component.CENTER_ALIGNMENT);


        loginButton = new JButton("Login");
        JPanel row3 = new JPanel();
        row3.setLayout(new BoxLayout(row3, BoxLayout.X_AXIS));
        row3.add(loginButton);
        row3.setAlignmentX(Component.CENTER_ALIGNMENT);


        panel.add(Box.createVerticalGlue());
        panel.add(row1);
        panel.add(Box.createVerticalStrut(15));
        panel.add(row2);
        panel.add(Box.createVerticalStrut(15));
        panel.add(row3);
        panel.add(Box.createVerticalStrut(15));
        panel.add(Box.createVerticalGlue());


        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userField.getText(); // fixed variable name
                String password = new String(passwordField.getPassword());

                // Example: Check hardcoded credentials
                if (username.equals("admin") && password.equals("1234")) {
                    JOptionPane.showMessageDialog(null, "Login successful");
                    frame.dispose(); // fixed dispose
                    MainMenu.menuScreen(); // open next screen
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid username or password");
                }
            }
        });

    }
}