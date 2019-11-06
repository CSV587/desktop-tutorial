package cs.工厂模式;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/9/26.
 * @Description :
 */
public class test {
    public static void main(String[] arg){
        SimplePizzaFactory factory = new SimplePizzaFactory();
        PizzaStore pizzaStore = new PizzaStore(factory);
        System.out.println(pizzaStore.orderPizza("veggie"));
    }
}
