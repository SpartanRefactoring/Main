package il.org.spartan.java.cfg;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.java.cfg.CFG.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.utils.*;

/** Testing utilities for {@link CFGTest}.
 * @author Ori Roth
 * @since 2017-07-06 */
public class CFGTestUtil {
  public static IOAble cfg(final String code) {
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(WrapIntoComilationUnit.find(code).on(code));
    return new IOAble() {
      final IOAble self = this;

      @Override public Contains outs(String n1) {
        return new Contains(n1) {
          @Override public Nodes nodes(ASTNode n) {
            return CFG.out(n);
          }
        };
      }
      @Override public Contains ins(String n1) {
        return new Contains(n1) {
          @Override public Nodes nodes(ASTNode n) {
            return CFG.in(n);
          }
        };
      }

      abstract class Contains implements CFGTestUtil.Contains {
        private String n1;

        public Contains(final String n1) {
          this.n1 = n1;
        }
        public abstract Nodes nodes(final ASTNode ns);
        @Override public IOAble contains(String n2) {
          final ASTNode a1 = find(u, n1);
          assert a1 != null : "\nproblem in finding node\n" + tide.clean(n1) + "\nin compilation unit\n" + tide.clean(u.toString());
          final ASTNode a2 = find(u, n2);
          assert a1 != null : "\nproblem in finding node\n" + tide.clean(n2) + "\nin compilation unit\n" + tide.clean(u.toString());
          final Nodes ns = nodes(a1);
          assert ns != null : "\nnull nodes of\n" + tide.clean(n1);
          assert ns.contains(a2) : "\nnodes of\n" + tide.clean(n1) + "\ndoes not contain\n" + tide.clean(n2);
          return self;
        }
      }
    };
  }

  public interface IOAble {
    Contains outs(final String s);
    Contains ins(final String s);
  }

  public interface Contains {
    IOAble contains(final String s);
  }

  static ASTNode find(final ASTNode root, final String code) {
    final String tidy = tide.clean(code);
    Wrapper<ASTNode> $ = new Wrapper<>();
    root.accept(new ASTVisitor() {
      @Override public void preVisit(ASTNode n) {
        if (tide.clean(n.toString()).equals(tidy))
          $.set(n);
      }
    });
    return $.get();
  }
}
