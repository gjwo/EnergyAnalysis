package org.ladbury.energyAnalysis.meters;

import org.ladbury.energyAnalysis.metadata.MetricType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MetricMappings
{
    private static HashMap<MeterType, ArrayList<String>> supportedDBFields;
    private static HashMap<MeterType, ArrayList<MetricType>> supportedMetrics;

    static {
        supportedDBFields = new HashMap<>();
        supportedDBFields.put(MeterType.TASMOTA, new ArrayList<>(
                Arrays.asList("current", "voltage", "realPower", "apparentPower", "reactivePower", "cumulativeEnergy", "Yesterday")));
        supportedDBFields.put(MeterType.PMON10, new ArrayList<>(
                Arrays.asList("current", "voltage", "realPower", "apparentPower", "reactivePower", "powerfactor", "intervalEnergy", "cumulativeEnergy")));
        supportedMetrics = new HashMap<>();
        supportedMetrics.put(MeterType.TASMOTA, new ArrayList<>(
                Arrays.asList(MetricType.CURRENT,MetricType.VOLTAGE, MetricType.REAL_POWER, MetricType.APPARENT_POWER, MetricType.REACTIVE_POWER,
                        MetricType.ENERGY_KILO)));
        supportedMetrics.put(MeterType.PMON10, new ArrayList<>(
                Arrays.asList(MetricType.CURRENT,MetricType.VOLTAGE, MetricType.REAL_POWER, MetricType.APPARENT_POWER, MetricType.REACTIVE_POWER, MetricType.POWERFACTOR,
                        MetricType.ENERGY, MetricType.ENERGY_KILO)));
    }


    public static ArrayList<String> getSupportedDBFields(MeterType meterType){return supportedDBFields.get(meterType);}
    public static ArrayList<MetricType> getSupportedMetricTypes(MeterType meterType){return  supportedMetrics.get(meterType);}


    static MetricType getMetricType(String metricDBName)
    {
        switch (metricDBName) {
            case "current":
                return MetricType.CURRENT;
            case "voltage":
                return MetricType.VOLTAGE;
            case "realPower":
                return MetricType.REAL_POWER;
            case "apparentPower":
                return MetricType.APPARENT_POWER;
            case "reactivePower":
                return MetricType.REACTIVE_POWER;
            case "powerfactor":
                return MetricType.POWERFACTOR;
            case "intervalEnergy":
                return MetricType.ENERGY;
            case "Yesterday":
            case "cumulativeEnergy":
                return MetricType.ENERGY_KILO;
            default:
                return null;
        }
    }

    static String getMetricDBName(MetricType metricType)
    {
        switch (metricType) {
            case CURRENT:
                return "current";
            case VOLTAGE:
                return "voltage";
            case REAL_POWER:
                return "realPower";
            case APPARENT_POWER:
                return "apparentPower";
            case REACTIVE_POWER:
                return "reactivePower";
            case POWERFACTOR:
                return "powerfactor";
            case ENERGY:
                return "intervalEnergy";
            case ENERGY_KILO:
                return "cumulativeEnergy";
            default:
                return null;
        }
    }
}
