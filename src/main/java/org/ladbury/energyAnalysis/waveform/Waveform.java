package org.ladbury.energyAnalysis.waveform;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Waveform<E> extends ArrayList
{
    private final List<TimedElement> elements;

    Waveform()
    {
        elements = new ArrayList<>();
    }

    Waveform(List<? extends TimedElement<E>> list)
    {
        elements = new ArrayList<>();
        for(TimedElement<E> element: list){
            elements.add(new TimedElement<>(element.getTime(), element.getElement()));
        }
    }

    Instant containsSample(Waveform sample)
    {
        return null;
    }
}
