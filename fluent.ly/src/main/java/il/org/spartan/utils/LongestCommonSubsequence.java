package il.org.spartan.utils;

import java.util.*;

import fluent.ly.*;
import il.org.spartan.*;

/** Utility class for computing the "Longest Common Subsequence" of two strings.
 * The LCS is computed after removing all spaces from both strings.
 * @author Yossi Gil
 * @since 2015-09-23 */
public class LongestCommonSubsequence {
  private final char[] as;
  private final char[] bs;
  private final int[][] length;

  public LongestCommonSubsequence(final String a, final String b) {
    as = tide.clean(a).toCharArray();
    bs = tide.clean(b).toCharArray();
    length = new int[as.length][];
    for (int ¢ = 0; ¢ < as.length; ++¢)
      Arrays.fill(length[¢] = new int[bs.length], -1);
  }
  /** @return A value between 0 and 1 */
  public double similarity() {
    return 2. * length() / (as.length + bs.length);
  }
  private int compute(final int i, final int j) {
    return i < 0 || j < 0 ? 0 : threeWayDynamicProgramingStep(i, j);
  }
  private int length() {
    return as.length <= 0 || bs.length <= 0 ? 0 : length(as.length - 1, bs.length - 1); //
  }
  /** Returns the length of the LCS of two prefixes of the current strings,
   * <code>as[0]...as[i]</code>, and <code>as[0]...as[i]</code>, i (respectively
   * j) must be a valid index of array a (respectively b), or else, the
   * substring of a (respectively b) are empty.
   * @param i
   * @param j
   * @return */
  private int length(final int i, final int j) {
    return i < 0 || j < 0 ? 0 : obtainLength(i, j);
  }
  private int obtainLength(final int i, final int j) {
    return length[i][j] != -1 ? length[i][j] : (length[i][j] = compute(i, j));
  }
  private int threeWayDynamicProgramingStep(final int i, final int j) {
    return the.max(length(i - 1, j), length(i, j - 1), length(i - 1, j - 1) + fluent.ly.as.bit(as[i] == bs[j]));
  }
}