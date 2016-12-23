package il.org.spartan.athenizer.expanders;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.athenizer.inflate.expanders.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** @author Raviv Rachmiel
 * @since 8-12-2016 */
@SuppressWarnings("static-method")
public class TernaryExpanderTests {
  @Test public void basicCanTip() {
    wizard.ast("return a==0? 2:3;").accept(new ASTVisitor() {
      @Override public boolean visit(final ReturnStatement node) {
        assert new ReturnTernaryExpander().canTip(node);
        return true;
      }
    });
  }

  @Test public void basicCanTip2() {
    wizard.ast("a = a==0? 2:3;").accept(new ASTVisitor() {
      @Override public boolean visit(final ReturnStatement node) {
        assert new ReturnTernaryExpander().canTip(node);
        return true;
      }
    });
  }

  @Test public void basicCanTip3() {
    wizard.ast("int b = a==0? 2:3;").accept(new ASTVisitor() {
      @Override public boolean visit(final ReturnStatement node) {
        assert new ReturnTernaryExpander().canTip(node);
        return true;
      }
    });
  }

  @Test public void nestedCanTip() {
    wizard.ast("a = a==0? (b==2? 4: 5 ):3;").accept(new ASTVisitor() {
      @Override public boolean visit(final ReturnStatement node) {
        assert new ReturnTernaryExpander().canTip(node);
        return true;
      }
    });
  }

  @Test public void appCanTip() {
    wizard.ast("a = (a==0? (b==2? 4: 5 ):3);").accept(new ASTVisitor() {
      @Override public boolean visit(final ReturnStatement node) {
        assert new ReturnTernaryExpander().canTip(node);
        return true;
      }
    });
  }

  @Test public void basicTest() {
    wizard.ast("return a==0? 1:2;").accept(new ASTVisitor() {
      @Override public boolean visit(final ReturnStatement node) {
        assert new ReturnTernaryExpander().replacement(node) instanceof IfStatement;
        return true;
      }
    });
  }

  @Test public void basicTest2() {
    wizard.ast("a = a==0? 1:2;").accept(new ASTVisitor() {
      @Override public boolean visit(final ReturnStatement node) {
        assert new ReturnTernaryExpander().replacement(node) instanceof IfStatement;
        return true;
      }
    });
  }

  @Test public void basicTest3() {
    wizard.ast("int a = a==0? 1:2;").accept(new ASTVisitor() {
      @Override public boolean visit(final ReturnStatement node) {
        assert new ReturnTernaryExpander().replacement(node) instanceof IfStatement;
        return true;
      }
    });
  }

  @Test public void nestedTest() {
    wizard.ast("a = b==0? (a==0? 1:2) : 4;").accept(new ASTVisitor() {
      @Override public boolean visit(final ReturnStatement node) {
        assert new ReturnTernaryExpander().replacement(node) instanceof IfStatement;
        return true;
      }
    });
  }

  @Test public void returnTest() {
    wizard.ast("return a==0? 1:2;").accept(new ASTVisitor() {
      @Override public boolean visit(final ReturnStatement node) {
        assert new ReturnTernaryExpander().replacement(node) instanceof IfStatement;
        assert "a == 0".equals(az.ifStatement(new ReturnTernaryExpander().replacement(node)).getExpression() + "");
        return true;
      }
    });
  }
}
