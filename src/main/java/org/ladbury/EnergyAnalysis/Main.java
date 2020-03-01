package org.ladbury.EnergyAnalysis;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBResultMapper;

import java.util.List;

public class Main
{
    public static void main(String[] args)
    {
        InfluxDB influxDB = InfluxDBFactory.connect("http://10.0.128.2:8086");
        String dbName = "energy";

        //Query query = new Query("SELECT * FROM \"Whole_House\" WHERE time >= now() - 15m GROUP BY time(1s) fill(none)", dbName);
        Query query = new Query("SELECT * FROM \"Whole_House\" WHERE time >= now() - 15m", dbName);
        QueryResult res = influxDB.query(query);
        influxDB.close();
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
        List<Measurement> measurements = resultMapper.toPOJO(res, Measurement.class);
        for (Measurement m: measurements) System.out.println(m);
        System.out.println(measurements.size());
    }
}
