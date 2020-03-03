package org.ladbury.energyAnalysis;

import org.influxdb.InfluxDB;
import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBResultMapper;
import org.ladbury.energyAnalysis.dataAccess.InfluxDataSource;
import org.ladbury.energyAnalysis.dataAccess.pOJOs.AllMeasurements;
import org.ladbury.energyAnalysis.dataAccess.QueryName;
import org.ladbury.energyAnalysis.dataAccess.Querys;
import org.ladbury.energyAnalysis.timeSeries.Waveform;

import java.util.List;

public class Main
{
    private static InfluxDataSource influxDataSource;
    private static Querys querys ;
    public static Querys getQuerys() {return querys;}
    public static void main(String[] args)
    {
        String dbName = "energy";
        querys = new Querys(dbName);
        influxDataSource = new InfluxDataSource("http://10.0.128.2:8086",dbName);

        QueryResult res;

        res = influxDataSource.query(QueryName.SHOW_RETENTION);
        System.out.println("Retention");
        System.out.println(res.toString());
        System.out.println("Series");
        res = influxDataSource.query(QueryName.SHOW_SERIES);
        System.out.println(res.toString());
        System.out.println("<Measurements>");
        res = influxDataSource.query(QueryName.SHOW_MEASUREMENTS);
        System.out.println(res.toString());
        System.out.println("Tag Keys");
        res = influxDataSource.query(QueryName.SHOW_TAG_KEYS);
        System.out.println(res.toString());
        System.out.println("Field Keys");
        res = influxDataSource.query(QueryName.SHOW_FIELD_KEYS);
        System.out.println(res.toString());

        res = influxDataSource.query(QueryName.LAST_MEASUREMENTS);

        InfluxDBResultMapper resultMapper = new InfluxDBResultMapper(); // thread-safe - can be reused
        List<AllMeasurements> allMeasurements = resultMapper.toPOJO(res, AllMeasurements.class);
        for (AllMeasurements m: allMeasurements) System.out.println(m);
        System.out.println(allMeasurements.size());

        Waveform powerRealWaveform = new Waveform();

        influxDataSource.close();
     }
}
