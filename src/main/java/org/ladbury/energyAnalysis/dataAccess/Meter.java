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
        MetricType metricType = MetricPair.getMetricType(metricName);
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
