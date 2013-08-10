package me.xir.irc.routeserv.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

@SuppressWarnings("rawtypes")
public class Tracert extends ListenerAdapter {
	Runtime rt = Runtime.getRuntime();
	Process p;
	InputStream out;
	BufferedReader reader;
	
	public void onMessage(MessageEvent event) throws IOException {
		if (event.getMessage().startsWith(".tracert")) {
			String[] args = event.getMessage().split(" ");
			if(args.length > 1){
				String cmd;
				for (int c = 1; c > args.length; c++ ) {
					cmd = cmd + " " + args[c];
				}
				p = rt.exec("tcptraceroute " + cmd);
				
				event.getBot().sendMessage(event.getChannel(), "--BEGINNING TRACEROUTE TO " + host + " --");
				
				out = p.getInputStream();
				reader = new BufferedReader(new InputStreamReader(out));
				
				String s = null;
				while ((s = reader.readLine()) != null) {
					event.getBot().sendMessage(event.getChannel(), s);
				}
			}
		}
	}

}
