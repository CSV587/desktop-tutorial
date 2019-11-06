package cs.动态代理;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/9/27.
 * @Description :
 */
public interface PersonBean {

    String getName();
    String getGender();
    String getInterests();
    int getHotOrNotRating();

    void setName(String name);
    void setGender(String gender);
    void setInterests(String interests);
    void setHotOrNotRating(int rating);

}
