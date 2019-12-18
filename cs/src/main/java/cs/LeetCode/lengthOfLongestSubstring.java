package cs.LeetCode;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/11/12.
 * @Description :
 */
public class lengthOfLongestSubstring {

    //给定一个字符串，请你找出其中不含有重复字符的最长子串的长度,用滑动窗口解决
    public int lengthOfLongestSubstring(String s) {
        int n = s.length(), ans = 0;
        Map<Character, Integer> map = new HashMap<>();
        for (int end = 0, start = 0; end < n; end++) {
            char alpha = s.charAt(end);
            if (map.containsKey(alpha)) {
                start = Math.max(map.get(alpha), start);
            }
            ans = Math.max(ans, end - start + 1);
            map.put(s.charAt(end), end + 1);
        }
        return ans;
    }

}
