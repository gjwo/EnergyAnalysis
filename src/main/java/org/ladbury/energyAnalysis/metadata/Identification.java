package org.ladbury.energyAnalysis.metadata;

public class Identification
{
    private String name;
    private String source;
    public Identification(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getSource()
    {
        return source;
    }

    public void setSource(String source)
    {
        this.source = source;
    }

    @Override
    public String toString()
    {
        return "Identification{" +
                "name='" + name + '\'' +
                ", source='" + source + '\'' +
                '}';
    }
}
