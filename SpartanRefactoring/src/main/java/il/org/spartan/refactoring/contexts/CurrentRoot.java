package il.org.spartan.refactoring.contexts;

import static il.org.spartan.azzert.*;
import static il.org.spartan.lazy.Environment.*;

import org.eclipse.jdt.annotation.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.lazy.*;
import il.org.spartan.lazy.Environment.*;
import il.org.spartan.refactoring.contexts.Described.Monitored;

@SuppressWarnings("javadoc") //
public abstract class CurrentRoot extends Described.Monitored.¢ implements CurrentParser {
  public static CurrentRoot fromCompilationUnit(final Monitored m, final Property<ICompilationUnit> u) {
    return new CurrentRoot(m) {
      // @formatter:off
      final Property<@Nullable ASTParser> parser = super.fromICompilationUnit(u);
      final Property<@Nullable ASTNode> root = bind((final ASTParser p) -> p.createAST(¢.progressMonitor())).to(parser);
      @Override public Property<ASTParser> parser() { return parser; }
      @Override public Property<ASTNode> root() { return root; }
      // @formatter:on
    };
  }
  public static CurrentRoot fromCharArray(final Monitored m, final Property<char[]> cs) {
    return new CurrentRoot(m) {
      // @formatter:off
      Property<@Nullable ASTParser> parser = super.fromCharArray(cs);
      Property<@Nullable ASTNode> root = bind((final ASTParser p) -> p.createAST(¢.progressMonitor())).to(parser);
      @Override public Property<ASTParser> parser() { return parser; }
      @Override public Property<ASTNode> root() { return root; }
      // @formatter:on
    };
  }
  CurrentRoot(final Monitored m) {
    m.super();
  }
  @Override public abstract Property<ASTParser> parser();
  public abstract Property<ASTNode> root();
  /** Inner class, inheriting all of its container's {@link Property}s, and
   * possibly adding some of its own. Access to container's c {@link Property}
   * is through the {@link #¢} variable.
   * <p>
   * Clients extend this class to create more specialized contexts, adding more
   * @see {@link Environment#undefined()}
   * @see {@link Environment#function()}
   * @see {@link Environment#bind(Function1)}
   * @see {@link Environment#bind(Function2)}
   * @see {@link Environment#bind(Function3)}
   * @see {@link Environment#bind(Function4)}
   * @since 2016` 
   * @author Yossi Gil
   * */
  public abstract class ¢ {
    /** the containing instance */
    @SuppressWarnings("hiding") public final CurrentRoot ¢ = CurrentRoot.this;
  }
  @SuppressWarnings("static-method") public static class __META {
    public static class TEST {
      @Test public void sessionA01() {
        final Described.Monitored m = new Described().new Monitored();
        final Property<char[]> cs = value("package a.b;".toCharArray());
        final CurrentRoot a = fromCharArray(m, cs);
        azzert.notNull(a);
        azzert.that(a.root(), instanceOf(Property.class));
        azzert.that(a.root().¢(), instanceOf(CompilationUnit.class));
      }
    }
  }
  
}
