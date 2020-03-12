package org.ladbury.energyAnalysis.dataAccess;

import java.util.ArrayList;
import java.util.HashMap;

public class Meters extends ArrayList<Meter>
{
    Meters( ArrayList<String> measurements){
        super();
        for (String measurement :measurements){
            this.add(new Meter(measurement));
        }
    }
    public Meter getMeter(String name){
        for (Meter meter :this)
        if (meter.getName().compareTo(name)==0){
            return meter;
        }
        System.out.println("Meter: "+name+" not found");
        return null;
    }

    @Override
    public String toString()
    {
        return super.toString();
    }
}
