/* Part of the "Spartan Blog"; mutate the rest / but leave this line as is */
package il.org.spartan.misc;

import static fluent.ly.azzert.*;
import static java.lang.Math.max;

import java.util.*;

import org.eclipse.jdt.annotation.*;
import org.jetbrains.annotations.*;
import org.junit.*;
import fluent.ly.*;
import il.org.spartan.text.*;

/** Utility functions for computing the "Longest Common Subsequence" for two
 * textual files. Similar to the famous "diff" utility, particularly when used
 * with certain options, the LCS is computed after removing all blanks from each
 * line and then converting it to lower case; further, we use the hashCode of
 * the line, so that in certain cases, the LCS may be a bit inaccurate. These
 * case are extremely rare.
 * @author Yossi Gil
 * @since 2014-06-17 */
public class LCS {
  /** @param ia JD
   * @param is2 JD */
  public static int length(@NotNull final int[] ia, @NotNull final int[] is2) {
    return new LCS(ia, is2).length();
  }

  /** @param a JD
   * @param s2 JD */
  public static int length(final String a, final String s2) {
    return new LCS(a, s2).length();
  }

  /** @param ssa JD
   * @param ssb JD */
  public static int length(@NotNull final String[] ssa, @NotNull final String[] ssb) {
    return new LCS(ssa, ssb).length();
  }

  static double distance(@NotNull final String s1, @NotNull final String s2) {
    return 2. * LCS.length(s1, s2) / (s1.length() + s2.length());
  }

  private static int hash(@NotNull final String ¢) {
    return ¢.replaceAll("\\s+", "").toLowerCase().hashCode();
  }

  @NotNull private static int[] hash(@NotNull final String[] ss) {
    @NotNull final int @NonNull [] $ = new int @NonNull [ss.length];
    for (int ¢ = 0; ¢ < $.length; ++¢)
      $[¢] = hash(ss[¢]);
    return $;
  }

  @NotNull private final int[] A_s;
  @NotNull private final int[] B_s;
  @NotNull private final int[][] length;

  /** Instantiates this class.
   * @param as JD
   * @param bs JD */
  public LCS(@NotNull final int[] as, @NotNull final int[] bs) {
    A_s = as;
    B_s = bs;
    length = new int @NonNull [as.length][];
    for (int ¢ = 0; ¢ < as.length; ++¢)
      Arrays.fill(length[¢] = new int[bs.length], -1);
  }

  /** Instantiates this class.
   * @param a JD
   * @param b JD */
  public LCS(final String a, final String b) {
    this(Lines.scatter(a), Lines.scatter(b));
  }

  /** TODO:Document this method Instantiates this class.
   * @param as JD
   * @param bs JD */
  public LCS(@NotNull final String[] as, @NotNull final String[] bs) {
    this(hash(as), hash(bs));
  }

  private int compute(final int i, final int j) {
    return i < 0 || j < 0 ? 0 : threeWayDynamicProgramingStep(i, j);
  }

  private int length() {
    return A_s.length <= 0 || B_s.length <= 0 ? 0 : length(A_s.length - 1, B_s.length - 1); //
  }

  /** Returns the length of the LCS of two prefixes of the current strings,
   * <code>as[0]...as[i]</code>, and <code>as[0]...as[i]</code>, i (respectively
   * j) must be a valid index of array a (respectively b), or else, the
   * substring of a (respectively b) are empty.
   * @param i JD
   * @param j */
  private int length(final int i, final int j) {
    return i < 0 || j < 0 ? 0 : obtainLength(i, j);
  }

  private int obtainLength(final int i, final int j) {
    return length[i][j] != -1 ? length[i][j] : (length[i][j] = compute(i, j));
  }

  private int threeWayDynamicProgramingStep(final int i, final int j) {
    return max(length(i - 1, j), max(length(i, j - 1), length(i - 1, j - 1) + as.bit(A_s[i] == B_s[j])));
  }

  @SuppressWarnings({ "static-method", "synthetic-access" }) //
  public static class TEST {
    /** Dumb implementation, yeah, I know. --yg. */
    private static String[] chars2Lines(@NotNull final String s) {
      @NotNull final StringBuilder $ = new StringBuilder();
      for (final char ¢ : s.toCharArray())
        $.append(¢).append('\n');
      return Lines.scatter(as.string($));
    }

    @Test public void chars2LinesSanity() {
      azzert.that(chars2Lines("").length, is(0));
    }

    @Test public void chars2LinesTypical() {
      azzert.that(chars2Lines("ABC").length, is(3));
    }

