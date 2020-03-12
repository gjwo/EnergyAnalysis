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
        case "realPower":       return MetricType.REAL_POWER;
        case "apparentPower":   return MetricType.APPARENT_POWER;
        case "reactivePower":   return MetricType.REACTIVE_POWER;
        case "powerfactor":     return MetricType.POWERFACTOR;
        case "Today":
        case "Yesterday":
        case "intervalEnergy":  return MetricType.ENERGY;
        case "cumulativeEnergy":return MetricType.ENERGY_KILO;
        default: return null;
    }
}
static String getMetricDBName(MetricType metricType)
{
    switch (metricType){
        case CURRENT:         return "current";
        case VOLTAGE:         return "voltage";
        case REAL_POWER:     return "realPower";
        case APPARENT_POWER:  return "apparentPower";
        case REACTIVE_POWER:   return "reactivePower";
        case POWERFACTOR:     return "powerfactor";
        //case "Today":
        //case "Yesterday":
        case ENERGY:          return "intervalEnergy";
        case ENERGY_KILO:    return "cumulativeEnergy";
        default: return null;
    }
}
}
