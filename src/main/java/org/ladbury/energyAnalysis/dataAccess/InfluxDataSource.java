package org.ladbury.energyAnalysis.dataAccess;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.QueryResult;
import org.influxdb.dto.Query;
import org.influxdb.impl.InfluxDBResultMapper;
import org.ladbury.energyAnalysis.dataAccess.pOJOs.CumulativeEnergyMeasures;
import org.ladbury.energyAnalysis.dataAccess.pOJOs.DiscreteMeasures;
import org.ladbury.energyAnalysis.dataAccess.pOJOs.RealPowerMeasurement;
import org.ladbury.energyAnalysis.meters.Meter;
import org.ladbury.energyAnalysis.meters.Meters;
import org.ladbury.energyAnalysis.timeSeries.Granularity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class InfluxDataSource
{
    private final InfluxDB influxDBServer;
    private final String url;
    private String dbName;
    private final List<String> validDBs;
    private final List<String> validMeterTags;
    private final List<String> validMeasurements;
    private final InfluxDBResultMapper resultMapper;

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
        validMeterTags = loadMeterTags();
        this.resultMapper = new InfluxDBResultMapper(); // thread-safe - can be reused
    }

    //getters
    public InfluxDB getInfluxDBServer(){return this.influxDBServer;}
    public String getDbName(){return this.dbName;}
    public List<String> getValidDBs() {return validDBs;}
    public List<String> getValidMeterTags() { return validMeterTags; }
    public List<String> getValidMeasurements() { return validMeasurements; }
    public void printDBInfo()
    {
        System.out.print("Database name: "+dbName);
        System.out.println(" url: "+url);
        System.out.print("Measures: ");
        for (String measure: getValidMeasurements()){
            System.out.print(measure+" ");
        }
        System.out.println();
        System.out.print("Meter Tags: ");
        for (String tag: getValidMeterTags()){
            System.out.print(tag+" ");
        }
        System.out.println();
    }


    public Meters loadMeters(){
        return new Meters(loadMeterTags());
    }

    public QueryResult query(String queryString){
        return influxDBServer.query(new Query(queryString,dbName));
    }

    // Query result helper function
    private ArrayList<String> extractResults(QueryResult queryResult)
    {
        ArrayList<String> results = new ArrayList<>();
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
    return  results;
    }

    private ArrayList<String> loadValidDBs(){
        QueryResult queryResult = influxDBServer.query(new Query("SHOW DATABASES"));
        return extractResults(queryResult);
    }

    private ArrayList<String> loadMeasurements(){
        QueryResult queryResult = influxDBServer.query(new Query("SHOW MEASUREMENTS",getDbName()));
        return extractResults(queryResult);
    }

    public ArrayList<String> loadMeterTags(){
        QueryResult queryResult;
        ArrayList<String> results = new ArrayList<>();
        queryResult = influxDBServer.query(new Query("SHOW TAG VALUES WITH KEY = \"meter\"",getDbName()));
        List<QueryResult.Result> resultsList= queryResult.getResults();
        List <QueryResult.Series> resultSeriesList;
        for (QueryResult.Result result :resultsList) {
            resultSeriesList = result.getSeries();
            QueryResult.Series series = resultSeriesList.get(0);// element zero is first measure alphabetically  (cumulativeMeasures)
            //for (QueryResult.Series series : resultSeriesList){
                for (List<Object> objects : series.getValues()) {
                    results.add(objects.toArray()[1].toString()); // element 0 is key, element 1 is value
                    //System.out.print(objects.toArray()[1].toString()+", ");
                }
                //System.out.println();
            //}
        }
        //System.out.println();
        return results;
    }
