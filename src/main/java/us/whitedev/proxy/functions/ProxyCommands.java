package us.whitedev.proxy.functions;

import com.github.steveice10.packetlib.Session;
import us.whitedev.proxy.repository.BotsRep;
import us.whitedev.proxy.repository.OptionsRep;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ProxyCommands {
    private final SessionCreator sessionCreator = new SessionCreator();
    private final BotsRep botsRep = BotsRep.getInstance();
    private final OptionsRep optionsRep = OptionsRep.getInstance();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public void onConnectCommand(String arg){
        if (arg.contains(":")) {
            String[] data = arg.split(":");
            sessionCreator.initSession(null, true, data[0], data[1]);
        } else {
            sessionCreator.initSession(null, true, arg, null);
        }
    }

    public void onFloodCommand(int flood, int delayIncrement){
        long initialDelay = 0;
        for (int i = 0; i < flood; i++) {
            long delay = initialDelay;
            scheduler.schedule(() -> sessionCreator.initSession(null, true, null, null), delay, TimeUnit.MILLISECONDS);
            initialDelay += delayIncrement;
        }
    }

    public void onDisconnectAllCommand(){
        botsRep.disconnectAll();
    }

    public void onMotherCommand(boolean isMother, int motherDelay){
        optionsRep.setMother(isMother);
        optionsRep.setMotherDelay(motherDelay);
    }
}
