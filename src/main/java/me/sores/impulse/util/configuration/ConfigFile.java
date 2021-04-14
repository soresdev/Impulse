package me.sores.impulse.util.configuration;

import me.sores.impulse.util.StringUtil;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by sores on 3/3/2020.
 */
public class ConfigFile extends YamlConfiguration {

    private File file;

    public ConfigFile(String name, Plugin plugin) {
        this.file = new File(plugin.getDataFolder(), name);

        if(!this.file.exists()) {
            plugin.saveResource(name, false);
        }

        try {
            this.load(this.file);
        } catch(IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            this.save(this.file);
        } catch(IOException e) {
            StringUtil.log("&cFailed to save file &f" + file.getName() + " &cwith exception: ");
            e.printStackTrace();
        }
    }

    @Override
    public int getInt(String path) {
        return super.getInt(path, 0);
    }

    @Override
    public double getDouble(String path) {
        return super.getDouble(path, 0.0);
    }

    @Override
    public boolean getBoolean(String path) {
        return super.getBoolean(path, false);
    }

    @Override
    public String getString(String path) {
        return StringUtil.color(super.getString(path, ""));
    }

    @Override
    public List<String> getStringList(String path) {
        return super.getStringList(path).stream().map(StringUtil::color).collect(Collectors.toList());
    }

    @Override
    public boolean contains(String path){
        return super.contains(path);
    }

    @Override
    public void set(String path, Object value){
        super.set(path, value);
        save();
    }

    public File getFile() {
        return file;
    }

}
