package org.ladbury.energyAnalysis.waveform;

import java.time.Instant;

public class TimedElement<E>
{
    Instant time;
    E element;
    TimedElement(Instant time, E element)
    {
        this.time = time;
        this.element = element;
    }
    Instant getTime(){return time;}
    E getElement(){return element;}
}
