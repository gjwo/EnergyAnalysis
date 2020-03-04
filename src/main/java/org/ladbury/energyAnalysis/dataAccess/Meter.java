package org.ladbury.energyAnalysis.dataAccess;

import org.ladbury.energyAnalysis.metadata.MetricType;

import java.util.ArrayList;

public class Meter
{
    private class MetricPair {
        String metricDBName;
        MetricType metricType;
        MetricPair (String metricDBName, MetricType metricType)
        {
            this.metricDBName = metricDBName;
            this.metricType = metricType;
        }

        @Override
        public String toString()
        {
            return "MetricPair{" +
                    "metricDBName='" + metricDBName + '\'' +
                    ", metricType=" + metricType +
                    '}';
        }
    }
    private String name;
    private final ArrayList<MetricPair> supportedMetricTypes;

    Meter(String name){
        this.name = name;
        supportedMetricTypes = new ArrayList<>();
    }

    private static MetricType getMetricType(String metricDBName)
    {
        switch (metricDBName){
            case "current":         return MetricType.CURRENT;
            case "voltage":         return MetricType.VOLTAGE;
            case "power":           return MetricType.REAL_POWER;
            case "apparentpower":   return MetricType.APPARENT_POWER;
            case "reactivepower":   return MetricType.REACTIVE_POWER;
            case "powerfactor":     return MetricType.POWERFACTOR;
            case "Today":
            case "Yesterday":
            case "energy":          return MetricType.ENERGY;
            case "cumulativeenergy":return MetricType.ENERGY_KILO;
            default: return null;
        }
    }
    public String getName()
    {
        return name;
    }
    public void addMetricDBName(String metricName){
        MetricType metricType = Meter.getMetricType(metricName);
        if (metricType != null) supportedMetricTypes.add(new MetricPair(metricName,metricType));
    }

    @Override
    public String toString()
    {
        return "Meter{" +
                "name='" + name + '\'' +
                ", supportedMetricTypes=" + supportedMetricTypes +
                '}';
    }
}
