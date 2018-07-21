package com.whg.medium.nextpermutation;

/**
 * Solution class
 *
 * problemId 31
 * @author wanghaogang
 * @date 2018/6/24
 */
public class Solution {

    public void nextPermutation(int[] nums) {
        if(nums==null||nums.length<=1) {
            return;
        }
        int next = nums.length-1;
        for (;;) {
            int next1 = next;
            if (nums[--next] < nums[next1]) {
                int mid = nums.length - 1;
                for (; mid>1&&nums[next] >= nums[mid]; ) {
                    --mid;
                }
                int tmp = nums[mid];
                nums[mid] = nums[next];
                nums[next] = tmp;
                int l = next1, r = nums.length - 1;
                while (l < r) {
                    tmp = nums[l];
                    nums[l] = nums[r];
                    nums[r] = tmp;
                    ++l;
                    --r;
                }
                return;
            }
            if(next==0) {
                int l = 0, r = nums.length - 1;
                while (l < r) {
                    int tmp = nums[l];
                    nums[l] = nums[r];
                    nums[r] = tmp;
                    ++l;
                    --r;
                }
                break;
            }
        }
    }
    public static void main(String[] args) {
        int[][] numss = {{1,3,2},{5,5,5},{2,1,3},{1,2,5,4,3},{1}};
        for (int[] nums: numss) {
            new Solution().nextPermutation(nums);
            for (int x : nums) {
                System.out.print(x + ",");
            }
            System.out.println();
        }
    }
}
