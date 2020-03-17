package org.ladbury.energyAnalysis.dataAccess.pOJOs;

import org.influxdb.annotation.Column;
import org.influxdb.annotation.TimeColumn;
import org.ladbury.energyAnalysis.metadata.MetricType;

import java.time.Instant;

@org.influxdb.annotation.Measurement(name = "DiscreteMeasures")
public class RealPowerMeasurement implements MetricCapable
{
    @TimeColumn
    @Column(name = "time")
    private Instant time;
    @Column(name = "realPower")
    private double realPower;

    public Instant getTime()
    {
        return time;
    }
    public void setTime(Instant time)
    {
        this.time = time;
    }
    public double getRealPower()
    {
        return realPower;
    }
    public void setRealPower(double realPower)
    {
        this.realPower = realPower;
    }

    @Override
    public String toString()
    {
        return "DiscreteMeasures{" +
                "time=" + time +
                ", realPower=" + realPower +
                '}';
    }
    public double getValue(MetricType metricType)
{
    switch (metricType){
        case REAL_POWER: return getRealPower();
        default: return 0.0;
    }
}
    public boolean containsMetric(MetricType metricType)
    {
        switch (metricType){
            case REAL_POWER:    return  true;
            default: return false;
        }
    }
}
