package com.example.docbao;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    MyAdapter myAdapter;
    ListView lw;
    ArrayList<New> newArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView2);
        lw = findViewById(R.id.lw);
        newArrayList = new ArrayList<>();
        myAdapter = new MyAdapter(this, R.layout.item_new, newArrayList);
        lw.setAdapter(myAdapter);
        new ReadRss().execute("https://vtv.vn/trong-nuoc.rss");
        lw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, TrangDocBao.class);
                New a = newArrayList.get(i);
                intent.putExtra("link",a.getUrl());
                startActivity(intent);
            }
        });
    }

    private class ReadRss extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            StringBuffer content = new StringBuffer();
            try {
                URL url = new URL(strings[0]);
                InputStreamReader inputStreamReader = new InputStreamReader(url.openConnection().getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String line = "";
                while ((line = bufferedReader.readLine()) != null){
                    content.append(line);
                }
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return content.toString();
        }
        @Override
        protected void onPostExecute(String s){
            super.onPostExecute(s);

            XMLDOMParser parser = new XMLDOMParser();

            Document document = parser.getDocument(s);
            String Title = null;
            NodeList nodeTitle = document.getElementsByTagName("title");
            if (nodeTitle.getLength() > 0) {
                Element element = (Element) nodeTitle.item(0);
                Title = element.getTextContent();
            }
            textView.setText(Title);

            NodeList nodeList = document.getElementsByTagName("item");
            String tieuDe = "";
            String name = null, img = null, link = null, description = null;
            for (int i = 0; i < nodeList.getLength(); i++){
                Element element = (Element) nodeList.item(i);
                NodeList nodeListTitle = element.getElementsByTagName("title");
                if (nodeListTitle.getLength() > 0){
                    Element elementTitle = (Element) nodeListTitle.item(0);
//                    tieuDe += elementTitle.getTextContent() + "\n";
                    name = elementTitle.getTextContent();
                }

                NodeList nodeListLink = element.getElementsByTagName("link");
                if (nodeListLink.getLength() > 0){
                    Element elementLink = (Element) nodeListLink.item(0);
                    link = elementLink.getTextContent();
                }

                NodeList nodeDescription = element.getElementsByTagName("description");
                if (nodeDescription.getLength() > 0){
                    Element eleDescription = (Element) nodeDescription.item(0);
                    tieuDe += extractJpgUrlFromHtml(eleDescription.getTextContent()) + "\n";
                    img = extractJpgUrlFromHtml(eleDescription.getTextContent());
                    description = extractTextAfterVTV(eleDescription.getTextContent());
                }
                newArrayList.add(new New(name, link, img, description));

            }
            myAdapter.notifyDataSetChanged();
//            Toast.makeText(MainActivity.this, tieuDe, Toast.LENGTH_SHORT).show();
        }
    }


    public static String extractJpgUrlFromHtml(String html) {
        Pattern pattern = Pattern.compile("img src=\\\"(.*?)\\\"");
        Matcher matcher = pattern.matcher(html);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }

    public static String extractTextAfterVTV(String html) {
        Pattern pattern = Pattern.compile("VTV\\.vn\\s*-\\s*(.*?)$");
        Matcher matcher = pattern.matcher(html);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }
}