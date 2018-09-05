package me.minidigger.minecraftlauncer.renderer.util;

/**
 * @author Mark Vainomaa
 */
public interface ArrayUtilities {
    static int[] reverseIntArray(int[] input) {
        int tmp, i = 0, j = input.length - 1;

        while (j > i) {
            tmp = input[j];
            input[j] = input[i];
            input[i] = tmp;
            j--;
            i++;
        }

        return input;
    }

    static int[] mergeIntArrays(int[] a, int[] b) {
        int totalLen = a.length + b.length;
        int[] newArray = new int[totalLen];

        System.arraycopy(a, 0, newArray, 0, a.length);
        System.arraycopy(b, 0, newArray, a.length, b.length);

        return newArray;
    }
}
