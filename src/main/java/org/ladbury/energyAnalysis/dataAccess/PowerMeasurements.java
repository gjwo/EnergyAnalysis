package org.ladbury.energyAnalysis.dataAccess;

import org.influxdb.annotation.Column;
import org.influxdb.annotation.TimeColumn;

import java.time.Instant;

@org.influxdb.annotation.Measurement(name = "Whole_House")
public class PowerMeasurements
{
    @TimeColumn
    @Column(name = "time")
    private Instant time;
    @Column(name = "power", tag = true)
    private double power;
    @Column(name = "apparentpower", tag = true)
    private double apparentpower;
    @Column(name = "reactivepower", tag = true)
    private double reactivepower;

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
    public double getApparentpower()
    {
        return apparentpower;
    }
    public void setApparentpower(double apparentpower)
    {
        this.apparentpower = apparentpower;
    }
    public double getReactivepower()
    {
        return reactivepower;
    }
    public void setReactivepower(double reactivepower)
    {
        this.reactivepower = reactivepower;
    }

    @Override
    public String toString()
    {
        return "Measurement{" +
                "time=" + time +
                ", power=" + power +
                ", apparentpower=" + apparentpower +
                ", reactivepower=" + reactivepower +
                '}';
    }
}
