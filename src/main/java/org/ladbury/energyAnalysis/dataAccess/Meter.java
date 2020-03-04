package org.ladbury.energyAnalysis.dataAccess;

import java.util.ArrayList;

public class Meter
{
    private String name;
    private final ArrayList<String> metricNames;
    Meter(String name){
        this.name = name;
        metricNames = new ArrayList<>();
    }

    public String getName()
    {
        return name;
    }
    public void addMetricDBName(String metricName){
        metricNames.add(metricName);
    }

    @Override
    public String toString()
    {
        return "Meter{" +
                "name='" + name + '\'' +
                ", metricNames=" + metricNames +
                '}';
    }
}
