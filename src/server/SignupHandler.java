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
import user.userLogin;
/**
 *
 * @author User
 */
public class SignupHandler implements Runnable{
    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    public String Username;
    private static String Password;
    private static String AUsername; 
    private static String APassword;
    
    public SignupHandler (Socket clientSocket,String username,String password) throws IOException{
        this.client = clientSocket;
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        out = new PrintWriter(client.getOutputStream(),true);
        Username = username;
        Password = password;
        
    }
    
    @Override
    public void run() {
        FileWriter fout = null;
        if (Username.length() > 3 && Password.length() > 3) {
            try {
                fout = new FileWriter("login.txt",true);
                PrintWriter pout = new PrintWriter(fout,true);
                pout.println(Username+":"+Password);
                out.println("[SERVER] Registration Passed");
            } catch (IOException ex) {
                Logger.getLogger(userLogin.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    fout.close();
                } catch (IOException ex) {
                    Logger.getLogger(userLogin.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            out.println("[SERVER] Registration Failed");
        }
    }
    
}
