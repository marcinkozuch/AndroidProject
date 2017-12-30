package com.example.kmarc.rainalert.api;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by PC on 2017-12-11.
 */

public class Weather {
    @SerializedName("id")
    @Expose
    public Long id;
    @SerializedName("main")
    @Expose
    public String main;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("icon")
    @Expose
    public String icon;
}
