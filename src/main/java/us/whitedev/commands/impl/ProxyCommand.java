package us.whitedev.commands.impl;

import us.whitedev.commands.Command;
import us.whitedev.proxy.XynisProxy;
import us.whitedev.proxy.functions.ProxyCommands;

import java.util.Locale;
import java.util.Objects;

public class ProxyCommand implements Command {
    private static final String COMMAND_NAME = "proxy";
    private final ProxyCommands proxyCommands = new ProxyCommands();

    @Override
    public String getName() {
        return COMMAND_NAME;
    }

    @Override
    public void onCommand(String[] args) {
        String proxyIp = XynisProxy.HOST + ":" + XynisProxy.PORT;
        if (mc.getConnection() != null && Objects.requireNonNull(mc.getConnection().getServerData()).ip.equals(proxyIp)) {
            if (args.length >= 2) {
                switch (args[1].toLowerCase(Locale.ROOT)) {
                    case "connect" -> {
                        if (args.length >= 3) {
                            msgHelper.sendMessage("&fProxy &8-> &7Connecting new session!", true);
                            proxyCommands.onConnectCommand(args[2]);
                        } else {
                            msgHelper.sendMessage("&fProxy &8-> &cIncorrect &7login format &8(&f\"name\" &7or &f\"email:password\"&8)", true);
                        }
                    }
                    case "flood" -> {
                        if (args.length >= 4) {
                            msgHelper.sendMessage("&fProxy &8-> &7Starting to add sessions!", true);
                            proxyCommands.onFloodCommand(Integer.parseInt(args[2]), Integer.parseInt(args[3]));
                        } else {
                            msgHelper.sendMessage("&fProxy &8-> &cIncorrect &7flood values format! &8(&f\"10 6000\"&8)", true);
                        }
                    }
                    case "disconnect-all" -> proxyCommands.onDisconnectAllCommand();
                    case "mother" -> {
                        if (args.length >= 4) {
                            proxyCommands.onMotherCommand(args[2].equals("true"), Integer.parseInt(args[3]));
                            msgHelper.sendMessage("&fProxy &8-> &aCorrectly &7set new values for mother settings!", true);
                        } else {
                            msgHelper.sendMessage("&fProxy &8-> &cIncorrect &7mother values format! &8(&f\"true 100\"&8)", true);
                        }
                    }
                    default -> displayAllOptions();
                }
            }else{
                displayAllOptions();
            }
        } else {
            msgHelper.sendMessage("To use proxy commands, connect to a proxy on " + proxyIp + "!", true);
        }
    }

    private void displayAllOptions(){
        msgHelper.sendSeparateLine();
        msgHelper.sendMessage("Proxy Commands:", true);
        msgHelper.sendMessage("&fconnect &7<name>&8/&7<email:password> &8- &7 Connect Bot to your target Server", true);
        msgHelper.sendMessage("&fflood &7<amount> <delay> &8- &7 Connect Bots to your target Server", true);
        msgHelper.sendMessage("&fdisconnect-all &8- &7 Disconnect all connected bots", true);
        msgHelper.sendMessage("&fmother &7<enable> <delay> &8- &7 Change settings for Mother Option", true);
        msgHelper.sendSeparateLine();
    }

}
