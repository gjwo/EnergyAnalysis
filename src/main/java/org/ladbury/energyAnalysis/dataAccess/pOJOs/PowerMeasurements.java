package org.ladbury.energyAnalysis.dataAccess.pOJOs;

import org.influxdb.annotation.Column;
import org.influxdb.annotation.TimeColumn;
import org.ladbury.energyAnalysis.dataAccess.MetricCapable;
import org.ladbury.energyAnalysis.metadata.MetricType;

import java.time.Instant;

@org.influxdb.annotation.Measurement(name = "discreteMeasures")
public class PowerMeasurements implements MetricCapable
{
    @TimeColumn
    @Column(name = "time")
    private Instant time;
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
            default: return 0.0;
        }
    }
    public boolean containsMetric(MetricType metricType)
    {
        switch (metricType){
            case REAL_POWER:
            case REACTIVE_POWER:
            case APPARENT_POWER:
            case POWERFACTOR:     return  true;
            default: return false;
        }
    }
}
