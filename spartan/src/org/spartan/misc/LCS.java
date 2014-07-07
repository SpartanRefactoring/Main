package org.spartan.misc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.spartan._.max;
import static org.spartan._.nonNull;

import java.util.Arrays;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.spartan.As;
import org.spartan.text.Lines;

/**
 * Utility functions for computing the "Longest Common Subsequence" for two
 * textual files. Similar to the famous "diff" utility, particularly when used
 * with certain options, the LCS is computed after removing all blanks from each
 * line and then converting it to lower case; further, we use the hashCode of
 * the line, so that in certain cases, the LCS may be a bit inaccurate. These
 * case are extremely rare.
 * 
 * @author Yossi Gil
 * @since 2014-06-17
 */
public class LCS {
  public LCS(final String a, final String b) {
	  this(Lines.scatter(a), Lines.scatter(b));
	}

	public LCS(final String[] as, final String[] bs) {
	  this(hash(as), hash(bs));
	}

	public LCS(final int[] as, final int[] bs) {
	  this.as = as;
	  this.bs = bs;
	  length = new int[as.length][];
	  for (int i = 0; i < as.length; ++i)
	    Arrays.fill(length[i] = new int[bs.length], -1);
	}

	public static int length(final int[] ia, final int[] is2) {
	  return new LCS(ia, is2).length();
	}

	public static int length(final String a, final String s2) {
	  return new LCS(a, s2).length();
	}

	public static int length(final String[] ssa, final String[] ssb) {
	  return new LCS(ssa, ssb).length();
	}

	private static int hash(final String s) {
    return s.replaceAll("\\s+", "").toLowerCase().hashCode();
  }

  private static int[] hash(final String[] ss) {
    final int[] $ = new int[ss.length];
    for (int i = 0; i < $.length; ++i)
      $[i] = hash(nonNull(ss[i]));
    return $;
  }

  private final int[] as;
  private final int[] bs;
  private final int[][] length;

  private int compute(final int i, final int j) {
    return i < 0 || j < 0 ? 0 : threeWayDynamicProgramingStep(i, j);
  }

  private int length() {
    return 0 >= as.length || 0 >= bs.length ? 0 : length(as.length - 1, bs.length - 1); //
  }

  /**
   * Returns the length of the LCS of two prefixes of the current strings,
   * <code>as[0]...as[i]</code>, and <code>as[0]...as[i]</code>, i (respectively
   * j) must be a valid index of array a (respectively b), or else, the
   * substring of a (respectively b) are empty.
   * 
   * @param i
   * @param j
   * @return
   */
  private int length(final int i, final int j) {
    return i < 0 || j < 0 ? 0 : obtainLength(i, j);
  }

  private int obtainLength(final int i, final int j) {
    return length[i][j] != -1 ? length[i][j] : (length[i][j] = compute(i, j));
  }

  private int threeWayDynamicProgramingStep(final int i, final int j) {
    return max(length(i - 1, j), length(i, j - 1), length(i - 1, j - 1) + As.bit(as[i] == bs[j]));
  }

	@SuppressWarnings("static-method")//
	@FixMethodOrder(MethodSorters.NAME_ASCENDING)//
	public static class TEST {
	  /**
	   * Dumb implementation, yeah, I know. --yg.
	   * 
	   * @param s
	   * @return
	   */
	  private static String[] chars2Lines(final String s) {
	    final StringBuilder $ = new StringBuilder();
	    for (final char c : s.toCharArray())
	      $.append(c).append('\n');
	    return Lines.scatter(As.string($));
	  }
	  @Test public void chars2LinesTypical() {
	    assertEquals(3, chars2Lines("ABC").length);
	  }
	
	  @Test public void chars2LinesSanity() {
	    assertEquals(0, chars2Lines("").length);
	  }
	
	  @Test public void length1StrgumentIsZero() {
	    assertEquals(0, length(new int[0], new int[10]));
	  }
	
	  @Test public void length2ndArgumentIsZero() {
	    assertEquals(0, length(new int[10], new int[0]));
	  }
	
	  @Test public void lengthArrayLengthOneDifferent() {
	    assertEquals(0, length(new int[] { 12 }, new int[] { 13 }));
	  }
	
	  @Test public void lengthArrayLengthOneIdentical() {
	    assertEquals(1, length(new int[] { 12 }, new int[] { 12 }));
	  }
	
	  @Test public void lengthExists() {
	    length(new int[0], new int[0]);
	  }
	
	  @Test public void lengthIdenticalIntegers() {
	    final int[] is = new int[] { 12, 13, 14, 8, 11, 60, 30 };
	    assertEquals(is.length, length(is, is));
	  }
	
