package org.ladbury.energyAnalysis.dataAccess.pOJOs;

import org.influxdb.annotation.Column;
import org.influxdb.annotation.TimeColumn;

import java.time.Instant;

@org.influxdb.annotation.Measurement(name = "discreteMeasures")
public class PowerMeasurements
{
    @TimeColumn
    @Column(name = "time")
    private Instant time;
    @Column(name = "realPower", tag = true)
    private double realPower;
    @Column(name = "apparentPower", tag = true)
    private double apparentPower;
    @Column(name = "reactivePower", tag = true)
    private double reactivePower;
    @Column(name = "powerfactor", tag = true)
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
        return "PowerMeasurements{" +
                "time=" + time +
                ", realPower=" + realPower +
                ", apparentPower=" + apparentPower +
                ", reactivePower=" + reactivePower +
                '}';
    }
}
