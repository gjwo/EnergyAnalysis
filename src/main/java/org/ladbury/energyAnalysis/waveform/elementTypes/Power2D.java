package org.ladbury.energyAnalysis.waveform.elementTypes;

public class Power2D
{
    public Double powerReal;
    public Double powerReactive;
    public Power2D(){
        powerReal = 0.0;
        powerReactive = 0.0;}
    public Power2D(Double powerReal, Double powerReactive){
        this.powerReactive = powerReactive;
        this.powerReal = powerReal;}
}
