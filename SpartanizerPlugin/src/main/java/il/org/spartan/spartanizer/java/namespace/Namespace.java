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

  protected Namespace addConstants(final EnumDeclaration d, final List<EnumConstantDeclaration> ds) {
    @knows("¢") final type t = type.bring(d.getName() + "");
    for (final EnumConstantDeclaration ¢ : ds)
      put(¢.getName() + "", new Binding(t));
    return this;
  }

  private Iterable<Environment> ancestors() {
    return () -> new Iterator<Environment>() {
      Environment next = Namespace.this;

      @Override public boolean hasNext() {
        return next != null;
      }

      @Override public Environment next() {
        final Environment $ = next;
        next = next.nest();
        return $;
      }
    };
  }

  public String ancestry() {
    return separate.these(ancestors()).by("\n\t * ");
  }

  public String description() {
    return description("");
  }

  public String description(final String indent) {
    return indent + name + "" + flat + (children.isEmpty() ? ""
        : ":\n" + separate.these(children.stream().map(x -> x.description(indent + "  ")).toArray()).by("\n" + indent + "- "));
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

  boolean fillScope(final ASTNode root) {
    return fillScope(root, root);
  }

  boolean fillScope(final ASTNode from, final ASTNode root) {
    property.attach(this).to(from);
    from.accept(new ASTVisitor() {
      @Override public boolean visit(final AnnotationTypeDeclaration ¢) {
        return ¢ == root || spawn(annotation, identifier(¢)).put(bodyDeclarations(¢)).fillScope(¢);
      }

      @Override public void preVisit(final ASTNode ¢) {
        System.out.println("visit " + wizard.trim(¢) + "/" + Namespace.this + "/" + Namespace.this.nest);
      }

      @Override public boolean visit(final AnonymousClassDeclaration ¢) {
        return ¢ == root || spawn(class¢).fillScope(¢);
      }

      @Override public boolean visit(final Block b) {
        if (b == root)
          return true;
        Namespace current = Namespace.this;
        for (final Statement s : statements(b)) {
          if (iz.typeDeclaration(s)) {
            final TypeDeclaration d = az.typeDeclaration(s);
            current = current.spawn(definition.kind(d)).put(d);
            current.fillScope(s);
            continue;
          }
          if (iz.variableDeclarationStatement(s)) {
            final VariableDeclarationStatement vds = az.variableDeclarationStatement(s);
            current = current.spawn(local);
            for (final VariableDeclarationFragment ¢ : fragments(vds))
              current.put(¢.getName(), vds.getType());
            current.fillScope(s);
            continue;
          }
          if (iz.forStatement(s))
            current.fillScope(s, null);
          if (iz.enhancedFor(s))
            current.fillScope(s, null);
          if (iz.tryStatement(s))
            current.fillScope(s, null);
          current.fillScope(s);
        }
        return false;
      }

      @Override public boolean visit(final CatchClause ¢) {
        return ¢ == root || spawn(catch¢).put(¢.getException()).fillScope(¢);
      }

      @Override public boolean visit(final CompilationUnit ¢) {
        put(types(¢));
        return true;
      }

      @Override public boolean visit(final EnhancedForStatement ¢) {
        return ¢ == root || spawn(foreach).put(¢.getParameter()).fillScope(¢);
      }

      @Override public boolean visit(final EnumDeclaration ¢) {
        return ¢ == root || spawn("enum " + ¢.getName()).addAll(bodyDeclarations(¢)).addConstants(¢, enumConstants(¢)).fillScope(¢);
      }

      @Override public boolean visit(final ForStatement ¢) {
        if (¢ == root)
          return true;
        final VariableDeclarationExpression $ = az.variableDeclarationExpression(¢);
        return $ == null || spawn(for¢).put($).fillScope(¢);
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
        return $.fillScope(x);
      }

      @Override public boolean visit(final MethodDeclaration d) {
        if (d == root)
          return true;
        final Namespace $ = spawn("method " + d.getName());
        for (final SingleVariableDeclaration ¢ : parameters(d))
          $.put(¢);
        return $.fillScope(d);
      }

      @Override public boolean visit(final TryStatement s) {
        System.out.println("visit " + wizard.trim(s));
        if (s == root)
          return true;
        final Namespace $ = spawn(try¢);
        System.out.println( "Filling scope " + $);
        System.out.println( "nest  " + $.nest());
        for (final VariableDeclarationExpression ¢ : resources(s))
          $.put(¢);
        System.out.println( "Filling scope " + $);
        System.out.println( "nest  " + $.nest());
        return $.fillScope(s.getBody(), null);
      }

      @Override public boolean visit(final TypeDeclaration ¢) {
        return ¢ == root || spawn((!¢.isInterface() ? "class" : "interface") + " " + ¢.getName()).addAll(bodyDeclarations(¢)).fillScope(¢);
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

  private Namespace put(final AnnotationTypeDeclaration ¢) {
    return put("type " + ¢.getName(), ¢.getName());
  }

  private Namespace put(final AnnotationTypeMemberDeclaration ¢) {
    return put(¢.getName() + "", ¢.getType());
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

  /** Add name to the current scope in the {@link Environment} . */
  @Override public Binding put(final String identifier, final Binding value) {
    flat.put(identifier, value);
    assert !flat.isEmpty();
    return hiding(identifier);
  }

  private Namespace put(final String key, final Type t) {
    put(key, new Binding(key, type.baptize(wizard.condense(t))));
    return this;
  }

  protected Namespace put(final TypeDeclaration ¢) {
    @knows("¢") final String key = "type " + ¢.getName();
    put(key, new Binding(key, type.baptize(¢.getName() + "", !¢.isInterface() ? "class" : "interface")));
    return this;
  }

  protected Namespace put(final VariableDeclarationExpression x) {
    final Type t = x.getType();
    for (final VariableDeclarationFragment ¢ : fragments(x))
      put(¢.getName(), t);
    return this;
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
    return name + "" + flat;
  }

  static boolean init(final Namespace n, final List<? extends ASTNode> children) {
    for (final ASTNode child : children)
      n.fillScope(child);
    return false;
  }
}