package org.ladbury.energyAnalysis;

import org.ladbury.energyAnalysis.dataAccess.InfluxDataSource;
import org.ladbury.energyAnalysis.dataAccess.Meter;
import org.ladbury.energyAnalysis.dataAccess.Querys;
import org.ladbury.energyAnalysis.metadata.MetricType;
import org.ladbury.energyAnalysis.timeSeries.Granularity;
import org.ladbury.energyAnalysis.timeSeries.TimeSeries;
import org.ladbury.energyAnalysis.timeSeries.TimestampedDouble;
import org.ladbury.energyAnalysis.timeSeries.Waveform;

public class Main
{
    public static InfluxDataSource influxDataSource;
    private static Querys querys ;

    public static InfluxDataSource getInfluxDataSource() { return influxDataSource; }
    public static Querys getQuerys() {return querys;}

    public static void main(String[] args)
    {
        String dbName = "energy";
        querys = new Querys(dbName);
        influxDataSource = new InfluxDataSource("http://10.0.128.2:8086",dbName);
        Meter wholeHouse = influxDataSource.getMeters().getMeter("Whole_House");
        wholeHouse.loadLatestReadingsSet(30);
        wholeHouse.loadLatestEnergyReadingsSet(120);
        TimeSeries ts = wholeHouse.getSeries(MetricType.REAL_POWER);
        Waveform realPowerWaveform = new Waveform(ts);
        System.out.println(realPowerWaveform.getIdentification().toString());
        System.out.println(realPowerWaveform.getDescription().toString());
        System.out.println(realPowerWaveform.getSummary().toString());
        for (TimestampedDouble tsd : realPowerWaveform){
            System.out.println(tsd.timestampString());
        }
        ts = wholeHouse.getSeries(MetricType.ENERGY);
        System.out.println(ts.getIdentification().toString());
        System.out.println(ts.getDescription().toString());
        System.out.println(ts.getSummary().toString());
        for (TimestampedDouble tsd : ts){
            System.out.println(tsd.timestampString());
        }

        influxDataSource.close();
     }
}
