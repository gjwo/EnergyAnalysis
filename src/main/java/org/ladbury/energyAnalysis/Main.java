package org.ladbury.energyAnalysis;

import org.ladbury.energyAnalysis.dataAccess.InfluxDataSource;
import org.ladbury.energyAnalysis.meters.Meter;
import org.ladbury.energyAnalysis.metadata.MetricType;
import org.ladbury.energyAnalysis.meters.Meters;
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
    public static Meters meters;

    public static InfluxDataSource getInfluxDataSource() { return influxDataSource; }
    public static Meters getMeters(){return meters;}

    public static void main(String[] args)
    {
        TimeSeries ts;
        influxDataSource = new InfluxDataSource("http://10.0.128.2:8086","household_energy");
        meters = influxDataSource.loadMeters();
        LocalDateTime localNow = now(ZoneId.of("Europe/London"));
        LocalDateTime yesterdayMidnight = localNow.truncatedTo(ChronoUnit.DAYS).minusDays(1); //start of today -1 + yesterday
        LocalDateTime todayMidnight = yesterdayMidnight.plusDays(1); //start of tomorrow
        Instant time1 = yesterdayMidnight.toInstant(ZoneOffset.ofHours(0));
        Instant time2 = todayMidnight.toInstant(ZoneOffset.ofHours(0));
        System.out.println(time1.toString()+ " <-> "+ time2.toString());

        //Load all discreteMeasures into meters
        ListIterator<Meter> metersIter = getMeters().listIterator();
        Meter meter;
        while ( metersIter.hasNext()) {
            meter = metersIter.next();
            meter.loadDiscreteReadingsSet(time1, time2, Granularity.SECOND);
            meter.loadEnergyReadingsSet(time1,time2,Granularity.FIVE_MINUTE);
            System.out.println(meter.getSeries(MetricType.ENERGY_KILO).toString());
        }

        ts = getMeters().getMeter( "Whole_House").getSeries(MetricType.REAL_POWER);
        Waveform wholeHouseRealPowerWaveform = new Waveform(ts);
        System.out.println(wholeHouseRealPowerWaveform.toString());
        ts = getMeters().getMeter("Plug4").getSeries(MetricType.REAL_POWER);
        Waveform plug4RealPowerWaveform = new Waveform(ts);
        System.out.println(plug4RealPowerWaveform.toString());
        influxDataSource.close();
     }
}
