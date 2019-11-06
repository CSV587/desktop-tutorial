package cs.装饰者模式.coffee;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/9/26.
 * @Description :
 */
public class Milk extends CondimentDecorator {

    Beverage beverage;

    public Milk(Beverage b){
        this.beverage = b;
    }

    @Override
    public String getDescription() {
        return beverage.getDescription() + ",Milk";
    }

    @Override
    public double cost() {
        return beverage.cost() + 0.25;
    }
}
