package cs.观察者模式;

import java.util.Observable;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/9/23.
 * @Description :
 */
public class WeatherData extends Observable {

    private float temperature;
    private float humidity;
    private float pressure;

    public void measurementsChanged(){
        setChanged();
        notifyObservers();
    }

    public void setMeasurements(float temperature, float humidity, float pressure){
        this.setTemperature(temperature);
        this.setHumidity(humidity);
        this.setPressure(pressure);
        measurementsChanged();
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    public float getPressure() {
        return pressure;
    }

    public void setPressure(float pressure) {
        this.pressure = pressure;
    }

}
