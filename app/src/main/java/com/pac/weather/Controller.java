package com.pac.weather;

public class Controller {
    private static NotificationCenter notificationCenter;
    private static Controller controller;
    private DispatchQueue dispatchQueue = new DispatchQueue("Controller");

    private Controller() {
    }

    public static Controller getInstance(NotificationCenter notificationCenter) {
        if(controller == null) {
            controller = new Controller();
            Controller.notificationCenter = notificationCenter;
        }
        return controller;
    }
}
