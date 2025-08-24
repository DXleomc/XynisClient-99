package us.whitedev.proxy.utils;

import us.whitedev.proxy.XynisProxy;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ProxyLogger {

    private static String getTime(){
        LocalTime now = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return now.format(formatter);
    }

    public static void send(String message, LogType logType){
        XynisProxy.LOGS.add(String.format("[%s] [%s] %s%n", getTime(), logType.name(), message));
    }
}