    @Test public void length1StrgumentIsZero() {
      azzert.that(length(new int @NonNull [0], new int @NonNull [10]), is(0));
    }

    @Test public void length2ndArgumentIsZero() {
      azzert.that(length(new int @NonNull [10], new int @NonNull [0]), is(0));
    }

    @Test public void lengthArrayLengthOneDifferent() {
      azzert.that(length(new int @NonNull [] { 12 }, new int @NonNull [] { 13 }), is(0));
    }

    @Test public void lengthArrayLengthOneIdentical() {
      azzert.that(length(new int @NonNull [] { 12 }, new int @NonNull [] { 12 }), is(1));
    }

    @Test public void lengthExists() {
      length(new int @NonNull [0], new int @NonNull [0]);
    }

    @Test public void lengthIdenticalIntegers() {
      @NotNull final int @NonNull [] is = new int @NonNull [] { 12, 13, 14, 8, 11, 60, 30 };
      azzert.that(length(is, is), is(is.length));
    }

    @Test public void lengthStringAbraCadabra() {
      // Common string is: "ABRA"
      azzert.that(length( //
          chars2Lines("ABRA"), //
          chars2Lines("CADABRA") //
      ), is(4));
    }

    @Test public void lengthStringAlmostTrivial() {
      // Common string is: "ABRA"
      azzert.that(length( //
          "A\nB\nR\nA", //
          "C\nA\nD\nA\nB\nR\nA\n" //
      ), is(4));
    }

    @Test public void lengthStringMiddle() {
      // Common string is: "ABC"
      azzert.that(length( //
          chars2Lines("bcde"), //
          chars2Lines("abcdef") //
      ), is(4));
    }

    @Test public void lengthStringPrefix() {
      // Common string is: "ABC"
      azzert.that(length( //
          chars2Lines("abc"), //
          chars2Lines("abcdef") //
      ), is(3));
    }

    @Test public void lengthStringSimple() {
      // Common string is: "A"
      azzert.that(length(//
          chars2Lines("A"), //
          chars2Lines("A") //
      ), is(1));
    }

    @Test public void lengthStringSimpleA() {
      // Common string is: "A"
      @NotNull final LCS lcs = new LCS(chars2Lines("A"), chars2Lines("A"));
      assert lcs != null;
      azzert.that(lcs.A_s.length, is(1));
      azzert.that(lcs.B_s.length, is(1));
      azzert.that(lcs.length(), is(1));
    }

    @Test public void lengthStringSimpleB() {
      // Common string is: "A"
      @NotNull final LCS lcs = new LCS(chars2Lines("A"), chars2Lines("A"));
      azzert.that(lcs.length(lcs.A_s.length - 1, lcs.B_s.length - 1), is(1));
    }

    @Test public void lengthStringSimpleC() {
      // Common string is: "A"
      azzert.that(new LCS(chars2Lines("A"), chars2Lines("A")).obtainLength(0, 0), is(1));
    }

    @Test public void lengthStringSimpleD() {
      // Common string is: "A"
      azzert.that(new LCS(chars2Lines("A"), chars2Lines("A")).compute(0, 0), is(1));
    }

    @Test public void lengthStringSimpleE() {
      // Common string is: "A"
      azzert.that(new LCS(chars2Lines("A"), chars2Lines("A")).compute(0, 0), is(1));
    }

    @Test public void lengthStringSimpleF() {
      // Common string is: "A"
      azzert.that(new LCS(chars2Lines("A"), chars2Lines("A")).threeWayDynamicProgramingStep(0, 0), is(1));
    }

    @Test public void lengthStringSimpleFalse() {
      // Common string is: "ABC"
      azzert.that(length(//
          chars2Lines("A"), //
          chars2Lines("A") //
      ), is(1));
    }

    @Test public void lengthStringSuffix() {
      // Common string is: "ABC"
      azzert.that(length(//
          chars2Lines("ABCD"), //
          chars2Lines("CD") //
      ), is(2));
    }

    @Test public void lengthStringTrivial() {
      azzert.that(length("A", "C\nA\nD\nA\nB\nR\nA\n"), is(1));
    }

    @Test public void lengthStringTypical() {
      // Common string is: "ABC"
      azzert.that(length(//
          chars2Lines(".A.B...C..."), //
          chars2Lines(",,,,A,,,,,B,,,,,,,,,C,,,") //
      ), is(3));
    }

    @Test public void lengthStringTypical_1() {
      // Common string is: "ABC"
      azzert.that(length(//
          chars2Lines(".A.B...C"), //
          chars2Lines(",,,,A,,,,,B,,,,,,,,,C,,,") //
      ), is(3));
    }

