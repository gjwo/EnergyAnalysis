package org.ladbury.energyAnalysis.timeSeries;

import org.ladbury.energyAnalysis.metadata.Description;
import org.ladbury.energyAnalysis.metadata.Identification;
import org.ladbury.energyAnalysis.metadata.TimeSeriesSummary;
import java.util.ArrayList;


public class TimeSeries extends ArrayList<TimestampedDouble>
{
    protected TimeSeriesSummary summary;
    protected Description description;
    protected Identification identification;

    public TimeSeries(Granularity grain)
    {
        super();
        summary = new TimeSeriesSummary();
        description = new Description();
        identification = new Identification("unNamed");
    }

    public void summarise()
    {
        if (this.size()>0) {
            for (TimestampedDouble element : this) {
                if ( element.getValue() < summary.getFloor()) summary.setFloor(element.getValue());
                if ( element.getValue() > summary.getCeiling()) summary.setCeiling(element.getValue());
            }
            summary.setEarliest(this.get(0).getTimestamp());
            summary.setLatest(this.get(this.size()-1).getTimestamp());
        }
    }
    // getters and setters
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

    @Override
    public String toString()
    {
        return "TimeSeries{" +
                "summary=" + summary +
                ", description=" + description +
                ", identification=" + identification +
                '}';
    }
    public String valuesToString(){
        return this.toString ();
    }
    public void printValues(){
        for( TimestampedDouble element : this){
            System.out.println("["+element.timestampString()+" : "+ element.getValue()+"]");
        }
    }

}
