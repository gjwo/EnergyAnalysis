package org.ladbury.energyAnalysis;

import org.ladbury.energyAnalysis.dataAccess.InfluxDataSource;
import org.ladbury.energyAnalysis.dataAccess.Meter;
import org.ladbury.energyAnalysis.dataAccess.Meters;
import org.ladbury.energyAnalysis.dataAccess.Querys;
import org.ladbury.energyAnalysis.metadata.MetricType;
import org.ladbury.energyAnalysis.timeSeries.Granularity;
import org.ladbury.energyAnalysis.timeSeries.TimeSeries;
import org.ladbury.energyAnalysis.timeSeries.Waveform;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ListIterator;

import static java.time.LocalDateTime.now;

public class Main
{
    public static InfluxDataSource influxDataSource;
    private static Querys querys ;

    public static InfluxDataSource getInfluxDataSource() { return influxDataSource; }
    public static Querys getQuerys() {return querys;}

    public static void main(String[] args)
    {
        TimeSeries ts;
        String dbName = "household_energy";
        querys = new Querys(dbName);
        influxDataSource = new InfluxDataSource("http://10.0.128.2:8086",dbName);
        LocalDateTime localNow = now(ZoneId.of("Europe/London"));
        LocalDateTime yesterdayMidnight = localNow.truncatedTo(ChronoUnit.DAYS).minusDays(1); //start of today -1 + yesterday
        LocalDateTime todayMidnight = yesterdayMidnight.plusDays(1); //start of tomorrow
        Instant time1 = yesterdayMidnight.toInstant(ZoneOffset.ofHours(0));
        Instant time2 = todayMidnight.toInstant(ZoneOffset.ofHours(0));
        System.out.println(time1.toString()+ " <-> "+ time2.toString());

        //Load all discreteMeasures into meters
        ListIterator<Meter> metersIter = influxDataSource.getMeters().listIterator();
        Meter meter;
        while ( metersIter.hasNext()) {
            meter = metersIter.next();
            meter.loadDiscreteReadingsSet(time1, time2, Granularity.SECOND);
            meter.loadEnergyReadingsSet(time1,time2,Granularity.FIVE_MINUTE);
            System.out.println(meter.getSeries(MetricType.ENERGY_KILO).toString());
        }

        ts = influxDataSource.getMeters().getMeter( "Whole_House").getSeries(MetricType.REAL_POWER);
        Waveform wholeHouseRealPowerWaveform = new Waveform(ts);
        System.out.println(wholeHouseRealPowerWaveform.toString());
        ts = influxDataSource.getMeters().getMeter("Plug4").getSeries(MetricType.REAL_POWER);
        Waveform plug4RealPowerWaveform = new Waveform(ts);
        System.out.println(plug4RealPowerWaveform.toString());



        influxDataSource.close();
     }
}
