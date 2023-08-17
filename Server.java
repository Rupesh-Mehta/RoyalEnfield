import java.io.*;
import java.net.*;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
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

public class Server extends JFrame {
    ServerSocket serverSocket;
    Socket socket;
    BufferedReader br;// It is used for reading.
    PrintWriter pr;// It is used for writing.

    private JLabel heading = new JLabel("Server Area");
    private JTextArea mesArea = new JTextArea();
    private JTextField mesField = new JTextField();
    private Font font = new Font("Roboto", Font.PLAIN, 18);

    // Constructor..
    public Server() {
        try {
            serverSocket = new ServerSocket(7779);
            System.out.println("Server is ready to get connction...");
            System.out.println("waiting...");
            socket = serverSocket.accept();

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
                // System.out.println("Key released " + e.getKeyCode());
                if (e.getKeyCode() == 10) {
                    // System.out.println("you have pressed enter button");
                    String contentToSend = mesField.getText();
                    mesArea.append("Me :" + contentToSend + "\n");
                    pr.println(contentToSend);
                    pr.flush();
                    mesField.setText("");
                    mesField.requestFocus();
                }

            }

        });
    }

    private void createGUI() {
        // gui create code...
        this.setTitle("Server Message[END]");
        this.setSize(600, 700);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // coding for component
        heading.setFont(font);
        mesArea.setFont(font);
        mesField.setFont(font);
        heading.setIcon(new ImageIcon("serverlogo.png"));
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mesArea.setEditable(false);
        mesField.setHorizontalAlignment(SwingConstants.CENTER);
        // frame kaa layout set karenge

        this.setLayout(new BorderLayout());

        // adding the components....
        this.add(heading, BorderLayout.NORTH);
        JScrollPane jScrollPane = new JScrollPane(mesArea);
        this.add(mesArea, BorderLayout.CENTER);
        this.add(mesField, BorderLayout.SOUTH);

        this.setVisible(true);

    }

    public void startReading() {
        // thread--> user se aaya hua data read karega
        Runnable r1 = () -> {
            System.out.println("Reader Started...");
            try {

                while (true) {
                    String msg = br.readLine();
                    if (msg.equals("exit")) {
                        System.out.println("Client has terminated the chat");
                        JOptionPane.showMessageDialog(this, "Client has terminated the chat");
                        mesField.setEnabled(false);
                        socket.close();
                        break;
                    }
                    // System.out.println("Client msg:" + msg);
                    mesArea.append("Client msg: " + msg + "\n");

                }
            } catch (Exception e) {
                // TODO: handle exception
                // e.printStackTrace();
                System.out.println("Connection Closed");
            }

        };
        new Thread(r1).start();

    }

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
        System.out.println("Server is getting... started");
        new Server();

    }
}