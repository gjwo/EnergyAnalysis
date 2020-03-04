package org.ladbury.energyAnalysis.timeSeries;

import java.time.Instant;

public class Waveform extends TimeSeries
{

    public Waveform()
    {
        super( Granularity.SECOND);
    }
    public Waveform(Granularity grain)
    {
        super(grain);
    }
    public Waveform(Granularity grain,TimeSeries timeSeries){
        super(grain);
        for(TimestampedDouble tsd: timeSeries)
        {
            this.add(tsd);
        }
        this.getIdentification().setName(timeSeries.getIdentification().getName());
        this.getIdentification().setSource(timeSeries.getIdentification().getSource());
        this.getDescription().setMetricType(timeSeries.getDescription().getMetricType());
        this.summarise();
    }
    public Instant containsSample(Waveform sample)
    {
        return null;
    }
    public Waveform minus(Waveform sample){return null;}
    public Waveform plus(Waveform sample){return null;}
    public void print(){
        for (TimestampedDouble td: this) System.out.println(td.toString());
    }
}
