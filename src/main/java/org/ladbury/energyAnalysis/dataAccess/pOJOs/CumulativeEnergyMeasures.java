package org.ladbury.energyAnalysis.dataAccess.pOJOs;

import org.influxdb.annotation.Column;
import org.influxdb.annotation.TimeColumn;
import org.ladbury.energyAnalysis.dataAccess.MetricCapable;
import org.ladbury.energyAnalysis.metadata.MetricType;

import java.time.Instant;

@org.influxdb.annotation.Measurement(name = "cumulativeMeasures")
public class CumulativeEnergyMeasures implements MetricCapable
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
    public double getValue(MetricType metricType)
    {
        switch (metricType){
            case ENERGY_KILO: return  getCumulativeEnergy();
            case ENERGY: return getIntervalEnergy();
            default: return 0.0;
        }
    }
    public boolean containsMetric(MetricType metricType)
    {
        switch (metricType){
            case ENERGY_KILO:
            case ENERGY:  return  true;
            default: return false;
        }
    }
}
