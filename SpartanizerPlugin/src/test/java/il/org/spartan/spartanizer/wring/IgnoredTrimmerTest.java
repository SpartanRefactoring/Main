package il.org.spartan.spartanizer.wring;

import static il.org.spartan.spartanizer.wring.TrimmerTestsUtils.*;
import org.junit.*;
import org.junit.runners.*;

/** * Unit tests for the nesting class Unit test for the containing class. Note
 * our naming convention: a) test methods do not use the redundant "test"
 * prefix. b) test methods begin with the name of the method they check.
 * @author Yossi Gil
 * @since 2014-07-10 */
@Ignore //
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) public class IgnoredTrimmerTest {
  public void doNotInlineDeclarationWithAnnotationSimplified() {
    trimming("" + //
        "    @SuppressWarnings() int $ = (Class<T>) findClass(className);\n" + //
        "    return $;\n" + //
        "  }").stays();
  }

  @Test public void forwardDeclaration1() {
    trimming("/*    * This is a comment    */      int i = 6;   int j = 2;   int k = i+2;   S.out.println(i-j+k); ")
        .to(" /*    * This is a comment    */      int j = 2;   int i = 6;   int k = i+2;   S.out.println(i-j+k); ");
  }

  @Test public void forwardDeclaration2() {
    trimming("/*    * This is a comment    */      int i = 6, h = 7;   int j = 2;   int k = i+2;   S.out.println(i-j+k); ")
        .to(" /*    * This is a comment    */      int h = 7;   int j = 2;   int i = 6;   int k = i+2;   S.out.println(i-j+k); ");
  }

  @Test public void forwardDeclaration3() {
    trimming("/*    * This is a comment    */      int i = 6;   int j = 3;   int k = j+2;   int m = k + j -19;   y(m*2 - k/m);   y(i);   y(i+m); ")
        .to(" /*    * This is a comment    */      int j = 3;   int k = j+2;   int m = k + j -19;   y(m*2 - k/m);   int i = 6;   y(i);   y(i+m); ");
  }

  @Test public void forwardDeclaration4() {
    trimming(
        " /*    * This is a comment    */      int i = 6;   int j = 3;   int k = j+2;   int m = k + j -19;   y(m*2 - k/m);   final BlahClass bc = new BlahClass(i);   y(i+m+bc.j);    private static class BlahClass {   public BlahClass(int i) {    j = 2*i;      public final int j; ")
            .to(" /*    * This is a comment    */      int j = 3;   int k = j+2;   int m = k + j -19;   y(m*2 - k/m);   int i = 6;   final BlahClass bc = new BlahClass(i);   y(i+m+bc.j);    private static class BlahClass {   public BlahClass(int i) {    j = 2*i;      public final int j; ");
  }

  @Test public void forwardDeclaration5() {
    trimming("/*    * This is a comment    */      int i = y(0);   int j = 3;   int k = j+2;   int m = k + j -19;   y(m*2 - k/m + i);   y(i+m); ")
        .to(" /*    * This is a comment    */      int j = 3;   int k = j+2;   int i = y(0);   int m = k + j -19;   y(m*2 - k/m + i);   y(i+m); ");
  }

  @Test public void forwardDeclaration6() {
    trimming(
        " /*    * This is a comment    */      int i = y(0);   int h = 8;   int j = 3;   int k = j+2 + y(i);   int m = k + j -19;   y(m*2 - k/m + i);   y(i+m); ")
            .to(" /*    * This is a comment    */      int h = 8;   int i = y(0);   int j = 3;   int k = j+2 + y(i);   int m = k + j -19;   y(m*2 - k/m + i);   y(i+m); ");
  }

  @Test public void forwardDeclaration7() {
    trimming(
        "  j = 2*i;   }      public final int j;    private BlahClass yada6() {   final BlahClass res = new BlahClass(6);   final Runnable r = new Runnable() {        @Override    public void run() {     res = new BlahClass(8);     S.out.println(res.j);     doStuff(res);        private void doStuff(BlahClass res2) {     S.out.println(res2.j);        private BlahClass res;   S.out.println(res.j);   return res; ")
            .to("  j = 2*i;   }      public final int j;    private BlahClass yada6() {   final Runnable r = new Runnable() {        @Override    public void run() {     res = new BlahClass(8);     S.out.println(res.j);     doStuff(res);        private void doStuff(BlahClass res2) {     S.out.println(res2.j);        private BlahClass res;   final BlahClass res = new BlahClass(6);   S.out.println(res.j);   return res; ");
  }

  @Test public void inline00() {
    trimming("" + //
        "  Object a() { " + //
        "    class a {\n" + //
        "      a a;\n" + //
        "      Object a() {\n" + //
        "        return a;\n" + // /
        "      }" + //
        "    }\n" + //
        "    final Object a = new Object();\n" + //
        "    if (a instanceof a)\n" + //
        "      new Object();  \n" + //
        "    final Object a = new Object();\n" + //
        "    if (a instanceof a)\n" + //
        "      new Object();" + //
        "}\n" + //
        "").to(//
            "  Object a() { " + //
                "    class a {\n" + //
                "      Object a() {\n" + //
                "        return a;\n" + // /
                "    }\n" + //
                "    final Object a = new Object();\n" + //
                "    if (a instanceof a)\n" + //
                "      new Object();  \n" + //
                "    final Object a = new Object();\n" + //
                "    if (a instanceof a)\n" + //
                "      new Object();" + //
                "}\n" + //
                "");
  }

  @Test public void inlineSingleUse01() {
    trimming("/*    * This is a comment    */      int i = y(0);   int j = 3;   int k = j+2;   int m = k + j -19;   y(m*2 - k/m + i); ")
        .to(" /*    * This is a comment    */      int j = 3;   int k = j+2;   int m = k + j -19;   y(m*2 - k/m + (y(0))); ");
  }

  @Test public void inlineSingleUse02() {
    trimming("/*    * This is a comment    */      int i = 5,j=3;   int k = j+2;   int m = k + j -19 +i;   y(k); ")
        .to(" /*    * This is a comment    */      int j=3;   int k = j+2;   int m = k + j -19 +(5);   y(k); ");
  }

  @Test public void inlineSingleUse03() {
    trimming("/*    * This is a comment    */      int i = 5;   int j = 3;   int k = j+2;   int m = k + j -19;   y(m*2 - k/m + i); ")
        .to(" /*    * This is a comment    */      int j = 3;   int k = j+2;   int m = k + j -19;   y(m*2 - k/m + (5)); ");
  }

  @Test public void inlineSingleUse04() {
    trimming("int x = 6;   final BlahClass b = new BlahClass(x);   int y = 2+b.j;   y(y-b.j);   y(y*2); ")
        .to(" final BlahClass b = new BlahClass((6));   int y = 2+b.j;   y(y-b.j);   y(y*2); ");
  }

  @Test public void inlineSingleUse05() {
    trimming("int x = 6;   final BlahClass b = new BlahClass(x);   int y = 2+b.j;   y(y+x);   y(y*x); ")
        .to(" int x = 6;   int y = 2+(new BlahClass(x)).j;   y(y+x);   y(y*x); ");
  }

  @Test public void inlineSingleUse06() {
    trimming(
        "   final Collection<Integer> outdated = new ArrayList<>();     int x = 6, y = 7;     S.out.println(x+y);     final Collection<Integer> coes = new ArrayList<>();     for (final Integer pi : coes)      if (pi.intValue() < x - y)       outdated.add(pi);     for (final Integer pi : outdated)      coes.remove(pi);     S.out.println(coes.size()); ")
            .stays();
  }

  @Test public void inlineSingleUse08() {
    trimming(
        "   final Collection<Integer> outdated = new ArrayList<>();     int x = 6, y = 7;     S.out.println(x+y);     final Collection<Integer> coes = new ArrayList<>();     for (final Integer pi : coes)      if (pi.intValue() < x - y)       outdated.add(pi);     S.out.println(coes.size());     S.out.println(outdated.size()); ")
            .stays();
  }

  @Test public void inlineSingleUse09() {
    trimming(
        " final A a = new D().new A(V){\nABRA\n{\nCADABRA\n{V;);   assertEquals(5, a.new Context().lineCount());   final PureIterable&lt;Mutant&gt; ms = a.generateMutants();   assertEquals(2, count(ms));   final PureIterator&lt;Mutant&gt; i = ms.iterator();    azzert.aye(i.hasNext());   assertEquals(V;{\nABRA\nABRA\n{\nCADABRA\n{\nV;, i.next().text);    azzert.aye(i.hasNext());   assertEquals(V;{\nABRA\n{\nCADABRA\nCADABRA\n{\nV;, i.next().text);    azzert.nay(i.hasNext());  ")
            .stays();
  }

  @Test public void inlineSingleUse10() {
    trimming(
        "      final A a = new A(\"{\nABRA\n{\nCADABRA\n{\");        assertEquals(5, a.new Context().lineCount());        final PureIterable<Mutant> ms = a.mutantsGenerator();        assertEquals(2, count(ms));        final PureIterator<Mutant> i = ms.iterator();         azzert.aye(i.hasNext());        assertEquals(\"{\nABRA\nABRA\n{\nCADABRA\n{\n\", i.next().text);         azzert.aye(i.hasNext());        assertEquals(\"{\nABRA\n{\nCADABRA\nCADABRA\n{\n\", i.next().text);         azzert.nay(i.hasNext());")
            .stays();
  }

  @Test public void issue06A() {
    trimming("x/a*-b/-c*- - - d / d")//
        .to("-x/a * b/ c * d/d")//
        .stays();
  }

  @Test public void issue06C2() {
    trimming("-a * b/ c * d/d").stays();
  }

  @Test public void issue06C3() {
    trimming("-a * b/ c * d").stays();
  }

  @Test public void issue120_1() {
    trimming("\"a\"+\"b\"").to("\"ab\"");
  }
  


  @Test public void issue120_2() {
    trimming("\"abc\"+\"de\"+\"fgh\"").to("\"abcdefgh\"");
  }

  @Test public void issue120_3() {
    trimming("\"abc\"+a.toString()+\"de\"+\"fgh\"").to("\"abc\"+a.toString()+\"defgh\"");
  }

  @Test public void issue120_4() {
    trimming("c.toString()+\"abc\"+a.toString()+\"de\"+\"fgh\"") //
        .to("c.toString()+\"abc\"+a.toString()+\"defgh\"");
  }

  @Test public void issue54ForPlainUseInCondition() {
    trimming("int a  = f(); for (int i = 0; a < 100;  ++i) b[i] = 3;")//
        .stays();
  }

  @Test public void issue54ForPlainUseInInitializer() {
    trimming("int a  = f(); for (int i = a; i < 100; i++) b[i] = 3;")//
        .to(" for (int i = f(); i < 100; i++) b[i] = 3;");
  }

  @Test public void issue54ForPlainUseInUpdaters() {
    trimming("int a  = f(); for (int i = 0; i < 100; i *= a) b[i] = 3;")//
        .stays();
  }

  public void issue62b() {
    trimming("int f(int i) { for(;i<100;i=i+1) if(false) break; return i; }")//
        .to("int f(int i) { for(;i<100;i+=1) if(false) break; return i; }")//
        .to("int f(int i) { for(;i<100;i++) if(false) break; return i; }")//
        .to("int f(int i) { for(;i<100;++i) if(false) break; return i; }")//
        .to("int f(int i) { for(;i<100;++i){} return i; }").stays();
  }

  @Test public void issue74a() {
    trimming("int[] a = new int[] {,}").to("int[] a = new int[] {}");
  }

  @Test public void issue74b() {
    trimming("int[] a = new int[] {2,3,}").to("int[] a = new int[] {2,3}");
  }

  @Test public void issue74c() {
    trimming("a = new int[]{2,3,}").to("a = new int[] {2,3}");
  }

  @Test public void reanmeReturnVariableToDollar01() {
    trimming(
        " public BlahClass(int i) {    j = 2*i;      public final int j;    public BlahClass yada6() {   final BlahClass res = new BlahClass(6);   S.out.println(res.j);   return res; ")
            .to(" public BlahClass(int i) {    j = 2*i;      public final int j;    public BlahClass yada6() {   final BlahClass $ = new BlahClass(6);   S.out.println($.j);   return $; ");
  }

  @Test public void reanmeReturnVariableToDollar02() {
    trimming(
        " int res = blah.length();   if (blah.contains(0xDEAD))    return res * 2;   if (res % 2 ==0)    return ++res;   if (blah.startsWith(\"y\")) {    return y(res);   int x = res + 6;   if (x>1)    return res + x;   res -= 1;   return res; ")
            .to(" int $ = blah.length();   if (blah.contains(0xDEAD))    return $ * 2;   if ($ % 2 ==0)    return ++$;   if (blah.startsWith(\"y\")) {    return y($);   int x = $ + 6;   if (x>1)    return $ + x;   $ -= 1;   return $; ");
  }

  @Test public void reanmeReturnVariableToDollar03() {
    trimming(
        " public BlahClass(int i) {    j = 2*i;      public final int j;   public int yada7(final String blah) {   final BlahClass res = new BlahClass(blah.length());   if (blah.contains(0xDEAD))    return res.j;   int x = blah.length()/2;   if (x==3)    return x;   x = y(res.j - x);   return x; ")
            .to(" public BlahClass(int i) {    j = 2*i;      public final int j;   public int yada7(final String blah) {   final BlahClass res = new BlahClass(blah.length());   if (blah.contains(0xDEAD))    return res.j;   int $ = blah.length()/2;   if ($==3)    return $;   $ = y(res.j - $);   return $; ");
  }

  @Test public void reanmeReturnVariableToDollar04() {
    trimming("int res = 0;   String $ = blah + known;   y(res + $.length());   return res + $.length();").stays();
  }

  @Test public void reanmeReturnVariableToDollar05() {
    trimming(
        "  j = 2*i;   }      public final int j;    public BlahClass yada6() {   final BlahClass res = new BlahClass(6);   final Runnable r = new Runnable() {        @Override    public void run() {     final BlahClass res2 = new BlahClass(res.j);     S.out.println(res2.j);     doStuff(res2);        private void doStuff(final BlahClass res) {     S.out.println(res.j);   S.out.println(res.j);   return res; ")
            .to("  j = 2*i;   }      public final int j;    public BlahClass yada6() {   final BlahClass $ = new BlahClass(6);   final Runnable r = new Runnable() {        @Override    public void run() {     final BlahClass res2 = new BlahClass($.j);     S.out.println(res2.j);     doStuff(res2);        private void doStuff(final BlahClass res) {     S.out.println(res.j);   S.out.println($.j);   return $; ");
  }

  @Test public void reanmeReturnVariableToDollar06() {
    trimming(
        "  j = 2*i;   }      public final int j;    public void yada6() {   final BlahClass res = new BlahClass(6);   final Runnable r = new Runnable() {        @Override    public void run() {     final BlahClass res2 = new BlahClass(res.j);     S.out.println(res2.j);     doStuff(res2);        private int doStuff(final BlahClass r) {     final BlahClass res = new BlahClass(r.j);     return res.j + 1;   S.out.println(res.j); ")
            .to("  j = 2*i;   }      public final int j;    public void yada6() {   final BlahClass res = new BlahClass(6);   final Runnable r = new Runnable() {        @Override    public void run() {     final BlahClass res2 = new BlahClass(res.j);     S.out.println(res2.j);     doStuff(res2);        private int doStuff(final BlahClass r) {     final BlahClass $ = new BlahClass(r.j);     return $.j + 1;   S.out.println(res.j); ");
  }

  @Test public void reanmeReturnVariableToDollar07() {
    trimming(
        "  j = 2*i;   }      public final int j;    public BlahClass yada6() {   final BlahClass res = new BlahClass(6);   final Runnable r = new Runnable() {        @Override    public void run() {     res = new BlahClass(8);     S.out.println(res.j);     doStuff(res);        private void doStuff(BlahClass res2) {     S.out.println(res2.j);        private BlahClass res;   S.out.println(res.j);   return res; ")
            .to("  j = 2*i;   }      public final int j;    public BlahClass yada6() {   final BlahClass $ = new BlahClass(6);   final Runnable r = new Runnable() {        @Override    public void run() {     res = new BlahClass(8);     S.out.println(res.j);     doStuff(res);        private void doStuff(BlahClass res2) {     S.out.println(res2.j);        private BlahClass res;   S.out.println($.j);   return $; ");
  }

  @Test public void reanmeReturnVariableToDollar08() {
    trimming(
        " public BlahClass(int i) {    j = 2*i;      public final int j;    public BlahClass yada6() {   final BlahClass res = new BlahClass(6);   if (res.j == 0)    return null;   S.out.println(res.j);   return res; ")
            .to(" public BlahClass(int i) {    j = 2*i;      public final int j;    public BlahClass yada6() {   final BlahClass $ = new BlahClass(6);   if ($.j == 0)    return null;   S.out.println($.j);   return $; ");
  }

  @Test public void reanmeReturnVariableToDollar09() {
    trimming(
        " public BlahClass(int i) {    j = 2*i;      public final int j;    public BlahClass yada6() {   final BlahClass res = new BlahClass(6);   if (res.j == 0)    return null;   S.out.println(res.j);   return null;")
            .stays();
  }

  @Test public void reanmeReturnVariableToDollar10() {
    trimming(
        "@Override public IMarkerResolution[] getResolutions(final IMarker m) {   try {    final Spartanization s = All.get((String) m.getAttribute(Builder.SPARTANIZATION_TYPE_KEY)); ")
            .to("@Override public IMarkerResolution[] getResolutions(final IMarker m) {   try {    final Spartanization $ = All.get((String) m.getAttribute(Builder.SPARTANIZATION_TYPE_KEY)); ");
  }

  @Test public void reanmeReturnVariableToDollar11() {
    trimming("").stays();
  }

  @Test public void sameAssignmentDifferentTypes() {
    trimming("" //
        + "public void f() {\n" //
        + "  double x;\n" //
        + "  int y;\n" //
        + "  x = 0;\n" //
        + "  y = 0;\n" //
        + "}").stays();
  }

  @Test public void shortestOperand09() {
    trimming("return 2 - 4 < 50 - 20 - 10 - 5;").to("return 2 - 4 < 50 - 5 - 10 - 20 ;");
  }

  @Test public void sortSubstraction() {
    trimming("1-c-b").to("1-b-c");
  }

  @Test public void dontELiminateSwitch() {
    trimming("switch (a) { default: }").stays();
  }

  @Test public void ifDoNotRemoveBracesWithVariableDeclarationStatement() {
    trimming("if(a) { int i = 3; }").stays();
  }

  @Test public void ifDoNotRemoveBracesWithVariableDeclarationStatement2() {
    trimming("if(a) { Object o; }").stays();
  }

  @Test public void ifToSwitch1() {
    trimming("" //
        + "if (\"1\".equals(s))\n" // ))
        + "  System.out.println(s);\n" //
        + "else if (\"2\".equals(s))\n" //
        + "  System.out.println(s + \"!\");\n" //
        + "else {\n" //
        + "  s += \"@\";\n" //
        + "  System.out.println(s);\n" //
        + "}\n" //
    ).to("" //
        + "switch (s) {\n" //
        + "  case \"1\":\n" //
        + "    System.out.println(s);\n" //
        + "    break;\n" //
        + "  case \"2\":\n" //
        + "    System.out.println(s + \"!\");\n" //
        + "    break;\n" //
        + "  default:\n" //
        + "    s += \"@\";\n" //
        + "    System.out.println(s);\n" //
        + "    break;\n" //
        + "  }\n");
  }

  @Test public void renameVariableUnderscore2() {
    trimming("class A {int _; int f(int _) {return _;}}").to("class A {int __; int f(int __) {return __;}}");
  }

  @Test public void replaceClassInstanceCreationWithFactoryClassInstanceCreation() {
    trimming("Character x = new Character(new Character(f()));").to("Character x = Character.valueOf(Character.valueOf(f()));");
  }

  @Test public void shortestOperand05() {
    trimming("    final W s = new W(\"bob\");\n" + //
        "    return s.l(hZ).l(\"-ba\").toString() == \"bob-ha-banai\";").to("return(new W(\"bob\")).l(hZ).l(\"-ba\").toString()==\"bob-ha-banai\";");
  }

  @Test public void stringFromBuilderAddParenthesis() {
    trimming("new StringBuilder(f()).append(1+1).toString()").to("\"\" + f() + (1+1)");
  }

  @Test public void stringFromBuilderGeneral() {
    trimming("new StringBuilder(myName).append(\"\'s grade is \").append(100).toString()").to("myName + \"\'s grade is \" + 100");
  }

  @Test public void stringFromBuilderNoStringComponents() {
    trimming("new StringBuilder(0).append(1).toString()").to("\"\" + 0 + 1");
  }

  @Test public void switchBrakesToReturnCaseWithoutSequencer() {
    trimming("" //
        + " switch (x) {\n" //
        + "     case 1:\n"//
        + "         System.out.println(\"1\");\n" //
        + "         break;\n" //
        + "     case 2:\n"//
        + "         System.out.println(\"2\");\n" //
        + "         return 1;\n" //
        + "     case 3:\n"//
        + "         System.out.println(\"3\");\n" //
        + " }\n"//
        + " return 2;")
            .to("" //
                + " switch (x) {\n" //
                + "     case 1:\n"//
                + "         System.out.println(\"1\");\n" //
                + "         return 2;\n" //
                + "     case 2:\n"//
                + "         System.out.println(\"2\");\n" //
                + "         return 1;\n" //
                + "     case 3:\n"//
                + "         System.out.println(\"3\");\n" //
                + " }\n"//
                + " return 2;\n");
  }

  @Test public void switchBrakesToReturnDefaultWithSequencer() {
    trimming("" //
        + " switch (x) {\n" //
        + "     case 1:\n"//
        + "         System.out.println(\"1\");\n" //
        + "         break;\n" //
        + "     case 2:\n"//
        + "         System.out.println(\"2\");\n" //
        + "         return 1;\n" //
        + "     case 3:\n"//
        + "         System.out.println(\"3\");\n" //
        + "     default:\n"//
        + "         return 2;\n" //
        + " }\n"//
        + " return 3;")
            .to("" //
                + " switch (x) {\n" //
                + "     case 1:\n"//
                + "         System.out.println(\"1\");\n" //
                + "         return 3;\n" //
                + "     case 2:\n"//
                + "         System.out.println(\"2\");\n" //
                + "         return 1;\n" //
                + "     case 3:\n"//
                + "         System.out.println(\"3\");\n" //
                + "     default:\n"//
                + "         return 2;\n" //
                + " }");
  }

  @Test public void switchBreakesToReturnAllCases() {
    trimming("" //
        + " switch (x) {\n" //
        + "     case 1:\n"//
        + "         System.out.println(\"1\");\n" //
        + "         break;\n" //
        + "     case 2:\n"//
        + "         System.out.println(\"2\");\n" //
        + "         return 1;\n" //
        + "     case 3:\n"//
        + "         System.out.println(\"3\");\n" //
        + " }\n"//
        + " return 3;")
            .to("" //
                + " switch (x) {\n" //
                + "     case 1:\n"//
                + "         System.out.println(\"1\");\n" //
                + "         return 3;\n" //
                + "     case 2:\n"//
                + "         System.out.println(\"2\");\n" //
                + "         return 1;\n" //
                + "     case 3:\n"//
                + "         System.out.println(\"3\");\n" //
                + " }\n"//
                + " return 3;");
  }

  @Test public void switchSimplifiyNoSequencer() {
    trimming("" //
        + "switch(x) {\n" //
        + "case 1:\n" //
        + "  System.out.println('!');\n" //
        + "case 2:\n" //
        + "  System.out.println('@');\n" //
        + "}")
            .to("" //
                + "switch(x) {\n" //
                + "case 1:\n" //
                + "  System.out.println('!');\n" //
                + "  System.out.println('@');\n" //
                + "  break;\n" //
                + "case 2:\n" //
                + "  System.out.println('@');\n" //
                + "}");
  }

  @Test public void switchSimplifyCaseAfterDefault2() {
    trimming("" //
        + "switch (e.getNodeType()) {\n" //
        + "default:\n" //
        + "  break;\n" //
        + "case CONDITIONAL_EXPRESSION:\n" //
        + "  return true;\n" //
        + "case PARENTHESIZED_EXPRESSION:\n" //
        + "  if (((ParenthesizedExpression) e).getExpression().getNodeType() == CONDITIONAL_EXPRESSION)\n" //
        + "    return true;\n" //
        + "}")
            .to("" //
                + "switch (e.getNodeType()) {\n" //
                + "case CONDITIONAL_EXPRESSION:\n" //
                + "  return true;\n" //
                + "default:\n" //
                + "  break;\n" //
                + "case PARENTHESIZED_EXPRESSION:\n" //
                + "  if (((ParenthesizedExpression) e).getExpression().getNodeType() == CONDITIONAL_EXPRESSION)\n" //
                + "    return true;\n" //
                + "}");
  }

  @Test public void switchSimplifyCaseAfterefault3() {
    trimming("" //
        + "switch (totalNegation) {\n" //
        + "default:\n" //
        + "  break;\n" //
        + "  case 0:\n" //
        + "  return null;\n" //
        + "case 1:\n" //
        + "  if (negationLevel(es.get(0)) == 1)\n" //
        + "    return null;\n" //
        + "}")
            .to("" //
                + "switch (totalNegation) {\n" //
                + "  case 0:\n" //
                + "  return null;\n" //
                + "default:\n" //
                + "  break;\n" //
                + "case 1:\n" //
                + "  if (negationLevel(es.get(0)) == 1)\n" //
                + "    return null;\n" //
                + "}");
  }

  @Test public void switchSimplifyCasesMergeWithDefault() {
    trimming("" //
        + "switch (n.getNodeType()) {\n" //
        + "default:\n" //
        + "  return -1;\n" //
        + "case BREAK_STATEMENT:\n" //
        + "  return 0;\n" //
        + "case CONTINUE_STATEMENT:\n" //
        + "  return 1;\n" //
        + "case RETURN_STATEMENT:\n" //
        + "  return 2;\n" //
        + "case THROW_STATEMENT:\n" //
        + "  return -1;\n" //
        + "}")
            .to("" //
                + "switch (n.getNodeType()) {\n" //
                + "default:\n" //
                + "  return -1;\n" //
                + "case BREAK_STATEMENT:\n" //
                + "  return 0;\n" //
                + "case CONTINUE_STATEMENT:\n" //
                + "  return 1;\n" //
                + "case RETURN_STATEMENT:\n" //
                + "  return 2;\n" //
                + "}");
  }

  @Test public void switchSimplifyNoDefault() {
    trimming("" //
        + "switch (x) {" //
        + "  case 1:" //
        + "    System.out.println('!');" //
        + "  case 2:" //
        + "    break;" //
        + "  case 3:" //
        + "    System.out.println('!');" //
        + "    break;" //
        + "   case 4:" //
        + "    break;" //
        + "  }")
            .to("" //
                + "switch (x) {" //
                + "  case 1:" //
                + "  case 3:" //
                + "    System.out.println('!');" //
                + "    break;" //
                + "  case 2:" //
                + "  case 4:" //
                + "    break;" //
                + "  }");
  }

  @Test public void switchSimplifyParenthesizedCases() {
    trimming("" //
        + "switch (checkMatrix(A)) {\n" //
        + "  case -1: {\n" //
        + "    System.out.println(\"1\");\n" //
        + "    System.exit(0);\n" //
        + "  }\n" //
        + "  case -2: {\n" //
        + "    System.out.println(\"2\");\n" //
        + "    System.exit(0);\n" //
        + "  }\n" //
        + "  case 0: {\n" //
        + "    System.out.println(\"3\");\n" //
        + "    break;\n" //
        + "  }\n" //
        + "}")
            .to("" //
                + "switch (checkMatrix(A)) {\n" //
                + "case -1: {\n" //
                + "  System.out.println(\"1\");\n" //
                + "  System.exit(0);\n" //
                + "} {\n" //
                + "  System.out.println(\"2\");\n" //
                + "  System.exit(0);\n" //
                + "} {\n" //
                + "  System.out.println(\"3\");\n" //
                + "  break;\n" //
                + "}\n" //
                + "case -2: {\n" //
                + "  System.out.println(\"2\");\n" //
                + "  System.exit(0);\n" //
                + "} {\n" //
                + "  System.out.println(\"3\");\n" //
                + "  break;\n" //
                + "}\n" //
                + "case 0: {\n" //
                + "  System.out.println(\"3\");\n" //
                + "  break;\n" //
                + "}\n" //
                + "}");
    // switch (checkMatrix(A)) {
    // case -1: {
    // System.out.println("1");
    // System.exit(0);
    // }
    // case -2: {
    // System.out.println("2");
    // System.exit(0);
    // }
    // case 0: {
    // System.out.println("3");
    // break;
    // }
    // }
  }

  @Test public void switchSimplifyWithDefault() {
    trimming("" + "switch (internalDelta.getKind()) {" //
        + "case IResourceDelta.ADDED:" //
        + "case IResourceDelta.CHANGED:" //
        + "  // handle added and changed resource" //
        + "  // handle added and changed resource" //
        + "  addMarkers(internalDelta.getResource());" //
        + "  // return true to continue visiting children." //
        + "  // return true to continue visiting children." //
        + "  return true;" //
        + "default:" //
        + "  return true; // return true to continue visiting children." //
        + "}").stays();
  }

  @Test public void switchSimplifyWithDefault1() {
    trimming("" //
        + "switch (x) {" //
        + "  case 1:" //
        + "    System.out.println('!');" //
        + "  case 2:" //
        + "    break;" //
        + "  case 3:" //
        + "    System.out.println('!');" //
        + "    break;" //
        + "   default:" //
        + "     break;" //
        + "   case 4:" //
        + "    break;" //
        + "  }")
            .to("" //
                + "switch (x) {" //
                + "  case 1:" //
                + "  case 3:" //
                + "    System.out.println('!');" //
                + "    break;" //
                + "  default:" //
                + "    break;" //
                + "  }");
  }
}