package org.ladbury.energyAnalysis.dataAccess;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;

public class InfluxDataSource
{
    private final InfluxDB influxDB;
    private final String url;
    private final String dbName;
    public InfluxDataSource(String url, String dbName)
    {
        this.url = url;
        this.dbName = "energy";
        influxDB = InfluxDBFactory.connect("http://10.0.128.2:8086");
    }
    public InfluxDB getInfluxDB(){return influxDB;}
    public void close(){influxDB.close();}
}
