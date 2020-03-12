package org.ladbury.energyAnalysis.dataAccess.pOJOs;

import org.influxdb.annotation.Column;
import org.influxdb.annotation.TimeColumn;

import java.time.Instant;

@org.influxdb.annotation.Measurement(name = "cumulativeMeasures")
public class CumulativeEnergyMeasures
{
    @TimeColumn
    @Column(name = "time")
    private Instant time;
    @Column(name = "intervalEnergy")
    private double intervalEnergy;
    @Column(name = "cumulativeEnergy")
    private double cumulativeEnergy;

    public Instant getTime()
    {
        return time;
    }
    public void setTime(Instant time)
    {
        this.time = time;
    }
    public double getIntervalEnergy()
    {
        return intervalEnergy;
    }
    public void setIntervalEnergy(double intervalEnergy)
    {
        this.intervalEnergy = intervalEnergy;
    }
    public double getCumulativeEnergy()
    {
        return cumulativeEnergy;
    }
    public void setCumulativeEnergy(double cumulativeEnergy)
    {
        this.cumulativeEnergy = cumulativeEnergy;
    }

    @Override
    public String toString()
    {
        return "cumulativeMeasures{" +
                "time=" + time +
                ", Interval Energy=" + intervalEnergy +
                ", Cumulative Energy=" + cumulativeEnergy +
                '}';
    }
}
