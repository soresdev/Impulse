package me.sores.impulse.util.serialization.interf;


import me.sores.impulse.util.json.JSONObject;

/**
 * Created by LavaisWatery on 5/13/2017.
 */
public interface JsonSerializable {

    public JSONObject serialize();

    public void deserialize(JSONObject object);

}
