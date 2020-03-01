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
        queryMap.put(QueryName.LAST_MEASUREMENTS,"SELECT * FROM \"Whole_House\" WHERE time >= now() - 15m");
    }
    public Query getQuery( QueryName queryName)
    {
        return new Query("SELECT * FROM \"Whole_House\" WHERE time >= now() - 15m", dbName);
    }
}
