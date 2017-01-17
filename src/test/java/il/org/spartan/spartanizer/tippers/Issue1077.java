package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

/** test case for bug in {@link SingleVariableDeclarationEnhancedForRenameParameterToCent}
 * 
 * @author YuvalSimon <tt>yuvaltechnion@gmail.com</tt>
 * @since 2017-01-17
 */
@SuppressWarnings("static-method")
public class Issue1077 {
  @Test public void t1() {
    trimmingOf(//
        "@SuppressWarnings(\"unchecked\") static boolean sequencerComplex(final ASTNode ¢, int type) {"//
            + "if (¢ == null)"//
            + "return false;"//
            + "switch (¢.getNodeType()) {" //
            + "case ASTNode.IF_STATEMENT:" //
            + "final IfStatement $ = (IfStatement) ¢;" //
            + "return sequencerComplex($.getThenStatement(),type) && sequencerComplex($.getElseStatement(),type);" //
            + "case ASTNode.BLOCK:" //
            + "for (final Statement s : (List<Statement>) ((Block) ¢).statements())" //
            + "if (sequencerComplex(s,type))" //
            + "return true;" //
            + "return false;" //
            + "default:" //
            + "return sequencer(¢,type);" //
            + "}" //
            + "}")//
    .stays(); //
  }

  @Test public void t2() {
    trimmingOf(//
        "@SuppressWarnings(\"unchecked\") static boolean sequencerComplex(final ASTNode ¢, int type) {" //
            + "for (final Statement s : (List<Statement>) ((Block) ¢).statements())" //
            + "if (sequencerComplex(s))" //
            + "return true;" //
            + "}")//
    .stays();
  }
  
  @Test public void t3() {
    trimmingOf("class a { "
        + "final ASTNode ¢; "
        + "@SuppressWarnings(\"unchecked\") static boolean sequencerComplex(final ASTNode k, int type) {"
        + "for (final Statement s : (List<Statement>) ((Block) ¢).statements())"
        + "if (s.has())"
        + "return true;"
        + "}"
        + "}").stays();
  }
  
  @Test public void t4() {
    trimmingOf("class a { "
        + "final ASTNode ¢;"
        + "final List<Statement> l; "
        + "@SuppressWarnings(\"unchecked\") static boolean sequencerComplex(final ASTNode k, int type) {"
        + "for (final Statement s : (List<Statement>) ((Block) ¢).statements())"
        + "if (s.has())"
        + "return true;"
        + "}"
        + "@SuppressWarnings(\"unchecked\") static AA sss(final ASTNode k, int type) {"
        + "for (final Statement s : (List<Statement>) ((Block) ¢).statements())"
        + "if (s.has())"
        + "return new AA();"
        + "return new AA() {"
        + "void aa() {"
        + "for (final Statement t : l)"
        + "if (t.has())"
        + "return true;"
        + "}"
        + "};"
        + "}"
        + "}").stays();
  }
  
  @Test public void t5() {
    trimmingOf("class a { "
        + "final List<Statement> l; "
        + "@SuppressWarnings(\"unchecked\") static boolean sequencerComplex(final ASTNode k, int type) {"
        + "final ASTNode ¢;"
        + "for (final Statement s : (List<Statement>) ((Block) ¢).statements())"
        + "if (s.has())"
        + "return true;"
        + "}"
        + "@SuppressWarnings(\"unchecked\") int sss(final ASTNode k, int type) {"
        + "for (final Statement s : l)"
        + "if (s.has())"
        + "return 1;"
        + "return 0;"
        + "}"
        + "}")
    .gives("class a { "
        + "final List<Statement> l; "
        + "@SuppressWarnings(\"unchecked\") static boolean sequencerComplex(final ASTNode k, int type) {"
        + "final ASTNode ¢;"
        + "for (final Statement s : (List<Statement>) ((Block) ¢).statements())"
        + "if (s.has())"
        + "return true;"
        + "}"
        + "@SuppressWarnings(\"unchecked\") int sss(final ASTNode k, int type) {"
        + "for (final Statement ¢ : l)"
        + "if (¢.has())"
        + "return 1;"
        + "return 0;"
        + "}"
        + "}");
  }
}