	  @Test public void lengthStringAbraCadabra() {
	    // Common string is: "ABRA"
	    assertEquals(4, length( //
	        chars2Lines("ABRA"), //
	        chars2Lines("CADABRA") //
	        ));
	  }
	
	  @Test public void lengthStringAlmostTrivial() {
	    // Common string is: "ABRA"
	    assertEquals(4, length( //
	        "A\nB\nR\nA", //
	        "C\nA\nD\nA\nB\nR\nA\n" //
	    ));
	  }
	
	  @Test public void lengthStringMiddle() {
	    // Common string is: "ABC"
	    assertEquals(4, length( //
	        chars2Lines("bcde"), //
	        chars2Lines("abcdef") //
	        ));
	  }
	
	  @Test public void lengthStringPrefix() {
	    // Common string is: "ABC"
	    assertEquals(3, length( //
	        chars2Lines("abc"), //
	        chars2Lines("abcdef") //
	        ));
	  }
	
	  @Test public void lengthStringSimple() {
	    // Common string is: "A"
	    assertEquals(1, length(//
	        chars2Lines("A"), //
	        chars2Lines("A") //
	        ));
	  }
	
	  @SuppressWarnings("synthetic-access")//
	  @Test public void lengthStringSimpleA() {
	    // Common string is: "A"
	    final LCS lcs = new LCS(chars2Lines("A"), chars2Lines("A"));
	    assertNotNull(lcs);
	    assertEquals(1, lcs.as.length);
	    assertEquals(1, lcs.bs.length);
	    assertEquals(1, lcs.length());
	  }
	
	  @SuppressWarnings("synthetic-access")//
	  @Test public void lengthStringSimpleB() {
	    // Common string is: "A"
	    final LCS lcs = new LCS(chars2Lines("A"), chars2Lines("A"));
	    assertEquals(1, lcs.length(lcs.as.length - 1, lcs.bs.length - 1));
	  }
	
	  @SuppressWarnings("synthetic-access")//
	  @Test public void lengthStringSimpleC() {
	    // Common string is: "A"
	    assertEquals(1, new LCS(chars2Lines("A"), chars2Lines("A")).obtainLength(0, 0));
	  }
	
	  @SuppressWarnings("synthetic-access")//
	  @Test public void lengthStringSimpleD() {
	    // Common string is: "A"
	    assertEquals(1, new LCS(chars2Lines("A"), chars2Lines("A")).compute(0, 0));
	  }
	
	  @SuppressWarnings("synthetic-access")//
	  @Test public void lengthStringSimpleE() {
	    // Common string is: "A"
	    assertEquals(1, new LCS(chars2Lines("A"), chars2Lines("A")).compute(0, 0));
	  }
	
	  @SuppressWarnings("synthetic-access")//
	  @Test public void lengthStringSimpleF() {
	    // Common string is: "A"
	    assertEquals(1, new LCS(chars2Lines("A"), chars2Lines("A")).threeWayDynamicProgramingStep(0, 0));
	  }
	
	  @Test public void lengthStringSimpleFalse() {
	    // Common string is: "ABC"
	    assertEquals(1, length(//
	        chars2Lines("A"), //
	        chars2Lines("A") //
	        ));
	  }
	
	  @Test public void lengthStringSuffix() {
	    // Common string is: "ABC"
	    assertEquals(2, length(//
	        chars2Lines("ABCD"), //
	        chars2Lines("CD") //
	        ));
	  }
	
	  @Test public void lengthStringTrivial() {
	    assertEquals(1, length("A", "C\nA\nD\nA\nB\nR\nA\n"));
	  }
	
	  @Test public void lengthStringTypical() {
	    // Common string is: "ABC"
	    assertEquals(3, length(//
	        chars2Lines(".A.B...C..."), //
	        chars2Lines(",,,,A,,,,,B,,,,,,,,,C,,,") //
	        ));
	  }
	
	  @Test public void lengthStringTypical_1() {
	    // Common string is: "ABC"
	    assertEquals(3, length(//
	        chars2Lines(".A.B...C"), //
	        chars2Lines(",,,,A,,,,,B,,,,,,,,,C,,,") //
	        ));
	  }
	
	  @Test public void lengthStringTypical_2() {
	    // Common string is: "ABC"
	    assertEquals(3, length(//
	        chars2Lines(".A.B...C"), //
	        chars2Lines(",,,,A,,,,,B,,,C,,,") //
	        ));
	  }
	
