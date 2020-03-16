package org.ladbury.energyAnalysis.timeSeries;

import java.time.Instant;

public class Waveform extends TimeSeries
{
    //Constructors
    public Waveform() {this( Granularity.SECOND);}
    public Waveform(Granularity grain)
    {
        super(grain);
    }
    public Waveform(TimeSeries timeSeries){
        super(timeSeries.getSummary().getGrain());
        this.copyFields(timeSeries);
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
