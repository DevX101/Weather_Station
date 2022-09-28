/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author User
 */
public class updateWSDataHandler implements Runnable{
    String message;
    public static String[] stationID = new String[10];
    String line;
    public static String[] stationData = new String[10];
    String messageOut = "updateServerListReply:";
    private Socket client;

    public updateWSDataHandler(Socket clientSocket,String messageIn) throws IOException{
        this.client = clientSocket;
        message = messageIn;
    }
    
    @Override
    public void run() {
        try {            
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            FileReader fin = new FileReader("StationData.txt");
            BufferedReader din = new BufferedReader(fin);
            StringTokenizer st = new StringTokenizer(message, ":");
            String waste = st.nextToken().trim();
             //write data in GUI
            int num = st.countTokens();
            for (int i = 0; i < num; i++) {
                 String mm = st.nextToken().trim();
                 stationID[i] = mm;
                 String returnData = checkfilefordata(i);
                 messageOut = messageOut + returnData;
            }
            fin.close();
            out.println(messageOut);
        } catch (IOException e) {System.err.println("Error! - " + e.getMessage());}    
    }
    String checkfilefordata(int i){
        
        try {
            FileReader fin = new FileReader("StationData.txt");
            BufferedReader din = new BufferedReader(fin);
            while((line = din.readLine())!= null){
                StringTokenizer st2 = new StringTokenizer(line, ":");
                if(stationID[i].equals((st2.nextToken().trim()))){
                    fin.close();
                    return line + ":";
                }
            }
            fin.close();
            return "";
        } catch (FileNotFoundException ex) {
            Logger.getLogger(updateWSDataHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(updateWSDataHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
        
    }
}
