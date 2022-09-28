/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package weatherstation;

/**
 *
 * @author Admin
 */

import java.net.*; 
import java.io.*;
import java.util.*;
import java.math.*;

import server.*;
import user.*;

//Weather attributes
/*
    GPS positioning, temperature, humidity, rainfall, air pressure, 
*/

interface weatherSensor{
    double getReading();
}

class Temperature implements weatherSensor{ //units °C
    
    private double temp;
    private double min=25,max=35;
    
    public Temperature() {
        temp = min;
    }
    
    @Override
    public double getReading(){
        
        temp = min + new Random().nextDouble() * (max - min);
        temp = Math.round(temp*100.0)/100.0;
            
        return temp;
    }
}

class Humidity implements weatherSensor{ //units %
    private int humidity;
    private int min=30,max=50;
    
    public Humidity(){
        humidity = min;
    }
    
    @Override
    public double getReading(){
        Random rand = new Random();
        humidity = rand.nextInt(max-min)+min;
        
        return humidity;
    }
}

class Rainfall implements weatherSensor{ //units mm
    private double rainfall;
    private double min=2,max=8;
    
    public Rainfall(){
        rainfall = min;
    }

    @Override 
    public double getReading(){
        rainfall = min + new Random().nextDouble() * (max - min);
        rainfall = Math.round(rainfall*100.0)/100.0;
        
        return rainfall;
    }
}

class AirPressure implements weatherSensor{ //units Hg
    private double pressure;
    private double min=28,max=31;
    
    public AirPressure(){
       pressure = min; 
    }
    
    @Override 
    public double getReading(){
        pressure = min + new Random().nextDouble() * (max - min);
        pressure = Math.round(pressure*100.0)/100.0;
        
        return pressure;
    }
}

public class wsClient {
    
    private static Socket client;
    private static final String Client_IP = "127.0.0.1";
    private static final int Client_PORT = 9090;
    
    private BufferedReader in;
    private PrintWriter out;
    
    private Date date;
    private Temperature temperature;
    private Humidity humidity;
    private Rainfall rainfall;
    private AirPressure airpressure;
    
    public wsClient() {
        temperature = new Temperature();
        humidity = new Humidity();
        airpressure = new AirPressure();
        rainfall = new Rainfall();
    }
    
    private String getID() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter station ID: ");
        String location = sc.nextLine();
        return location;
    }
    
    //GPS location of station
    private String getLocation() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter station location: "); 
        String location = sc.nextLine();
        return location;
    }
    
    //Description of field
    private String getField(){
       ArrayList<String> fieldData = new ArrayList<String>();
       
        Scanner sc = new Scanner(System.in);
        
        System.out.print("Enter area of field: ");
        String area = sc.nextLine();
        fieldData.add(area);
       
       return area;
    } 
    
    public void run() throws IOException {
        String constData;
        String StationID;
        String Location;
        String FieldArea;
        // Establish connection
        Socket socket = new Socket(Client_IP, Client_PORT);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        StationID = getID();
        Location = getLocation();
        FieldArea = getField();
        constData = StationID+":"+Location+":"+FieldArea;
        
           try{
               while(true){
                    date = new Date();
                    System.out.println("\nRetrieving data...");
                    String weatherData = StationID+":StationData:Station:"+constData+":"+String.valueOf(temperature.getReading())+":"+String.valueOf(humidity.getReading())+":"+String.valueOf(rainfall.getReading())+":"+String.valueOf(airpressure.getReading())+":"+date;
                    
                    System.out.println(weatherData);
                    out.println(weatherData); //Send weather data to server
                    
                    System.out.println("Weather data sent to server");
                    Thread.sleep(5000); //Retrieves reading every 5 seconds
               }
            }catch(InterruptedException ie) {
                System.err.println(ie.getMessage());
            }
    }
    
    public static void main(String[] args) throws IOException{
        
            wsClient weatherSensor = new wsClient();
            weatherSensor.run();
       
    }
}
