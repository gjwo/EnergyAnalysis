package org.ladbury.energyAnalysis.dataAccess.pOJOs;

import org.influxdb.annotation.Column;
import org.influxdb.annotation.TimeColumn;

import java.time.Instant;

@org.influxdb.annotation.Measurement(name = "DiscreteMeasures")
public class RealPowerMeasurement
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
}
