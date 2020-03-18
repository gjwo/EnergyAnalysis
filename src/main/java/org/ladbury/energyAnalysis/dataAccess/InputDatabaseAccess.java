package org.ladbury.energyAnalysis.dataAccess;

import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBResultMapper;
import org.ladbury.energyAnalysis.dataAccess.pOJOs.CumulativeEnergyMeasures;
import org.ladbury.energyAnalysis.dataAccess.pOJOs.DiscreteMeasures;
import org.ladbury.energyAnalysis.meters.Meters;
import org.ladbury.energyAnalysis.timeSeries.Granularity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class InputDatabaseAccess
{
    private final List<String> validMeterTags;
    private final List<String> validMeasurements;
    private final InfluxDBResultMapper resultMapper;
    private final InfluxDataSource influxDBServer;
    private final String dbName;

    //Constructor
    public InputDatabaseAccess(InfluxDataSource influxDataSource, String dbName){
        this.influxDBServer = influxDataSource;
        this.dbName = dbName;
        this.resultMapper = new InfluxDBResultMapper();
        this.validMeasurements = influxDataSource.loadMeasurements(dbName);
        this.validMeterTags = loadMeterTags();
    }

    //Getters
    public List<String> getValidMeterTags() { return validMeterTags; }
    public List<String> getValidMeasurements() { return validMeasurements; }
    public InfluxDBResultMapper getResultMapper() { return resultMapper; }
    public InfluxDataSource getInfluxDBServer() { return influxDBServer; }
    public String getDbName() { return dbName; }

     public Meters loadMeters(){
        return new Meters(loadMeterTags());
    }

    public QueryResult query(String queryString){
        return getInfluxDBServer().query(queryString,dbName);
    }


    public ArrayList<String> loadMeterTags(){
        QueryResult queryResult;
        ArrayList<String> results = new ArrayList<>();
        queryResult = influxDBServer.query("SHOW TAG VALUES WITH KEY = \"meter\"",getDbName());
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
}