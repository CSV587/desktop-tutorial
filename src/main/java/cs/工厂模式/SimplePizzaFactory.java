package cs.工厂模式;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/9/26.
 * @Description :
 */
public class SimplePizzaFactory {
    public String createPizza(String type){
        String p = null;
        if(type.equals("cheese")){
            p = "CheesePizza";
        } else if(type.equals("clam")){
            p = "ClamPizza";
        } else if(type.equals("veggie")){
            p = "VeggiePizza";
        }
        return p;
    }
}
