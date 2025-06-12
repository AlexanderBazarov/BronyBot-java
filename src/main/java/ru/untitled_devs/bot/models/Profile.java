package ru.untitled_devs.bot.models;

import com.mongodb.client.model.geojson.Point;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import org.bson.types.ObjectId;

@Entity("Profiles")
public class Profile implements Model {
    @Id
    private ObjectId id;

    private String description;
    private int age;
    private String city;
    private String image;

    private Point coordinates;

    private boolean show;

}
