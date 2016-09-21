package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.wrings.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;

/** * Unit tests for the nesting class Unit test for the containing class. Note
 * our naming convention: a) test methods do not use the redundant "test"
 * prefix. b) test methods begin with the name of the method they check.
 * @author Yossi Gil
 * @since 2014-07-10 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public final class Version250Test {
  // can be String concatenating, so can't remove 0
  @Test public void additionZeroTest_a() {
    trimmingOf("b = a + 0;").stays();
  }

  // can be String concatenating, so can't remove 0
  @Test public void additionZeroTest_b() {
    trimmingOf("b=0+a;").stays();
  }

  @SuppressWarnings("unused") @Test public void issue_177_BitWiseAnd_withSideEffectsEXT() {
    class Class {
      Inner in = new Inner(0);

      Class() {
        final int x = in.f(1) & 1;
        azzert.that(x, is(0));
        azzert.that(in.a, is(1));
      }

      class Inner {
        int a;

        Inner(final int i) {
          a = i;
        }

        int f(final int $) {
          azzert.that($, is(1));
          return g();
        }

        int g() {
          class C {
            C() {
              h();
              ++a;
            }

            int h() {
              return 2;
            }
          }
          return new C().h();
        }
      }
    }
    new Class();
    trimmingOf("a=a & b").gives("a&=b");
  }

  @Test public void issue_177_bitWiseOr_noSideEffects() {
    int a = 1;
    final int b = 2;
    a |= b;
    azzert.that(a, is(3));
    trimmingOf("a=a|b").gives("a|=b");
  }

  @SuppressWarnings("unused") @Test public void issue_177_bitWiseOr_withSideEffects() {
    class Class {
      Class() {
        azzert.that(f(1) | 1, is(3));
      }

      int f(final int $) {
        azzert.that($, is(1));
        return $ + 1;
      }
    }
    new Class();
    trimmingOf("a=a|b").gives("a|=b");
  }

  @SuppressWarnings("unused") @Test public void issue_177_BitWiseOr_withSideEffectsEXT() {
    class Class {
      Inner in = new Inner(0);

      Class() {
        final int x = in.f(1) | 1;
        azzert.that(x, is(3));
        azzert.that(in.a, is(1));
      }

      class Inner {
        int a;

        Inner(final int i) {
          a = i;
        }

        int f(final int $) {
          azzert.that($, is(1));
          return g();
        }

        int g() {
          class C {
            C() {
              h();
              ++a;
            }

            int h() {
              return 2;
            }
          }
          return new C().h();
        }
      }
    }
    new Class();
    trimmingOf("a=a | b").gives("a|=b");
  }

  @SuppressWarnings("unused") @Test public void issue_177_BitWiseXor_withSideEffectsEXT() {
    class Class {
      Inner in = new Inner(0);

      Class() {
        final int x = in.f(1) ^ 1;
        azzert.that(x, is(3));
        azzert.that(in.a, is(1));
      }

      class Inner {
        int a;

        Inner(final int i) {
          a = i;
        }

        int f(final int $) {
          azzert.that($, is(1));
          return g();
        }

        int g() {
          class C {
            C() {
              h();
              ++a;
            }

            int h() {
              return 2;
            }
          }
          return new C().h();
        }
      }
    }
    new Class();
    trimmingOf("a = a ^ b ").gives("a ^= b");
  }

  @Test public void issue_177_logicalAnd_noSideEffects() {
    boolean a = true;
    final boolean b = false;
    a &= b;
    azzert.nay(a);
    trimmingOf("a=a && b").gives("a&=b");
  }

  @SuppressWarnings("unused") @Test public void issue_177_logicalAnd_withSideEffects() {
    class Class {
      int a;

      Class() {
        a = 0;
        final boolean x = f(true) & true;
        azzert.nay(x);
        azzert.that(a, is(1));
      }

      boolean f(final boolean $) {
        azzert.aye($);
        ++a;
        return false;
      }
    }
    new Class();
    trimmingOf("a=a && b").gives("a&=b");
  }

  @SuppressWarnings("unused") @Test public void issue_177_logicalAnd_withSideEffectsEX() {
    class Class {
      Inner in = new Inner(0);

      Class() {
        final boolean x = in.f(true) & true;
        azzert.nay(x);
        azzert.aye(in.a == 1);
      }

      class Inner {
        int a;

        Inner(final int i) {
          a = i;
        }

        boolean f(final boolean $) {
          azzert.aye($);
          ++a;
          return false;
        }
      }
    }
    new Class();
    trimmingOf("a=a && b").gives("a&=b");
  }

  @SuppressWarnings("unused") @Test public void issue_177_logicalAnd_withSideEffectsEXT() {
    class Class {
      Inner in = new Inner(0);

      Class() {
        final boolean x = in.f(true) & true;
        azzert.nay(x);
        azzert.that(in.a, is(1));
      }

      class Inner {
        int a;

        Inner(final int i) {
          a = i;
        }

        boolean f(final boolean $) {
          azzert.aye($);
          return g();
        }

        boolean g() {
          class C {
            C() {
              h();
              ++a;
            }

            boolean h() {
              return false;
            }
          }
          return new C().h();
        }
      }
    }
    new Class();
    trimmingOf("a=a && b").gives("a&=b");
  }

  @Test public void issue_177_logicalOr_noSideEffects() {
    boolean a = false;
    final boolean b = true;
    a |= b;
    azzert.aye(a);
    trimmingOf("a=a||b").gives("a|=b");
  }

  @SuppressWarnings("unused") @Test public void issue_177_logicalOr_withSideEffects() {
    class Class {
      int a;

      Class() {
        a = 0;
        final boolean x = f(false) | false;
        azzert.aye(x);
        azzert.that(a, is(1));
      }

      boolean f(final boolean $) {
        azzert.nay($);
        ++a;
        return true;
      }
    }
    new Class();
    trimmingOf("a=a||b").gives("a|=b");
  }

  @SuppressWarnings("unused") @Test public void issue_177_logicalOr_withSideEffectsEX() {
    class Class {
      Inner in = new Inner(0);

      Class() {
        final boolean x = in.f(false) | false;
        azzert.aye(x);
        azzert.that(in.a, is(1));
      }

      class Inner {
        int a;

        Inner(final int i) {
          a = i;
        }

        boolean f(final boolean $) {
          azzert.nay($);
          ++a;
          return true;
        }
      }
    }
    new Class();
    trimmingOf("a=a||b").gives("a|=b");
  }

  @SuppressWarnings("unused") @Test public void issue_177_LogicalOr_withSideEffectsEXT() {
    class Class {
      Inner in = new Inner(0);

      Class() {
        final int x = in.f(1) | 1;
        azzert.that(x, is(3));
        azzert.that(in.a, is(1));
      }

      class Inner {
        int a;

        Inner(final int i) {
          a = i;
        }

        int f(final int $) {
          azzert.that($, is(1));
          return g();
        }

        int g() {
          class C {
            C() {
              h();
              ++a;
            }

            int h() {
              return 2;
            }
          }
          return new C().h();
        }
      }
    }
    new Class();
    trimmingOf("a=a|(b=b&a)").gives("a|=b=b&a").gives("a|=b&=a");
  }

  @Test public void issue103_AND1() {
    trimmingOf("a=a&5;").gives("a&=5;");
  }

  @Test public void issue103_AND2() {
    trimmingOf("a=5&a;").gives("a&=5;");
  }

  @Test public void issue103_div1() {
    trimmingOf("a=a/5;").gives("a/=5;");
  }

  @Test public void issue103_div2() {
    trimmingOf("a=5/a;").stays();
  }

  @Test public void issue103_leftShift1() {
    trimmingOf("a=a<<5;").gives("a<<=5;");
  }

  @Test public void issue103_leftShift2() {
    trimmingOf("a=5<<a;").stays();
  }

  @Test public void issue103_modulo1() {
    trimmingOf("a=a%5;").gives("a%=5;");
  }

  @Test public void issue103_modulo2() {
    trimmingOf("a=5%a;").stays();
  }

  @Test public void issue103_OR1() {
    trimmingOf("a=a|5;").gives("a|=5;");
  }

  @Test public void issue103_OR2() {
    trimmingOf("a=5|a;").gives("a|=5;");
  }

  @Test public void issue103_rightShift1() {
    trimmingOf("a=a>>5;").gives("a>>=5;");
  }

  @Test public void issue103_rightShift2() {
    trimmingOf("a=5>>a;").stays();
  }

  @Test public void issue103_XOR1() {
    trimmingOf("x = x ^ a.getNum()").gives("x ^= a.getNum()");
  }

  @Test public void issue103_XOR2() {
    trimmingOf("j = j ^ k").gives("j ^= k");
  }

  @Test public void issue103a() {
    trimmingOf("x=x+y").gives("x+=y");
  }

  @Test public void issue103b() {
    trimmingOf("x=y+x").stays();
  }

  @Test public void issue103c() {
    trimmingOf("x=y+z").stays();
  }

  public void issue103d() {
    trimmingOf("x = x + x").gives("x+=x");
  }

  public void issue103e() {
    trimmingOf("x = y + x + z + x + k + 9").gives("x += y + z + x + k + 9");
  }

  @Test public void issue103f() {
    trimmingOf("a=a+5").gives("a+=5");
  }

  @Test public void issue103g() {
    trimmingOf("a=a+(alex)").gives("a+=alex");
  }

  @Test public void issue103h() {
    trimmingOf("a = a + (c = c + kif)").gives("a += c = c + kif").gives("a += c += kif").stays();
  }

  @Test public void issue103i_mixed_associative() {
    trimmingOf("a = x = x + (y = y*(z=z+3))").gives("a = x += y=y*(z=z+3)").gives("a = x += y *= z=z+3").gives("a = x += y *= z+=3");
  }

  @Test public void issue103j() {
    trimmingOf("x=x+foo(x,y)").gives("x+=foo(x,y)");
  }

  @Test public void issue103k() {
    trimmingOf("z=foo(x=(y=y+u),17)").gives("z=foo(x=(y+=u),17)");
  }

  @Test public void issue103l_mixed_associative() {
    trimmingOf("a = a - (x = x + (y = y*(z=z+3)))").gives("a-=x=x+(y=y*(z=z+3))").gives("a-=x+=y=y*(z=z+3)");
  }

  @Test public void issue103mma() {
    trimmingOf("x=x*y").gives("x*=y");
  }

  // Not provably-not-string.
  @Test public void issue107a() {
    trimmingOf("a+=1;").stays();
  }

  @Test public void issue107b() {
    trimmingOf("for(int c = 0; c < 5; c-=1)\n" + "c*=2;").gives("for(int c = 0; c < 5; c--)" + "c*=2;");
  }

  @Test public void issue107c() {
    trimmingOf("java_is_even_nice+=1+=1;").gives("java_is_even_nice+=1++;");
  }

  @Test public void issue107d() {
    trimmingOf("for(int a ; a<10 ; (--a)+=1){}").gives("for(int a ; a<10 ; (--a)++){}");
  }

  @Test public void issue107e() {
    trimmingOf("for(String a ; a.length()<3 ; (a = \"\")+=1){}").stays();
  }

  @Test public void issue107f() {
    trimmingOf("a+=2;").stays();
  }

  @Test public void issue107g() {
    trimmingOf("a/=1;").stays();
  }

  public void issue107h() {
    trimmingOf("a-+=1;").gives("a-++;");
  }

  @Test public void issue107i() {
    trimmingOf("a-=1;").gives("a--;").gives("--a;").stays();
  }

  @Test public void issue107j() {
    trimmingOf("for(int a ; a<10 ; a-=1){}").gives("for(int a ; a<10 ; a--){}");
  }

  @Test public void issue107k() {
    trimmingOf("a-=2;").stays();
  }

  @Test public void issue107l() {
    trimmingOf("while(x-=1){}").gives("while(x--){}");
  }

  @Test public void issue107m() {
    trimmingOf("s = \"hello\"; \n" + "s += 1;").stays();
  }

  @Test public void issue107n() {
    trimmingOf("for(;; (a = 3)+=1){}").gives("for(;; (a = 3)++){}");
  }

  @Test public void issue107o() {
    trimmingOf("for(int a ; a<3 ; a+=1){}").stays();
  }

  @Test public void issue108a() {
    trimmingOf("x=x*y").gives("x*=y");
  }

  @Test public void issue108b() {
    trimmingOf("x=y*x").gives("x*=y");
  }

  @Test public void issue108c() {
    trimmingOf("x=y*z").stays();
  }

  @Test public void issue108d() {
    trimmingOf("x = x * x").gives("x*=x");
  }

  @Test public void issue108e() {
    trimmingOf("x = y * z * x * k * 9").gives("x *= y * z * k * 9");
  }

  @Test public void issue108f() {
    trimmingOf("a = y * z * a").gives("a *= y * z");
  }

  @Test public void issue108g() {
    trimmingOf("a=a*5").gives("a*=5");
  }

  @Test public void issue108h() {
    trimmingOf("a=a*(alex)").gives("a*=alex");
  }

  @Test public void issue108i() {
    trimmingOf("a = a * (c = c * kif)").gives("a *= c = c*kif").gives("a *= c *= kif").stays();
  }

  @Test public void issue108j() {
    trimmingOf("x=x*foo(x,y)").gives("x*=foo(x,y)");
  }

  @Test public void issue108k() {
    trimmingOf("z=foo(x=(y=y*u),17)").gives("z=foo(x=(y*=u),17)");
  }

  @Test public void issue141_01() {
    trimmingOf("public static void go(final Object os[], final String... ss) {  \n"//
        + "for (final String saa : ss) \n"//
        + "out(saa);  \n" + "out(\"elements\", os);   \n"//
        + "}").stays();
  }

  @Test public void issue141_02() {
    trimmingOf("public static void go(final List<Object> os, final String... ss) {  \n"//
        + "for (final String saa : ss) \n"//
        + "out(saa);  \n" + "out(\"elements\", os);   \n"//
        + "}").stays();
  }

  @Test public void issue141_03() {
    trimmingOf("public static void go(final String ss[],String abracadabra) {  \n" + "for (final String a : ss) \n" + "out(a);  \n"
        + "out(\"elements\",abracadabra);   \n" + "}").stays();
  }

  @Test public void issue141_04() {
    trimmingOf("public static void go(final String ss[]) {  \n" + "for (final String a : ss) \n" + "out(a);  \n" + "out(\"elements\");   \n" + "}")
        .stays();
  }

  @Test public void issue141_05() {
    trimmingOf("public static void go(final String s[]) {  \n" + "for (final String a : s) \n" + "out(a);  \n" + "out(\"elements\");   \n" + "}")
        .gives("public static void go(final String ss[]) {  \n" + "for (final String a : ss) \n" + "out(a);  \n" + "out(\"elements\");   \n" + "}")
        .stays();
  }

  @Test public void issue141_06() {
    trimmingOf("public static void go(final String s[][][]) {  \n" + "for (final String a : s) \n" + "out(a);  \n" + "out(\"elements\");   \n" + "}")
        .gives("public static void go(final String ssss[][][]) {  \n" + "for (final String a : ssss) \n" + "out(a);  \n" + "out(\"elements\");   \n"
            + "}")
        .stays();
  }

  @Test public void issue141_07() {
    trimmingOf("public static void go(final Stringssssss ssss[]) {  \n" + "for (final Stringssssss a : ssss) \n" + "out(a);  \n"
        + "out(\"elements\");   \n" + "}")
            .gives("public static void go(final Stringssssss ss[]) {  \n" + "for (final Stringssssss a : ss) \n" + "out(a);  \n"
                + "out(\"elements\");   \n" + "}")
            .stays();
  }

  @Test public void issue141_08() {
    trimmingOf(
        "public static void go(final Integer ger[]) {  \n" + "for (final Integer a : ger) \n" + "out(a);  \n" + "out(\"elements\");   \n" + "}")
            .gives(
                "public static void go(final Integer is[]) {  \n" + "for (final Integer a : is) \n" + "out(a);  \n" + "out(\"elements\");   \n" + "}")
            .stays();
  }

  @Test public void issue207() {
    trimmingOf("size() == 0").stays();
  }

  @Test public void issue218() {
    trimmingOf("(long)(long)2")//
        .gives("1L*(long)2")//
        .gives("1L*1L*2")//
        .gives("2L")//
        .stays();
  }

  @Test public void issue218a() {
    trimmingOf("(long)(long)2").gives("1L*(long)2").gives("1L*1L*2").gives("2L").stays();
  }

  @Test public void issue218x() {
    trimmingOf("(long)1L*2").gives("2*(long)1L").gives("2*1L*1L").gives("2L").stays();
  }

  @Test public void issue237() {
    trimmingOf("class X {final int __ = 0;}").stays();
    trimmingOf("class X {final boolean __ = false;}").stays();
    trimmingOf("class X {final double __ = 0.0;}").stays();
    trimmingOf("class X {final Object __ = null;}").stays();
  }

  @Test public void issue31a() {
    trimmingOf(" static boolean hasAnnotation(final VariableDeclarationStatement n, int abcd) {\n" + //
        "      return hasAnnotation(now.modifiers());\n" + //
        "    }").gives(" static boolean hasAnnotation(final VariableDeclarationStatement s, int abcd) {\n" + //
            "      return hasAnnotation(now.modifiers());\n" + //
            "    }");
  }

  @Test public void issue31b() {
    trimmingOf(" void f(final VariableDeclarationStatement n, int abc) {}") //
        .gives("void f(final VariableDeclarationStatement s, int abc) {}");
  }

  @Test public void issue31c() {
    trimmingOf(" void f(final VariableDeclarationAtatement n, int abc) {}") //
        .gives("void f(final VariableDeclarationAtatement a, int abc) {}");
  }

  @Test public void issue31d() {
    trimmingOf(" void f(final Expression n) {}") //
        .gives("void f(final Expression x) {}");
  }

  @Test public void issue31e() {
    trimmingOf(" void f(final Exception n) {}") //
        .gives("void f(final Exception x) {}");
  }

  @Test public void issue31f() {
    trimmingOf(" void f(final Exception exception, Expression expression) {}") //
        .gives("void f(final Exception x, Expression expression) {}");
  }

  @Test public void issue31g() {
    trimmingOf("void foo(TestExpression exp,TestAssignment testAssignment)") //
        .gives("void foo(TestExpression x,TestAssignment testAssignment)").gives("void foo(TestExpression x,TestAssignment a)");
  }

  @Test public void issue31h() {
    trimmingOf(" void f(final Exception n) {}") //
        .gives("void f(final Exception x) {}");
  }

  @Test public void issue31i() {
    trimmingOf(" void f(final Exception n) {}") //
        .gives("void f(final Exception x) {}");
  }

  @Test public void issue31j() {
    trimmingOf("void foo(Exception exception, Assignment assignment)").gives("void foo(Exception x, Assignment assignment)")
        .gives("void foo(Exception x, Assignment a)").stays();
  }

  @Test public void issue31k() {
    trimmingOf("String tellTale(Example example)").gives("String tellTale(Example x)");
  }

  @Test public void issue31l() {
    trimmingOf("String tellTale(Example examp)").gives("String tellTale(Example x)");
  }

  @Test public void issue31m() {
    trimmingOf("String tellTale(ExamplyExamplar lyEx)").gives("String tellTale(ExamplyExamplar x)");
  }

  @Test public void issue31n() {
    trimmingOf("String tellTale(ExamplyExamplar foo)").stays();
  }

  @Test public void issue70_01() {
    trimmingOf("(double)5").gives("1.*5");
  }

  @Test public void issue70_02() {
    trimmingOf("(double)4").gives("1.*4");
  }

  @Test public void issue70_03() {
    trimmingOf("(double)1.2").gives("1.*1.2");
  }

  @Test public void issue70_04() {
    trimmingOf("(double)'a'").gives("1.*'a'");
  }

  @Test public void issue70_05() {
    trimmingOf("(double)A").gives("1.*A");
  }

  @Test public void issue70_06() {
    trimmingOf("(double)a.b").gives("1.*a.b");
  }

  @Test public void issue70_07() {
    trimmingOf("(double)(double)5").gives("1.*(double)5").gives("1.*1.*5");
  }

  @Test public void issue70_08() {
    trimmingOf("(double)((double)5)").gives("1.*(double)5").gives("1.*1.*5");
  }

  @Test public void issue70_09() {
    trimmingOf("(double) 2. * (double)5")//
        .gives("(double)5 * (double)2.") //
        .gives("1. * 5  * 1. * 2.")//
        .gives("10.0");
  }

  @Test public void issue70_10() {
    trimmingOf("(double)5 - (double)3").gives("1.*5-1.*3");
  }

  @Test public void issue70_11() {
    trimmingOf("(double)f + (int)g").gives("(int)g+(double)f").gives("(int)g + 1.*f").gives("1.*f + (int)g").stays();
  }

  @Test public void issue70_12() {
    trimmingOf("foo((double)18)").gives("foo(1.*18)");
  }

  @Ignore @Test public void issue73_01() {
    trimmingOf("\"\" + \"abc\"").gives("\"abc\"");
  }

  @Ignore @Test public void issue73_02() {
    trimmingOf("\"\" + \"abc\" + \"\"").gives("\"abc\"");
  }

  @Ignore @Test public void issue73_03() {
    trimmingOf("\"abc\" + \"\"").gives("\"abc\"");
  }

  @Ignore @Test public void issue73_04() {
    trimmingOf("x + \"\"").stays();
  }

  @Ignore @Test public void issue73_05() {
    trimmingOf("\"\" + x").stays();
  }

  @Ignore @Test public void issue73_06() {
    trimmingOf("\"abc\" + \"\" + x").gives("\"abc\" + x");
  }

  @Test public void issue75a() {
    trimmingOf("int i = 0;").stays();
  }

  @Test public void issue75b() {
    trimmingOf("int i = +1;").gives("int i = 1;");
  }

  @Test public void issue75c() {
    trimmingOf("int i = +a;").gives("int i = a;");
  }

  @Test public void issue75d() {
    trimmingOf("+ 0").gives("0");
  }

  @Test public void issue75e() {
    trimmingOf("a = +0").gives("a = 0");
  }

  @Test public void issue75f() {
    trimmingOf("a = 1+0").gives("a = 1");
  }

  @Test public void issue75g() {
    trimmingOf("i=0").stays();
  }

  @Test public void issue75h() {
    trimmingOf("int i; i = +0;").gives("int i = +0;").gives("int i=0;");
  }

  @Test public void issue75i() {
    trimmingOf("+0").gives("0");
  }

  @Test public void issue75i0() {
    trimmingOf("-+-+2").gives("--+2");
  }

  @Test public void issue75i1() {
    trimmingOf("+0").gives("0");
  }

  @Test public void issue75i2() {
    trimmingOf("+1").gives("1");
  }

  @Test public void issue75i3() {
    trimmingOf("+-1").gives("-1");
  }

  @Test public void issue75i4() {
    trimmingOf("+1.0").gives("1.0");
  }

  @Test public void issue75i5() {
    trimmingOf("+'0'").gives("'0'");
  }

  @Test public void issue75i6() {
    trimmingOf("+1L").gives("1L");
  }

  @Test public void issue75i7() {
    trimmingOf("+0F").gives("0F");
  }

  @Test public void issue75i8() {
    trimmingOf("+0L").gives("0L");
  }

  @Test public void issue75il() {
    trimmingOf("+(a+b)").gives("a+b");
  }

  @Test public void issue75j() {
    trimmingOf("+1E3").gives("1E3");
  }

  @Test public void issue75k() {
    trimmingOf("(+(+(+x)))").gives("(x)");
  }

  @Test public void issue75m() {
    trimmingOf("+ + + i").gives("i");
  }

  @Test public void issue75n() {
    trimmingOf("(2*+(a+b))").gives("(2*(a+b))");
  }

  @Ignore("Disabled: there is some bug in distributive rule") @Test public void issue76a() {
    trimmingOf("a*b + a*c").gives("a*(b+c)");
  }

  @Ignore("Disabled: there is some bug in distributive rule") @Test public void issue76b() {
    trimmingOf("b*a + c*a").gives("a*(b+c)");
  }

  @Ignore("Disabled: there is some bug in distributive rule") @Test public void issue76c() {
    trimmingOf("b*a + c*a + d*a").gives("a*(b+c+d)");
  }

  @Test public void issue76d() {
    trimmingOf("a * (b + c)").stays();
  }

  @Test public void issue82a() {
    trimmingOf("(long)5").gives("1L*5");
  }

  @Test public void issue82b() {
    trimmingOf("(long)(int)a").gives("1L*(int)a").stays();
  }

  @Test public void issue82b_a_cuold_be_double() {
    trimmingOf("(long)a").stays();
  }

  @Ignore("Issue #218") @Test public void issue82c() {
    trimmingOf("(long)(long)2").gives("1L*(long)2").gives("1L*1L*2").stays();
  }

  @Test public void issue82d() {
    trimmingOf("(long)a*(long)b").stays();
  }

  @Test public void issue82e() {
    trimmingOf("(double)(long)a").gives("1.*(long)a").stays();
  }

  @Test public void issue83a() {
    trimmingOf("if(x.size()>=0) return a;").gives("if(true) return a;");
  }

  @Test public void issue83b() {
    trimmingOf("if(x.size()<0) return a;").gives("if(false) return a;");
  }

  @Test public void issue83c() {
    trimmingOf("if(x.size()>0)return a;").gives("if(!x.isEmpty())return a;");
  }

  @Test public void issue83d() {
    trimmingOf("if(x.size()==1) return a;").stays();
  }

  @Test public void issue83e() {
    trimmingOf("if(x.size()==2) return a;").stays();
  }

  @Test public void issue83f() {
    trimmingOf("if(2==x.size()) return a;").gives("if(x.size()==2) return a;");
  }

  @Test public void issue83g() {
    trimmingOf("if(x.size()==4) return a;").stays();
  }

  @Test public void issue83h() {
    trimmingOf("if(x.size()==0) return a;").gives("if(x.isEmpty()) return a;");
  }

  @Test public void issue83i() {
    trimmingOf("if(es.size() >= 2) return a;").stays();
  }

  @Test public void issue83j() {
    trimmingOf("if(es.size() > 2) return a;").stays();
  }

  @Test public void issue83k() {
    trimmingOf("if(es.size() < 2) return a;").stays();
  }

  @Test public void issue83l() {
    trimmingOf("uses(ns).size() <= 1").stays();
  }

  @Test public void issue83m() {
    trimmingOf("if(a.size() >= -3) ++a;").gives("if(true) ++a;").gives("++a;");
  }

  @Test public void issue83n() {
    trimmingOf("if(a.size() <= -9) ++a;a+=1;")//
        .gives("if(false) ++a;a+=1;") //
        .gives("{}a+=1;") //
        .gives("a+=1;") //
        .stays();
  }

  @Test public void issue85_86a() {
    trimmingOf("if(true){   \n" + "x(); }   \n" + "else{   \n" + "y();   \n" + "}").gives("{x();}").gives("x();");
  }

  @Test public void issue85_86b() {
    trimmingOf("if(false){   \n" + "x(); }   \n" + "else{   \n" + "y();   \n" + "}").gives("{y();}").gives("y();");
  }

  @Test public void issue85_86c() {
    trimmingOf("if(false)   \n" + "x();    \n" + "else   \n" + "y();   \n").gives("y();");
  }

  @Test public void issue85_86d() {
    trimmingOf("if(false){   \n" + "x(); }   \n" + "else{   \n" + "if(false) a();   \n" + "else b();" + "}").gives("{b();}").gives("b();");
  }

  @Test public void issue85_86e() {
    trimmingOf("if(false){   \n" + "x(); }   \n" + "else{   \n" + "if(true) a();   \n" + "else b();" + "}").gives("{a();}").gives("a();");
  }

  @Test public void issue85_86f() {
    trimmingOf("if(true){   \n" + "if(true) a();   \n" + "else b(); }   \n" + "else{   \n" + "if(false) a();   \n" + "else b();" + "}")
        .gives("{a();}").gives("a();");
  }

  @Test public void issue85_86g() {
    trimmingOf("if(z==k)   \n" + "x();    \n" + "else   \n" + "y();   \n").stays();
  }

  @Test public void issue85_86h() {
    trimmingOf("if(5==5)   \n" + "x();    \n" + "else   \n" + "y();   \n").stays();
  }

  @Test public void issue85_86i() {
    trimmingOf("if(z){   \n" + "if(true) a();   \n" + "else b(); }   \n" + "else{   \n" + "if(false) a();   \n" + "else b();" + "}")
        .gives("if(z)\n" + "if(true) a();   \n" + "else b();\n" + "else\n" + "if(false) a();   \n" + "else b();")
        .gives("if(z)\n" + "a(); \n" + "else \n" + "b();   \n");
  }

  @Test public void issue85_86j() {
    trimmingOf("if(true){ \n" + "if(true) \n" + "a(); \n" + "else \n" + "b(); \n" + "} \n" + "else c();").gives("{a();}").gives("a();");
  }

  @Test public void issue85_86k() {
    trimmingOf("if(false){ \n" + "if(true) \n" + "a(); \n" + "else \n" + "b(); \n" + "} \n" + "else c();").gives("c();");
  }

  @Test public void issue85_86l() {
    trimmingOf("if(false)" + "c();" + "else {\n" + "if(true) \n" + "a(); \n" + "else \n" + "b(); \n" + "} \n").gives("{a();}").gives("a();");
  }

  @Test public void issue86_1() {
    trimmingOf("if(false)" + "c();\n" + "int a;").gives("{}int a;").gives("int a;").stays();
  }

  @Test public void issue86_2() {
    trimmingOf("if(false) {c();\nb();\na();}").gives("{}");
  }

  @Ignore public void issue86_3() {
    trimmingOf("if(false) {c();\nb();\na();}").gives("{}").stays();
  }

  @Ignore public void issue86_4() {
    trimmingOf("if(false) {c();\nb();\na();}").gives("{}").stays();
  }

  @Ignore public void issue86_5() {
    trimmingOf("if(false) {c();\nb();\na();}").gives("{}").stays();
  }

  @Test public void issue87a() {
    trimmingOf("a-b*c - (x - - - (d*e))").gives("a  - b*c -x + d*e");
  }

  @Test public void issue87b() {
    trimmingOf("a-b*c").stays();
  }

  @Test public void issue87c() {
    trimmingOf("a + (b-c)").stays();
  }

  @Test public void issue87d() {
    trimmingOf("a - (b-c)").gives("a - b + c");
  }

  @Test public void simpleForLoop() {
    trimmingOf("for (int i = 0; i < 100; ++i) sum+=i;")//
        .gives("for(int ¢=0;¢<100;++¢)sum+=¢;")//
        .stays();
  }

  @Ignore public void test_a() {
    azzert.that("studiesA".replaceAll("ies$", "y").replaceAll("es$", "").replaceAll("s$", ""), is("studyA"));
  }

  @Test public void test_b() {
    azzert.that("studies".replaceAll("ies$", "y").replaceAll("es$", "").replaceAll("s$", ""), is("study"));
  }

  @Test public void test_c() {
    azzert.that("studes".replaceAll("ies$", "y").replaceAll("es$", "").replaceAll("s$", ""), is("stud"));
  }

  @Test public void test_d() {
    azzert.that("studs".replaceAll("ies$", "y").replaceAll("es$", "").replaceAll("s$", ""), is("stud"));
  }

  @Ignore public void trimmerBugXOR() {
    trimmingOf("j=j^k").gives("j^=k");
  }

  @Test public void trimmerBugXORCompiling() {
    trimmingOf("j = j ^ k").gives("j ^= k");
  }

  // @formatter:off
  enum A { a1() {{ f(); }
      public void f() {
        g();
      }
       void g() {
        h();
      }
       void h() {
        i();
      }
       void i() {
        f();
      }
    }, a2() {{ f(); }
      public void i() {
        f();
      }
      void f() {
        g();
      }
      void g() {
        h();
      }
      void h() {
        i();
      }
    }
  }

}
