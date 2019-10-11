package com.klchen.subway.Graph;
import android.os.Build;
import android.os.StrictMode;
import android.util.Pair;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.alibaba.fastjson.*;

import javax.net.ssl.HttpsURLConnection;

public class DealGraph {
    private WeightedGraph weightedGraph;
    private HashMap<String,Integer> stationToVertical;
    private HashMap<Integer,String> verticalToStation;
    private HashMap<String,List<String>> lineStations;
    private int count;


    public DealGraph() throws FileNotFoundException, MalformedURLException {
        HashSet<String> ss = new HashSet<>();
        this.stationToVertical = new HashMap<>();
        verticalToStation = new HashMap<>();
        lineStations = new HashMap<>();
        count = 0;
        JSONObject file = null;
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        try {


            final URL url = new URL("http://106.14.117.206:8000/map.json");
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            conn.connect();
            InputStream inputStream=null;
            BufferedReader reader=null;
            if(conn.getResponseCode()==HttpURLConnection.HTTP_OK) {
                //获得连接的输入流
                inputStream = conn.getInputStream();
                //转换成一个加强型的buffered流
                reader = new BufferedReader(new InputStreamReader(inputStream));
                //把读到的内容赋值给result
                JSONReader jsonReader = new JSONReader(reader);
                file = ((JSONObject) jsonReader.readObject());
                reader.close();
                inputStream.close();
                conn.disconnect();
            }

            JSONArray map = file.getJSONArray("maps");
            JSONArray lines = ((JSONObject)map.get(0)).getJSONArray("lines");
            for (Object line : lines) {
                JSONArray stations = ((JSONObject) line).getJSONArray("stations");
                String linename = ((JSONObject) line).getString("subway-line-name");
                List<String> sta = new ArrayList<>();
                for (int i = 0;i < stations.size();i++) {
                    ss.add(stations.getObject(i,String.class));
                    sta.add(stations.getObject(i,String.class));
                }
                lineStations.put(linename,sta);
            }
//        System.out.println(lines);

            int N = ss.size();
            this.weightedGraph = new WeightedGraph(N);
            for (Object line : lines) {
                JSONArray stations = ((JSONObject) line).getJSONArray("stations");
                String[] sta = new String[stations.size()];
                for (int i = 0;i < stations.size();i++) {
                    sta[i] = stations.getObject(i,String.class);
                }
                for (int i = 0;i + 1 < stations.size();i++) {
                    int u = this.getId(sta[i]); int v = this.getId(sta[i+1]);
                    this.addBiEdge(u,v,((JSONObject) line).getString("subway-line-name"));

                }
            }



        } catch (IOException e) {
            e.printStackTrace();
        }





    }
    public static void main(String[] args) {
        try {
            DealGraph dealGraph = new DealGraph();
            System.out.println(dealGraph.getStations());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }
    private int getId(String staNames) {
        if (stationToVertical.get(staNames) == null) {
            stationToVertical.put(staNames,count);
            verticalToStation.put(count,staNames);
            count++;
        }
        return stationToVertical.get(staNames);
    }
    private void addBiEdge(int u,int v,String tag) {
        weightedGraph.addedge(u,v,tag);
        weightedGraph.addedge(v,u,tag);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void dijstra(int s) {
       weightedGraph.dijstra(s);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public String getPath(String S, String T){

        return getPath(stationToVertical.get(S),stationToVertical.get(T));
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public String getPath(int S, int T) {
        if (S == T) return "不用换乘";
        dijstra(S);
        ArrayList<WeightedEdge> edges;
        ArrayList<Integer> intStations;
        ArrayList<String> stations = new ArrayList<>();
        Pair<ArrayList<WeightedEdge>,ArrayList<Integer> >pair = weightedGraph.getPreEdge(S,T);
        edges = pair.first; intStations = pair.second;
        for (Integer st : intStations){
            stations.add(verticalToStation.get(st));
        }
        String ans = stations.get(0) + " -> " + stations.get(1);
        for (int i = 1;i < edges.size();i++) {
            ans = ans + " -> " + stations.get(i+1);
            if (!edges.get(i - 1).getTag().equals(edges.get(i).getTag())) {
                ans = ans + String.format("(从%s转到%s)",edges.get(i - 1).getTag(),edges.get(i).getTag());
            }
        }
        return ans;
    }
    public String[] getLines() {
        String[] ans = new String[lineStations.size()];
        lineStations.keySet().toArray(ans);
        return ans;
    }
    public ArrayList<String> getStationByLine(String linename) {
        String[] ans = new String[lineStations.get(linename).size()];
        lineStations.get(linename).toArray(ans);
        ArrayList<String> lst = new ArrayList<>();
        for (String sta : ans)
            lst.add(sta);
        return lst;
    }
    public String[] getStations() {
        String[] ans = new String[count];
        stationToVertical.keySet().toArray(ans);
        return ans;
    }
}
