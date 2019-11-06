package cs.工厂模式;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/9/26.
 * @Description :
 */
public class PizzaStore {
    SimplePizzaFactory factory;
    public PizzaStore(SimplePizzaFactory factory){
        this.factory = factory;
    }
    public String orderPizza(String type){
        String pizza;
        pizza = factory.createPizza(type);
        return pizza;
    }
}
