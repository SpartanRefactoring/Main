package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.wizard.nullProgressMonitor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.text.edits.UndoEdit;

import fluent.ly.azzert;
import fluent.ly.dump;
import il.org.spartan.tide;
import il.org.spartan.spartanizer.ast.navigate.GuessedContext;
import il.org.spartan.spartanizer.tipping.Tip;
import il.org.spartan.spartanizer.tipping.Tipper;
import il.org.spartan.spartanizer.traversal.DispatchingVisitor;
import il.org.spartan.spartanizer.traversal.TraversalImplementation;

/** TODO Yossi Gil Document Class
 * @author Yossi Gil
 * @since Sep 25, 2016 */
public class fluentTraversalApplication extends TraversalImplementation {
  public final String codeFragment;
  public final GuessedContext guessedContext;
  public final String wrappedFragment;
  public final CompilationUnit compilationUnit;
  public final Document document;
  public final ASTRewrite createRewrite;
  public final TextEdit textEdit;
  public final UndoEdit undoEdit;

  public fluentTraversalApplication(final String codeFragment) {
    this.codeFragment = codeFragment;
    assert codeFragment != null;
    dump.of(codeFragment);
    guessedContext = GuessedContext.find(codeFragment);
    assert guessedContext != null;
    dump.of(guessedContext);
    wrappedFragment = guessedContext.on(codeFragment);
    dump.of(wrappedFragment, "wrappedFragment");
    assert wrappedFragment != null;
    document = new Document(wrappedFragment);
    assert document != null;
    dump.of(document, "This is the document");
    dump.of(document.get(), "and this is its content");
    compilationUnit = guessedContext.intoCompilationUnit(document.get());
    assert compilationUnit != null;
    createRewrite = go(compilationUnit);
    assert createRewrite != null;
    textEdit = createRewrite.rewriteAST(document, null);
    assert textEdit != null;
    try {
      undoEdit = textEdit.apply(document);
      assert undoEdit != null;
      dump.of(document.get(), "DOC Content now");
    } catch (MalformedTreeException | BadLocationException ¢) {
      throw new AssertionError("MISSING_CASE", ¢);
    }
    assert undoEdit != null;
  }
  String aboutTheSame(final String s1, final String s2) {
    assert s1 != null;
    assert s2 != null;
    if (s1.equals(s2)) // Highly unlikely, but what the heck
      return null;
    final String $ = tide.clean(s1);
    assert $ != null;
    final String g2a = guessedContext.off(s2);
    assert g2a != null;
    final String g2b = tide.clean(g2a);
    assert g2b != null;
    return tide.eq($, g2b) || tide.eq(s1, g2b) || tide.eq($, g2a) ? g2b : null;
  }
  String common(final String expected) {
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
  public final ASTRewrite createRewrite(final IProgressMonitor ¢) {
    ¢.beginTask("Creating rewrite operation...", 1);
    final ASTRewrite $ = ASTRewrite.create(compilationUnit.getAST());
    fillRewrite($);
    ¢.done();
    return $;
  }
  protected final void fillRewrite(final ASTRewrite r) {
    compilationUnit.accept(new DispatchingVisitor() {
      @Override protected <N extends ASTNode> boolean go(final N n) {
        final Tipper<N> w = toolbox.firstTipper(n);
        if (w == null)
          return true;
        final Tip make = w.tip(n);
        if (make != null)
          make.go(r, null);
        return true;
      }
    });
  }
  public fluentTraversalApplication gives(final String expected) {
    if (aboutTheSame(expected, codeFragment) != null) {
      dump.of(this);
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
      dump.of(codeFragment);
      dump.of(compilationUnit);
      dump.of(document);
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
    final String difference = common(codeFragment);
    if (difference != null)
      azzert.fail(//
          "something ELSE:\n" //
              + "\n Guessing the context of " + guessedContext //
              + "\n this '" + codeFragment + "' should have converted" //
              + "\n   to '" + expected + "', but for it converted instead" //
              + "\n   to '" + difference + "'!" //
      );
    return new fluentTraversalApplication(document.get());
  }
  public void stays() {
    final String difference = common(codeFragment);
    if (difference != null)
      azzert.fail(//
          "Should not change" //
              + "\n __ guessed context of " //
              + guessedContext //
              + "\n this '" + codeFragment + "' does not stay." //
              + "\n It converts instead to  '" + difference + "'" //
      );
  }
}
