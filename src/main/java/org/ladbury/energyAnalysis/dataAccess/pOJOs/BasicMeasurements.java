package org.ladbury.energyAnalysis.dataAccess.pOJOs;

import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;
import org.influxdb.annotation.TimeColumn;
import org.ladbury.energyAnalysis.dataAccess.MetricCapable;
import org.ladbury.energyAnalysis.metadata.MetricType;

import java.time.Instant;

@Measurement(name = "discreteMeasures")
public class BasicMeasurements  implements MetricCapable
{
    @TimeColumn
    @Column(name = "time")
    private Instant time;
    @Column(name = "voltage")
    private double voltage;
    @Column(name = "current")
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
    public double getValue(MetricType metricType)
    {
        switch (metricType){
            case VOLTAGE: return  getVoltage();
            case CURRENT: return getCurrent();
            default: return 0.0;
        }
    }
    public boolean containsMetric(MetricType metricType)
    {
        switch (metricType){
            case VOLTAGE:
            case CURRENT:  return  true;
            default: return false;
        }
    }

}
