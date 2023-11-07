package com.example.docbao;
public class New {
    private String Name;
    private String Url;
    private String Image;
    private  String Description;

    public New(String name, String url, String image, String description) {
        Name = name;
        Url = url;
        Image = image;
        Description = description;
    }

    public String getName() {
        return Name;
    }

    public String getUrl() {
        return Url;
    }

    public String getImage() {
        return Image;
    }

    public String getDescription() {
        return Description;
    }
}
