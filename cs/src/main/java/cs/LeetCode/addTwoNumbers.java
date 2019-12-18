package cs.LeetCode;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/11/12.
 * @Description :
 */
public class addTwoNumbers {

    class ListNode{

        int val;
        ListNode next;

        public ListNode(int val){
            this.val = val;
        }

        public int getVal() {
            return val;
        }

        public ListNode getNext() {
            return next;
        }

        public void setNext(ListNode next) {
            this.next = next;
        }
    }

    //给出两个链表，按位相加，带进位，输出相加后的链表，即头节点
    public ListNode addTwoNumbers(ListNode l1, ListNode l2){
        int carry = 0;
        ListNode lResult = new ListNode(0);
        ListNode lPointer = lResult;
        while(l1 != null || l2 != null){
            int n1 = 0,n2 = 0;
            if(l1 != null){
                n1 = l1.getVal();
                l1 = l1.next;
            }
            if(l2 != null){
                n1 = l2.getVal();
                l2 = l2.next;
            }
            int tmp = n1 + n2 + carry;
            carry = tmp / 10;
            tmp %= 10;
            lPointer.next = new ListNode(tmp);
            lPointer = lPointer.next;
        }
        if(carry > 0){
            lPointer.next = new ListNode(carry);
        }
        return lResult.next;

    }

}
