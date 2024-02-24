package com.example.androidd;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class ValuteFragment extends Fragment {
    ArrayList<Values> values;

    Handler handler;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataThread dataThread = new DataThread();
        dataThread.start();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment,container, false);
        //ArrayList<Values> values = new ArrayList<>();
        /*values.add(new Values("ghjkl", "100.3", null));
        values.add(new Values("ghjkl", "100.3", null));
        values.add(new Values("ghjkl", "100.3", null));
        values.add(new Values("ghjkl", "100.3", null));
        values.add(new Values("ghjkl", "100.3", null));
        values.add(new Values("ghjkl", "100.3", null));*/

        RecyclerView recyclerView = view.findViewById(R.id.RV);
        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg){
                super.handleMessage(msg);

                values = (ArrayList<Values>) msg.obj;
                ValuteAdapter valuteAdapter = new ValuteAdapter(values);
                recyclerView.setAdapter(valuteAdapter);

            }
        };
        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.SW);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recyclerView.removeAllViews();
                values.clear();
                DataThread dataThread = new DataThread();
                dataThread.start();
                if(swipeRefreshLayout.isRefreshing()){
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
        return view;
    }

    class DataThread extends  Thread{
        ArrayList<Values> values = new ArrayList<>();
        @Override
        public void run() {
            super.run();
            try {
                Bitmap bitmap = null;
                URL infolink = new URL("https://www.cbr-xml-daily.ru/daily_json.js");
                URL pictureslink = new URL("https://gist.github.com/sanchezzzhak/8606e9607396fb5f8216/raw/8a7209a4c1f4728314ef4208abc78be6e9fd5a2f/ISO3166_RU.json");
                String infoString = "";
                String pictureString = "";
                Scanner in = new Scanner(infolink.openStream());
                while (in.hasNext()){
                    infoString+=in.nextLine();
                }
                in.close();
                in = new Scanner(pictureslink.openStream());
                while (in.hasNext()){
                    pictureString+=in.nextLine();
                }
                in.close();
                JSONObject jsoninfo = new JSONObject(infoString);
                JSONArray jsonpicture = new JSONArray(pictureString);

                JSONObject jsonValuets = jsoninfo.getJSONObject("Valute");
                for(int i = 0;i<jsonValuets.names().length();i++){
                    JSONObject jsonvalute = jsonValuets.getJSONObject(jsonValuets.names().getString(i));
                    String charcode = jsonvalute.getString("CharCode").substring(0, 2);
                    for (int j = 0;j<jsonpicture.length();j++){
                        if (jsonpicture.getJSONObject(j).getString("iso_code2").equals(charcode)){
                            String picurl = jsonpicture.getJSONObject(j).getString("flag_url");
                            URL url = new URL("https:"+picurl);

                            //уровень соединения
                            HttpURLConnection con = (HttpURLConnection) url.openConnection();
                            con.setConnectTimeout(1000);
                            con.setReadTimeout(1000);

                            con.connect();

                            int recponseode = con.getResponseCode();
                            if (recponseode ==200){
                                InputStream inputStream = con.getInputStream();
                                bitmap = BitmapFactory.decodeStream(inputStream);
                            }
                        }
                    }
                    values.add(new Values(jsonvalute.getString("Name"), jsonvalute.getString("Value"), bitmap));
                }


                Message msg = new Message();
                msg.obj = values;
                handler.sendMessage(msg);

            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
