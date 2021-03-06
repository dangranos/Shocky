import java.io.Console;
import java.io.File;

import pl.shockah.shocky.Module;
import pl.shockah.shocky.Shocky;
import pl.shockah.shocky.cmds.Command;
import pl.shockah.shocky.cmds.CommandCallback;
import pl.shockah.shocky.cmds.Parameters;

public class ModuleConsoleOld extends Module {
	private static ThreadOldConsoleInput ConsoleThread = new ThreadOldConsoleInput();
	private static Boolean Stop = false;
	public static Console c = null;
	
	public String name() {
		return "consoleold";
	}
	
	public void onEnable(File dir) {
		ConsoleThread.start();
	}
	
	public void onDisable() {
		Stop = true;
		ConsoleThread = null;
	}
	
	private static class ThreadOldConsoleInput extends Thread {
		public void run() {
			c = System.console();
			if (c == null) return;
			
			String line;
			while (Stop==false) {
				line = c.readLine();
				if (line != null) {
					CommandCallback callback = new CommandCallback();
					String[] args = line.split("\\s+", 2);
					Command cmd = Command.getCommand(null,null,null,Command.EType.Console,callback,args[0]);
					if (cmd != null) {
						String s = (args.length == 1) ? "" : args[1];
						Parameters params = new Parameters(null,Command.EType.Console,null,null,s);
						try {
							cmd.doCommand(params,callback);
						} catch (Exception e) {
							Shocky.sendConsole(e.toString());
							continue;
						}
					}
					if (callback.length()>0)
						Shocky.sendConsole(callback.toString());
				}
			}
		}
	}
}
