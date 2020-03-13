package org.ladbury.energyAnalysis.dataAccess;

import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBResultMapper;
import org.ladbury.energyAnalysis.Main;
import org.ladbury.energyAnalysis.dataAccess.pOJOs.DiscreteMeasures;
import org.ladbury.energyAnalysis.dataAccess.pOJOs.CumulativeEnergyMeasures;
import org.ladbury.energyAnalysis.dataAccess.pOJOs.RealPowerMeasurement;
import org.ladbury.energyAnalysis.metadata.MetricType;
import org.ladbury.energyAnalysis.timeSeries.Granularity;
import org.ladbury.energyAnalysis.timeSeries.TimeSeries;
import org.ladbury.energyAnalysis.timeSeries.TimestampedDouble;

import java.time.Instant;
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
    // query helper methods
    private String meanAsMetricField(String metric){return " MEAN(\""+metric+"\") AS \""+metric+"\" ";}
    private String meterClause(){return "(\"meter\" = '"+this.name+"')";    }
    private String lastSeconds(int seconds){return "time >=now() - " + seconds + "s";}
    private String lastMinutes(int minutes){return "time >=now() - " + minutes + "m";}
    private String timeInterval(Instant t1, Instant t2){return "time >= '"+t1.toString()+ "' AND  time <= '"+t2.toString()+ "'";}

    public void loadLatestDiscreteReadingsSet(int seconds)
    {
        seconds++; //to get the right number of readings
        String query = "SELECT" + meanAsMetricField("realPower") + ","
                + meanAsMetricField("reactivePower") + ","
                + meanAsMetricField("apparentPower") + ","
                + meanAsMetricField("powerfactor") + ","
                + meanAsMetricField("current") + ","
                + meanAsMetricField("voltage")
                + "FROM " + "\"discreteMeasures\"" + " WHERE "+ meterClause()+" AND " + lastSeconds(seconds) + " GROUP BY time(1s) fill(0)";
        System.out.println(query);
        influxDataSource = Main.getInfluxDataSource();
        processDiscreteReadings(influxDataSource.query(query));
    }
    public void loadDiscreteReadingsSet(Instant t1, Instant t2)
    {
        //todo need to fetch appliance name
        String query = "SELECT" + meanAsMetricField("realPower") + ","
                + meanAsMetricField("reactivePower") + ","
                + meanAsMetricField("apparentPower") + ","
                + meanAsMetricField("powerfactor") + ","
                + meanAsMetricField("current") + ","
                + meanAsMetricField("voltage")
                + "FROM \"discreteMeasures\" WHERE "+ meterClause()+" AND "+timeInterval(t1,t2)+" GROUP BY time(1s) fill(0)";
        System.out.println(query);
        influxDataSource = Main.getInfluxDataSource();
        processDiscreteReadings(influxDataSource.query(query));
    }
    public void loadMetricReadings(MetricType metricType, Instant t1, Instant t2)
    {
        //todo need to specify granularity for energy readings
        String query = "SELECT " + meanAsMetricField(MetricPair.getMetricDBName(metricType))
                + " FROM  \"discreteMeasures\" WHERE "+ meterClause()+" AND "+timeInterval(t1,t2)+ " GROUP BY time(1s) fill(0)";
        System.out.println(query);
        influxDataSource = Main.getInfluxDataSource();
        processReadingsResult(influxDataSource.query(query),metricType);
    }

    public void processReadingsResult(QueryResult res, MetricType metricType){
        List<RealPowerMeasurement> realPowerMeasurement = resultMapper.toPOJO(res, RealPowerMeasurement.class);
        TimeSeries timeSeries;

        timeSeries = new TimeSeries(Granularity.SECOND);
        timeSeries.getIdentification().setName(MetricType.REAL_POWER.getMetricName());
        timeSeries.getIdentification().setMeterName(name);
        timeSeries.getDescription().setMetricType(MetricType.REAL_POWER);
        readingsSet.put(MetricType.REAL_POWER,timeSeries);
        for (RealPowerMeasurement  m: realPowerMeasurement)
        {
            readingsSet.get(MetricType.REAL_POWER).add(new TimestampedDouble(m.getRealPower(),m.getTime()));
        }
        readingsSet.get(MetricType.REAL_POWER).summarise();
    }
    public void processDiscreteReadings(QueryResult res){

        List<DiscreteMeasures> discreteMeasures = resultMapper.toPOJO(res, DiscreteMeasures.class);
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
        System.out.println("Number of measures = "+discreteMeasures.size());
        for (DiscreteMeasures m: discreteMeasures)
        {
            System.out.println(m.toString());
            readingsSet.get(MetricType.REAL_POWER).add(new TimestampedDouble(m.getRealPower(),m.getTime()));
            System.out.print(readingsSet.get(MetricType.REAL_POWER).size()+",");
            readingsSet.get(MetricType.REACTIVE_POWER).add(new TimestampedDouble(m.getReactivePower(),m.getTime()));
            readingsSet.get(MetricType.APPARENT_POWER).add(new TimestampedDouble(m.getApparentPower(),m.getTime()));
            readingsSet.get(MetricType.POWERFACTOR).add(new TimestampedDouble(m.getPowerfactor(),m.getTime()));
            readingsSet.get(MetricType.VOLTAGE).add(new TimestampedDouble(m.getVoltage(),m.getTime()));
            readingsSet.get(MetricType.CURRENT).add(new TimestampedDouble(m.getCurrent(),m.getTime()));
        }
        System.out.println();
        readingsSet.get(MetricType.REAL_POWER).summarise();
        readingsSet.get(MetricType.REACTIVE_POWER).summarise();
        readingsSet.get(MetricType.APPARENT_POWER).summarise();
        readingsSet.get(MetricType.POWERFACTOR).summarise();
        readingsSet.get(MetricType.VOLTAGE).summarise();
        readingsSet.get(MetricType.CURRENT).summarise();
    }
    private String qs(String metric){return " SUM(\""+metric+"\") AS \""+metric+"\" ";}
    public void loadLatestEnergyReadingsSet(int minutes)
    {
        minutes++; //to get the right number of readings
        String query = "SELECT" + qs("intervalEnergy") + "," + qs("\"cumulativeEnergy\"") + "FROM " + " WHERE "+ meterClause()+" AND " + lastMinutes(minutes) + " GROUP BY time(5m)";
        System.out.println(query);
        influxDataSource = Main.getInfluxDataSource();
        processEnergyReadings(influxDataSource.query(query));
    }
    public void processEnergyReadings(QueryResult res){
        List<CumulativeEnergyMeasures> cumulativeEnergyMeasurements = resultMapper.toPOJO(res, CumulativeEnergyMeasures.class);
        TimeSeries timeSeries;

        timeSeries = new TimeSeries(Granularity.FIVE_MINUTE);
        timeSeries.getIdentification().setName(MetricType.ENERGY.getMetricName());
        timeSeries.getIdentification().setMeterName(name);
        timeSeries.getDescription().setMetricType(MetricType.ENERGY);
        readingsSet.put(MetricType.ENERGY,timeSeries);

        timeSeries = new TimeSeries(Granularity.FIVE_MINUTE);
        timeSeries.getIdentification().setName(MetricType.ENERGY_KILO.getMetricName());
        timeSeries.getIdentification().setMeterName(name);
        timeSeries.getDescription().setMetricType(MetricType.ENERGY_KILO);
        timeSeries.getDescription().setCumulative(true);
        readingsSet.put(MetricType.ENERGY_KILO,timeSeries);

        for (CumulativeEnergyMeasures m: cumulativeEnergyMeasurements)
        {
            readingsSet.get(MetricType.ENERGY).add(new TimestampedDouble(m.getIntervalEnergy(),m.getTime()));
            readingsSet.get(MetricType.ENERGY_KILO).add(new TimestampedDouble(m.getCumulativeEnergy(),m.getTime()));
        }
        readingsSet.get(MetricType.ENERGY).summarise();
        readingsSet.get(MetricType.ENERGY_KILO).summarise();
    }
    public TimeSeries getSeries(MetricType metricType){
        if(readingsSet.containsKey(metricType)) return readingsSet.get(metricType);
        return null;
    }
}
