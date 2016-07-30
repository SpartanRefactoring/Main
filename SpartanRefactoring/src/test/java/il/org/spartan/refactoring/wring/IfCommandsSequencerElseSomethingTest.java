package il.org.spartan.refactoring.wring;
import static  il.org.spartan.azzert.*;
import static il.org.spartan.azzert.assertNotEquals;
import static il.org.spartan.refactoring.spartanizations.TESTUtils.*;
import static il.org.spartan.refactoring.utils.Funcs.*;
import static il.org.spartan.utils.Utils.*;
import static org.hamcrest.text.IsEqualIgnoringWhiteSpace.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;
import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;
import org.junit.runners.Parameterized.*;

import il.org.spartan.*;
import il.org.spartan.refactoring.spartanizations.*;
import il.org.spartan.refactoring.utils.*;
import il.org.spartan.refactoring.wring.AbstractWringTest.*;
import il.org.spartan.utils.Utils;

/**
 * Unit tests for {@link Wrings#ADDITION_SORTER}.
 *
 * @author Yossi Gil
 * @since 2014-07-13
 */
@SuppressWarnings({ "javadoc", "static-method" }) //
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
public class IfCommandsSequencerElseSomethingTest {
  static final IfThenOrElseIsCommandsFollowedBySequencer WRING = new IfThenOrElseIsCommandsFollowedBySequencer();

  @Test public void checkSteps() {
    final Statement s = asSingle("if (a) return a = b; else a = c;");
     azzert.notNull(s);
     azzert.notNull(asIfStatement(s));
  }
  @Test public void checkStepsFull() throws MalformedTreeException, BadLocationException {
    final IfStatement s = (IfStatement) asSingle("if (a) return b; else a();");
    azzert.that(WRING.scopeIncludes(s), is(true));
    azzert.that(WRING.eligible(s), is(true));
    final Rewrite m = WRING.make(s);
    azzert.notNull(m);
    final Wring<IfStatement> w = Toolbox.instance.find(s);
    azzert.notNull(w);
    azzert.that(w, instanceOf(WRING.getClass()));
    final String wrap = Wrap.Statement.on(s.toString());
    final CompilationUnit u = (CompilationUnit) MakeAST.COMPILATION_UNIT.from(wrap);
     azzert.notNull(u);
    final Document d = new Document(wrap);
     azzert.notNull(d);
    final Trimmer t = new Trimmer();
    final ASTRewrite r = t.createRewrite(u, null);
    final TextEdit x = r.rewriteAST(d, null);
    x.apply(d);
    final String unpeeled = d.get();
    if (wrap.equals(unpeeled))
      fail("Nothing done on " + s);
    final String peeled = Wrap.Statement.off(unpeeled);
      azzert.that("No similification of " + s, s, not(peeled));
      String compressSpaces = compressSpaces(peeled);
      String compressSpaces2 = compressSpaces(s.toString());
      azzert.that("Simpification of " + s + " is just reformatting", compressSpaces, not(compressSpaces2));
    assertSimilar(" if(a)return b;a(); ", peeled);
  }
  @Test public void checkStepsTrimmer() throws MalformedTreeException, BadLocationException {
    final String input = "if (a) return b; else a();";
    final String wrap = Wrap.Statement.on(input);
    final CompilationUnit u = (CompilationUnit) MakeAST.COMPILATION_UNIT.from(wrap);
     azzert.notNull(u);
    final IfStatement s = extract.firstIfStatement(u);
    azzert.notNull(s);
    azzert.that(s.toString(), equalToIgnoringWhiteSpace(input));
    final Wring<IfStatement> w = Toolbox.instance.find(s);
    azzert.notNull(w);
    azzert.that(w.scopeIncludes(s), is(true));
    azzert.that(w.eligible(s), is(true));
    azzert.that(w, instanceOf(WRING.getClass()));
    final Rewrite m = w.make(s);
    azzert.notNull(m);
    final ASTRewrite r = ASTRewrite.create(s.getAST());
    m.go(r, null);
    azzert.that(r.toString(), allOf(startsWith("Events:"), containsString("[replaced:"), containsString("]")));
    final Document d = new Document(wrap);
     azzert.notNull(d);
    azzert.that(d.get(), equalToIgnoringWhiteSpace(wrap.toString()));
    final TextEdit x = r.rewriteAST(d, null);
    x.apply(d);
    final String unpeeled = d.get();
    if (wrap.equals(unpeeled))
      fail("Nothing done on " + s);
    final String peeled = Wrap.Statement.off(unpeeled);
    if (peeled.equals(s))
      assertNotEquals("No similification of " + s, s, peeled);
    if (compressSpaces(peeled).equals(compressSpaces(s.toString())))
      assertNotEquals("Simpification of " + s + " is just reformatting", compressSpaces(peeled), compressSpaces(s.toString()));
    assertSimilar(" if (a) return b; a(); ", peeled);
  }
  @Test public void checkStepsWRING() throws MalformedTreeException {
    final IfStatement s = (IfStatement) asSingle("if (a) return b; else a();");
    azzert.that(WRING.scopeIncludes(s), is(true));
    azzert.that(WRING.eligible(s), is(true));
    final Rewrite m = WRING.make(s);
    azzert.notNull(m);
    final ASTRewrite r = ASTRewrite.create(s.getAST());
    m.go(r, null);
    azzert.that(r.toString(), allOf(startsWith("Events:"), containsString("[replaced:"), containsString("]")));
  }