	  @Test public void lengthStringTypical_3() {
	    // Common string is: "ABC"
	    assertEquals(3, length(//
	        chars2Lines(".A.B...C"), //
	        chars2Lines(",A,,,,,B,,,C,,,") //
	        ));
	  }
	
	  @Test public void lengthStringTypical_4() {
	    // Common string is: "ABC"
	    assertEquals(3, length(//
	        chars2Lines(".A.B.C"), //
	        chars2Lines(",A,,,,,B,,,C,,,") //
	        ));
	  }
	
	  @Test public void lengthStringTypical_5() {
	    // Common string is: "ABC"
	    assertEquals(3, length(//
	        chars2Lines(".A.B.C"), //
	        chars2Lines(",A,,,B,,C,") //
	        ));
	  }
	
	  @Test public void lengthStringTypical_6() {
	    // Common string is: "ABC"
	    assertEquals(3, length(//
	        chars2Lines(".A.B.C"), //
	        chars2Lines(",A,B,,C,") //
	        ));
	  }
	
	  @Test public void lengthStringTypical_6A() {
	    // Common string is: "ABC"
	    assertEquals(1, length(//
	        chars2Lines(".A."), //
	        chars2Lines(",A,") //
	        ));
	  }
	
	  @Test public void lengthStringTypical_6B() {
	    // Common string is: "ABC"
	    assertEquals(1, length(//
	        chars2Lines(".A"), //
	        chars2Lines(",A") //
	        ));
	  }
	
	  @Test public void lengthStringTypical_6C() {
	    // Common string is empty
	    assertEquals(0, length(//
	        chars2Lines("."), //
	        chars2Lines(",") //
	        ));
	  }
	
	  @Test public void lengthStringTypical_6D() {
	    // Common string is empty
	    assertEquals(0, length(//
	        chars2Lines("X"), //
	        chars2Lines("Y") //
	        ));
	  }
	
	  @Test public void lengthStringTypical_6E() {
	    // Common string is: "X"
	    assertEquals(1, length(//
	        chars2Lines("X"), //
	        chars2Lines("X") //
	        ));
	  }
	
	  @Test public void lengthStringTypical_7() {
	    // Common string is: "ABC"
	    assertEquals(3, length(//
	        chars2Lines("A.B.C"), //
	        chars2Lines(",A,B,,C,") //
	        ));
	  }
	
	  @Test public void lengthStringTypical_7A() {
	    // Common string is: "ABC"
	    assertEquals(3, length(//
	        chars2Lines("A.B.C"), //
	        chars2Lines(",A,B,,C,,,,") //
	        ));
	  }
	
	  @Test public void lengthStringTypical_7B() {
	    // Common string is: "ABC"
	    assertEquals(3, length(//
	        chars2Lines("A.B.C"), //
	        chars2Lines(",,,,,A,B,,C,,,,,") //
	        ));
	  }
	
	  @Test public void lengthStringTypical_7C() {
	    // Common string is: "ABC"
	    assertEquals(3, length(//
	        chars2Lines(".A.B.C"), //
	        chars2Lines(",A,B,,C,") //
	        ));
	  }
	
	  @Test public void lengthStringTypical_8() {
	    // Common string is: "ABC"
	    assertEquals(3, length(//
	        chars2Lines("A.B.C"), //
	        chars2Lines(",A,BC,") //
	        ));
	  }
	
	  @Test public void lengthStringTypical_9() {
	    // Common string is: "ABC"
	    assertEquals(3, length(//
	        chars2Lines("A.B.C"), //
	        chars2Lines("ABC,") //
	        ));
	  }
	
	  @Test public void lengthStringTypical_A() {
	    // Common string is: "ABC"
	    assertEquals(3, length(//
	        chars2Lines("AB.C"), //
	        chars2Lines("ABC,") //
	        ));
	  }
	
	  @Test public void lengthStringTypical_B() {
	    // Common string is: "ABC"
	    assertEquals(3, length(//
	        chars2Lines("AB.C"), //
	        chars2Lines("ABC") //
	        ));
	  }
	
	  @Test public void lengthStringTypical_C() {
	    // Common string is: "ABC"
	    assertEquals(3, length(//
	        chars2Lines("ABC"), //
	        chars2Lines("ABC") //
	        ));
	  }
	
	  @Test public void lengthStringTypicalWithDigits() {
	    // Common string is: "ABC"
	    assertEquals(3, length(//
	        chars2Lines(".A.B...C..."), //
	        chars2Lines(",,,,A,,,,,B,,,,,,,,,C,,,") //
	        ));
	  }
	}
}
