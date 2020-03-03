package org.ladbury.energyAnalysis.dataAccess;

import java.util.HashMap;
import org.influxdb.dto.Query;

public class Querys
{
    private final HashMap<QueryName,String> queryMap = new HashMap<>();
    String dbName;

    public Querys(String dbName)
    {
        this.dbName = dbName;
        queryMap.put(QueryName.LAST_MEASUREMENTS,"SELECT * FROM \"Whole_House\" WHERE time >= now() - 1m");
        queryMap.put(QueryName.SHOW_FIELD_KEYS,"SHOW FIELD KEYS");
        queryMap.put(QueryName.SHOW_MEASUREMENTS,"SHOW MEASUREMENTS");
        queryMap.put(QueryName.SHOW_RETENTION,"SHOW RETENTION POLICIES");
        queryMap.put(QueryName.SHOW_SERIES,"SHOW SERIES");
        queryMap.put(QueryName.SHOW_TAG_KEYS ,"SHOW TAG KEYS");
    }
    public Query getQuery( QueryName queryName)
    {
        if (queryMap.containsKey(queryName)){
            return new Query (queryMap.get(queryName),dbName);
        }
        return new Query("SELECT * FROM \"Whole_House\" WHERE time >= now() - 5m", dbName);
    }
}
