package us.whitedev.proxy.listeners;

import com.github.steveice10.mc.protocol.data.status.PlayerInfo;
import com.github.steveice10.mc.protocol.data.status.ServerStatusInfo;
import com.github.steveice10.mc.protocol.data.status.VersionInfo;
import com.github.steveice10.mc.protocol.data.status.handler.ServerInfoBuilder;
import com.github.steveice10.packetlib.Session;
import net.kyori.adventure.text.Component;
import us.whitedev.proxy.XynisProxy;
import us.whitedev.proxy.utils.ColorUtil;

import java.util.ArrayList;

public class ServerPingListener implements ServerInfoBuilder {

    @Override
    public ServerStatusInfo buildInfo(Session session) {
        return new ServerStatusInfo(
                new VersionInfo("§7GodIsKing0584 x GodIsKingClient", 763),
                new PlayerInfo(1, 0, new ArrayList<>()),
                Component.text(ColorUtil.format(
                        "&b&lGodIsKing&f&lClient &9&lProxy &8- &7Created by &f0GodIsKing\n"
                                + "&8  [-------- &7Proxy Version&8: &f&l" + XynisProxy.VERSION + " &8--------]"
                )),
                null,
                false
        );
    }
}
