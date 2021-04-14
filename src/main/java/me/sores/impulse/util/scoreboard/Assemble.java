package me.sores.impulse.util.scoreboard;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Assemble {

	//Instance
	private static Assemble instance;

	private JavaPlugin plugin;
	private AssembleAdapter adapter;
	private Map<UUID, AssembleBoard> boards;
	private AssembleThread thread;

	//Scoreboard Ticks
	private long ticks = 2;

	public void setTicks(long ticks) {
		this.ticks = ticks;
	}

	//Default Scoreboard Style
	private AssembleStyle assembleStyle = AssembleStyle.MODERN;

	public void setAssembleStyle(AssembleStyle assembleStyle) {
		this.assembleStyle = assembleStyle;
	}

	public Assemble(JavaPlugin plugin, AssembleAdapter adapter) {
		if (instance != null) {
			throw new RuntimeException("Assemble has already been instantiated!");
		}

		if (plugin == null) {
			throw new RuntimeException("Assemble can not be instantiated without a plugin instance!");
		}

		instance = this;

		this.plugin = plugin;
		this.adapter = adapter;
		this.boards = new ConcurrentHashMap<>();

		this.setup();
	}

	private void setup() {
		//Register Events
		this.plugin.getServer().getPluginManager().registerEvents(new AssembleListener(), this.plugin);

		//Ensure that the thread has stopped running
		if (this.thread != null) {
			this.thread.stop();
			this.thread = null;
		}

		//Start Thread
		this.thread = new AssembleThread(this);
	}

	public AssembleAdapter getAdapter() {
		return adapter;
	}

	public Map<UUID, AssembleBoard> getBoards() {
		return boards;
	}

	public AssembleThread getThread() {
		return thread;
	}

	public long getTicks() {
		return ticks;
	}

	public AssembleStyle getAssembleStyle() {
		return assembleStyle;
	}

	public static Assemble getInstance() {
		return instance;
	}

	public JavaPlugin getPlugin() {
		return plugin;
	}
}
