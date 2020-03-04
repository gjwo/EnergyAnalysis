package org.ladbury.energyAnalysis.dataAccess;

import org.ladbury.energyAnalysis.metadata.MetricType;

class MetricPair
{
    String metricDBName;
    MetricType metricType;
    MetricPair(String metricDBName, MetricType metricType)
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


 static MetricType getMetricType(String metricDBName)
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
static String getMetricDBName(MetricType metricType)
{
    switch (metricType){
        case CURRENT:         return "current";
        case VOLTAGE:         return "voltage";
        case REAL_POWER:     return "power";
        case APPARENT_POWER:  return "apparentpower";
        case REACTIVE_POWER:   return "reactivepower";
        case POWERFACTOR:     return "powerfactor";
        //case "Today":
        //case "Yesterday":
        case ENERGY:          return "energy";
        case ENERGY_KILO:    return "cumulativeenergy";
        default: return null;
    }
}
}