  @RunWith(Parameterized.class) //
  public static class OutOfScope extends AbstractWringTest.OutOfScope<IfStatement> {
    static String[][] cases = Utils.asArray(//
        new String[] { "Literal vs. Literal", "if (a) return b; else c;" }, //
        new String[] { "Simple if return", "if (a) return b; else return c;" }, //
        new String[] { "Simply nested if return", "{if (a)  return b; else return c;}" }, //
        new String[] { "Nested if return", "if (a) {;{{;;return b; }}} else {{{;return c;};;};}" }, //
        new String[] { "Not same assignment", "if (a) a /= b; else a /= c;" }, //
        new String[] { "Another distinct assignment", "if (a) a /= b; else a %= c;" }, //
        new String[] { "Simple if assign", "if (a) a = b; else a = c;" }, //
        new String[] { "Simple if plus assign", "if (a) a += b; else a += c;" }, //
        new String[] { "Simple if plus assign", "if (a) a *= b; else a *= c;" }, //
        null);

    /**
     * Generate test cases for this parameterized class.
     *
     * @return a collection of cases, where each case is an array of three
     *         objects, the test case name, the input, and the file.
     */
    @Parameters(name = DESCRIPTION) //
    public static Collection<Object[]> cases() {
      return collect(cases);
    }
    /** Instantiates the enclosing class ({@link OutOfScope}) */
    public OutOfScope() {
      super(WRING);
    }
  }

  @RunWith(Parameterized.class) //
  @FixMethodOrder(MethodSorters.NAME_ASCENDING) //
  public static class Wringed extends AbstractWringTest.Wringed.IfStatementAndSurrounding {
    private static String[][] cases = Utils.asArray(//
        new String[] { "Vanilla: sequencer in then", "if (a) return b; else a();", "if(a)return b;a();" }, //
        new String[] { "Vanilla: sequencer in else", "if (a) return b; else a();", "if(a)return b;a();" }, //
        new String[] { "Plant two statements", "if (a) return b; else a(); f();", "if(a)return b;a(); f();" }, //
        null, //
        new String[] { "Compressed complex", " if (x) {;f();;;return a;;;} else {;g();{;;{}}{}}",
            "if (x) {;f();;;return a;;;}\n g();" }, //
        null, //
        new String[] { "Compressed complex", " if (x) {;f();;;return a;;;} else {;g();{;;{}}{}}",
            "  if(x){;f();;;return a;;;} g();" }, //
        new String[] { "Compressed complex", " if (x) {;f();;;return a;;;} else {;g();{;;{}}{}}",
            "" + //
                " if (x) {\n" + //
                "   f();\n" + //
                "   return a;\n" + //
                " }\n" + //
                " g();\n" + //
                "" }, //
        null, //
        new String[] { "Complex with many junk statements",
            "" + //
                " if (x) {\n" + //
                "   ;\n" + //
                "   f();\n" + //
                "   return a;\n" + //
                " } else {\n" + //
                "   ;\n" + //
                "   g();\n" + //
                "   {\n" + //
                "   }\n" + //
                " }\n" + //
                "",
            "" + //
                " if (x) {\n" + //
                "   f();\n" + //
                "   return a;\n" + //
                " }\n" + //
                " g();\n" + //
                "" }, //
        null);

    /**
     * Generate test cases for this parameterized class.
     *
     * @return a collection of cases, where each case is an array of three
     *         objects, the test case name, the input, and the file.
     */
    @Parameters(name = DESCRIPTION) //
    public static Collection<Object[]> cases() {
      return collect(cases);
    }
    /**
     * Instantiates the enclosing class ({@link Wringed})
     */
    public Wringed() {
      super(WRING);
    }
  }
}
