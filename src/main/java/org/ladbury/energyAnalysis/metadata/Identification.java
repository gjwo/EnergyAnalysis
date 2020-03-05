package org.ladbury.energyAnalysis.metadata;

import org.ladbury.energyAnalysis.dataAccess.Meter;

public class Identification
{
    private String name;
    private String meterName;
    private String applianceName;
    public Identification(String name)
    {
        this.name = name;
    }
    public Identification(String name, String meterName, String applianceName)
    {
        this.name = name;
        this.meterName = meterName;
        this.applianceName = applianceName;
     }

    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public String getMeterName()
    {
        return meterName;
    }
    public void setMeterName(String meterName)
    {
        this.meterName = meterName;
    }
    public String getApplianceName() {return this.applianceName;}
    public void setApplianceName(String applianceName){this.applianceName = applianceName;}
    public void copyFields(Identification identification){
        this.name = identification.name;
        this.meterName = identification.meterName;
        this.applianceName = identification.applianceName;
    }

    @Override
    public String toString()
    {
        return "Identification{" +
                "name = '" + name + '\'' +
                ", meterName = '" + meterName + '\'' +
                ", applianceName = '" + applianceName + '\'' +
                '}';
    }
}
