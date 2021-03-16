package me.sores.spark.util.serialization.interf;

/**
 * Created by LavaisWatery on 11/7/2016.
 */
public interface Serializable {

    public String serialize();

    public void deserialize(String str);

}
