package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

@SuppressWarnings("static-method")
public class Issue1077 {
  @Test public void t1() {
    trimmingOf("@SuppressWarnings(\"unchecked\") static boolean sequencerComplex(final ASTNode ¢, int type) {"
        + "if (¢ == null)"
        + "return false;"
        + "switch (¢.getNodeType()) {"
        + "case ASTNode.IF_STATEMENT:"
        + "final IfStatement $ = (IfStatement) ¢;"
        + "return sequencerComplex($.getThenStatement(),type) && sequencerComplex($.getElseStatement(),type);"
        + "case ASTNode.BLOCK:"
        + "for (final Statement s : (List<Statement>) ((Block) ¢).statements())"
        + "if (sequencerComplex(s,type))"
        + "return true;"
        + "return false;"
        + "default:"
        + "return sequencer(¢,type);"
        + "}"
        + "}").stays();
  }
  @Test public void t2() {
    trimmingOf("@SuppressWarnings(\"unchecked\") static boolean sequencerComplex(final ASTNode ¢, int type) {"
        + "for (final Statement s : (List<Statement>) ((Block) ¢).statements())"
        + "if (sequencerComplex(s))"
        + "return true;"
        + "}").stays();
  }
}
