package org.ladbury.energyAnalysis.metadata;

import org.ladbury.energyAnalysis.timeSeries.Granularity;

import java.time.Instant;

public class TimeSeriesSummary
{
    Instant earliest;
    Instant latest;
    Granularity grain;
    double floor;
    double ceiling;
}
