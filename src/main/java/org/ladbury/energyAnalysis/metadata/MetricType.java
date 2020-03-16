package org.ladbury.energyAnalysis.metadata;

public enum MetricType
{
    VOLTAGE("Voltage",1.0,"V", "Volts",false),
    VOLTAGE_MILLI("Voltage(mV)",0.001,"mV", "milli-volts",false),
    CURRENT("Current",1.0,"A", "Amps",false),
    CURRENT_MILLI("Current(mA)",0.001,"mA", "milli-amps",false),
    ENERGY("Energy",1.0,"WH", "Watt Hours",true),
    ENERGY_KILO("Energy(kWH)",1000.0,"kWH", "KiloWatt Hours",true),
    REAL_POWER("Real_Power",1.0,"W","Watts",false),
    REAL_POWER_KILO("Real_Power(kW)",1000.0,"KW", "Kilowatt",false),
    APPARENT_POWER("Apparent_Power", 1.0,"VA", "Volt Amperes",false),
    REACTIVE_POWER("Reactive_Power", 1.0,"VAR", "Volt Amperes Reactive",false),
    POWERFACTOR("PowerFactor", 1.0,"PF","Power Factor",false);


    private final String metricName;
    private final double factor;
    private final String symbol;
    private final String unitName;
    private final boolean isCumulative;

    //Constructor
    MetricType( String metricName, double factor, String symbol, String unitName, boolean isCumulative)
    {

        this.metricName = metricName;
        this.factor = factor;
        this.symbol = symbol;
        this.unitName = unitName;
        this.isCumulative = isCumulative;
    }

    //Getters
    public String getUnitName()
    {
        return unitName;
    }
    public String getSymbol()
    {
        return symbol;
    }
    public String getMetricName()
    {
        return metricName;
    }
    public double getFactor()
    {
        return factor;
    }
    public boolean isCumulative() {return isCumulative;}
}
