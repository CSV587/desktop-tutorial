package cs.观察者模式;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/9/23.
 * @Description :
 */
public class test {

    public static void main(String[] arg) {
        WeatherData weatherData = new WeatherData();
        CurrentConditionDisplay ccd = new CurrentConditionDisplay(weatherData);
        weatherData.setMeasurements(1,2,3);
        weatherData.setMeasurements(4,5,6);
        weatherData.setMeasurements(7,8,9);
    }

}
