package de.malte.f1strat.helper;

public class ArrayHelper {

    public static int[] clipArray (int[] arr, int start, int end) {

        int[] res = new int[arr.length];
        if (end + 1 - start >= 0)
            System.arraycopy(arr, start, res, start, end + 1 - start);

        return res;

    }

    public static int sumArray (int[] arr) {
        int res = 0;
        for (int i : arr)
            res += i;

        return res;
    }


}
