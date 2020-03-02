package org.ladbury.energyAnalysis.timeSeries;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;

public class TimestampedDouble implements Timestamped<Double>
{
    private Double value;
    private Instant timestamp;

    /**
     * TimestampedDouble constructor, default both parameters (0,now)
     */
    public TimestampedDouble()
    {
        value = 0.0;
        timestamp = Instant.now();
    }

    /**
     * TimestampedDouble constructor default timestamp to now, set value
     * @param value     The value to be stored
     */
    public TimestampedDouble(double value)
    {
        this.value = value;
        this.timestamp = Instant.now();
    }

    /**
     * TimestampedDouble constructor set both values
     * @param value         The value to be stored
     * @param timestamp     The time to be stored
     */
    public TimestampedDouble(double value, Instant timestamp)
    {
        this.value = value;
        this.timestamp = timestamp;
    }
    /**
     * TimestampedDouble constructor set both values
     * @param value         The value to be stored
     * @param epochMilli    The time in epochMilli
     */
    public TimestampedDouble(double value, long epochMilli)
    {
        this.value = value;
        this.timestamp = Instant.ofEpochMilli(epochMilli);
    }

    /**
     * TimestampedDouble constructor set both values
     * @param value         The value to be stored
     * @param timeString    The time in ISO_ZONED_DATE_TIME format
     */
    public TimestampedDouble(double value, String timeString)
    {
        this.value = value;
        TemporalAccessor creationAccessor = DateTimeFormatter.ISO_ZONED_DATE_TIME.parse(timeString);
        this.timestamp = Instant.from(creationAccessor);
    }

    /**
     * toString         Converts the contents to a printable string
     * @return          The printable string
     */
    public String toString()
    {
        final DateTimeFormatter formatter =
                DateTimeFormatter.ISO_OFFSET_DATE_TIME
                        .withLocale( Locale.UK )
                        .withZone( ZoneId.systemDefault() );
        //System.out.println(String.format("Metric: {%.03f %s at %s}", value,unit.getSymbol(), formatter.format(timestamp)));
        return String.format("%.03f at %s", value, formatter.format(timestamp));
    }

    //
    // getters and setters
    //
    public Instant getTimestamp()
    {
        return timestamp;
    }
    public void setTimestamp(Instant timestamp)
    {
        this.timestamp = timestamp;
    }
    public double getValue()
    {
        return value;
    }
    public void setValue(double value)
    {
        this.value = value;
    }
    public long toEpochMilli()
    {
        return timestamp.toEpochMilli();
    }

    @Override
    public Instant timestamp()
    {
        return timestamp;
    }

    @Override
    public String timestampString()
    {
        return toString();
    }

    @Override
    public boolean happenedBetween(Instant ts1, Instant ts2)
    {
        return (timestamp.isBefore(ts2)&& timestamp.isAfter(ts1));
    }
}
