package cs.装饰者模式.coffee;

import cs.装饰者模式.coffee.Beverage;
import cs.装饰者模式.coffee.CondimentDecorator;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/9/26.
 * @Description :
 */
public class Whip extends CondimentDecorator {

    Beverage beverage;

    public Whip(Beverage b){
        this.beverage = b;
    }

    @Override
    public String getDescription() {
        return beverage.getDescription() + ",Whip";
    }

    @Override
    public double cost() {
        return beverage.cost() + 0.16;
    }
}
