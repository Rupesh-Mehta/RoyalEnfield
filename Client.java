import java.net.*;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
//import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;

import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;

public class Client extends JFrame {

    Socket socket;
    BufferedReader br;// It is used for reading.
    PrintWriter pr;// It is used for writing.

    // components of GUI
    private JLabel heading = new JLabel("Client Area");
    private JTextArea mesArea = new JTextArea();
    private JTextField mesField = new JTextField();
    private Font font = new Font("Roboto", Font.PLAIN, 20);
    // private Container color=Heading.getContentPane();

    // constructor
    public Client() {
        try {
            System.out.println("Sending request to server...");
            socket = new Socket("127.0.0.1", 7779);
            System.out.println("connection done..");
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            pr = new PrintWriter(socket.getOutputStream());

            createGUI();
            handleEvents();
            startReading();
            // startWriting();

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

    private void handleEvents() {
        mesField.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
                /// System.out.println("key released:" + e.getKeyCode());

                if (e.getKeyCode() == 10) {
                    // System.out.println("you have pressed the enter button");
                    String contentToSend = mesField.getText();
                    mesArea.append("me: " + contentToSend + "\n");
                    pr.println(contentToSend);
                    pr.flush();
                    mesField.setText("");
                    mesField.requestFocus();

                }

            }

        });

    }

    private void createGUI() {

        // Code for GuI....
        this.setTitle("Client Msg[End]");
        this.setSize(500, 700);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // getcomponents
        heading.setFont(font);
        mesArea.setFont(font);
        mesField.setFont(font);
        heading.setIcon(new ImageIcon("clogo.png"));

        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mesArea.setEditable(false);
        mesField.setHorizontalAlignment(SwingConstants.CENTER);
        // setting the layout of the frame
        this.setLayout(new BorderLayout());

        // adding the components to frame

        this.add(heading, BorderLayout.NORTH);
        JScrollPane jScrollPane = new JScrollPane(mesArea);
        this.add(mesArea, BorderLayout.CENTER);
        this.add(mesField, BorderLayout.SOUTH);

        this.setVisible(true);

    }

    // startreading
    public void startReading() {
        // thread--> user se aaya hua data read karega
        Runnable r1 = () -> {
            System.out.println("Reader Started...");
            try {

                while (true) {
                    String msg = br.readLine();
                    if (msg.equals("exit")) {
                        System.out.println("Server has terminated the chat");
                        JOptionPane.showMessageDialog(this, "Server has terminated the chat");
                        mesField.setEnabled(false);
                        socket.close();
                        break;
                    }
                    // System.out.println("Server msg:" + msg);
                    mesArea.append("server: " + msg + "\n");
                }
            } catch (Exception e) {
                // TODO: handle exception
                // e.printStackTrace();
                System.out.println("Connection closed");
            }

        };
        new Thread(r1).start();

    }

    // start writing
    public void startWriting() {
        // thread--> data user se lega aur send karega client tak

        Runnable r2 = () -> {
            System.out.println("Writer started...");
            try {
                while (!socket.isClosed()) {

                    // system.in meaning to take a input from console or user
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();

                    pr.println(content);
                    pr.flush();// this method is used to send data forcefully to output
                    if (content.equals("exit")) {
                        socket.close();
                        break;
                    }

                }
            } catch (Exception e) {
                // TODO: handle exception
                // e.printStackTrace();
                System.out.println("Connection is Closed");
            }

        };
        new Thread(r2).start();

    }

    public static void main(String[] args) {
        System.out.println("client is getting Started..");
        new Client();

    }

}
