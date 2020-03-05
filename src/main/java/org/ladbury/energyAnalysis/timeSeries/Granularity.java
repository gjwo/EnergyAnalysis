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
    HOUR		(Timestamped.HOUR_IN_MS),
    DAY			(Timestamped.DAY_IN_MS);

    private final int grainInterval;

    Granularity(int grainInterval) {this.grainInterval = grainInterval;}
    public int getGrainInterval() {	return grainInterval;}
}
