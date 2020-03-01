package org.ladbury.energyAnalysis.waveform;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Waveform<E> extends ArrayList
{
    private final List<TimedElement> elements;

    public Waveform()
    {
        elements = new ArrayList<>();
    }

    public Waveform(List<? extends TimedElement<E>> list)
    {
        elements = new ArrayList<>();
        for(TimedElement<E> element: list){
            elements.add(new TimedElement<>(element.getTime(), element.getElement()));
        }
    }

    public Instant containsSample(Waveform sample)
    {
        return null;
    }
}
