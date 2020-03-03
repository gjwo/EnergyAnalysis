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

    public Instant containsSample(Waveform sample)
    {
        return null;
    }
    public Waveform minus(Waveform sample){return null;}
    public Waveform plus(Waveform sample){return null;}
}
