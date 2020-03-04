package org.ladbury.energyAnalysis;

import org.ladbury.energyAnalysis.dataAccess.InfluxDataSource;
import org.ladbury.energyAnalysis.dataAccess.Meter;
import org.ladbury.energyAnalysis.dataAccess.Querys;
import org.ladbury.energyAnalysis.metadata.MetricType;
import org.ladbury.energyAnalysis.timeSeries.Granularity;
import org.ladbury.energyAnalysis.timeSeries.TimeSeries;
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
        System.out.println(wholeHouse.toString());
        wholeHouse.loadLatestReadingsSet(30);
        TimeSeries realPowerSeries = wholeHouse.getSeries(MetricType.REAL_POWER);
        Waveform powerRealWaveform = new Waveform(Granularity.SECOND);
        powerRealWaveform.addAll(realPowerSeries);

        influxDataSource.close();
     }
}
