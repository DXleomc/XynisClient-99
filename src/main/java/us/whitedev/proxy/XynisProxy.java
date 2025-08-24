package us.whitedev.proxy;

import com.github.steveice10.mc.protocol.MinecraftProtocol;
import com.github.steveice10.packetlib.Server;
import com.github.steveice10.packetlib.tcp.TcpServer;
import us.whitedev.proxy.functions.crashers.CrashHandler;
import us.whitedev.proxy.functions.crashers.impl.*;
import us.whitedev.proxy.listeners.ServerLoginListener;
import us.whitedev.proxy.listeners.ServerPingListener;
import us.whitedev.proxy.utils.LogType;
import us.whitedev.proxy.utils.ProxyLogger;

import java.util.ArrayList;
import java.util.List;

public class XynisProxy {
    public static List<String> LOGS = new ArrayList<>();
    public static boolean DEBUG_MODE = false;
    private static final CrashHandler crashHandler = CrashHandler.getInstance();
    public static final String VERSION = "2.0";
    public static final String HOST = "127.0.0.1";
    public static final int PORT = 1337;
    public static boolean IS_ENABLED = false;

    public static void loadProxy() {
        try {
            Server server = new TcpServer(HOST, PORT, MinecraftProtocol::new);
            ProxyLogger.send(String.format("Trying to port new server on %d", PORT), LogType.INFO);
            ProxyLogger.send("Registering global flags...", LogType.INFO);
            server.setGlobalFlag("compression-threshold", 256);
            server.setGlobalFlag("verify-users", false);
            server.setGlobalFlag("info-builder", new ServerPingListener());
            server.setGlobalFlag("login-handler", new ServerLoginListener());
            ProxyLogger.send("Registered all global flags!", LogType.INFO);
            ProxyLogger.send("Registering crash methods...", LogType.INFO);
            crashHandler.registerMethods(
                    new BotNuke1(),
                    new BotExclusive1(),
                    new BotExclusive2(),
                    new BotCreative1(),
                    new BotCreative2(),
                    new BotAbuser2(),
                    new BotAbuser3(),
                    new BotPhantom1()
            );
            ProxyLogger.send("Registered all crash methods", LogType.INFO);
            ProxyLogger.send(String.format("Proxy run on -> %s:%d", HOST, PORT), LogType.INFO);
            server.bind();
            IS_ENABLED = true;
        }catch (Exception e){
            ProxyLogger.send("Proxy can't be run! Read StackTrace to more details:", LogType.ERROR);
            e.printStackTrace();
        }
    }
}