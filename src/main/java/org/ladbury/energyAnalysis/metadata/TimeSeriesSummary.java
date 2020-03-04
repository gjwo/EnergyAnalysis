package org.ladbury.energyAnalysis.metadata;

import org.ladbury.energyAnalysis.timeSeries.Granularity;

import java.time.Instant;

public class TimeSeriesSummary
{
    protected Instant earliest;
    protected Instant latest;
    protected Granularity grain;
    protected double floor;
    protected double ceiling;

    public TimeSeriesSummary(){
        this(Granularity.SECOND);
    }
    public TimeSeriesSummary(Granularity grain)
    {
        this.earliest = Instant.now();
        this.latest = Instant.now();
        this.grain = grain;
        this.floor = 0;
        this.ceiling = 0;
    }

    public Instant getEarliest()
    {
        return earliest;
    }

    public void setEarliest(Instant earliest)
    {
        this.earliest = earliest;
    }

    public Instant getLatest()
    {
        return latest;
    }

    public void setLatest(Instant latest)
    {
        this.latest = latest;
    }

    public Granularity getGrain()
    {
        return grain;
    }

    public void setGrain(Granularity grain)
    {
        this.grain = grain;
    }

    public double getFloor()
    {
        return floor;
    }

    public void setFloor(double floor)
    {
        this.floor = floor;
    }

    public double getCeiling()
    {
        return ceiling;
    }

    public void setCeiling(double ceiling)
    {
        this.ceiling = ceiling;
    }

    @Override
    public String toString()
    {
        return "TimeSeriesSummary{" +
                "earliest=" + earliest +
                ", latest=" + latest +
                ", grain=" + grain +
                ", floor=" + floor +
                ", ceiling=" + ceiling +
                '}';
    }
}
