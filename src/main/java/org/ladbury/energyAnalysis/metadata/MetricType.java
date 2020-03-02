package org.ladbury.energyAnalysis.metadata;

public enum MetricType
{
    VOLTAGE("Voltage",1.0,"V", "Volts"),
    VOLTAGE_MILLI("Voltage(mV)",0.001,"mV", "milli-volts"),
    CURRENT("Current",1.0,"A", "Amps"),
    CURRENT_MILLI("Current(mA)",0.001,"mA", "milli-amps"),
    ENERGY("Energy",1.0,"WH", "Watt Hours"),
    ENERGY_KILO("Energy(kWH)",1000.0,"kWH", "KiloWatt Hours"),
    REAL_POWER("Real_Power",1.0,"W","Watts"),
    REAL_POWER_KILO("Real_Power(kW)",1000.0,"KW", "Kilowatt"),
    APPARENT_POWER("Apparent_Power", 1.0,"VA", "Volt Amperes"),
    REACTIVE_POWER("Reactive_Power", 1.0,"VAR", "Volt Amperes Reactive"),
    POWERFACTOR("PowerFactor", 1.0,"PF","Power Factor");


    private final String metricName;
    private final double factor;
    private final String symbol;
    private final String unitName;

    //Constructor
    MetricType( String metricName, double factor, String symbol, String unitName)
    {

        this.metricName = metricName;
        this.factor = factor;
        this.symbol = symbol;
        this.unitName = unitName;
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
}
