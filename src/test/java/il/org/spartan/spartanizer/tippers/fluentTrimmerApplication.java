/* TODO Yossi Gil {@code Yossi.Gil@GMail.COM} please add a description
 *
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 *
 * @since Sep 25, 2016 */
package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class fluentTrimmerApplication extends Trimmer.With {
  @NotNull public final String codeFragment;
  public final GuessedContext guessedContext;
  @NotNull public final String wrappedFragment;
  @NotNull public final CompilationUnit compilationUnit;
  @NotNull public final Document document;
  public final ASTRewrite createRewrite;
  public final TextEdit textEdit;
  public final UndoEdit undoEdit;

  public fluentTrimmerApplication(@NotNull final Trimmer t, @NotNull final String codeFragment) {
    t.super();
    this.codeFragment = codeFragment;
    assert codeFragment != null;
    dump.go(codeFragment);
    guessedContext = GuessedContext.find(codeFragment);
    assert guessedContext != null;
    dump.go(guessedContext);
    wrappedFragment = guessedContext.on(codeFragment);
    dump.go(wrappedFragment, "wrappedFragment");
    assert wrappedFragment != null;
    document = new Document(wrappedFragment);
    assert document != null;
    dump.go(document, "This is the document");
    dump.go(document.get(), "and this is its content");
    compilationUnit = guessedContext.intoCompilationUnit(document.get());
    assert compilationUnit != null;
    createRewrite = trimmer().createRewrite(compilationUnit);
    assert createRewrite != null;
    textEdit = createRewrite.rewriteAST(document, null);
    assert textEdit != null;
    try {
      undoEdit = textEdit.apply(document);
      assert undoEdit != null;
      dump.go(document.get(), "DOC Content now");
    } catch (@NotNull MalformedTreeException | BadLocationException ¢) {
      throw new AssertionError("MISSING_CASE", ¢);
    }
    assert undoEdit != null;
  }

  @Nullable String aboutTheSame(@NotNull final String s1, @NotNull final String s2) {
    assert s1 != null;
    assert s2 != null;
    if (s1.equals(s2)) // Highly unlikely, but what the hack
      return null;
    final String $ = tide.clean(s1);
    assert $ != null;
    final String g2a = guessedContext.off(s2);
    assert g2a != null;
    final String g2b = tide.clean(g2a);
    assert g2b != null;
    return tide.eq($, g2b) || tide.eq(s1, g2b) || tide.eq($, g2a) ? g2b : null;
  }

  @Nullable String common(@NotNull final String expected) {
    return aboutTheSame(expected, document.get());
  }

  /** creates an ASTRewrite which contains the changes
   * @return an ASTRewrite which contains the changes */
  public final ASTRewrite createRewrite() {
    return createRewrite(nullProgressMonitor);
  }

  /** creates an ASTRewrite which contains the changes
   * @param pm a progress monitor in which the progress of the refactoring is
   *        displayed
   * @return an ASTRewrite which contains the changes */
  public final ASTRewrite createRewrite(@NotNull final IProgressMonitor ¢) {
    return createRewrite(¢, null);
  }

  private ASTRewrite createRewrite(@NotNull final IProgressMonitor pm, final IMarker m) {
    pm.beginTask("Creating rewrite operation...", 1);
    final ASTRewrite $ = ASTRewrite.create(compilationUnit.getAST());
    fillRewrite($, m);
    pm.done();
    return $;
  }

  protected final void fillRewrite(final ASTRewrite r, final IMarker m) {
    compilationUnit.accept(new DispatchingVisitor() {
      @Override protected <N extends ASTNode> boolean go(@NotNull final N n) {
        if (!trimmer().inRange(m, n))
          return true;
        final Tipper<N> w = trimmer().toolbox.firstTipper(n);
        if (w != null) {
          @Nullable final Tip make = w.tip(n, exclude);
          if (make != null)
            make.go(r, null);
        }
        return true;
      }
    });
  }

  <N extends ASTNode> N findNode(@NotNull final Class<N> clazz) {
    assert GuessedContext.find(codeFragment) != null;
    final N $ = firstInstance(clazz);
    assert $ != null;
    return $;
  }

  <N extends ASTNode> N firstInstance(@NotNull final Class<N> clazz) {
    @NotNull final Wrapper<N> $ = new Wrapper<>();
    compilationUnit.accept(new ASTVisitor(true) {
      /** The implementation of the visitation procedure in the JDT seems to be
       * buggy. Each time we find a node which is an instance of the sought
       * class, we return false. Hence, we do not anticipate any further calls
       * to this function after the first such node is found. However, this does
       * not seem to be the case. So, in the case our wrapper is not null, we do
       * not carry out any further tests.
       * @param pattern the node currently being visited.
       * @return whether the sought node is found. */
      @Override @SuppressWarnings("unchecked") public boolean preVisit2(@NotNull final ASTNode ¢) {
        if ($.get() != null)
          return false;
        if (!clazz.isAssignableFrom(¢.getClass()))
          return true;
        $.set((N) ¢);
        return false;
      }
    });
    return $.get();
  }

  public fluentTrimmerApplication gives(@NotNull final String expected) {
    if (aboutTheSame(expected, codeFragment) != null) {
      dump.go(this);
      azzert.fail(//
          "no CHNAGE\n" //
              + "I guessed the context of " + guessedContext //
              + "\n and in this context it seems as if your expectation is" ///
              + "\n that'" + codeFragment + "' is spartanized" //
              + "\n   to '" + expected + "', but, both look" //
              + "\n pretty much the same to me. You may want"//
              + "\n change your @Test to <code>" + //
              "\n\tipper\tipper spartiziation.of(" + codeFragment + ").stays()\n </code>." //
      );
    }
    if (common(codeFragment) != null) {
      dump.go(codeFragment);
      dump.go(compilationUnit);
      dump.go(document);
      azzert.fail(//
          "no CHNAGE\n" //
              + "\n Guessing the context of " + guessedContext //
              + "\n this '" + codeFragment + "' should have converted" //
              + "\n   to '" + expected + "', but it did appears to me"//
              + "\n that it did not change at all. What I got"//
              + "\n   was '" + document.get() + "' which" //
              + "\n which looks to me pretty much the same" //
              + "\n    as '" + codeFragment + "---the original"//
              + "\n snippet.");
    }
    @Nullable final String difference = common(codeFragment);
    if (difference != null)
      azzert.fail(//
          "something ELSE:\n" //
              + "\n Guessing the context of " + guessedContext //
              + "\n this '" + codeFragment + "' should have converted" //
              + "\n   to '" + expected + "', but for it converted instead" //
              + "\n   to '" + difference + "'!" //
      );
    return new fluentTrimmerApplication(trimmer(), document.get());
  }

  public void stays() {
    @Nullable final String difference = common(codeFragment);
    if (difference != null)
      azzert.fail(//
          "Should not change" //
              + "\n With guessed context of " //
              + guessedContext //
              + "\n this '" + codeFragment + "' does not stay." //
              + "\n It converts instead to  '" + difference + "'" //
      );
  }
}
