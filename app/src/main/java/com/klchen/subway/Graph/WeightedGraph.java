
package com.klchen.subway.Graph;


import android.os.Build;
import android.util.Pair;

import androidx.annotation.RequiresApi;

import java.util.*;

public class WeightedGraph {
    private int N;
    private List<List<WeightedEdge>> Graph;
    private WeightedEdge[] preEdge;
    private Integer[] preV;
    private Integer[] dist;
    public WeightedGraph(int N) {
        this.N = N;
        Graph = new ArrayList<List<WeightedEdge>>();
        for (int i = 0;i < N;i++) {
            Graph.add(new ArrayList<WeightedEdge>());
        }
        preEdge = new WeightedEdge[N];
        dist = new Integer[N];
        preV = new Integer[N];
        for (int i = 0;i < N;i++) {
            preV[i] = -1;
            preEdge[i] = null;
            dist[i] = 0x3f3f3f3f;}
    }
    public void addedge(int from,int to,String tag) {
        addedge(from,to,tag,1);
    }
    public void addedge(int from,int to,String tag,int weight) {
        Graph.get(from).add(new WeightedEdge(to,tag,weight));
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void dijstra(int s) {
        for (int i = 0;i < N;i++) { preV[i] = -1; preEdge[i] = null; dist[i] = 0x3f3f3f3f;}
        PriorityQueue<Pair<Integer,Integer>> que = new PriorityQueue<Pair<Integer, Integer>>(new Comparator<Pair<Integer, Integer>>() {
            @Override
            public int compare(Pair<Integer, Integer> o1, Pair<Integer, Integer> o2) {
                return (o1.first).compareTo(o2.first);
            }
        });
        dist[s] = 0;
        que.add(new Pair<>(0,s));
        while (que.isEmpty() == false) {
            Pair<Integer, Integer> p = que.poll();
            int nowdist = p.first; int u = p.second;
            if (nowdist > dist[u]) continue;
            for (WeightedEdge edge : Graph.get(u)) {
                int v = edge.getTo();
                int weight = edge.getWeight();
                if (dist[v] > dist[u] + weight) {
                    dist[v] = dist[u] + weight;
                    preEdge[v] = edge;
                    preV[v] = u;
                    que.add(new Pair<>(dist[v],v));
                }
            }
        }
    }
    public Pair<ArrayList<WeightedEdge>,ArrayList<Integer> > getPreEdge(int S,int T) {
        Pair<ArrayList<WeightedEdge>,ArrayList<Integer> > ans = new Pair<ArrayList<WeightedEdge>,ArrayList<Integer> >(new ArrayList<WeightedEdge>(),new ArrayList<Integer>());
        ans.second.add(T);
        for (int v = T;v != S;v = preV[v]) {
            ans.first.add(preEdge[v]);
            ans.second.add(preV[v]);
        }
        Collections.reverse(ans.first);
        Collections.reverse(ans.second);
        return ans;
    }

}
