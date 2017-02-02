package il.org.spartan.spartanizer.java.namespace;

import static il.org.spartan.Utils.*;
import static il.org.spartan.spartanizer.java.namespace.definition.Kind.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.java.namespace.definition.*;
import org.jetbrains.annotations.NotNull;

/** Dictionary with a parent. Insertions go the current node, searches start at
 * the current node and delegate to the parent unless it is null.
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2016-12-28 */
public final class Namespace implements Environment {
  private final List<Namespace> children = new ArrayList<>();
  public final Map<String, Binding> flat = new LinkedHashMap<>();
  public final String name;
  public final Environment nest;

  Namespace(final Environment environment) {
    this(environment, "");
  }

  public Namespace(final Environment nest, final String name) {
    this.nest = nest;
    this.name = name;
  }

  Namespace addAll(final List<BodyDeclaration> ¢) {
    ¢.forEach(this::put);
    return this;
  }

  protected Namespace addAllReources(final List<VariableDeclarationExpression> ¢) {
    ¢.forEach(this::put);
    return this;
  }

  Namespace addChild(final Namespace child) {
    children.add(child);
    return child;
  }

  public Namespace getChild(final int ¢) {
    return children.get(¢);
  }

  protected Namespace addConstants(final EnumDeclaration d, final List<EnumConstantDeclaration> ds) {
    @knows("¢") final type t = type.bring(d.getName() + "");
    ds.forEach(λ -> put(step.name(λ) + "", new Binding(t)));
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
        : ":\n" + separate.these(children.stream().map(λ -> λ.description(indent + "  ")).toArray()).by("\n" + indent + "- "));
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

  @NotNull static Namespace spawnFor(final Namespace $, final ForStatement s) {
    final VariableDeclarationExpression x = az.variableDeclarationExpression(s);
    return s == null || x == null ? $ : $.spawn(for¢).put(x);
  }

  @NotNull static Namespace spawnEnhancedFor(final Namespace n, final EnhancedForStatement s) {
    return s == null ? n : n.spawn(foreach).put(s.getParameter());
  }

  boolean fillScope(final ASTNode root) {
    if (root == null)
      return false;
    property.attach(this).to(root);
    root.accept(new ASTVisitor() {
      @Override public boolean visit(final AnnotationTypeDeclaration ¢) {
        return ¢ == root || spawn(annotation, identifier(¢)).put(bodyDeclarations(¢)).fillScope(¢);
      }

      @Override public boolean visit(final AnonymousClassDeclaration ¢) {
        return ¢ == root || spawn(class¢).fillScope(¢);
      }

      @Override public boolean visit(final Block b) {
        Namespace n = Namespace.this;
        for (final Statement s : statements(b)) {
          if (iz.tryStatement(s)) {
            spawnAndFill(n, az.tryStatement(s));
            continue;
          }
          if (iz.forStatement(s)) {
            spawnFor(n, az.forStatement(s)).fillScope(s);
            continue;
          }
          if (iz.enhancedFor(s)) {
            spawnEnhancedFor(n, az.enhancedFor(s)).fillScope(s);
            continue;
          }
          if (iz.typeDeclaration(s)) {
            final TypeDeclaration d = az.typeDeclaration(s);
            n.spawn(definition.kind(d)).put(d).fillScope(s);
          }
          if (iz.variableDeclarationStatement(s)) {
            final VariableDeclarationStatement vds = az.variableDeclarationStatement(s);
            n = n.spawn(local);
            for (final VariableDeclarationFragment ¢ : fragments(vds))
              n.put(step.name(¢), type(vds));
          }
          n.fillScope(s);
        }
        return false;
      }

      @Override public boolean visit(final CatchClause ¢) {
        return ¢ == root || spawn(catch¢).put(exception(¢)).fillScope(¢);
      }

      @Override public boolean visit(final CompilationUnit ¢) {
        put(types(¢));
        return true;
      }

      @Override public boolean visit(final EnhancedForStatement ¢) {
        return ¢ == root || spawnEnhancedFor(Namespace.this, ¢).fillScope(¢);
      }

      @Override public boolean visit(final EnumDeclaration ¢) {
        return ¢ == root || spawn("enum " + step.name(¢)).addAll(bodyDeclarations(¢)).addConstants(¢, enumConstants(¢)).fillScope(¢);
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
        final Namespace $ = spawn(lambda + " ");
        for (final VariableDeclaration ¢ : parameters(x))
          if (iz.singleVariableDeclaration(¢))
            $.put(az.singleVariableDeclaration(¢));
          else
            $.put(step.name(az.variableDeclrationFragment(¢)), null);
        return $.fillScope(x);
      }

      @Override public boolean visit(final MethodDeclaration ¢) {
        if (¢ == root)
          return true;
        final Namespace $ = spawn("method " + step.name(¢));
        parameters(¢).forEach($::put);
        return $.fillScope(¢);
      }

      @Override public boolean visit(final TryStatement ¢) {
        if (¢ == root)
          return true;
        spawnAndFill(Namespace.this, ¢);
        return false;
      }

      @Override public boolean visit(final TypeDeclaration ¢) {
        return ¢ == root || spawn((!¢.isInterface() ? "class" : "interface") + " " + step.name(¢)).addAll(bodyDeclarations(¢)).fillScope(¢);
      }
    });
    return false;
  }

