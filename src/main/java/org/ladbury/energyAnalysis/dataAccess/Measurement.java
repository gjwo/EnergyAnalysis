package org.ladbury.energyAnalysis.dataAccess;

import org.influxdb.annotation.*;

import java.time.Instant;

@org.influxdb.annotation.Measurement(name = "Whole_House")
public class Measurement
{
    @TimeColumn
    @Column(name = "time")
    private Instant time;
    @Column(name = "voltage", tag = true)
    private double voltage;
    @Column(name = "current", tag = true)
    private double current;
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
                ", voltage=" + voltage +
                ", current=" + current +
                ", power=" + power +
                ", apparentpower=" + apparentpower +
                ", reactivepower=" + reactivepower +
                '}';
    }
}
