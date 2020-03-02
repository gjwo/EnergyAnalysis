package org.ladbury.energyAnalysis.timeSeries;

import org.ladbury.energyAnalysis.metadata.Description;
import org.ladbury.energyAnalysis.metadata.Identification;
import org.ladbury.energyAnalysis.metadata.TimeSeriesSummary;

import java.util.ArrayList;

public class TimeSeries extends ArrayList<TimestampedDouble>
{
    TimeSeriesSummary summary;
    Description description;
    Identification identification;
    TimeSeries()
    {
        super();
    }

    public TimeSeriesSummary getSummary()
    {
        return summary;
    }

    public void setSummary(TimeSeriesSummary summary)
    {
        this.summary = summary;
    }

    public Description getDescription()
    {
        return description;
    }

    public void setDescription(Description description)
    {
        this.description = description;
    }

    public Identification getIdentification()
    {
        return identification;
    }

    public void setIdentification(Identification identification)
    {
        this.identification = identification;
    }
}
