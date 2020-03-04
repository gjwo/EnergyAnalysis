package org.ladbury.energyAnalysis.dataAccess.pOJOs;

import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;
import org.influxdb.annotation.TimeColumn;

import java.time.Instant;

@Measurement(name = "Whole_House")
public class BasicMeasurements
{
    @TimeColumn
    @Column(name = "time")
    private Instant time;
    @Column(name = "voltage", tag = true)
    private double voltage;
    @Column(name = "current", tag = true)
    private double current;
    public Instant getTime()
    {
        return time;
    }
    public void setTime(Instant time)
    {
        this.time = time;
    }
    public double getVoltage()
    {
        return voltage;
    }
    public void setVoltage(double voltage)
    {
        this.voltage = voltage;
    }
    public double getCurrent()
    {
        return current;
    }
    public void setCurrent(double current)
    {
        this.current = current;
    }

    @Override
    public String toString()
    {
        return "BasicMeasurements{" +
                "time=" + time +
                ", voltage=" + voltage +
                ", current=" + current +
                '}';
    }
}
