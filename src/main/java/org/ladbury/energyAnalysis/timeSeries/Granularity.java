package org.ladbury.energyAnalysis.timeSeries;

@SuppressWarnings("unused")
public enum Granularity
{
    UNDEFINED (0),
    SECOND 		(Timestamped.SECOND_IN_MS),
    TEN_SECOND 	(Timestamped.SECOND_IN_MS*10),
    MINUTE		(Timestamped.MINUTE_IN_MS),
    FIVE_MINUTE (Timestamped.MINUTE_IN_MS*5),
    TEN_MINUTE	(Timestamped.MINUTE_IN_MS*10),
    FIFTEEN_MINUTE (Timestamped.MINUTE_IN_MS*15),
    THIRTY_MINUTE (Timestamped.MINUTE_IN_MS*30),
    HOUR		(Timestamped.HOUR_IN_MS),
    DAY			(Timestamped.DAY_IN_MS),
    WEEK			(Timestamped.DAY_IN_MS*7);

    private final long grainInterval;

    Granularity(long grainInterval) {this.grainInterval = grainInterval;}
    public long getGrainInterval() {	return grainInterval;}
}
