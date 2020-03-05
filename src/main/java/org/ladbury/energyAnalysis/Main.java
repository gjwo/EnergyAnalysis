package org.ladbury.energyAnalysis;

import org.ladbury.energyAnalysis.dataAccess.InfluxDataSource;
import org.ladbury.energyAnalysis.dataAccess.Meter;
import org.ladbury.energyAnalysis.dataAccess.Querys;
import org.ladbury.energyAnalysis.metadata.MetricType;
import org.ladbury.energyAnalysis.timeSeries.TimeSeries;
import org.ladbury.energyAnalysis.timeSeries.TimestampedDouble;
import org.ladbury.energyAnalysis.timeSeries.Waveform;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

import static java.time.LocalDateTime.now;

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
        Meter plug1 = influxDataSource.getMeters().getMeter("Plug1");
        /*
        Meter plug2 = influxDataSource.getMeters().getMeter("Plug2");
        Meter plug3 = influxDataSource.getMeters().getMeter("Plug3");
        Meter plug4 = influxDataSource.getMeters().getMeter("Plug4");
        Meter plug5 = influxDataSource.getMeters().getMeter("Plug5");
        Meter plug6 = influxDataSource.getMeters().getMeter("Plug6");
        Meter plug7 = influxDataSource.getMeters().getMeter("Plug7");
        Meter plug8 = influxDataSource.getMeters().getMeter("Plug8");
    */
        LocalDateTime localNow = now(ZoneId.of("Europe/London"));
        LocalDateTime yesterdayMidnight = localNow.truncatedTo(ChronoUnit.DAYS).minusDays(1); //start of today
        LocalDateTime todayMidnight = yesterdayMidnight.plusDays(1); //start of tomorrow
        Instant time1 = yesterdayMidnight.toInstant(ZoneOffset.ofHours(0));
        Instant time2 = todayMidnight.toInstant(ZoneOffset.ofHours(0));

        System.out.println(time1.toString()+ " <-> "+ time2.toString());
        wholeHouse.loadReadingsSet(time1,time2);
        plug1.loadReadings(MetricType.REAL_POWER,time1, time2);
        /*
        plug2.loadReadingsSet(time1, time2);
        plug3.loadReadingsSet(time1, time2);
        plug4.loadReadingsSet(time1, time2);
        plug5.loadReadingsSet(time1, time2);
        plug6.loadReadingsSet(time1, time2);
        plug7.loadReadingsSet(time1, time2);
        plug8.loadReadingsSet(time1, time2);
        */
        //wholeHouse.loadLatestReadingsSet(30);
        //wholeHouse.loadLatestEnergyReadingsSet(120);
        TimeSeries ts = wholeHouse.getSeries(MetricType.REAL_POWER);
        Waveform wholeHouseRealPowerWaveform = new Waveform(ts);
        System.out.println(wholeHouseRealPowerWaveform.getIdentification().toString());
        System.out.println(wholeHouseRealPowerWaveform.getDescription().toString());
        System.out.println(wholeHouseRealPowerWaveform.getSummary().toString());

        ts = plug1.getSeries(MetricType.REAL_POWER);
        Waveform plug1RealPowerWaveform = new Waveform(ts);
        System.out.println(plug1RealPowerWaveform.getIdentification().toString());
        System.out.println(plug1RealPowerWaveform.getDescription().toString());
        System.out.println(plug1RealPowerWaveform.getSummary().toString());
        /*
        for (TimestampedDouble tsd : wholeHouseRealPowerWaveform){
            System.out.println(tsd.timestampString());
        }
        ts = wholeHouse.getSeries(MetricType.ENERGY);
        System.out.println(ts.getIdentification().toString());
        System.out.println(ts.getDescription().toString());
        System.out.println(ts.getSummary().toString());
        for (TimestampedDouble tsd : ts){
            System.out.println(tsd.timestampString());
        }
        */
        influxDataSource.close();
     }
}
