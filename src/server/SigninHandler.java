/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author User
 */
public class SigninHandler implements Runnable{
    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    public static String Username;
    private static String Password;
    private static String AUsername; 
    private static String APassword;
    private static final int Key = 4;
    
    public SigninHandler (Socket clientSocket,String username,String password) throws IOException{
        this.client = clientSocket;
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        out = new PrintWriter(client.getOutputStream(),true);
        NotAEncryptionFile.setKey(Key);
        Username = NotAEncryptionFile.Decryption(username);
        Password = NotAEncryptionFile.Decryption(password);
    }
    
    @Override
    public void run()  {
        String message = null;    
        FileWriter fout = null;
        try {
            FileReader fin = new FileReader("login.txt");
            BufferedReader din = new BufferedReader(fin);
            //read from the file
            String line = null; // line of text
            int checker = 0;
            while ((line = din.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line, ":");
                String AUsername = st.nextToken().trim();
                String APassword = st.nextToken().trim();
                if (Password.equals(APassword) && Username.equals(AUsername)) {
                    out.println("[SERVER] : Login Success");
                    checker = 1;
                    
                }
            }

            if (checker == 0) {
                out.println("[SERVER] : Login Failed");
            }
            din.close(); // close the stream
            if(checker == 1){
                fout = new FileWriter("UserList.txt",true);
                PrintWriter pout = new PrintWriter(fout,true);
                pout.println(Username);
                pout.close();
            }
        } catch (IOException e) {
            System.err.println("Error! - " + e.getMessage());
        }
    }    
}

