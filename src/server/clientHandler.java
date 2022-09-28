/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import user.userLogin;

/**
 *
 * @author User
 */
public class clientHandler implements Runnable {
    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private static final int SERVER_PORT = 9090;
    private static final String SERVER_IP = "127.0.0.1";
    private static String username;
    private static String password;
    private static final ExecutorService pool2 = Executors.newFixedThreadPool(100);
    private static ArrayList<clientHandler> clients;
    public static String[] stationID = new String[10];

    public clientHandler(Socket clientSockert, ArrayList<clientHandler> clients) throws IOException{
        this.client = clientSockert;
        this.clients = clients;
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        out = new PrintWriter(client.getOutputStream(), true);
    }
    
    
    @Override
    public void run(){
        String command = "";
        while(true){
            try{    
                System.out.println("[SERVER THREAD] Waiting..");
                String message = in.readLine();
                System.out.println("{CLIENT] [" + client.getInetAddress() + "] : " + message);
                
                if(message != null){
                    
                    StringTokenizer st = new StringTokenizer(message, ":");
                    String Identity = st.nextToken().trim();
                    command = st.nextToken().trim();
                    
                    if ((identityCheck(Identity)) == true){
                        if(command.equals("TERMINATESERVER"))break;
                        System.out.println("Identity Check passed");
                        switch (command) {
                            case "signin" -> {
                                    String firstMessage = st.nextToken().trim();
                                    String secondMessage = st.nextToken().trim();
                                    username = firstMessage;
                                    password = secondMessage;
                                    SigninHandler clientThread2 = new SigninHandler(client, username, password);
                                    pool2.execute(clientThread2);
                            }
                            case "signup" -> {
                                    String firstMessage = st.nextToken().trim();
                                    String secondMessage = st.nextToken().trim();
                                    username = firstMessage;
                                    password = secondMessage;
                                    SignupHandler clientThread3 = new SignupHandler(client,username,password);
                                    pool2.execute(clientThread3);
                            }
                            case "updateServerList" -> {
                                    String firstMessage = st.nextToken().trim();                                
                                    if("ws".equals(firstMessage)){
                                        String secondMessage = st.nextToken().trim();
                                        boolean num;
                                        FileWriter fout;
                                        try {
                                            FileReader fin = new FileReader("StationList.txt"); 
                                            BufferedReader din = new BufferedReader(fin);
                                            String line;
                                            num = false;
                                            fout = null;
                                            while((line = din.readLine())!= null){
                                                if(line.equals(secondMessage)){
                                                    num = false;
                                                    break;
                                                }else num = true;
                                            }   
                                            din.close();
                                            fin.close();
                                            if(num == true){
                                                fout = new FileWriter("StationList.txt",true);
                                                PrintWriter pout = new PrintWriter(fout,true);
                                                pout.println(secondMessage);
                                            }
                                            fout.close();
                                        } catch (IOException e){
                                            Logger.getLogger(userLogin.class.getName()).log(Level.SEVERE, null, e);
                                        }

                                    }else if("client".equals(firstMessage)){

                                        FileReader fin = new FileReader("StationList.txt"); 
                                        BufferedReader din = new BufferedReader(fin);
                                        String line;
                                        String output = "";
                                        int i=0;
                                        while((line = din.readLine())!= null){
                                            output = output + ":" + line;
                                            i++;
                                        }
                                        out.println("updateStationListReply" + output);
                                    }
                                    break;
                            }
                            case "StationData" -> {
                                    String firstMessage = st.nextToken().trim();
                                    String secondMessage = st.nextToken().trim();
                                    String entier = "";
                                    if("Station".equals(firstMessage)){ 
                                        String something = st.nextToken().trim();
                                        entier = something + (st.nextToken(""));
                                    }

                                    StationData(firstMessage,secondMessage,entier);
                            }
                            default -> {System.out.println("Command: "+command + " not found in client handler :("); break;}
                        }
                    }    
                }else{break;}
            } catch(IOException e){
                System.err.println("IOException in client handler");
                System.err.println(Arrays.toString(e.getStackTrace()));
                break;
            }
        }
        try {
            in.close();
            out.close();
            System.out.println("[SERVER THREAD] "+ command +" - TERMINATING THREAD");
        } catch (IOException ex) {
            Logger.getLogger(clientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
    
    private synchronized void StationData(String firstMessage, String secondMessage,String message){
        if("client".equals(firstMessage)){
            String file = secondMessage + ".txt";
            String line;
            try{
                FileReader fin = new FileReader(file); 
                BufferedReader din = new BufferedReader(fin);  
                
                if((line = din.readLine()) == null){line = "noFile";}
                
                out.println("StationData:"+line);
                din.close();
            } catch (IOException ex) {Logger.getLogger(userLogin.class.getName()).log(Level.SEVERE, null, ex);}                 
        }else if ("Station".equals(firstMessage)) {
            
            FileWriter fout = null;
            String line;
            int counter = 0;
            try {
                File myFile = new File(secondMessage+".txt");
                myFile.createNewFile();
                fout = new FileWriter(myFile,false);
                PrintWriter pout = new PrintWriter(fout,false);
                pout.println(message);
                pout.close();
                
                FileReader fin = new FileReader("StationList.txt"); 
                BufferedReader din = new BufferedReader(fin); 
                while((line = din.readLine())!=null){
                    if(line.equals(secondMessage)){
                        counter = 1;
                    }
                }
                if (counter == 0) {
                    fout = new FileWriter("StationList.txt",true);
                    PrintWriter pout2 = new PrintWriter(fout,true);
                    pout2.println(secondMessage);
                    pout2.close();
                    fout.close();
                }
            } 
            catch (IOException ex) {Logger.getLogger(userLogin.class.getName()).log(Level.SEVERE, null, ex);} 
            
        }
    }
    

    private boolean identityCheck(String Identity) {
        try{
            System.out.println("Running Identity Check");
            String line;
            FileReader fin = new FileReader("AuthorisedStationID.txt"); 
            BufferedReader din = new BufferedReader(fin);  
            while((line = din.readLine())!= null){
                if(Identity.equals(line)){
                    return true;
                }
            }
            din.close();
            System.out.println("Running Identity Check user");
            FileReader fin2 = new FileReader("login.txt"); 
            BufferedReader din2 = new BufferedReader(fin2);  
            while((line = din2.readLine())!= null){
                StringTokenizer st = new StringTokenizer(line, ":");
                line = st.nextToken().trim();
                if(Identity.equals(line)){
                    return true;
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(clientHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(clientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Identity Check Failed");
        return false;
    }
}
