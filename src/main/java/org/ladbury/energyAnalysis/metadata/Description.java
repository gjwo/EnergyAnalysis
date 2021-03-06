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

    public MetricType getMetricType(){return metricType;}
    public void setMetricType(MetricType metricType){this.metricType = metricType;}
    public boolean isCumulative()
    {
        return isCumulative;
    }
    public void setCumulative(boolean cumulative)
    {
        isCumulative = cumulative;
    }
    public void copyFields(Description description){
        this.metricType = description.metricType;
        this.isCumulative = description.isCumulative;
    }
    @Override
    public String toString()
    {
        return "Description{" +
                "metricType = " + metricType +
                ", isCumulative = " + isCumulative +
                '}';
    }
}
