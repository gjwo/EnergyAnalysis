package org.ladbury.energyAnalysis.dataAccess;

import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBResultMapper;
import org.ladbury.energyAnalysis.Main;
import org.ladbury.energyAnalysis.dataAccess.pOJOs.PowerMeasurements;
import org.ladbury.energyAnalysis.metadata.MetricType;
import org.ladbury.energyAnalysis.timeSeries.Granularity;
import org.ladbury.energyAnalysis.timeSeries.TimeSeries;
import org.ladbury.energyAnalysis.timeSeries.TimestampedDouble;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Meter
{
    private static class MetricPair {
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
    private static String getMetricDBName(MetricType metricType)
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

    //Class fields
    private InfluxDataSource influxDataSource;
    private String name;
    private final ArrayList<MetricPair> supportedMetricTypes;
    private final InfluxDBResultMapper resultMapper;
    private final Map<MetricType,TimeSeries> readingsSet;

    //Constructor
    Meter(String name){
        this.name = name;
        this.supportedMetricTypes = new ArrayList<>();
        this.resultMapper = new InfluxDBResultMapper(); // thread-safe - can be reused
        this.readingsSet = new HashMap<>();
    }
    //getter
    public String getName() { return name; }

    void addMetricDBName(String metricName){
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
    public void loadLatestReadingsSet(int seconds){
        seconds++; //to get the right number of readings
        String query = "SELECT * FROM "+this.name+ " WHERE time >=now() - "+seconds+"s";
        System.out.println(query);
        influxDataSource = Main.getInfluxDataSource();
        QueryResult res = influxDataSource.query(query);

        List<PowerMeasurements> powerMeasurements = resultMapper.toPOJO(res, PowerMeasurements.class);
        readingsSet.put(MetricType.REAL_POWER,new TimeSeries(Granularity.SECOND));
        readingsSet.put(MetricType.REACTIVE_POWER,new TimeSeries(Granularity.SECOND));
        readingsSet.put(MetricType.APPARENT_POWER,new TimeSeries(Granularity.SECOND));
        readingsSet.put(MetricType.POWERFACTOR,new TimeSeries(Granularity.SECOND));
        for (PowerMeasurements  m: powerMeasurements)
        {
            readingsSet.get(MetricType.REAL_POWER).add(new TimestampedDouble(m.getPower(),m.getTime()));
            readingsSet.get(MetricType.REACTIVE_POWER).add(new TimestampedDouble(m.getReactivepower(),m.getTime()));
            readingsSet.get(MetricType.APPARENT_POWER).add(new TimestampedDouble(m.getApparentpower(),m.getTime()));
            readingsSet.get(MetricType.POWERFACTOR).add(new TimestampedDouble(m.getPowerfactor(),m.getTime()));
        }
        readingsSet.get(MetricType.REAL_POWER).summarise();
        readingsSet.get(MetricType.REACTIVE_POWER).summarise();
        readingsSet.get(MetricType.APPARENT_POWER).summarise();
        readingsSet.get(MetricType.POWERFACTOR).summarise();
        System.out.println(readingsSet.get(MetricType.REAL_POWER).getIdentification().toString());
        System.out.println(readingsSet.get(MetricType.REAL_POWER).getDescription().toString());
        System.out.println(readingsSet.get(MetricType.REAL_POWER).getSummary().toString());
        readingsSet.get(MetricType.REAL_POWER).printValues();
    }
    public TimeSeries getSeries(MetricType metricType){
        if(readingsSet.containsKey(metricType)) return readingsSet.get(metricType);
        return null;
    }
}