/*
    public ArrayList<String> LoadMeterFieldKeys(Meters meters)
    {
        // now done statically in meter
        ArrayList<String> keys = new ArrayList<>();
        QueryResult queryResult;
        queryResult = influxDBServer.query(new Query("SHOW FIELD KEYS",getDbName()));
        List<QueryResult.Result> resultsList= queryResult.getResults();
        List <QueryResult.Series> resultSeriesList;
        for(Meter meter : meters) {
            for (QueryResult.Result result : resultsList) {
                resultSeriesList = result.getSeries();
                for (QueryResult.Series series : resultSeriesList) {
                    for (List<Object> objects : series.getValues()) {
                        keys.add(objects.toArray()[0].toString());
                    }
                }
            }
            System.out.println(meter.toString());
        }
        return keys;
    }

 */
    // query helper methods
    private String meanAsMetricField(String metric){return " MEAN(\""+metric+"\") AS \""+metric+"\" ";}
    private String meterClause(String meterName){return "(\"meter\" = '"+meterName+"')";    }
    private String lastSeconds(int seconds){return "time >=now() - " + seconds + "s";}
    private String lastMinutes(int minutes){return "time >=now() - " + minutes + "m";}
    private String timeInterval(Instant t1, Instant t2){return "time >= '"+t1.toString()+ "' AND time <= '"+t2.toString()+ "'";}

    public List<DiscreteMeasures> loadLatestDiscreteReadingsSet(int seconds, Granularity grain, String meterName)
    {
        seconds++; //to get the right number of readings
        String query = "SELECT" + meanAsMetricField("realPower") + ","
                + meanAsMetricField("reactivePower") + ","
                + meanAsMetricField("apparentPower") + ","
                + meanAsMetricField("powerfactor") + ","
                + meanAsMetricField("current") + ","
                + meanAsMetricField("voltage")
                + "FROM " + "\"discreteMeasures\""
                + " WHERE "+ meterClause(meterName)+" AND " + lastSeconds(seconds)
                + " GROUP BY time("+grain.getInfluxGrain()+") fill(0)";
        System.out.println(query);
        QueryResult res = query(query);
        return resultMapper.toPOJO(res, DiscreteMeasures.class);
    }

    public  List<DiscreteMeasures>  loadDiscreteReadingsSet(Instant t1, Instant t2, Granularity grain, String meterName)
    {
        //todo need to fetch appliance name
        String query = "SELECT" + meanAsMetricField("realPower") + ","
                + meanAsMetricField("reactivePower") + ","
                + meanAsMetricField("apparentPower") + ","
                + meanAsMetricField("powerfactor") + ","
                + meanAsMetricField("current") + ","
                + meanAsMetricField("voltage")
                + "FROM \"discreteMeasures\""
                + " WHERE "+ meterClause(meterName)+" AND "+timeInterval(t1,t2)
                + " GROUP BY time("+grain.getInfluxGrain()+") fill(0)";
        System.out.println(query);
        QueryResult res = query(query);
       return resultMapper.toPOJO(res, DiscreteMeasures.class);
    }
/*
    public void loadMetricReadings(MetricType metricType, Instant t1, Instant t2, Granularity grain)
    {
        String query = "SELECT" + meanAsMetricField(MetricMappings.getMetricDBName(metricType))
                + "FROM \"discreteMeasures\""
                +" WHERE "+ meterClause()+" AND "+timeInterval(t1,t2)
                + " GROUP BY time("+grain.getInfluxGrain()+") fill(0)";
        System.out.println(query);
        QueryResult res = query(query);
        processReadingsResult(resultMapper.toPOJO(res, RealPowerMeasurement.class),metricType);
    }

 */
    private String qs(String metric){return " SUM(\""+metric+"\") AS \""+metric+"\" ";}
    public List<CumulativeEnergyMeasures> loadLatestEnergyReadingsSet(int minutes, Granularity grain, String meterName)
    {
        minutes++; //to get the right number of readings
        String query = "SELECT" + qs("intervalEnergy") + "," + qs("cumulativeEnergy")
                + "FROM \"cumulativeMeasures\" "
                + " WHERE "+ meterClause(meterName)+" AND " + lastMinutes(minutes)
                + " GROUP BY time("+grain.getInfluxGrain()+")";
        System.out.println(query);
        QueryResult res = query(query);
        return (resultMapper.toPOJO(res, CumulativeEnergyMeasures.class));
    }

    public List<CumulativeEnergyMeasures> loadEnergyReadingsSet(Instant t1, Instant t2, Granularity grain, String meterName)
    {
        String query = "SELECT" + qs("intervalEnergy") + "," + qs("cumulativeEnergy")
                + "FROM \"cumulativeMeasures\" "
                + " WHERE "+ meterClause(meterName)+" AND " + timeInterval(t1,t2)
                + " GROUP BY time("+grain.getInfluxGrain()+")";
        System.out.println(query);
        QueryResult res = query(query);
        return resultMapper.toPOJO(res, CumulativeEnergyMeasures.class);
    }

    public void close(){
        influxDBServer.close();
    }
}
