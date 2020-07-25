package il.org.spartan.spartanizer.utils.tdd;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclaration;

import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;

/** TODO Ori Marcovitch please add a description
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
  public static List<MethodDeclaration> methods(final CompilationUnit u) {
    if (u == null)
      return null;
    final List<MethodDeclaration> $ = an.empty.list();
    u.accept(new ASTVisitor(true) {
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
  public static List<Name> names(final Block b) {
    if (b == null)
      return null;
    final List<Name> $ = an.empty.list();
    b.accept(new ASTVisitor(true) {
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
  public static List<String> publicFields(final TypeDeclaration d) {
    final List<String> $ = an.empty.list();
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
  public static List<VariableDeclaration> stringVariables(final MethodDeclaration ¢) {
    final List<VariableDeclaration> $ = an.empty.list();
    if (¢ == null)
      return $;
    // noinspection SameReturnValue
    ¢.accept(new ASTVisitor(true) {
      @Override public boolean visit(final SingleVariableDeclaration node) {
        if ("String".equals(node.getType() + ""))
          $.add(node);
        return true;
      }
    });
    return $;
  }
}