    @Test public void lengthStringTypical_2() {
      // Common string is: "ABC"
      azzert.that(length(//
          chars2Lines(".A.B...C"), //
          chars2Lines(",,,,A,,,,,B,,,C,,,") //
      ), is(3));
    }

    @Test public void lengthStringTypical_3() {
      // Common string is: "ABC"
      azzert.that(length(//
          chars2Lines(".A.B...C"), //
          chars2Lines(",A,,,,,B,,,C,,,") //
      ), is(3));
    }

    @Test public void lengthStringTypical_4() {
      // Common string is: "ABC"
      azzert.that(length(//
          chars2Lines(".A.B.C"), //
          chars2Lines(",A,,,,,B,,,C,,,") //
      ), is(3));
    }

    @Test public void lengthStringTypical_5() {
      // Common string is: "ABC"
      azzert.that(length(//
          chars2Lines(".A.B.C"), //
          chars2Lines(",A,,,B,,C,") //
      ), is(3));
    }

    @Test public void lengthStringTypical_6() {
      // Common string is: "ABC"
      azzert.that(length(//
          chars2Lines(".A.B.C"), //
          chars2Lines(",A,B,,C,") //
      ), is(3));
    }

    @Test public void lengthStringTypical_6A() {
      // Common string is: "ABC"
      azzert.that(length(//
          chars2Lines(".A."), //
          chars2Lines(",A,") //
      ), is(1));
    }

    @Test public void lengthStringTypical_6B() {
      // Common string is: "ABC"
      azzert.that(length(//
          chars2Lines(".A"), //
          chars2Lines(",A") //
      ), is(1));
    }

    @Test public void lengthStringTypical_6C() {
      // Common string is empty
      azzert.that(length(//
          chars2Lines("."), //
          chars2Lines(",") //
      ), is(0));
    }

    @Test public void lengthStringTypical_6D() {
      // Common string is empty
      azzert.that(length(//
          chars2Lines("X"), //
          chars2Lines("Y") //
      ), is(0));
    }

    @Test public void lengthStringTypical_6E() {
      // Common string is: "X"
      azzert.that(length(//
          chars2Lines("X"), //
          chars2Lines("X") //
      ), is(1));
    }

    @Test public void lengthStringTypical_7() {
      // Common string is: "ABC"
      azzert.that(length(//
          chars2Lines("A.B.C"), //
          chars2Lines(",A,B,,C,") //
      ), is(3));
    }

    @Test public void lengthStringTypical_7A() {
      // Common string is: "ABC"
      azzert.that(length(//
          chars2Lines("A.B.C"), //
          chars2Lines(",A,B,,C,,,,") //
      ), is(3));
    }

    @Test public void lengthStringTypical_7B() {
      // Common string is: "ABC"
      azzert.that(length(//
          chars2Lines("A.B.C"), //
          chars2Lines(",,,,,A,B,,C,,,,,") //
      ), is(3));
    }

    @Test public void lengthStringTypical_7C() {
      // Common string is: "ABC"
      azzert.that(length(//
          chars2Lines(".A.B.C"), //
          chars2Lines(",A,B,,C,") //
      ), is(3));
    }

    @Test public void lengthStringTypical_8() {
      // Common string is: "ABC"
      azzert.that(length(//
          chars2Lines("A.B.C"), //
          chars2Lines(",A,BC,") //
      ), is(3));
    }

    @Test public void lengthStringTypical_9() {
      // Common string is: "ABC"
      azzert.that(length(//
          chars2Lines("A.B.C"), //
          chars2Lines("ABC,") //
      ), is(3));
    }

    @Test public void lengthStringTypical_A() {
      // Common string is: "ABC"
      azzert.that(length(//
          chars2Lines("AB.C"), //
          chars2Lines("ABC,") //
      ), is(3));
    }

    @Test public void lengthStringTypical_B() {
      // Common string is: "ABC"
      azzert.that(length(//
          chars2Lines("AB.C"), //
          chars2Lines("ABC") //
      ), is(3));
    }

    @Test public void lengthStringTypical_C() {
      // Common string is: "ABC"
      azzert.that(length(//
          chars2Lines("ABC"), //
          chars2Lines("ABC") //
      ), is(3));
    }

    @Test public void lengthStringTypicalWithDigits() {
      // Common string is: "ABC"
      azzert.that(length(//
          chars2Lines(".A.B...C..."), //
          chars2Lines(",,,,A,,,,,B,,,,,,,,,C,,,") //
      ), is(3));
    }
  }
}
