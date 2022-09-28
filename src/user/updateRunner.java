/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package user;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author User
 */
public class updateRunner extends TimerTask {
    int SERVER_port;
    String SERVER_ip;
    String Username;
    
    public updateRunner(String SERVER_IP, int SERVER_PORT,String username) {
        SERVER_ip = SERVER_IP;
        SERVER_port = SERVER_PORT;
        Username = username;
    }


    @Override
    public void run() {
               try {
            Socket socket = new Socket(SERVER_ip, SERVER_port);
            PrintWriter outToServer = new PrintWriter(socket.getOutputStream(), true);
            serverHandler cListner = new serverHandler(socket);
            new Thread(cListner).start();
            outToServer.println(Username+":updateServerList:client");
        } catch (IOException ex) {
            Logger.getLogger(updateRunner.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
