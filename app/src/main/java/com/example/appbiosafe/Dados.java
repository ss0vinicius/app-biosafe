package com.example.appbiosafe;

public class Dados {
    public String ip = "http://192.168.0.14/";
    public String ledStopStatus = "OFF";
    public String ledOnCicleStatus = "OFF";
    public String ledStandByStatus = "OFF";
    public String relayHeatStatus = "OFF";
    public String relayLampStatus = "OFF";
    public String doorStatus = "OPEN";
    public String temperature;
    public String timeRemaing;


    public String getIp() {
        return ip;
    }
    public String getLedStopStatus() {
        return ledStopStatus;
    }
    public String getLedOnCicleStatus() {
        return ledOnCicleStatus;
    }
    public String getLedStandByStatus() {
        return ledStandByStatus;
    }
    public String getRelayHeatStatus() {
        return relayHeatStatus;
    }
    public String getRelayLampStatus() {
        return relayLampStatus;
    }
    public String getDoorStatus() {
        return doorStatus;
    }
    public String getTemperature() {
        return temperature;
    }
    public String getTimeRemaing() {
        return timeRemaing;
    }


    public void setIp(String ip) {
        this.ip = ip;
    }
    public void setLedStopStatus(String ledStopStatus) {
        this.ledStopStatus = ledStopStatus;
    }
    public void setLedOnCicleStatus(String ledOnCicleStatus) {
        this.ledOnCicleStatus = ledOnCicleStatus;
    }
    public void setLedStandByStatus(String ledStandByStatus) {
        this.ledStandByStatus = ledStandByStatus;
    }
    public void setRelayHeatStatus(String relayHeatStatus) {
        this.relayHeatStatus = relayHeatStatus;
    }
    public void setRelayLampStatus(String relayLampStatus) {
        this.relayLampStatus = relayLampStatus;
    }
    public void setDoorStatus(String doorStatus) {
        this.doorStatus = doorStatus;
    }
    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }
    public void setTimeRemaing(String timeRemaing) {
        this.timeRemaing = timeRemaing;
    }
}
