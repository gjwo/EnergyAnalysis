package org.ladbury.energyAnalysis.timeSeries;

import java.time.Instant;

public class Waveform extends TimeSeries
{

    public Waveform()
    {
        super( Granularity.SECOND);

    }



    public Instant containsSample(Waveform sample)
    {
        return null;
    }
}
