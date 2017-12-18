package com.example.pc.weathernow.api;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by PC on 2017-12-11.
 */

public class Sys {
    @SerializedName("type")
    @Expose
    public Long type;
    @SerializedName("id")
    @Expose
    public Long id;
    @SerializedName("message")
    @Expose
    public Double message;
    @SerializedName("country")
    @Expose
    public String country;
    @SerializedName("sunrise")
    @Expose
    public Long sunrise;
    @SerializedName("sunset")
    @Expose
    public Long sunset;
}
