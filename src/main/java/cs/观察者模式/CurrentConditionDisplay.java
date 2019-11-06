package cs.观察者模式;

import java.util.Observable;
import java.util.Observer;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/9/23.
 * @Description :
 */
public class CurrentConditionDisplay implements Observer {

    private float temperature;
    private float humidity;
    private float pressure;
    private Observable observable;

    public CurrentConditionDisplay(Observable o) {
        this.setObservable(o);
        observable.addObserver(this);
    }

    @Override
    public void update(Observable obs, Object arg) {
        if(obs instanceof WeatherData){
            WeatherData wd = (WeatherData)obs;
            this.setTemperature(wd.getTemperature());
            this.setHumidity(wd.getHumidity());
            this.setPressure(wd.getPressure());
            display();
        }
    }

    public void display(){
        System.out.println("Current temperature is " + this.getTemperature());
        System.out.println("Current humidity is " + this.getHumidity());
        System.out.println("Current pressure is " + this.getPressure());
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

    public void setObservable(Observable observable) {
        this.observable = observable;
    }

}
