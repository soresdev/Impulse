package me.sores.impulse.util.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

/**
 * Created by sores on 4/14/2021.
 */
public class MongoBase {

    private String host, username, password, databaseName, collectionName;

    private MongoDatabase database;
    private MongoCollection<Document> collection;

    public MongoBase(String host, String username, String password, String databaseName, String collectionName) {
        this.host = host;
        this.username = username;
        this.password = password;
        this.databaseName = databaseName;
        this.collectionName = collectionName;

        setup();
    }

    private void setup(){
        MongoClient client = MongoClients.create(createURI());
        database = client.getDatabase(databaseName);
        collection = database.getCollection(collectionName);
    }

    private String createURI(){
        return "mongodb+srv://" + this.username + ":" + this.password + "@" + this.host + "/" + databaseName;
    }

    public MongoDatabase getDatabase() {
        return database;
    }

    public MongoCollection<Document> getCollection() {
        return collection;
    }

}
