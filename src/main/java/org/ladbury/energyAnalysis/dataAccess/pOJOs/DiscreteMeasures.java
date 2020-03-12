package org.ladbury.energyAnalysis.dataAccess.pOJOs;

import org.influxdb.annotation.*;

import java.time.Instant;

@org.influxdb.annotation.Measurement(name = "discreteMeasures")
public class DiscreteMeasures
{
    @TimeColumn
    @Column(name = "time")
    private Instant time;
    @Column(name = "voltage")
    private double voltage;
    @Column(name = "current")
    private double current;
    @Column(name = "realPower")
    private double realPower;
    @Column(name = "apparentPower")
    private double apparentPower;
    @Column(name = "reactivePower")
    private double reactivePower;
    @Column(name = "powerfactor")
    private double powerfactor;

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
    public double getApparentPower()
    {
        return apparentPower;
    }
    public void setApparentPower(double apparentPower)
    {
        this.apparentPower = apparentPower;
    }
    public double getReactivePower()
    {
        return reactivePower;
    }
    public void setReactivePower(double reactivePower)
    {
        this.reactivePower = reactivePower;
    }
    public double getPowerfactor()
    {
        return powerfactor;
    }
    public void setPowerfactor(double powerfactor)
    {
        this.powerfactor = powerfactor;
    }

    @Override
    public String toString()
    {
        return "DiscreteMeasures{" +
                "time=" + time +
                ", voltage=" + voltage +
                ", current=" + current +
                ", realPower=" + realPower +
                ", apparentPower=" + apparentPower +
                ", reactivePower=" + reactivePower +
                ", powerfactor=" + powerfactor +
                '}';
    }
}
