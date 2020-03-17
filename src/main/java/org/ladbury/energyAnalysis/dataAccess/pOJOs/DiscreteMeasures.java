package org.ladbury.energyAnalysis.dataAccess.pOJOs;

import org.influxdb.annotation.*;
import org.ladbury.energyAnalysis.metadata.MetricType;

import java.time.Instant;

@org.influxdb.annotation.Measurement(name = "discreteMeasures")
public class DiscreteMeasures implements MetricCapable
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
    public double getValue(MetricType metricType)
    {
        switch (metricType){
            case REAL_POWER: return getRealPower();
            case REACTIVE_POWER: return getReactivePower();
            case APPARENT_POWER: return getApparentPower();
            case POWERFACTOR:   return getPowerfactor();
            case VOLTAGE: return  getVoltage();
            case CURRENT: return getCurrent();
            default: return 0.0;
        }
    }
    public boolean containsMetric(MetricType metricType)
    {
        switch (metricType){
            case REAL_POWER:
            case REACTIVE_POWER:
            case APPARENT_POWER:
            case POWERFACTOR:
            case VOLTAGE:
            case CURRENT:  return  true;
            default: return false;
        }
    }
}
