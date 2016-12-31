package il.org.spartan.spartanizer.java.namespace;

import static il.org.spartan.spartanizer.java.namespace.definition.Kind.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.java.namespace.definition.*;

/** Dictionary with a parent. Insertions go the current node, searches start at
 * the current note and Delegate to the parent unless it is null.
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2016-12-28 */
final class Namespace implements Environment {
  Namespace(final Environment environment) {
    this(environment, "");
  }

  public Namespace(final Environment nest, final String name) {
    this.nest = nest;
    this.name = name;
  }

  private final List<Namespace> children = new ArrayList<>();
  public final Map<String, Binding> flat = new LinkedHashMap<>();
  public final String name;
  public final Environment nest;

  private Namespace put(final AnnotationTypeDeclaration ¢) {
    return put("type " + ¢.getName(), ¢.getName());
  }
  @SuppressWarnings({}) protected Namespace put(final BodyDeclaration ¢) {
    return iz.methodDeclaration(¢) ? put(az.methodDeclaration(¢))
        : iz.fieldDeclaration(¢) ? put(az.fieldDeclaration(¢))
            : iz.enumConstantDeclaration(¢) ? put(az.enumConstantDeclaration(¢))
                : iz.annotationTypeDeclaration(¢) ? put(az.annotationTypeDeclration(¢))
                    : iz.enumDeclaration(¢) ? put(az.enumDeclaration(¢))
                    : iz.typeDeclaration(¢) ? put(az.typeDeclaration(¢))
                        : iz.annotationTypeMemberDeclaration(¢) ? put(az.annotationTypeMemberDeclaration(¢)) : null;
  }
  private Namespace put(final AnnotationTypeMemberDeclaration ¢) {
    return put(¢.getName() + "", ¢.getType()); 
  }

  private Namespace put(final EnumConstantDeclaration ¢) {
    return put(¢.getName() + "", az.enumDeclaration(¢.getParent()).getName());
  }

  private Namespace put(final EnumDeclaration ¢) {
    return put("type " + ¢.getName(), ¢.getName());
  }

  private Namespace put(final FieldDeclaration d) {
    final Type t = d.getType();
    for (final VariableDeclarationFragment ¢ : fragments(d))
      put(¢.getName(), t);
    return this;
  }

  protected Namespace put(final List<? extends BodyDeclaration> ds) {
    for (final BodyDeclaration ¢ : ds)
      put(¢);
    return this;
  }

  private Namespace put(final MethodDeclaration ¢) {
    return put(¢.getName() + "/" + ¢.parameters().size(), ¢.getReturnType2());
  }

  Namespace put(final SimpleName key, final Type t) {
    return put(key + "", t);
  }

  protected Namespace put(final SingleVariableDeclaration ¢) {
    return put(¢.getName(), ¢.getType());
  }

  private Namespace put(final String key, final ASTNode n) {
    put(key, new Binding(key, n));
    return this;
  }

  private Namespace put(final String key, final Type t) {
    put(key, new Binding(key, type.baptize(wizard.condense(t))));
    return this;
  }

  protected Namespace put(final TypeDeclaration ¢) {
    String key = "type " + ¢.getName();
    put(key, new Binding(key, type.baptize(¢.getName() + "", !¢.isInterface() ? "class" : "interface")));
    return this;
  }

  protected Namespace put(final VariableDeclarationExpression x) {
    final Type t = x.getType();
    for (final VariableDeclarationFragment ¢ : fragments(x))
      put(¢.getName(), t);
    return this;
  }

  Namespace addAll(final List<BodyDeclaration> ds) {
    for (final BodyDeclaration ¢ : ds)
      put(¢);
    return this;
  }

  protected Namespace addAllReources(final List<VariableDeclarationExpression> xs) {
    for (final VariableDeclarationExpression ¢ : xs)
      put(¢);
    return this;
  }

  Namespace addChild(final Namespace child) {
    children.add(child);
    return child;
  }

  public String description() {
    return description("");
  }
  public String description(String indent) {
    return indent + name + "" +  flat + ((children.isEmpty()) ? "" : ":\n" + separate.these(children.stream().map(x ->x.description(indent + "  ")).toArray()).by("\n" + indent + "- ")); 
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
        return ¢ == root || spawn(annotation, identifier(¢)).put(bodyDeclarations(¢)).init(¢);
      }

      @Override public boolean visit(final AnonymousClassDeclaration ¢) {
        return ¢ == root || spawn(class¢).init(¢);
      }

      @Override public boolean visit(final Block b) {
        if (b == root)
          return true;
        Namespace current = Namespace.this;
        for (final Statement s : statements(b)) {
            if (iz.typeDeclaration(s)) {
              final TypeDeclaration x = az.typeDeclaration(s);
              current = current.spawn(definition.kind(x)).put(x);
            }
            if (iz.variableDeclarationStatement(s)) {
              VariableDeclarationStatement x = az.variableDeclarationStatement(s);
              Type t = x.getType();
              current = current.spawn(local);
              for (VariableDeclarationFragment ¢ : fragments(x))
                current.put(¢.getName(), t);
          }
          current.init(s);
        }
        return false;
      }

      @Override public boolean visit(final CatchClause ¢) {
        return ¢ == root || spawn(catch¢).put(¢.getException()).init(¢);
      }

      @Override public boolean visit(final CompilationUnit ¢) {
        if (¢ != root)
          put(types(¢));
        return true;
      }

      @Override public boolean visit(final EnhancedForStatement ¢) {
        return ¢ == root || spawn(foreach).put(¢.getParameter()).init(¢);
      }

      @Override public boolean visit(final ForStatement ¢) {
        if (¢ == root)
          return true;
        final VariableDeclarationExpression $ = az.variableDeclarationExpression(¢);
        return $ == null || spawn(for¢).put($).init(¢);
      }

      @Override public boolean visit(final LambdaExpression x) {
        if (x == root)
          return true;
        final Namespace $ = spawn(lambda + "");
        for (final VariableDeclaration ¢ : parameters(x))
          if (¢ instanceof SingleVariableDeclaration)
            $.put((SingleVariableDeclaration) ¢);
          else
            $.put(az.variableDeclrationFragment(¢).getName(), null);
        return $.init(x);
      }
      @Override public boolean visit(final MethodDeclaration d) {
        if (d == root)
          return true;
        final Namespace $ = spawn("method " + d.getName());
        for (final SingleVariableDeclaration ¢ : parameters(d))
            $.put(¢);
        return $.init(d);
      }

      @Override public boolean visit(final TryStatement ¢) {
        return ¢ == root || spawn(try¢).addAllReources(resources(¢)).init(¢);
      }

      @Override public boolean visit(final TypeDeclaration ¢) {
        return ¢ == root || spawn((!¢.isInterface() ? "class" : "interface") + " " + ¢.getName()).addAll(bodyDeclarations(¢)).init(¢);
      }
    });
    return false;
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
    return spawn(¢ + "");
  }

  Namespace spawn(final Kind k, final String childName) {
    return spawn(k + " " + childName);
  }

  Namespace spawn(final String childName) {
    return addChild(new Namespace(Namespace.this, childName));
  }

  @Override public String toString() {
    return name + ": " + flat;
  }

  static boolean init(final Namespace n, final List<? extends ASTNode> children) {
    for (final ASTNode child : children)
      n.init(child);
    return false;
  }

  enum Subspace {
    METHOD, TYPE, VARIABLE
  }
}