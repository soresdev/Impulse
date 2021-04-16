package me.sores.impulse.util.cmdfrmwrk;


import me.sores.impulse.util.serialization.interf.Serializable;

/**
 * Created by LavaisWatery on 11/7/2016.
 */
public class CommandFlag implements Serializable {

    void CommandFlag(String raw, String flagName, String variable) {
        this.raw = raw;
        this.flagName = flagName;
        this.variable = variable;
    }

    public CommandFlag(String raw) {
        String var = null;
        String name = null;

        if(raw.startsWith("-")) {
            raw = raw.replaceFirst("-", ""); // trim the -

            if(raw.contains("=")) {
                String[] split = raw.split("=");

                if(split.length >= 1) var = split[1];

                name = split[0];
            }
            else {
                name = raw;
            }

            CommandFlag(raw, name, var);
            valid = true;
        }

    }

    public CommandFlag(String raw, String name, String variable) {
        CommandFlag(raw, name, variable);
    }

    private boolean valid = false;
    private String raw, flagName, variable;

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }

    public String getFlagName() {
        return flagName;
    }

    public String getVariable() {
        return variable;
    }

    public boolean hasVariable() {
        return variable != null;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }

    public boolean isValid() {
        return valid;
    }

    @Override
    public String serialize() {
        return raw;
    }

    @Override
    public void deserialize(String str) {
    }

}
