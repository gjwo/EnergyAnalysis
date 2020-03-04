package org.ladbury.energyAnalysis.dataAccess.pOJOs;

import org.influxdb.annotation.Column;
import org.influxdb.annotation.TimeColumn;

import java.time.Instant;

@org.influxdb.annotation.Measurement(name = "Whole_House")
public class EnergyMeasurements
{
    @TimeColumn
    @Column(name = "time")
    private Instant time;
    @Column(name = "energy", tag = true)
    private double energy;
    @Column(name = "cumulativeenergy", tag = true)
    private double cumulativeenergy;

    public Instant getTime()
    {
        return time;
    }
    public void setTime(Instant time)
    {
        this.time = time;
    }
    public double getEnergy()
    {
        return energy;
    }
    public void setEnergy(double energy)
    {
        this.energy = energy;
    }
    public double getCumulativeenergy()
    {
        return cumulativeenergy;
    }
    public void setCumulativeenergy(double cumulativeenergy)
    {
        this.cumulativeenergy = cumulativeenergy;
    }

    @Override
    public String toString()
    {
        return "EnergyMeasurements{" +
                "time=" + time +
                ", Energy=" + energy +
                ", Cumulative Energy=" + cumulativeenergy +
                '}';
    }
}
