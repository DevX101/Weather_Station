/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package user;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import static user.UserClient.locationData;
import static user.UserClient.setServerOnline;
import static user.UserClient.stationData;
import static user.UserClient.FeildData;
import static user.UserClient.TempData;
import static user.UserClient.HumidityData;
import static user.UserClient.DateData;
import static user.UserClient.RainfallData;
import static user.UserClient.AirPData;
import static user.UserClient.jComboBox2;
/**
 *
 * @author User
 */
public class serverHandler implements Runnable{
    private Socket server;
    private BufferedReader in;
    
    public serverHandler(Socket Server) throws IOException{
        server = Server;
        in = new BufferedReader(new InputStreamReader(server.getInputStream()));
    }
    
    @Override
    public void run() {
        String command = "";
        try{
            while(true){
                System.out.println("[CLIENT THREAD] Waiting..");
                String serverResponse = in.readLine();
                System.out.println("[CLIENT THREAD] "+serverResponse);
                
                if(serverResponse == null){System.out.println("null message");break;}
                
                StringTokenizer st = new StringTokenizer(serverResponse, ":");
                command = st.nextToken().trim();
                
                switch (command) { 
                    case "updateStationListReply" -> {updateStationIDList(serverResponse);break;}
                    case "iAmAlive" -> {UserClient.setServerOnline();}
                    case "StationData" -> {
                        stationData.setText((jComboBox2.getSelectedItem().toString()));
                        locationData.setText((st.nextToken().trim()));
                        FeildData.setText((st.nextToken().trim()));
                        TempData.setText((st.nextToken().trim()));
                        HumidityData.setText((st.nextToken().trim()));
                        RainfallData.setText((st.nextToken().trim()));
                        AirPData.setText((st.nextToken().trim()));
                        DateData.setText((st.nextToken().trim())+":"+ (st.nextToken().trim())+":"+ (st.nextToken().trim()));
                        break;
                    }
                    default -> {System.out.println("Command: "+command + " not found in server handler :("); break;}
                }
                break;
            }
        }
        catch(IOException e){
            System.err.println("IOException in client handler");
            System.err.println(Arrays.toString(e.getStackTrace()));
        }
        finally{
            try {
                System.out.println("[CLIENT THREAD] "+ command +" - CLOSING THREAD.");
                in.close();
            } catch(IOException e){
                System.err.println("IOException in client handler");
                System.err.println(Arrays.toString(e.getStackTrace()));
            }
        }

    }

    public void updateStationIDList(String serverResponse) throws IOException {
        String message = serverResponse;
        StringTokenizer st = new StringTokenizer(message, ":");
        String stationID = st.nextToken().trim();
        int count = st.countTokens();
        String[] stations = new String[count];
        for (int i = 0; i < count; i++) {
            stationID = st.nextToken().trim();
            stations[i] = stationID; 
        }
        DefaultComboBoxModel dm = new DefaultComboBoxModel(stations);
        UserClient.jComboBox2.setModel(dm);
        //if(UserClient.jComboBox2 != null){
        //    UserClient.jComboBox2.removeAllItems();
        //   UserClient.jComboBox2.addItem("Please Select");
        //}
        //for (int i = 0; i < count; i++) {
        //    UserClient.jComboBox2.addItem(stations[i]);
        //}
    }
    
}
