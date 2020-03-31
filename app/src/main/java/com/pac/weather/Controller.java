package com.pac.weather;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Controller {
    private final String filePath = "";
    private static NotificationCenter notificationCenter;
    private static Controller controller;
    public DispatchQueue dispatchQueue = new DispatchQueue("Controller");

    private Controller() {
    }

    public static Controller getInstance(NotificationCenter notificationCenter) {
        if(controller == null) {
            controller = new Controller();
            Controller.notificationCenter = notificationCenter;
        }
        return controller;
    }

    private void writeDataToFile(ArrayList<Weather> dailyForecast){
        try {

            FileOutputStream file = new FileOutputStream(filePath);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(file);
            objectOutputStream.writeObject(dailyForecast);
            objectOutputStream.close();
            file.close();
            System.err.println("Database was succesfully written to the file");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private ArrayList<Weather> readDataFromFile(){
        try {

            FileInputStream fileIn = new FileInputStream(filePath);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileIn);
            ArrayList<Weather> obj = (ArrayList<Weather>)objectInputStream.readObject();
            objectInputStream.close();
            fileIn.close();
            System.err.println("Database has been read from the file");
            return obj;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
