package org.ladbury.energyAnalysis.dataAccess;

import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBResultMapper;
import org.ladbury.energyAnalysis.Main;
import org.ladbury.energyAnalysis.dataAccess.pOJOs.BasicAndPowerMeasurements;
import org.ladbury.energyAnalysis.dataAccess.pOJOs.EnergyMeasurements;
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

        List<BasicAndPowerMeasurements> basicAndPowerMeasurements = resultMapper.toPOJO(res, BasicAndPowerMeasurements.class);
        TimeSeries timeSeries;

        timeSeries = new TimeSeries(Granularity.SECOND);
        timeSeries.getIdentification().setName(MetricType.REAL_POWER.getMetricName());
        timeSeries.getIdentification().setMeterName(name);
        timeSeries.getDescription().setMetricType(MetricType.REAL_POWER);
        readingsSet.put(MetricType.REAL_POWER,timeSeries);

        timeSeries = new TimeSeries(Granularity.SECOND);
        timeSeries.getIdentification().setName(MetricType.REACTIVE_POWER.getMetricName());
        timeSeries.getIdentification().setMeterName(name);
        timeSeries.getDescription().setMetricType(MetricType.REACTIVE_POWER);
        readingsSet.put(MetricType.REACTIVE_POWER,timeSeries);

        timeSeries = new TimeSeries(Granularity.SECOND);
        timeSeries.getIdentification().setName(MetricType.APPARENT_POWER.getMetricName());
        timeSeries.getIdentification().setMeterName(name);
        timeSeries.getDescription().setMetricType(MetricType.APPARENT_POWER);
        readingsSet.put(MetricType.APPARENT_POWER,timeSeries);

        timeSeries = new TimeSeries(Granularity.SECOND);
        timeSeries.getIdentification().setName(MetricType.POWERFACTOR.getMetricName());
        timeSeries.getIdentification().setMeterName(name);
        timeSeries.getDescription().setMetricType(MetricType.POWERFACTOR);
        readingsSet.put(MetricType.POWERFACTOR,timeSeries);

        timeSeries = new TimeSeries(Granularity.SECOND);
        timeSeries.getIdentification().setName(MetricType.VOLTAGE.getMetricName());
        timeSeries.getIdentification().setMeterName(name);
        timeSeries.getDescription().setMetricType(MetricType.VOLTAGE);
        readingsSet.put(MetricType.VOLTAGE,timeSeries);

        timeSeries = new TimeSeries(Granularity.SECOND);
        timeSeries.getIdentification().setName(MetricType.CURRENT.getMetricName());
        timeSeries.getIdentification().setMeterName(name);
        timeSeries.getDescription().setMetricType(MetricType.CURRENT);
        readingsSet.put(MetricType.CURRENT,timeSeries);

        for (BasicAndPowerMeasurements  m: basicAndPowerMeasurements)
        {
            readingsSet.get(MetricType.REAL_POWER).add(new TimestampedDouble(m.getPower(),m.getTime()));
            readingsSet.get(MetricType.REACTIVE_POWER).add(new TimestampedDouble(m.getReactivepower(),m.getTime()));
            readingsSet.get(MetricType.APPARENT_POWER).add(new TimestampedDouble(m.getApparentpower(),m.getTime()));
            readingsSet.get(MetricType.POWERFACTOR).add(new TimestampedDouble(m.getPowerfactor(),m.getTime()));
            readingsSet.get(MetricType.VOLTAGE).add(new TimestampedDouble(m.getVoltage(),m.getTime()));
            readingsSet.get(MetricType.CURRENT).add(new TimestampedDouble(m.getCurrent(),m.getTime()));
        }
        readingsSet.get(MetricType.REAL_POWER).summarise();
        readingsSet.get(MetricType.REACTIVE_POWER).summarise();
        readingsSet.get(MetricType.APPARENT_POWER).summarise();
        readingsSet.get(MetricType.POWERFACTOR).summarise();
        readingsSet.get(MetricType.VOLTAGE).summarise();
        readingsSet.get(MetricType.CURRENT).summarise();
    }
    public void loadLatestEnergyReadingsSet(int minutes){
        minutes++; //to get the right number of readings
        String query = "SELECT * FROM "+this.name+ " WHERE time >=now() - "+minutes+"m";
        System.out.println(query);
        influxDataSource = Main.getInfluxDataSource();
        QueryResult res = influxDataSource.query(query);

        List<EnergyMeasurements> energyMeasurements = resultMapper.toPOJO(res, EnergyMeasurements.class);
        TimeSeries timeSeries;

        timeSeries = new TimeSeries(Granularity.TEN_MINUTE);
        timeSeries.getIdentification().setName(MetricType.ENERGY.getMetricName());
        timeSeries.getIdentification().setMeterName(name);
        timeSeries.getDescription().setMetricType(MetricType.ENERGY);
        readingsSet.put(MetricType.ENERGY,timeSeries);

        timeSeries = new TimeSeries(Granularity.TEN_MINUTE);
        timeSeries.getIdentification().setName(MetricType.ENERGY_KILO.getMetricName());
        timeSeries.getIdentification().setMeterName(name);
        timeSeries.getDescription().setMetricType(MetricType.ENERGY_KILO);
        timeSeries.getDescription().setCumulative(true);
        readingsSet.put(MetricType.ENERGY_KILO,timeSeries);

        for (EnergyMeasurements  m: energyMeasurements)
        {
            readingsSet.get(MetricType.ENERGY).add(new TimestampedDouble(m.getEnergy(),m.getTime()));
            readingsSet.get(MetricType.ENERGY_KILO).add(new TimestampedDouble(m.getCumulativeenergy(),m.getTime()));
        }
        readingsSet.get(MetricType.ENERGY).summarise();
        readingsSet.get(MetricType.ENERGY_KILO).summarise();
    }
    public TimeSeries getSeries(MetricType metricType){
        if(readingsSet.containsKey(metricType)) return readingsSet.get(metricType);
        return null;
    }
}
