package com.example.docbao;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyAdapter extends ArrayAdapter {
    Activity context;
    int Idlayout;
    ArrayList<New> myList;
    public MyAdapter(Activity context, int Idlayout, ArrayList<New> myList) {
        super(context, Idlayout, myList);
        this.context = context;
        this.Idlayout = Idlayout;
        this.myList = myList;
    }

    @NonNull
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup
            parent) {
        LayoutInflater myInflactor = context.getLayoutInflater();
        convertView = myInflactor.inflate(Idlayout,null);
        New itemnew = myList.get(position);
        ImageView img = convertView.findViewById(R.id.imageView);

        TextView name = convertView.findViewById(R.id.textView);

        TextView description = convertView.findViewById(R.id.textView1);

        name.setText(itemnew.getName());
        description.setText(itemnew.getDescription());

//        URL url = null;
//        try {
//            url = new URL(itemnew.getUrl());
//        } catch (MalformedURLException e) {
//            throw new RuntimeException(e);
//        }
//        Bitmap bitmap = null;
//        try {
//            bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        img.setImageBitmap(bitmap);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                URL url = new URL(itemnew.getImage());
                Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                if (parent.getContext() instanceof Activity)
                    ((Activity)parent.getContext()).runOnUiThread(() -> img.setImageBitmap(bitmap));
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });


        return convertView;
    }
}
