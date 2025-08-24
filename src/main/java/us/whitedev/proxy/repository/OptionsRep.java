package us.whitedev.proxy.repository;

import java.util.HashMap;
import java.util.Map;

public class OptionsRep {
    private static OptionsRep optionsRep;
    private String username;
    private String password;
    private String serverIp;
    private int serverPort;
    private long motherDelay = 100;
    private boolean mother = true;
    private final Map<String, String[]> crashOptionsMap = new HashMap<>();

    public static OptionsRep getInstance(){
        if(optionsRep == null) optionsRep = new OptionsRep();
        return optionsRep;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public long getMotherDelay() {
        return motherDelay;
    }

    public void setMotherDelay(long motherDelay) {
        this.motherDelay = motherDelay;
    }

    public boolean isMother() {
        return mother;
    }

    public void setMother(boolean mother) {
        this.mother = mother;
    }

    public void addToCrashMap(String name, String[] args){
        this.crashOptionsMap.put(name, args);
    }

    public String[] getOptionsForCrash(String name){
        return this.crashOptionsMap.get(name);
    }
}
