package org.ladbury.energyAnalysis.meters;

import java.util.ArrayList;

public class Meters extends ArrayList<Meter>
{
    public Meters( ArrayList<String> meters){
        super();
        for (String meterName :meters){
            Meter meter ;
            if (meterName.startsWith("Plug")){
                meter= new Meter(meterName,MeterType.TASMOTA);
            } else {
                meter= new Meter(meterName,MeterType.PMON10);
            }
            this.add(meter);
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
