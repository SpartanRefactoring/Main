package il.org.spartan.spartanizer.utils.tdd;

import static java.util.stream.Collectors.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** TODO: Ori Marcovitch please add a description
 * @author Ori Marcovitch
 * @author Dor Ma'ayan
 * @author Raviv Rachmiel
 * @author Kfir Marx
 * @author Omri Ben- Shmuel
 * @author Ward Mattar
 * @author Vivian Shehadeh
 * @since Oct 31, 2016 */
public enum getAll {
  ;
  /** Get all the parameters that the method invocation depends on
   * @author Vivian Shehadeh
   * @author Ward Mattar
   * @param ¢ is a MethodInvocation
   * @return List of the names of the methods */
  @Nullable public static Set<String> invocations(@Nullable final MethodInvocation ¢) {
    if (¢ == null)
      return null;
    @NotNull final Set<String> $ = new TreeSet<>();
    // noinspection SameReturnValue
    ¢.accept(new ASTVisitor(true) {
      @Override public boolean visit(final SimpleName ¢¢) {
        if ((!iz.methodInvocation(parent(¢¢)) || !(step.name(az.methodInvocation(parent(¢¢))) + "").equals(¢¢ + ""))
            && (!iz.methodDeclaration(parent(¢¢)) || !(step.name(az.methodDeclaration(parent(¢¢))) + "").equals(¢¢ + "")))
          $.add(¢¢ + "");
        return true;
      }
    });
    return $;
  }

  /** Get all the methods invoked in m
   * @author Dor Ma'ayan
   * @param d JD
   * @return List of the names of the methods */
  @Nullable public static Set<String> invocations(@Nullable final MethodDeclaration ¢) {
    if (¢ == null)
      return null;
    @NotNull final Set<String> $ = new TreeSet<>();
    if (statements(body(¢)).isEmpty())
      return $;
    // noinspection SameReturnValue
    ¢.accept(new ASTVisitor(true) {
      @Override public boolean visit(@NotNull final MethodInvocation ¢¢) {
        $.add(¢¢.getName() + "");
        return true;
      }
    });
    return $;
  }

  /** Get list of names in a Block
   * @author Raviv Rachmiel
   * @author Kfir Marx
   * @param ¢ Block
   * @return List of the names in the block */
  @Nullable public static List<Name> names(@Nullable final Block b) {
    if (b == null)
      return null;
    @NotNull final List<Name> $ = new ArrayList<>();
    // noinspection SameReturnValue
    b.accept(new ASTVisitor(true) {
      @Override public boolean visit(final SimpleName ¢) {
        $.add(¢);
        return true;
      }
    });
    return $;
  }

  /** returns a list of all instances of expressions at given method
   * @author Koby Ben Shimol
   * @author Yuval Simon
   * @since 16-11-01 */
  @Nullable public static List<InstanceofExpression> instanceofs(@Nullable final MethodDeclaration d) {
    if (d == null)
      return null;
    @NotNull final List<InstanceofExpression> $ = new ArrayList<>();
    // noinspection SameReturnValue
    d.accept(new ASTVisitor(true) {
      @Override public boolean visit(final InstanceofExpression node) {
        $.add(node);
        return true;
      }
    });
    return $;
  }

  /** Takes a single parameter d, which is a MethodDeclaration. Returns a a
   * {@Link List} of {@link CastExpression} which is all casts in d.
   * @param d a MethodDeclaration
   * @author Inbal Matityahu
   * @author Or Troyaner
   * @author Tom Nof */
  @Nullable public static Collection<CastExpression> casts(@Nullable final MethodDeclaration d) {
    if (d == null)
      return null;
    @NotNull final Collection<CastExpression> $ = new ArrayList<>();
    // noinspection SameReturnValue
    d.accept(new ASTVisitor(true) {
      @Override public boolean visit(final CastExpression node) {
        $.add(node);
        return true;
      }
    });
    return $;
  }

  /** Takes a single parameter, which is an MethodDeclaration return a
   * List<VariableDeclaration> which is all String variable declarations in m
   * @param d a MethodDeclaration
   * @author Alexander Kaplan
   * @author Ariel Kolikant */
  @Nullable public static List<VariableDeclaration> stringVariables(@Nullable final MethodDeclaration d) {
    if (d == null)
      return null;
    @NotNull final List<VariableDeclaration> $ = new ArrayList<>();
    d.accept(new ASTVisitor(true) {
      @Override public void preVisit(final ASTNode ¢) {
        if (¢ instanceof SingleVariableDeclaration && "String".equals(((SingleVariableDeclaration) ¢).getType() + ""))
          $.add((VariableDeclaration) ¢);
        super.preVisit(¢);
      }
    });
    return $;
  }

  /** Takes a single parameter, which is a TypeDecleration returns a list of
   * public fields for this class (by fields' names)
   * @param ¢ TypeDecleration
   * @author Inbal Zukerman
   * @author Elia Traore */
  @Nullable public static List<String> publicFields(@Nullable final TypeDeclaration ¢) {
    if (¢ == null)
      return null;
    @NotNull final List<String> $ = new ArrayList<>();
    ¢.accept(publicFieldsCollector($));
    return $;
  }

  @NotNull private static ASTVisitor publicFieldsCollector(@NotNull final Collection<String> $) {
    // noinspection SameReturnValue
    return new ASTVisitor(true) {
      @Override public boolean visit(@NotNull final FieldDeclaration d) {
        if (iz.public¢(d))
          $.addAll(fragments(d).stream().map(λ -> step.name(λ) + "").collect(toList()));
        return true;
      }
    };
  }

  /** Takes a single CompilationUnit parameter, returns a list of method
   * declaration within that compilation unit
   * @param CompilationUnit
   * @author RoeiRaz
   * @author RoeyMaor */
  @Nullable public static List<MethodDeclaration> methods(@Nullable final CompilationUnit u) {
    if (u == null)
      return null;
    @NotNull final List<MethodDeclaration> $ = new ArrayList<>();
    u.accept(new ASTVisitor(true) {
      @Override public boolean visit(final MethodDeclaration ¢) {
        $.add(¢);
        return super.visit(¢);
      }
    });
    return $;
  }

  /** takes a single parameter, which is a TypeDeclaration. returns a list of
   * private fields for this class (by fields' names)
   * @param TypeDeclaration
   * @author yonzarecki
   * @author rodedzats
   * @author zivizhar */
  @NotNull public static List<String> privateFields(@Nullable final TypeDeclaration d) {
    @NotNull final List<String> $ = new ArrayList<>();
    if (d == null)
      return $;
    // noinspection SameReturnValue
    d.accept(new ASTVisitor(true) { // traverse all FieldDeclaration
      @Override public boolean visit(@NotNull final FieldDeclaration current) {
        if (current.getModifiers() == Modifier.PRIVATE)
          $.addAll(fragments(current).stream().map(λ -> λ.getName().getIdentifier()).collect(toList()));
        return true;
      }
    });
    return $;
  }
}
