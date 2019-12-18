package cs;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/12/9.
 * @Description :
 */
public class CloneDemo implements Cloneable{

    private int a = 1;
    private int b = 2;
    private Genericity g;

    @Override
    public CloneDemo clone() {
        CloneDemo cd = new CloneDemo();
        cd.setA(this.getA());
        cd.setB(this.getB());
        cd.setG(this.getG());
        return cd;
    }

    public Genericity getG() {
        return g;
    }

    public void setG(Genericity g) {
        this.g = g;
    }

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

}
