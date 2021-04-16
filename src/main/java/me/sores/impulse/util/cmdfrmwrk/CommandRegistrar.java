package me.sores.impulse.util.cmdfrmwrk;

import me.sores.impulse.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.security.CodeSource;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by LavaisWatery on 10/25/2016.
 */
public class CommandRegistrar {
    private Plugin plugin;

    private SimpleCommandMap commandMap;

    public CommandRegistrar(Plugin plugin) {
        this.plugin = plugin;
        try {
            this.commandMap = ((SimpleCommandMap)Bukkit.getServer().getClass().getDeclaredMethod("getCommandMap", (Class[])new Class[0]).invoke(Bukkit.getServer(), new Object[0]));
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public BaseCommand constructFromAnnotation(final IBaseCommand base) {
        try {
            Method execute = base.getClass().getMethod("execute", new Class[] { CommandSender.class, String.class});
            if (execute.isAnnotationPresent(BaseCommandAnn.class)) {
                BaseCommandAnn commandAnn = (BaseCommandAnn)execute.getAnnotation(BaseCommandAnn.class);

                BaseCommand command = new BaseCommand(commandAnn.name(), commandAnn.permission() == null ? null : commandAnn.permission(), commandAnn.usageBy(), commandAnn.aliases())
                {
                    public void execute(CommandSender sender, String[] args)
                    {
                        base.execute(sender, args);
                    }
                };
                command.setMaxArgs(commandAnn.maxArgs());
                command.setMinArgs(commandAnn.minArgs());
                command.setUsage(commandAnn.usage());

                return command;
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return null;
    }

    public void loadCommandsFromPackage(String packageName) {
        for (Class<?> clazz : getClassesInPackage(packageName)) {
            System.out.println(String.valueOf(clazz.getName()) + "\n\n");
            if (BaseCommand.class.isAssignableFrom(clazz)) {
                try {
                    BaseCommand executor = (BaseCommand)clazz.newInstance();
                    registerCommand(executor.getName(), executor);
                }
                catch (Exception e) {
                    e.printStackTrace();
                    System.out.print(String.format("Could not load '%s', see: %s", new Object[] { clazz.getSimpleName(), e.getMessage() }));
                }
            }
        }
    }

    private ArrayList<Class<?>> getClassesInPackage(String pkgname) {
        ArrayList<Class<?>> classes = new ArrayList();
        CodeSource codeSource = Plugin.class.getClass().getProtectionDomain().getCodeSource();
        URL resource = codeSource.getLocation();
        String relPath = pkgname.replace('.', '/');
        String resPath = resource.getPath().replace("%20", " ");
        String jarPath = resPath.replaceFirst("[.]jar[!].*", ".jar").replaceFirst("file:", "");
        JarFile jFile;
        try {
            jFile = new JarFile(jarPath);
        }
        catch (IOException e) {
            throw new RuntimeException("Unexpected IOException reading JAR File '" + jarPath + "'", e);
        }
        Enumeration<JarEntry> entries = jFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = (JarEntry)entries.nextElement();
            String entryName = entry.getName();
            String className = null;
            if ((entryName.endsWith(".class")) && (entryName.startsWith(relPath)) && (entryName.length() > relPath.length() + "/".length())) {
                className = entryName.replace('/', '.').replace('\\', '.').replace(".class", "");
            }
            if (className != null) {
                Class<?> c = null;
                try {
                    c = Class.forName(className);
                }
                catch (ClassNotFoundException e2) {
                    e2.printStackTrace();
                }
                if (c != null) {
                    classes.add(c);
                }
            }
        }
        try {
            jFile.close();
        }
        catch (IOException e3) {
            e3.printStackTrace();
        }
        return classes;
    }

    public void registerCommand(String cmd, BaseCommand executor) throws Exception {
        PluginCommand command = Bukkit.getServer().getPluginCommand(cmd.toLowerCase());
        if (command == null) {
            Constructor<?> constructor = PluginCommand.class.getDeclaredConstructor(new Class[] { String.class, Plugin.class });
            constructor.setAccessible(true);
            command = (PluginCommand)constructor.newInstance(new Object[] { cmd, plugin});
        }
        command.setExecutor(executor);
        List<String> list = Arrays.asList(executor.aliases);
        command.setAliases(list);
        if (command.getAliases() != null) {
            for (String alias : command.getAliases()) {
                unregisterCommand(alias);
            }
        }
        if ((executor.getPermission() != null) && (!executor.getPermission().isEmpty())) {
            command.setPermission(executor.getPermission());
        }
        else {
            StringUtil.log("Command has no permission");
            command.setPermission("");
        }
        if (executor.getUsage() != null) {
            command.setUsage(executor.getUsage());
        }
        try {
            Field field = executor.getClass().getDeclaredField("description");
            field.setAccessible(true);
            if ((field != null) && ((field.get(executor) instanceof String))) {
                command.setDescription(ChatColor.translateAlternateColorCodes('&', (String)field.get(executor)));
            }
        }
        catch (Exception ex) {}
        this.commandMap.register(cmd, command);
    }

    public void unregisterCommand(String name) {
        try {
            Field known = SimpleCommandMap.class.getDeclaredField("knownCommands");
            Field alias = SimpleCommandMap.class.getDeclaredField("aliases");
            known.setAccessible(true);
            alias.setAccessible(true);
            Map<String, Command> knownCommands = (Map)known.get(this.commandMap);
            Set<String> aliases = (Set)alias.get(this.commandMap);
            knownCommands.remove(name.toLowerCase());
            aliases.remove(name.toLowerCase());
        }
        catch (Exception ex) {}
    }
}

