package com.klchen.subway.Graph;
public class WeightedEdge {
    private int to; private String tag; private int weight;

    public int getTo() {
        return to;
    }

    public String getTag() {
        return tag;
    }

    public int getWeight() {
        return weight;
    }

    public WeightedEdge(int to, String tag, int weight) {
        this.to = to;
        this.tag = tag;
        this.weight = weight;
    }
}