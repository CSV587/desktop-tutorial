package cs.reflect;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/10/14.
 * @Description :
 */
public class PrivilegeManagement {

    int c;

    private PrivilegeManagement() {
        this.c = 111;
    }

    private void changeC(int s) {
        this.c = s;
        System.out.println(this.c);
    }

}
