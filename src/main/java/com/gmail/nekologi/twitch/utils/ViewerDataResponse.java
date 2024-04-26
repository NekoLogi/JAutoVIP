package com.gmail.nekologi.twitch.utils;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ViewerDataResponse {
    @SerializedName("ID")
    public List<ViewerData> Data;
}
