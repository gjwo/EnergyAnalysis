package org.ladbury.energyAnalysis.meters;

import org.ladbury.energyAnalysis.dataAccess.pOJOs.DiscreteMeasures;
import org.ladbury.energyAnalysis.dataAccess.pOJOs.CumulativeEnergyMeasures;
import org.ladbury.energyAnalysis.metadata.MetricType;
import org.ladbury.energyAnalysis.timeSeries.Granularity;
import org.ladbury.energyAnalysis.timeSeries.TimeSeries;
import org.ladbury.energyAnalysis.timeSeries.TimestampedDouble;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Meter
{
    //Class fields
    private String name;
    private final ArrayList<MetricType> supportedMetricTypes;
    private final Map<MetricType,TimeSeries> readingsSet;
    private MeterType meterType;

    //Constructor
    public Meter(String name, MeterType meterType){
        this.name = name;
        this.supportedMetricTypes = MetricMappings.getSupportedMetricTypes(meterType);
        this.readingsSet = new HashMap<>();
    }
    //getter
    public String getName() { return name; }
    public void setType(MeterType meterType){ this.meterType = meterType;}

    public void clearReadings()
    {
        this.readingsSet.clear();
    }

    @Override
    public String toString()
    {
        return "Meter{" +
                "name='" + name + '\'' +
                ", supportedMetricTypes=" + supportedMetricTypes +
                '}';
    }

    /*
    public void processReadingsResult(List<RealPowerMeasurement> realPowerMeasurement, MetricType metricType){
        TimeSeries timeSeries = new TimeSeries(Granularity.SECOND);
        timeSeries.getIdentification().setName(metricType.getMetricName());
        timeSeries.getIdentification().setMeterName(name);
        timeSeries.getDescription().setMetricType(metricType);
        readingsSet.put(MetricType.REAL_POWER,timeSeries);
        for (RealPowerMeasurement  m: realPowerMeasurement)
        {
            readingsSet.get(MetricType.REAL_POWER).add(new TimestampedDouble(m.getRealPower(),m.getTime()));
        }
        readingsSet.get(MetricType.REAL_POWER).summarise();
    }
    */

    public void processDiscreteReadings(List<DiscreteMeasures> discreteMeasures){
        TimeSeries timeSeries = new TimeSeries(Granularity.SECOND);
        for (MetricType mt : supportedMetricTypes){
            if (!mt.isCumulative()) {
                timeSeries.getIdentification().setName(mt.getMetricName());
                 timeSeries.getIdentification().setMeterName(name);
                timeSeries.getDescription().setMetricType(mt);
                readingsSet.put(mt, timeSeries);
            }
        }
        System.out.println("Number of measures = "+discreteMeasures.size());
        for (DiscreteMeasures m: discreteMeasures)
        {
            readingsSet.get(MetricType.REAL_POWER).add(new TimestampedDouble(m.getRealPower(),m.getTime()));
            readingsSet.get(MetricType.REACTIVE_POWER).add(new TimestampedDouble(m.getReactivePower(),m.getTime()));
            readingsSet.get(MetricType.APPARENT_POWER).add(new TimestampedDouble(m.getApparentPower(),m.getTime()));
            readingsSet.get(MetricType.POWERFACTOR).add(new TimestampedDouble(m.getPowerfactor(),m.getTime()));
            readingsSet.get(MetricType.VOLTAGE).add(new TimestampedDouble(m.getVoltage(),m.getTime()));
            readingsSet.get(MetricType.CURRENT).add(new TimestampedDouble(m.getCurrent(),m.getTime()));
        }

        System.out.println();
        for (MetricType mt : supportedMetricTypes){
            if (!mt.isCumulative()) {
                readingsSet.get(mt).summarise();
            }
        }
    }
    public void processEnergyReadings(List<CumulativeEnergyMeasures> cumulativeEnergyMeasurements){
        TimeSeries timeSeries = new TimeSeries(Granularity.FIVE_MINUTE);
        timeSeries.getIdentification().setName(MetricType.ENERGY.getMetricName());
        timeSeries.getIdentification().setMeterName(name);
        timeSeries.getDescription().setMetricType(MetricType.ENERGY);
        readingsSet.put(MetricType.ENERGY,timeSeries);

        timeSeries = new TimeSeries(Granularity.FIVE_MINUTE);
        timeSeries.getIdentification().setName(MetricType.ENERGY_KILO.getMetricName());
        timeSeries.getIdentification().setMeterName(name);
        timeSeries.getDescription().setMetricType(MetricType.ENERGY_KILO);
        timeSeries.getDescription().setCumulative(true);
        readingsSet.put(MetricType.ENERGY_KILO,timeSeries);

        for (CumulativeEnergyMeasures m: cumulativeEnergyMeasurements)
        {
            readingsSet.get(MetricType.ENERGY).add(new TimestampedDouble(m.getIntervalEnergy(),m.getTime()));
            readingsSet.get(MetricType.ENERGY_KILO).add(new TimestampedDouble(m.getCumulativeEnergy(),m.getTime()));
        }
        readingsSet.get(MetricType.ENERGY).summarise();
        readingsSet.get(MetricType.ENERGY_KILO).summarise();
    }
    public TimeSeries getSeries(MetricType metricType){
        if(readingsSet.containsKey(metricType)) return readingsSet.get(metricType);
        return null;
    }
}
