package il.org.spartan.spartanizer.utils.tdd;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.safety.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** TODO: Ori Marcovitch please add a description
 * @author Ori Marcovitch
 * @author Moshe ELiasof
 * @author Netanel Felcher
 * @author Doron Meshulam
 * @author Tomer Dragucki
 * @author Shimon Azulay
 * @author Idan Atias
 * @since Nov 8, 2016 */
public enum getAll2 {
  ;
  // For you to implement! Let's TDD and get it on!
  /** takes a single parameter, which is a CompilationUnit returns a list of
   * methods in cu.
   * @param u CompilationUnit
   * @author Moshe Eliasof
   * @author Netanel Felcher */
  @Nullable
  public static List<MethodDeclaration> methods(@Nullable final CompilationUnit u) {
    if (u == null)
      return null;
    final List<MethodDeclaration> $ = new ArrayList<>();
    u.accept(new ASTVisitor() {
      @Override public boolean visit(final MethodDeclaration ¢) {
        $.add(¢);
        return super.visit(¢);
      }
    });
    return $;
  }

  /** Takes Block b and returns list of names in it
   * @param b
   * @return List<Name> which is all names in b
   * @author Doron Meshulam
   * @author Tomer Dragucki */
  @Nullable
  public static List<Name> names(@Nullable final Block b) {
    if (b == null)
      return null;
    final List<Name> $ = new ArrayList<>();
    b.accept(new ASTVisitor() {
      @Override public void preVisit(final ASTNode an) {
        if (iz.name(an))
          $.add(az.name(an));
      }
    });
    return $;
  }

  /** @author Shimon Azulay
   * @author Idan Atias
   * @since 16-11-3 Given a TypeDeclaration argument, this function returns a
   *        list of it's public fields names.
   * @param d - the TypeDeclaration argument */
  @NotNull
  public static List<String> publicFields(@Nullable final TypeDeclaration d) {
    final ArrayList<String> $ = new ArrayList<>();
    if (d == null)
      return $;
    for (final FieldDeclaration fd : d.getFields()) // TOUGH
      if (iz.public¢(fd)) {
        final String[] field_splitted_to_words = (fd + "").trim().split(" ");
        if (field_splitted_to_words.length >= 1)
          $.add(field_splitted_to_words[field_splitted_to_words.length - 1].replace(";", "").trim());
      }
    return $;
  }

  /** @author Sapir Bismot
   * @author Yaniv Levinsky
   * @since 16-11-8 Given a MethodDeclaration argument, this function returns a
   *        list of it's all String variable declarations.
   * @param ¢ - the MethodDeclaration argument */
  @NotNull
  public static List<VariableDeclaration> stringVariables(@Nullable final MethodDeclaration ¢) {
    final List<VariableDeclaration> $ = new ArrayList<>();
    if (¢ == null)
      return $;
    ¢.accept(new ASTVisitor() {
      @Override public boolean visit(@NotNull final SingleVariableDeclaration node) {
        if ("String".equals(node.getType() + ""))
          $.add(node);
        return true;
      }
    });
    return $;
  }
}
