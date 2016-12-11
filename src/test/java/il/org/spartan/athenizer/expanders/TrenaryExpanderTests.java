package il.org.spartan.athenizer.expanders;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.athenizer.inflate.expanders.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** @author Raviv Rachmiel
 * @since 8-12-2016 */
public class TrenaryExpanderTests {
// TODO: Raviv, you can use one   @SuppressWarnings("static-method") for the entire class
 @Test public void basicCanTip() {
    wizard.ast("return a==0? 2:3;").accept(new ASTVisitor() {
      @Override public boolean visit(final ReturnStatement node) {
        assert new TernaryExpander().canTip(node);
        return true;
      }
    });
  }

  @Test @SuppressWarnings("static-method") public void basicCanTip2() {
    wizard.ast("a = a==0? 2:3;").accept(new ASTVisitor() {
      @Override public boolean visit(final ReturnStatement node) {
        assert new TernaryExpander().canTip(node);
        return true;
      }
    });
  }

  @Test @SuppressWarnings("static-method") public void basicCanTip3() {
    wizard.ast("int b = a==0? 2:3;").accept(new ASTVisitor() {
      @Override public boolean visit(final ReturnStatement node) {
        assert new TernaryExpander().canTip(node);
        return true;
      }
    });
  }

  @Test @SuppressWarnings("static-method") public void nestedCanTip() {
    wizard.ast("a = a==0? (b==2? 4: 5 ):3;").accept(new ASTVisitor() {
      @Override public boolean visit(final ReturnStatement node) {
        assert new TernaryExpander().canTip(node);
        return true;
      }
    });
  }

  @Test @SuppressWarnings("static-method") public void appCanTip() {
    wizard.ast("a = (a==0? (b==2? 4: 5 ):3);").accept(new ASTVisitor() {
      @Override public boolean visit(final ReturnStatement node) {
        assert new TernaryExpander().canTip(node);
        return true;
      }
    });
  }

  @Test @SuppressWarnings("static-method") public void basicTest() {
    wizard.ast("return a==0? 1:2;").accept(new ASTVisitor() {
      @Override public boolean visit(final ReturnStatement node) {
        assert new TernaryExpander().replacement(node) instanceof IfStatement;
        return true;
      }
    });
  }

  @Test @SuppressWarnings("static-method") public void basicTest2() {
    wizard.ast("a = a==0? 1:2;").accept(new ASTVisitor() {
      @Override public boolean visit(final ReturnStatement node) {
        assert new TernaryExpander().replacement(node) instanceof IfStatement;
        return true;
      }
    });
  }

  @Test @SuppressWarnings("static-method") public void basicTest3() {
    wizard.ast("int a = a==0? 1:2;").accept(new ASTVisitor() {
      @Override public boolean visit(final ReturnStatement node) {
        assert new TernaryExpander().replacement(node) instanceof IfStatement;
        return true;
      }
    });
  }

  @Test @SuppressWarnings("static-method") public void nestedTest() {
    wizard.ast("a = b==0? (a==0? 1:2) : 4;").accept(new ASTVisitor() {
      @Override public boolean visit(final ReturnStatement node) {
        assert new TernaryExpander().replacement(node) instanceof IfStatement;
        return true;
      }
    });
  }

  @Test @SuppressWarnings("static-method") public void returnTest() {
    wizard.ast("return a==0? 1:2;").accept(new ASTVisitor() {
      @Override public boolean visit(final ReturnStatement node) {
        assert new TernaryExpander().replacement(node) instanceof IfStatement;
        assert "a == 0".equals(az.ifStatement(new TernaryExpander().replacement(node)).getExpression() + "");
        return true;
      }
    });
  }
}
