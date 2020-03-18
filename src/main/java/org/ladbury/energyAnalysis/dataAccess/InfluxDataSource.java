package org.ladbury.energyAnalysis.dataAccess;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.QueryResult;
import org.influxdb.dto.Query;
import java.util.ArrayList;
import java.util.List;

public class InfluxDataSource
{
    private final InfluxDB influxDBServer;
    private final String url;
    private final List<String> validDBs;

    public InfluxDataSource(String url)
    {
        this.url = url;
        influxDBServer = InfluxDBFactory.connect(url);
        validDBs = loadValidDBs();
    }

    //getters
    public InfluxDB getInfluxDBServer(){return this.influxDBServer;}
    public List<String> getValidDBs() {return this.validDBs;}
    public String getURL() {return this.url;}
    public void printDBInfo(String dbName)
    {
        System.out.println(getInfluxDBServer().toString());
        System.out.print("url: "+getURL());
        System.out.print(" DataBases: ");
        for (String db: validDBs){
            System.out.print(db+" ");
        }
        System.out.println();
        System.out.println("Specific info for database: "+dbName);
        System.out.println("Measures: ");
        for (String measure: loadMeasurements(dbName)){
            System.out.print(measure+" ");
        }
        System.out.println();
    }
    public boolean isValidDBName(String DBName){
        for (String dbn: getValidDBs())
        {
            if (dbn.compareTo(DBName) == 0){
                return true;
            }
        }
        return false;
    }
    public QueryResult query(String queryString, String dbName){
        return influxDBServer.query(new Query(queryString,dbName));
    }
    // Query result helper function
    public static ArrayList<String> extractResults(QueryResult queryResult, int fieldNbr)
    {
        ArrayList<String> results = new ArrayList<>();
        List<QueryResult.Result> resultsList= queryResult.getResults();
        List <QueryResult.Series> resultSeriesList;
        for (QueryResult.Result result :resultsList) {
            resultSeriesList = result.getSeries();
            for (QueryResult.Series series : resultSeriesList){
                for (List<Object> objects : series.getValues()) {
                    results.add(objects.toArray()[fieldNbr].toString());
                }
            }
        }
        return  results;
    }

    private ArrayList<String> loadValidDBs(){
        QueryResult queryResult = influxDBServer.query(new Query("SHOW DATABASES"));
        return extractResults(queryResult,0);
    }

    public ArrayList<String> loadMeasurements(String dbName){
        QueryResult queryResult = influxDBServer.query(new Query("SHOW MEASUREMENTS",dbName));
        return extractResults(queryResult,0);
    }

    public void close(){
        influxDBServer.close();
    }
}
