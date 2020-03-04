package org.ladbury.energyAnalysis.metadata;

public class Description
{
    private MetricType metricType;
    private boolean isCumulative;

    public Description(){
        this(MetricType.VOLTAGE);
    }

    public Description(MetricType metricType){
        this.metricType = metricType;
        isCumulative = false;
    }

    public boolean isCumulative()
    {
        return isCumulative;
    }

    public void setCumulative(boolean cumulative)
    {
        isCumulative = cumulative;
    }

    @Override
    public String toString()
    {
        return "Description{" +
                "metricType=" + metricType +
                ", isCumulative=" + isCumulative +
                '}';
    }
}
