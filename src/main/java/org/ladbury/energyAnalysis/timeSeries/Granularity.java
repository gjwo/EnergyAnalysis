package org.ladbury.energyAnalysis.timeSeries;

import static org.ladbury.energyAnalysis.timeSeries.Timestamped.*;


public enum Granularity
{
    UNDEFINED (0,""),
    SECOND 		(SECOND_IN_MS,"1s"),
    TEN_SECOND 	(SECOND_IN_MS*10, "10s"),
    MINUTE		(MINUTE_IN_MS, "1m"),
    FIVE_MINUTE (MINUTE_IN_MS*5, "5m"),
    TEN_MINUTE	(MINUTE_IN_MS*10, "10m"),
    FIFTEEN_MINUTE (MINUTE_IN_MS*15, "15m"),
    THIRTY_MINUTE (MINUTE_IN_MS*30, "30m"),
    HOUR		(HOUR_IN_MS, "1h"),
    DAY			(Timestamped.DAY_IN_MS, "1d"),
    WEEK			(Timestamped.DAY_IN_MS*7, "1w");

    private final long grainInterval;
    private final String influxGrain;

    Granularity(long grainInterval, String influxGrain) {
        this.grainInterval = grainInterval;
        this.influxGrain = influxGrain;
    }
    public long getGrainIntervalInMS() {return grainInterval;}
    public long getGrainIntervalInSeconds() {return grainInterval/SECOND_IN_MS;}
    public long getGrainIntervalInMinutes() {return grainInterval/MINUTE_IN_MS;}
    public long getGrainIntervalInHours() {return grainInterval/HOUR_IN_MS;}
    public String getInfluxGrain(){return this.influxGrain;}
}