  static Namespace spawnAndFill(final Namespace n, final TryStatement s) {
    if (s == null)
      return n;
    catchClauses(s).forEach(λ -> n.spawn(catch¢).put(exception(λ)).fillScope(λ));
    n.fillScope(s.getFinally());
    final Namespace $ = n.spawn(try¢);
    resources(s).forEach($::put);
    $.fillScope(body(s));
    resources(s).forEach($::fillScope);
    return $;
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
    return put("type " + step.name(¢), step.name(¢));
  }

  private Namespace put(final AnnotationTypeMemberDeclaration ¢) {
    return put("annotation member" + step.name(¢), ¢.getType());
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
    return put("type " + step.name(¢), step.name(¢));
  }

  private Namespace put(final FieldDeclaration d) {
    fragments(d).forEach(λ -> put(step.name(λ), d.getType()));
    return this;
  }

  protected Namespace put(final List<? extends BodyDeclaration> ¢) {
    ¢.forEach(this::put);
    return this;
  }

  private Namespace put(final MethodDeclaration ¢) {
    return put(step.name(¢) + "/" + ¢.parameters().size(), ¢.getReturnType2());
  }

  Namespace put(final SimpleName key, final Type t) {
    return put(key + "", t);
  }

  protected Namespace put(final SingleVariableDeclaration ¢) {
    return put(step.name(¢), ¢.getType());
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
    put(key, new Binding(key, type.baptize(trivia.condense(t))));
    return this;
  }

  protected Namespace put(final TypeDeclaration ¢) {
    @knows("¢") final String key = "type " + step.name(¢);
    put(key, new Binding(key, type.baptize(step.name(¢) + "", !iz.interface¢(¢) ? "class" : "interface")));
    return this;
  }

  protected Namespace put(final VariableDeclarationExpression x) {
    fragments(x).forEach(λ -> put(step.name(λ), type(x)));
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
    children.forEach(n::fillScope);
    return false;
  }

  public boolean allows(final String identifier) {
    return has(identifier) || children.stream().anyMatch(λ -> λ.allows(identifier));
  }

  public static Iterable<String> namesGenerator(final SimpleType t) {
    return () -> new Iterator<String>() {
      final String base = namer.variableName(t);
      int n = -1;

      @Override public String next() {
        return ++n == 0 ? base : base + n;
      }

      @Override public boolean hasNext() {
        return true;
      }
    };
  }

  public String generateName(final Type ¢) {
    final String face = namer.shorten(¢);
    int postface = 0;
    String $ = face + "" + ++postface;
    while (has($))
      $ = face + "" + ++postface;
    return $;
  }

  public String generateName(final String ¢) {
    final String face = ¢;
    int postface = 0;
    String $ = face + "" + ++postface;
    while (has($))
      $ = face + "" + ++postface;
    return $;
  }

  public Namespace addNewName(final String s, final Type t) {
    return put(s, t);
  }

  public boolean hasChildren() {
    return !children.isEmpty();
  }

  public boolean allowsCurrentRecursive() {
    for (final String key : flat.keySet())
      if (isVariable(key) && !in(key, namer.specials))
        return false;
    return children.stream().allMatch(Namespace::allowsCurrentRecursive);
  }

  private static boolean isVariable(final String key) {
    return !key.contains(" ") && !key.contains("/");
  }
}