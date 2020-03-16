package org.ladbury.energyAnalysis.dataAccess;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.QueryResult;
import org.influxdb.dto.Query;
import org.ladbury.energyAnalysis.meters.Meter;
import org.ladbury.energyAnalysis.meters.Meters;

import java.util.ArrayList;
import java.util.List;

public class InfluxDataSource
{
    private final InfluxDB influxDBServer;
    private final String url;
    private String dbName;
    private final List<String> validDBs;
    private final List<String> validTags;
    private final List<String> validMeasurements;

    public InfluxDataSource(String url, String dbName)
    {
        this.url = url;
        this.dbName = dbName;
        influxDBServer = InfluxDBFactory.connect(url);
        validDBs = loadValidDBs();
        boolean found = false;
        for (String dbn: validDBs)
        {
            if (dbn.compareTo(this.dbName) == 0){
                found = true;
                break;
            }
        }
        if (!found) System.exit(2);
        validMeasurements = loadMeasurements();
        validTags = loadTags();
     }

    //getters
    public InfluxDB getInfluxDBServer(){return this.influxDBServer;}
    public String getDbName(){return this.dbName;}
    public void getDBInfo()
    {
        System.out.println("Database name: "+dbName);
        System.out.println("Database url: "+url);
    }

    public Meters loadMeters(){
        Meters meters = new Meters(loadTags());
        //System.out.println(meters);
        LoadMeterFieldKeys(meters);
        //System.out.println(meters);
        return meters;
    }

    private ArrayList<String> loadValidDBs(){
        QueryResult queryResult;
        ArrayList<String> results = new ArrayList<>();
        queryResult = influxDBServer.query(new Query("SHOW DATABASES"));
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

    public QueryResult query(String queryString){
        return influxDBServer.query(new Query(queryString,dbName));
    }

    private ArrayList<String> loadMeasurements(){
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

    public ArrayList<String> loadTags(){
        QueryResult queryResult;
        ArrayList<String> results = new ArrayList<>();
        queryResult = influxDBServer.query(new Query("SHOW TAG VALUES WITH KEY = \"meter\"",getDbName()));
        List<QueryResult.Result> resultsList= queryResult.getResults();
        List <QueryResult.Series> resultSeriesList;
        for (QueryResult.Result result :resultsList) {
            resultSeriesList = result.getSeries();
            QueryResult.Series series = resultSeriesList.get(0);
            //for (QueryResult.Series series : resultSeriesList){
                for (List<Object> objects : series.getValues()) {
                    results.add(objects.toArray()[1].toString());
                    //System.out.print(objects.toArray()[1].toString()+", ");
                }
                //System.out.println();
            //}
        }
        //System.out.println();
        return results;
    }

    public void LoadMeterFieldKeys(Meters meters)
    {
        //todo needs refining, currently adds all metrics to every meter
        QueryResult queryResult;
        queryResult = influxDBServer.query(new Query("SHOW FIELD KEYS",getDbName()));
        List<QueryResult.Result> resultsList= queryResult.getResults();
        List <QueryResult.Series> resultSeriesList;
        for(Meter meter : meters) {
            for (QueryResult.Result result : resultsList) {
                resultSeriesList = result.getSeries();
                for (QueryResult.Series series : resultSeriesList) {
                    String measurementName = series.getName();
                    for (List<Object> objects : series.getValues()) {
                        //System.out.print(objects.toArray()[0].toString() + ", ");
                        meter.addMetricDBName(objects.toArray()[0].toString());
                    }
                    //System.out.println();
                }
                //System.out.println();
            }
            System.out.println(meter.toString());
        }
    }

    public void close(){
        influxDBServer.close();
    }
}
