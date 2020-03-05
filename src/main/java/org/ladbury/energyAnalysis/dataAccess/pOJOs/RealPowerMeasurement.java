package org.ladbury.energyAnalysis.dataAccess.pOJOs;

import org.influxdb.annotation.Column;
import org.influxdb.annotation.TimeColumn;

import java.time.Instant;

@org.influxdb.annotation.Measurement(name = "Plug1")
public class RealPowerMeasurement
{
    @TimeColumn
    @Column(name = "time")
    private Instant time;
    @Column(name = "power", tag = true)
    private double power;

    public Instant getTime()
    {
        return time;
    }
    public void setTime(Instant time)
    {
        this.time = time;
    }
    public double getPower()
    {
        return power;
    }
    public void setPower(double power)
    {
        this.power = power;
    }

    @Override
    public String toString()
    {
        return "PowerMeasurements{" +
                "time=" + time +
                ", power=" + power +
                '}';
    }
}
