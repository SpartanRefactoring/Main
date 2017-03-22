package il.org.spartan.spartanizer.java.namespace;

import static il.org.spartan.Utils.*;
import static il.org.spartan.spartanizer.java.namespace.definition.Kind.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.java.namespace.definition.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Dictionary with a parent. Insertions go the current node, searches start at
 * the current node and delegate to the parent unless it is null.
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2016-12-28 */
public final class Namespace implements Environment {
  private final List<Namespace> children = new ArrayList<>();
  public final Map<String, Binding> flat = new LinkedHashMap<>();
  public final String name;
  public final Environment nest;

  /** A constructor which get only the environment without any name */
  Namespace(final Environment environment) {
    this(environment, "");
  }

  /** A constructor which get only the environment and a name of the
   * NameSpace */
  public Namespace(final Environment nest, final String name) {
    this.nest = nest;
    this.name = name;
  }

  /** add all the given itrable of BodyDeclarations to our NameSpace */
  @NotNull Namespace addAll(@NotNull final Iterable<BodyDeclaration> ¢) {
    ¢.forEach(this::put);
    return this;
  }

  @NotNull
  protected Namespace addAllReources(@NotNull final Iterable<VariableDeclarationExpression> ¢) {
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

  @NotNull
  protected Namespace addConstants(@NotNull final EnumDeclaration d, @NotNull final Iterable<EnumConstantDeclaration> ds) {
    @knows("¢") final type t = type.bring(d.getName() + "");
    ds.forEach(λ -> put(step.name(λ) + "", new Binding(t)));
    return this;
  }

  private Iterable<Environment> ancestors() {
    return () -> new Iterator<Environment>() {
      @Nullable Environment next = Namespace.this;

      @Override public boolean hasNext() {
        return next != null;
      }

      @Nullable
      @Override public Environment next() {
        @Nullable final Environment $ = next;
        next = next.nest();
        return $;
      }
    };
  }

  public String ancestry() {
    return separate.these(ancestors()).by("\n\t * ");
  }

  @NotNull
  public String description() {
    return description("");
  }

  @NotNull
  public String description(final String indent) {
    return indent + name + "" + flat + (children.isEmpty() ? ""
        : ":\n" + separate.these(children.stream().map(λ -> λ.description(indent + "  ")).toArray()).by("\n" + indent + "- "));
  }

  /** @return whether {@link Environment} is empty. */
  @Override public boolean empty() {
    return flat.isEmpty() && nest.empty();
  }

  /** @return Map entries used in the current scope. */
  @NotNull
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

  @NotNull
  static Namespace spawnFor(@NotNull final Namespace $, @Nullable final ForStatement s) {
    final VariableDeclarationExpression x = az.variableDeclarationExpression(s);
    return s == null || x == null ? $ : $.spawn(for¢).put(x);
  }

  @NotNull
  static Namespace spawnEnhancedFor(@NotNull final Namespace n, @Nullable final EnhancedForStatement s) {
    return s == null ? n : n.spawn(foreach).put(s.getParameter());
  }

  boolean fillScope(@Nullable final ASTNode root) {
    if (root == null)
      return false;
    property.attach(this).to(root);
    // noinspection SameReturnValue,SameReturnValue
    root.accept(new ASTVisitor(true) {
      @Override public boolean visit(@NotNull final AnnotationTypeDeclaration ¢) {
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
            @Nullable final TypeDeclaration d = az.typeDeclaration(s);
            n.spawn(definition.kind(d)).put(d).fillScope(s);
          }
          if (iz.variableDeclarationStatement(s)) {
            @Nullable final VariableDeclarationStatement vds = az.variableDeclarationStatement(s);
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

      @Override public boolean visit(@NotNull final EnumDeclaration ¢) {
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

      @Override public boolean visit(@NotNull final TypeDeclaration ¢) {
        return ¢ == root || spawn((!¢.isInterface() ? "class" : "interface") + " " + step.name(¢)).addAll(bodyDeclarations(¢)).fillScope(¢);
      }
    });
    return false;
  }

  static Namespace spawnAndFill(@NotNull final Namespace n, @Nullable final TryStatement s) {
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
  @NotNull
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

  private Namespace put(@NotNull final AnnotationTypeMemberDeclaration ¢) {
    return put("annotation member" + step.name(¢), ¢.getType());
  }

  /** Add to the NameSpace a new name according to its type, we habdle each kind
   * of node with a different function */
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

  @NotNull
  private Namespace put(@NotNull final FieldDeclaration d) {
    fragments(d).forEach(λ -> put(step.name(λ), d.getType()));
    return this;
  }

  @NotNull
  protected Namespace put(@NotNull final Iterable<? extends BodyDeclaration> ¢) {
    ¢.forEach(this::put);
    return this;
  }

  private Namespace put(@NotNull final MethodDeclaration ¢) {
    return put(step.name(¢) + "/" + ¢.parameters().size(), ¢.getReturnType2());
  }

  @NotNull Namespace put(final SimpleName key, final Type t) {
    return put(key + "", t);
  }

  protected Namespace put(@NotNull final SingleVariableDeclaration ¢) {
    return put(step.name(¢), ¢.getType());
  }

  @NotNull
  private Namespace put(final String key, final ASTNode n) {
    put(key, new Binding(key, n));
    return this;
  }

  /** Add name to the current scope in the {@link Environment} . */
  @Nullable
  @Override public Binding put(final String identifier, final Binding value) {
    flat.put(identifier, value);
    assert !flat.isEmpty();
    return hiding(identifier);
  }

  @NotNull
  private Namespace put(final String key, final Type t) {
    put(key, new Binding(key, type.baptize(trivia.condense(t))));
    return this;
  }

  @NotNull
  protected Namespace put(@NotNull final TypeDeclaration ¢) {
    @NotNull @knows("¢") final String key = "type " + step.name(¢);
    put(key, new Binding(key, type.baptize(step.name(¢) + "", !iz.interface¢(¢) ? "class" : "interface")));
    return this;
  }

  @NotNull
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
    return addChild(new Namespace(this, childName));
  }

  @NotNull
  @Override public String toString() {
    return name + "" + flat;
  }

  static boolean init(@NotNull final Namespace n, @NotNull final Iterable<? extends ASTNode> children) {
    children.forEach(n::fillScope);
    return false;
  }

  public boolean allows(final String identifier) {
    return has(identifier) || children.stream().anyMatch(λ -> λ.allows(identifier));
  }

  public static Iterable<String> namesGenerator(@NotNull final SimpleType t) {
    return () -> new Iterator<String>() {
      final String base = namer.variableName(t);
      int n = -1;

      @NotNull
      @Override public String next() {
        return ++n == 0 ? base : base + n;
      }

      @Override public boolean hasNext() {
        return true;
      }
    };
  }

  @NotNull
  public String generateName(final Type ¢) {
    return generateName(namer.shorten(¢));
  }

  @NotNull
  public String generateName(final String ¢) {
    int postface = 0;
    @NotNull String $ = ¢ + "" + ++postface;
    while (has($))
      $ = ¢ + "" + ++postface;
    return $;
  }

  @NotNull
  public Namespace addNewName(final String s, final Type t) {
    return put(s, t);
  }

  public boolean hasChildren() {
    return !children.isEmpty();
  }

  public boolean allowsCurrentRecursive() {
    return flat.keySet().stream().noneMatch(λ -> isVariable(λ) && !in(λ, namer.specials))
        && children.stream().allMatch(Namespace::allowsCurrentRecursive);
  }

  private static boolean isVariable(@NotNull final String key) {
    return !key.contains(" ") && !key.contains("/");
  }
}