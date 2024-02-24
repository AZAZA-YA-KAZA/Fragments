package com.example.androidd;

import android.graphics.Bitmap;

import java.util.BitSet;

public class Values {
    private String Name;
    private  String Value;
    private Bitmap pictures;

    public Values(String name, String value, Bitmap pictures){
        Name = name;
        Value = value;
        this.pictures = pictures;
    }

    public String getName() {
        return Name;
    }

    public String getValue() {
        return Value;
    }

    public Bitmap getPicture() {
        return pictures;
    }
}
