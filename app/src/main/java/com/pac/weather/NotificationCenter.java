package com.pac.weather;

import java.util.ArrayList;
import java.util.List;

public class NotificationCenter {
    private List<Observer> observers = new ArrayList<>();
    private static NotificationCenter notificationCenter;

    public static NotificationCenter getInstance(){
        if(notificationCenter == null)
            notificationCenter = new NotificationCenter();
        return notificationCenter;
    }

    public interface Observer {
        void update();
        void oWait();
        void release();
    }

    public void register(Observer observer){
        observers.add(observer);
    }

    public void unRegister(Observer observer) {
        observers.remove(observer);
    }

    public void waitObservers(){
        for (Observer observer : observers) {
            observer.oWait();
        }
    }

    public void releaseObservers(){
        for (Observer observer : observers) {
            observer.release();
        }
    }

    public void notifyObservers(){
        for (Observer observer : observers) {
            observer.update();
        }
    }
}
