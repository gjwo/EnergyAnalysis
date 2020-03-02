package org.ladbury.energyAnalysis.waveform;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Waveform<E> extends ArrayList
{
    private final List<TimedElement> elements;
    private Instant earliestTime;
    private Instant latestTime;
    private Double floor;
    private Double ceiling;

    public Waveform()
    {
        elements = new ArrayList<>();
        floor = 0.0;
        ceiling = 0.0;
        earliestTime = null;
        latestTime = null;
    }

    public Waveform(List<? extends TimedElement<E>> list)
    {
        this();
        if (list != null) {
            if (list.size()>0) {
                for (TimedElement<E> element : list) {
                    elements.add(new TimedElement<>(element.getTime(), element.getElement()));
                }
                Collections.sort(this);
                earliestTime = elements.get(0).getTime();
                latestTime = elements.get(list.size()-1).getTime();
            }
        }
    }

    public Instant containsSample(Waveform sample)
    {
        return null;
    }
}
