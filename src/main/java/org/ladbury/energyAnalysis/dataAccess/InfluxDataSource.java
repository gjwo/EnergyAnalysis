package org.ladbury.energyAnalysis.dataAccess;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.QueryResult;
import org.influxdb.dto.Query;
import org.ladbury.energyAnalysis.Main;

import java.util.ArrayList;
import java.util.List;

public class InfluxDataSource
{
    private final InfluxDB influxDBServer;
    private final String url;
    private String dbName;
    private final List<String> validDBs = new ArrayList<String>();
    public InfluxDataSource(String url, String dbName)
    {
        this.url = url;
        this.dbName = dbName;
        influxDBServer = InfluxDBFactory.connect(url);
        initialiseValidDBs();
        for (String dbn: validDBs)
        {
            if (dbn.compareTo(this.dbName) == 0) return;
        }
        System.exit(2);
    }

    public InfluxDB getInfluxDBServer(){return influxDBServer;}
    public String getDbName(){return dbName;}
    public void setDbName(String dbName){this.dbName = dbName;}

    public void close(){
        influxDBServer.close();
    }
    private void initialiseValidDBs(){
        QueryResult res;
        res =influxDBServer.query(new Query("SHOW DATABASES"));
        List<QueryResult.Result> resultsList= res.getResults();
        List <QueryResult.Series> resultSeriesList;
        for (QueryResult.Result result :resultsList) {
            resultSeriesList = result.getSeries();
            for (QueryResult.Series series : resultSeriesList){
                for (List<Object> objects : series.getValues()) {
                    validDBs.add(objects.toArray()[0].toString());
                }
            }
        }
    }
    public QueryResult query(QueryName queryName)
    {
        return influxDBServer.query( Main.getQuerys().getQuery(queryName));
    }

    public ArrayList<String> getMeasurements(){
        QueryResult queryResult;
        ArrayList<String> results = new ArrayList<>();
        queryResult = influxDBServer.query(new Query("SHOW MEASUREMENTS",getDbName()));
        List<QueryResult.Result> resultsList= queryResult.getResults();
        List <QueryResult.Series> resultSeriesList;
        for (QueryResult.Result result :resultsList) {
            resultSeriesList = result.getSeries();
            for (QueryResult.Series series : resultSeriesList){
                for (List<Object> objects : series.getValues()) {
                    results.add(objects.toArray()[0].toString());
                }
            }
        }
        return results;
    }
}
