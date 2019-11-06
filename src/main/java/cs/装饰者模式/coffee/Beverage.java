package cs.装饰者模式.coffee;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/9/26.
 * @Description :
 */
public abstract class Beverage {

    String description = "Unknown Beverage";
    public String getDescription(){
        return description;
    }
    public abstract double cost();

}
