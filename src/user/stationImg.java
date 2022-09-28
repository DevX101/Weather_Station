/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package user;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author User
 */
public class stationImg {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        JFrame jFrame = new JFrame("Server");
        jFrame.setSize(400,400);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JLabel jLabelText = new JLabel("Waiting for image from client...");
        
        jFrame.add(jLabelText,BorderLayout.SOUTH);
        
        jFrame.setVisible(true);
        
        ServerSocket serverSocket = new ServerSocket(9999);
        
        Socket socket = serverSocket.accept();
        
        InputStream inputStream = socket.getInputStream();
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        
        BufferedImage bufferedImage = ImageIO.read(bufferedInputStream);
        
        bufferedInputStream.close();
        socket.close();
        
        JLabel jLabelPic = new JLabel(new ImageIcon(bufferedImage));
        jLabelText.setText("Image Recived");
        jFrame.add(jLabelPic, BorderLayout.CENTER);
    }
    
}
