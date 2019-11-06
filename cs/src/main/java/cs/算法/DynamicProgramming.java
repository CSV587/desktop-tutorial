package cs.算法;

import static java.lang.Math.max;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/10/12.
 * @Description :
 */
public class DynamicProgramming {

    public int[] w = new int[]{ 0 , 2 , 3 , 4 , 5 };			//商品的体积2、3、4、5
    public int[] v = new int[]{ 0 , 3 , 4 , 5 , 6 };			//商品的价值3、4、5、6
    public int bagV = 8;					        //背包大小
    public int[][] dp = new int[5][9];		        //动态规划表
    public int[] item = new int[5];                 //最优解情况

    public void findMax() {					//动态规划
        for (int i = 1; i <= 4; i++) {
            for (int j = 1; j <= bagV; j++) {
                if (j < w[i])
                    dp[i][j] = dp[i - 1][j];
                else
                    dp[i][j] = max(dp[i - 1][j], dp[i - 1][j - w[i]] + v[i]);
            }
        }
    }

    public void findWhat(int i, int j) {				//最优解情况
        if (i > 0) {
            if (dp[i][j] == dp[i - 1][j]) {
                item[i] = 0;
                findWhat(i - 1, j);
            }
            else if (j - w[i] >= 0 && dp[i][j] == dp[i - 1][j - w[i]] + v[i]) {
                item[i] = 1;
                findWhat(i - 1, j - w[i]);
            }
        }
    }

    public void print() {
        for (int i = 0; i < 5; i++) {			//动态规划表输出
            for (int j = 0; j < 9; j++) {
                System.out.println(dp[i][j]);
            }
        }

        for (int i = 0; i < 5; i++)			//最优解输出
            System.out.println(item[i]);
    }

    public int run()
    {
        findMax();
        findWhat(4, 8);
        print();

        return 0;
    }
}
