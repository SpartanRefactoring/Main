package il.org.spartan.spartanizer.java.namespace;

import static il.org.spartan.spartanizer.java.namespace.definition.Kind.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.java.namespace.definition.*;
import il.org.spartan.utils.*;

/** Dictionary with a parent. Insertions go the current node, searches start at
 * the current note and Delegate to the parent unless it is null.
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2016-12-28 */
final class Namespace implements Environment {
  Namespace(final Environment environment) {
    this(environment, "");
  }

  public Namespace(final Environment environment, final String name) {
    this.nest = environment;
    this.name = name;
  }

  public Namespace(final Namespace nest, final definition.Kind kind, final String name) {
    this.nest = nest;
    this.name = name;
    this.kind = kind;
  }

  private final List<Namespace> children = new ArrayList<>();
  public final Map<String, Binding> flat = new LinkedHashMap<>();
  definition.Kind kind = definition.Kind.interface¢;
  public final String name;
  public final Environment nest;

  protected Namespace add(AbstractTypeDeclaration ¢) {
    return add(bodyDeclarations(¢));
  }

  protected Namespace add(AnonymousClassDeclaration ¢) {
    return add(bodyDeclarations(¢));
  }

  @SuppressWarnings({ "static-method", "unused" }) protected Namespace add(BodyDeclaration ¢) {
    // TODO Yossi: compilation error
    // return add(definition.kind(x))
    return null;
  }

  protected Namespace add(List<? extends BodyDeclaration> ds) {
    for (BodyDeclaration ¢ : ds)
      add(¢);
    return null;
  }

  protected Namespace add(VariableDeclaration ¢) {
    ___.______unused(¢);
    return this;
  }

  protected Namespace add(VariableDeclarationExpression ¢) {
    ___.______unused(¢);
    return this;
  }

  Namespace addChild(final Namespace child) {
    children.add(child);
    return child;
  }

  /** @return <code><b>true</b></code> <em>iff</em> {@link Environment} is
   *         empty. */
  @Override public boolean empty() {
    return flat.isEmpty() && nest.empty();
  }

  /** @return Map entries used in the current scope. */
  @Override public List<Map.Entry<String, Binding>> entries() {
    return new ArrayList<>(flat.entrySet());
  }

  /** @return The information about the name in current {@link Environment} */
  @Override public Binding get(final String identifier) {
    final Binding $ = flat.get(identifier);
    return $ != null ? $ : nest.get(identifier);
  }

  /** Check whether the {@link Environment} already has the name. */
  @Override public boolean has(final String identifier) {
    return flat.containsKey(identifier) || nest.has(identifier);
  }

  boolean init(final ASTNode root) {
    property.attach(this).to(root);
    root.accept(new ASTVisitor() {
      @Override public boolean visit(final AnnotationTypeDeclaration ¢) {
        return spawn(annotation, identifier(¢)).add(bodyDeclarations(¢)).init(¢);
      }

      @Override public boolean visit(final AnonymousClassDeclaration ¢) {
        return spawn(anonymous).add(¢).init(¢);
      }

      @Override public boolean visit(final Block b) {
        Namespace current = Namespace.this;
        for (final Statement s : statements(b)) {
          if (iz.variableDeclarationStatement(s)) {
            @SuppressWarnings("unused") final VariableDeclarationStatement x = az.variableDeclarationStatement(s);
            current = current.spawn(local);
            // TODO Yossi: compilation error
            // current.add(x);
          } else if (iz.typeDeclaration(s)) {
            final TypeDeclaration x = az.typeDeclaration(s);
            current = current.spawn(definition.kind(x));
            current.add(x);
            continue;
          }
          current.init(s);
        }
        return false;
      }

      @Override public boolean visit(final CatchClause ¢) {
        return spawn(catch¢).add(¢.getException()).init(¢);
      }

      @Override public boolean visit(final CompilationUnit ¢) {
        add(types(¢));
        return true;
      }

      @Override public boolean visit(final EnhancedForStatement ¢) {
        return spawn(foreach).add(¢.getParameter()).init(¢);
      }

      @Override public boolean visit(final ForStatement ¢) {
        VariableDeclarationExpression $ = az.variableDeclarationExpression(¢);
        return $ == null || spawn(for¢).add($).init(¢);
      }

      /**
       * [[SuppressWarningsSpartan]]
       */
      @Override public boolean visit(final LambdaExpression ¢) {
        Namespace $ = spawn(lambda);
        for (VariableDeclaration d : parameters(¢))
          $.add(d);
        return $.init(¢);
      }

      @Override public boolean visit(final TryStatement s) {
        Namespace $ = spawn(try¢);
        for (VariableDeclarationExpression ¢ : step.resources(s))
          $.add(¢);
        return $.init(s);
      }

      @Override public boolean visit(final TypeDeclaration node) {
        return init(spawn(lambda, ""), bodyDeclarations(node));
      }
    });
    return false;
  }

  @SuppressWarnings("unused") protected void xadd(VariableDeclarationStatement x) {
    //
  }

  /** @return names used the {@link Environment} . */
  @Override public LinkedHashSet<String> keys() {
    return new LinkedHashSet<>(flat.keySet());
  }

  @Override public String name() {
    return name;
  }

  /** One step up in the {@link Environment} tree. Funny but it even sounds like
   * next(). */
  @Override public Environment nest() {
    return nest;
  }

  /** Add name to the current scope in the {@link Environment} . */
  @Override public Binding put(final String identifier, final Binding value) {
    flat.put(identifier, value);
    assert !flat.isEmpty();
    return hiding(identifier);
  }

  @Override public int size() {
    return flat.size();
  }

  @Override public Namespace spawn() {
    return addChild(new Namespace(this));
  }

  Namespace spawn(final Kind ¢) {
    return spawn(¢, "");
  }

  Namespace spawn(final Kind k, final String childName) {
    return addChild(new Namespace(Namespace.this, k, childName));
  }

  static boolean init(final Namespace n, final List<? extends ASTNode> children) {
    for (final ASTNode child : children)
      n.init(child);
    return false;
  }
}