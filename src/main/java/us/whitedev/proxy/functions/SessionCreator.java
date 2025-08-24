package us.whitedev.proxy.functions;

import com.github.steveice10.mc.auth.data.GameProfile;
import com.github.steveice10.mc.protocol.MinecraftProtocol;
import com.github.steveice10.packetlib.Session;
import com.github.steveice10.packetlib.tcp.TcpClientSession;
import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import org.jetbrains.annotations.NotNull;
import us.whitedev.helpers.MessageHelper;
import us.whitedev.proxy.listeners.BotListener;
import us.whitedev.proxy.listeners.ServerTransferListener;
import us.whitedev.proxy.repository.BotsRep;
import us.whitedev.proxy.repository.OptionsRep;
import us.whitedev.proxy.repository.SessionRep;
import us.whitedev.proxy.utils.RandomHelper;
import us.whitedev.proxy.utils.SessionType;

import java.math.BigInteger;
import java.util.UUID;

public class SessionCreator {
    private final SessionRep sessionRep = SessionRep.getInstance();
    private final OptionsRep optionsRep = OptionsRep.getInstance();
    private final BotsRep botsRep = BotsRep.getInstance();
    private final RandomHelper randomHelper = RandomHelper.getRandomHelper();
    private final MessageHelper msgHelper = new MessageHelper();

    public void initSession(Session originalSession, boolean isBot, String name, String password){
        SessionType sessionType = SessionType.CRACKED;
        if(name == null || name.isEmpty()){
            optionsRep.setUsername(randomHelper.getRandomString(randomHelper.getRandomInt(6,12), false));
        }else if(password != null && !password.isEmpty()){
            optionsRep.setUsername(name);
            optionsRep.setPassword(password);
            sessionType = SessionType.PREMIUM;
        }else{
            optionsRep.setUsername(name);
        }
        Session session = null;
        switch (sessionType){
            case CRACKED -> session = createNewSession(originalSession, isBot);
            case PREMIUM -> session = createNewPremiumSession(originalSession, isBot);
        }
        if(session != null && session.isConnected()){
            msgHelper.sendMessage("&fProxy &8-> &7A new session has &ajoined &7the server! &8(&f" + optionsRep.getUsername() + "&8)", true);
        }else{
            msgHelper.sendMessage("&fProxy &8-> &cFailed &7to connect new session!", true);
        }
    }

    public Session createNewSession(Session originalSession, boolean isBot) {
        MinecraftProtocol protocol = new MinecraftProtocol(optionsRep.getUsername() + (isBot ? botsRep.getConnectedSessions().size() : ""));
        Session session = new TcpClientSession(optionsRep.getServerIp(), optionsRep.getServerPort(), protocol);
        if(isBot) {
            botsRep.addSession(session);
            session.connect();
            session.addListener(new BotListener());
        }else{
            sessionRep.setSession(session);
            session.connect();
            session.addListener(new ServerTransferListener(originalSession));
        }
        return session;
    }

    public Session createNewPremiumSession(Session originalSession, boolean isBot) {
        MicrosoftAuthResult result;
        MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
        try {
            result = authenticator.loginWithCredentials(optionsRep.getUsername(), optionsRep.getPassword());
        } catch (MicrosoftAuthenticationException e) {
            throw new RuntimeException(e);
        }
        Session session = getSession(result);
        if(isBot) {
            botsRep.addSession(session);
            session.connect();
            session.addListener(new BotListener());
        }else{
            sessionRep.setSession(session);
            session.connect();
            session.addListener(new ServerTransferListener(originalSession));
        }
        return session;
    }

    private @NotNull Session getSession(MicrosoftAuthResult result) {
        String uuid = result.getProfile().getId();
        BigInteger uuidGen0 = new BigInteger(uuid.substring(0, 16), 16);
        BigInteger uuidGen1 = new BigInteger(uuid.substring(16, 32), 16);
        UUID parsedUuid = new UUID(uuidGen0.longValue(), uuidGen1.longValue());
        GameProfile gameProfile = new GameProfile(parsedUuid, result.getProfile().getName());
        MinecraftProtocol protocol = new MinecraftProtocol(gameProfile, result.getAccessToken());
        return new TcpClientSession(optionsRep.getServerIp(), optionsRep.getServerPort(), protocol);
    }

}
