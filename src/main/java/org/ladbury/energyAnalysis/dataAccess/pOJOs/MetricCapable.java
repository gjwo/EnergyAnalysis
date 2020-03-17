package org.ladbury.energyAnalysis.dataAccess.pOJOs;

import org.ladbury.energyAnalysis.metadata.MetricType;

public interface MetricCapable
{
    public double getValue(MetricType metricType);
    public boolean containsMetric(MetricType metricType);
}
