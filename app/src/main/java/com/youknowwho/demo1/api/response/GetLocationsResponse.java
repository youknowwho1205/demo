
package com.youknowwho.demo1.api.response;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.youknowwho.demo1.api.response.models.Fromcentral;
import com.youknowwho.demo1.api.response.models.Location;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class GetLocationsResponse extends BaseResponse{

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("fromcentral")
    @Expose
    private Fromcentral fromcentral;
    @SerializedName("location")
    @Expose
    private Location location;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Fromcentral getFromcentral() {
        return fromcentral;
    }

    public void setFromcentral(Fromcentral fromcentral) {
        this.fromcentral = fromcentral;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public static ArrayList<GetLocationsResponse> fromJson(String pResult) {

        Gson lGson = new Gson();

        try {
            Type listType = new TypeToken<ArrayList<GetLocationsResponse>>() {}.getType();
            return lGson.fromJson(pResult, listType);
        } catch (JsonSyntaxException e) {
            return null;
        }

    }

}
