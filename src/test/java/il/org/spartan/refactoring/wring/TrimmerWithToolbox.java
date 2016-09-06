package il.org.spartan.refactoring.wring;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;

import il.org.spartan.*;
import il.org.spartan.refactoring.spartanizations.*;
import il.org.spartan.refactoring.utils.*;

public class TrimmerWithToolbox {
  static boolean eq(final String s1, final String s2) {
    return s1 == s2 || (s1 == null ? s2 == null : s1.equals(s2) || s2 != null && s2.equals(s1));
  }

  public final Trimmer trimmer;

  public TrimmerWithToolbox(final Trimmer trimmer) {
    this.trimmer = trimmer;
  }

  public ToolboxApplication of(String codeFragment) {
    return new ToolboxApplication(codeFragment);
  }

  static boolean eq(final String s1, final String s2) {
    return s1 == s2 || (s1 == null ? s2 == null : s1.equals(s2) || s2 != null && s2.equals(s1));
  }

  public class ToolboxApplication {
    public final String codeFragment;
    public final GuessedContext guessedContext;
    public final String wrappedFragment;
    public final CompilationUnit compilationUnit;
    public final Document document;
    public final ASTRewrite createRewrite;
    public final TextEdit textEdit;
    public final UndoEdit undoEdit;

    public Trimmer trimmer() {
      return TrimmerWithToolbox.this.trimmer;
    }

    public ToolboxApplication(final String codeFragment) {
      this.codeFragment = codeFragment;
      azzert.notNull(codeFragment);
      guessedContext = GuessedContext.find(codeFragment);
      azzert.notNull(guessedContext);
      wrappedFragment = guessedContext.on(codeFragment);
      azzert.notNull(wrappedFragment);
      document = new Document(wrappedFragment);
      azzert.notNull(document);
      compilationUnit = guessedContext.intoCompilationUnit(document.get());
      azzert.notNull(compilationUnit);
      createRewrite = trimmer.createRewrite(compilationUnit);
      azzert.notNull(createRewrite);
      textEdit = createRewrite.rewriteAST(document, null);
      azzert.notNull(textEdit);
      try {
        undoEdit = textEdit.apply(document);
        azzert.notNull(undoEdit);
      } catch (MalformedTreeException | BadLocationException x) {
        throw new AssertionError(x);
      }
      azzert.notNull(undoEdit);
    }

    public void stays() {
      final String difference = difference(codeFragment);
      if (difference != null)
        azzert.fail(//
            "\n With guessed context of " //
                + guessedContext //
                + "\n this '" + codeFragment + "' does not stay." //
                + "\n It converts instead to  '" + difference + "'" //
        );
    }

    public ToolboxApplication to(final String expected) {
      if (aboutTheSame(expected, codeFragment) == null)
        azzert.fail(//
            "\n I guessed the context of " + guessedContext //
                + "\n and in this context it seems as if your expectation is" ///
                + "\n that'" + codeFragment + "' is spartanized" //
                + "\n   to '" + expected + "', but, both look" //
                + "\n pretty much the same to me. You may want"//
                + "\n change your @Test to <pre>" + //
                "\n\t\t spartiziation.of(" + codeFragment + ").stays()" + "\n </pre>." //
        );
      if (difference(codeFragment) == null)
        azzert.fail(//
            "\n Guessing the context of " + guessedContext //
                + "\n this '" + codeFragment + "' should have converted" //
                + "\n   to '" + expected + "', but it did appears to me"//
                + "\n that it did not change at all. What I got"//
                + "\n   was '" + document.get() + "' which" //
                + "\n which looks to me pretty much the same" //
                + "\n    as '" + codeFragment + "---the original"//
                + "\n snippet.");
      final String difference = difference(codeFragment);
      if (difference != null)
        azzert.fail(//
            "\n Guessing the context of " + guessedContext //
                + "\n this '" + codeFragment + "' should have converted" //
                + "\n   to '" + expected + "', but for it converted instead" //
                + "\n   to '" + difference + "'!" //
        );
      return new ToolboxApplication(document.get());
    }

    private String aboutTheSame(final String s1, final String s2) {
      assert s1 != null;
      assert s2 != null;
      if (s1.equals(s2)) // Highly unlikely, but what the hack
        return null;
      final String g1 = Funcs.gist(s1);
      assert g1 != null;
      final String g2a = guessedContext.off(s2);
      assert g2a != null;
      final String g2b = Funcs.gist(g2a);
      assert g2b != null;
      return eq(g1, g2b) || eq(s1, g2b) || eq(g1, g2a) ? g2b : null;
    }

    private String difference(final String expected) {
      return aboutTheSame(expected, document.get());
    }
  }
}
