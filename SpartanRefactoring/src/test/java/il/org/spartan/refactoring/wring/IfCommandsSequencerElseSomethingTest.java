package il.org.spartan.refactoring.wring;

import static il.org.spartan.hamcrest.CoreMatchers.is;
import static il.org.spartan.hamcrest.MatcherAssert.assertThat;
import static il.org.spartan.refactoring.spartanizations.TESTUtils.asSingle;
import static il.org.spartan.refactoring.spartanizations.TESTUtils.assertSimilar;
import static il.org.spartan.refactoring.utils.Funcs.asIfStatement;
import static il.org.spartan.utils.Utils.compressSpaces;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.IsEqualIgnoringWhiteSpace.equalToIgnoringWhiteSpace;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Collection;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import il.org.spartan.refactoring.spartanizations.Wrap;
import il.org.spartan.refactoring.utils.Extract;
import il.org.spartan.refactoring.utils.MakeAST;
import il.org.spartan.refactoring.utils.Rewrite;
import il.org.spartan.refactoring.wring.AbstractWringTest.OutOfScope;
import il.org.spartan.refactoring.wring.AbstractWringTest.Wringed;
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
    assertNotNull(s);
    assertNotNull(asIfStatement(s));
  }
  @Test public void checkStepsFull() throws MalformedTreeException, BadLocationException {
    final IfStatement s = (IfStatement) asSingle("if (a) return b; else a();");
    assertThat(WRING.scopeIncludes(s), is(true));
    assertThat(WRING.eligible(s), is(true));
    final Rewrite m = WRING.make(s);
    assertThat(m, notNullValue());
    final Wring<IfStatement> w = Toolbox.instance.find(s);
    assertThat(w, notNullValue());
    assertThat(w, instanceOf(WRING.getClass()));
    final String wrap = Wrap.Statement.on(s.toString());
    final CompilationUnit u = (CompilationUnit) MakeAST.COMPILATION_UNIT.from(wrap);
    assertNotNull(u);
    final Document d = new Document(wrap);
    assertNotNull(d);
    final Trimmer t = new Trimmer();
    final ASTRewrite r = t.createRewrite(u, null);
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
    assertSimilar(" if(a)return b;a(); ", peeled);
  }
  @Test public void checkStepsTrimmer() throws MalformedTreeException, BadLocationException {
    final String input = "if (a) return b; else a();";
    final String wrap = Wrap.Statement.on(input);
    final CompilationUnit u = (CompilationUnit) MakeAST.COMPILATION_UNIT.from(wrap);
    assertNotNull(u);
    final IfStatement s = Extract.firstIfStatement(u);
    assertThat(s, notNullValue());
    assertThat(s.toString(), equalToIgnoringWhiteSpace(input));
    final Wring<IfStatement> w = Toolbox.instance.find(s);
    assertThat(w, notNullValue());
    assertThat(w.scopeIncludes(s), is(true));
    assertThat(w.eligible(s), is(true));
    assertThat(w, instanceOf(WRING.getClass()));
    final Rewrite m = w.make(s);
    assertThat(m, notNullValue());
    final ASTRewrite r = ASTRewrite.create(s.getAST());
    m.go(r, null);
    assertThat(r.toString(), allOf(startsWith("Events:"), containsString("[replaced:"), containsString("]")));
    final Document d = new Document(wrap);
    assertNotNull(d);
    assertThat(d.get(), equalToIgnoringWhiteSpace(wrap.toString()));
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
    assertThat(WRING.scopeIncludes(s), is(true));
    assertThat(WRING.eligible(s), is(true));
    final Rewrite m = WRING.make(s);
    assertThat(m, notNullValue());
    final ASTRewrite r = ASTRewrite.create(s.getAST());
    m.go(r, null);
    assertThat(r.toString(), allOf(startsWith("Events:"), containsString("[replaced:"), containsString("]")));
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
        new String[] { "Compressed complex", " if (x) {;f();;;return a;;;} else {;g();{;;{}}{}}", "if (x) {;f();;;return a;;;}\n g();" }, //
        null, //
        new String[] { "Compressed complex", " if (x) {;f();;;return a;;;} else {;g();{;;{}}{}}", "  if(x){;f();;;return a;;;} g();" }, //
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
