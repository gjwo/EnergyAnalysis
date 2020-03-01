package org.ladbury.energyAnalysis;

import org.influxdb.InfluxDB;
import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBResultMapper;
import org.ladbury.energyAnalysis.dataAccess.InfluxDataSource;
import org.ladbury.energyAnalysis.dataAccess.pOJOs.AllMeasurements;
import org.ladbury.energyAnalysis.dataAccess.QueryName;
import org.ladbury.energyAnalysis.dataAccess.Querys;

import java.util.List;

public class Main
{
    private static InfluxDataSource influxDataSource;
    private static Querys querys ;
    public static void main(String[] args)
    {
        String dbName = "energy";
        querys = new Querys(dbName);
        influxDataSource = new InfluxDataSource("http://10.0.128.2:8086",dbName);
        InfluxDB influxDB = influxDataSource.getInfluxDB();


        //Query query = new Query("SELECT * FROM \"Whole_House\" WHERE time >= now() - 15m GROUP BY time(1s) fill(none)", dbName);
        QueryResult res = influxDB.query(querys.getQuery(QueryName.LAST_MEASUREMENTS));
        /*for (QueryResult.Result r:res.getResults())
        {
            for (QueryResult.Series s:r.getSeries())
            {
                s.getValues().forEach(System.out::println);
                System.out.println(s.getValues().size() + " values");
                s.getColumns().forEach(System.out::println);
            }
        }*/

        InfluxDBResultMapper resultMapper = new InfluxDBResultMapper(); // thread-safe - can be reused
        List<AllMeasurements> allMeasurements = resultMapper.toPOJO(res, AllMeasurements.class);
        for (AllMeasurements m: allMeasurements) System.out.println(m);
        System.out.println(allMeasurements.size());

        influxDataSource.close();
    }
}
