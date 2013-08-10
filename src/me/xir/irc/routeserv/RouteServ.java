package me.xir.irc.routeserv;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

import me.xir.irc.routeserv.Config;
import me.xir.irc.routeserv.commands.*;

import org.pircbotx.PircBotX;
import org.pircbotx.UtilSSLSocketFactory;
import org.pircbotx.hooks.Listener;
import org.pircbotx.hooks.ListenerAdapter;

public class RouteServ extends ListenerAdapter implements Listener {

	public static PircBotX bot = new PircBotX();
	public final static Logger logger = Logger.getLogger("RouteServ");

	public static void main(String[] args) throws Exception, FileNotFoundException, IOException {

		try {
			Config.loadConfig();
		} catch (FileNotFoundException ex) {
			Config.config.setProperty("nick", "RouteServ");
			Config.config.setProperty("user", "RouteServ");
			Config.config.setProperty("server", "irc.xir.me");
			Config.config.setProperty("port", "6667");
			Config.config.setProperty("password", "");
			Config.config.setProperty("serverpassword", "");
			Config.config.setProperty("channels", "#opers, #services");
			Config.config.setProperty("ssl", "false");
			Config.config.setProperty("admins", "Cyberpew, SwooshyCueb");

			Config.config.store(new FileOutputStream("RouteServ.properties"), null);
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		bot.setAutoNickChange(true);
		bot.setVerbose(true);

		bot.setVersion("RouteServ - In Your Router");

		bot.setLogin(Config.user);
		bot.setName(Config.nick);
		bot.identify(Config.pass);

		//Connection start
		if(Config.SSL && !Config.serverpass.isEmpty()) {
			bot.connect(Config.server, Config.port, Config.serverpass, new UtilSSLSocketFactory().trustAllCertificates());			
		} else if (Config.SSL && Config.serverpass.isEmpty()) {
			bot.connect(Config.server, Config.port, new UtilSSLSocketFactory().trustAllCertificates());
		} else {
			bot.connect(Config.server, Config.port);
		}

		bot.setAutoReconnect(true);
		bot.setAutoReconnectChannels(true);

		joinChannels();
		loadListeners();
	}
	public static void joinChannels() {
		for (int i = 0; i < Config.channels.length; i++) {
			bot.joinChannel(Config.channels[i]);
		}
	}
	public static void loadListeners() throws Exception {
		bot.getListenerManager().addListener(new Tracert());

	}
}
