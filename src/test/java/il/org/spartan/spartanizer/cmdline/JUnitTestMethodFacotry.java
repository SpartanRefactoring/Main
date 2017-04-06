package il.org.spartan.spartanizer.cmdline;

import static il.org.spartan.spartanizer.engine.nominal.namer.*;
import static il.org.spartan.spartanizer.engine.nominal.trivia.*;
import static java.lang.String.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.text.edits.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.testing.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

/** TODO Ori Marcovitch please add a description
 * @author Ori Marcovitch
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2017-03-24 */
public enum JUnitTestMethodFacotry {
  ;
  static String from(final String name, final String raw) {
    return wrapTest(name, linify(escapeQuotes(format.code(shortenIdentifiers(raw)))));
  }

  public static String unWrapedTestCase(final String raw) {
    return linify(escapeQuotes(format.code(shortenIdentifiers(raw))));
  }

  public static String code(final String raw) {
    return format.code(shortenIdentifiers(raw));
  }

  private static String wrapTest(final String name, final String code) {
    return String.format("  @Test public void %s() {\n" //
        + "    trimmingOf(\n \"%s\" \n)\n" //
        + "       .gives(\n" //
        + "    // Edit this to reflect your expectation\n" //
        + "     \"%s\"\n//\n)//\n.stays()\n;\n}", name, code, code);
  }

  /** Renders the Strings a,b,c, ..., z, x1, x2, ... for lower case identifiers
   * and A, B, C, ..., Z, X1, X2, ... for upper case identifiers */
  static String renderIdentifier(final String old) {
    switch (old) {
      case "START":
        return "A";
      case "X":
        return "X1";
      case "start":
        return "a";
      case "z":
        return "x1";
      default:
        return old.length() == 1 ? String.valueOf((char) (old.charAt(0) + 1)) : String.valueOf(old.charAt(0)) + (old.charAt(1) + 1);
    }
  }

  /** Separate the string to lines
   * @param ¢ string to linify
   * @return */
  private static String linify(final String ¢) {
    String $ = "";
    try (Scanner scanner = new Scanner(¢)) {
      while (scanner.hasNextLine())
        $ += "\"" + scanner.nextLine() + "\"" + (!scanner.hasNextLine() ? "" : " + ") + "//\n";
    }
    return $;
  }

  public static String shortenIdentifiers(final String javaFragment) {
    final Wrapper<String> id = new Wrapper<>("start"), Id = new Wrapper<>("START");
    final IDocument $ = new Document(ASTutils.wrapCode(javaFragment));
    final ASTParser parser = ASTParser.newParser(AST.JLS8);
    parser.setSource($.get().toCharArray());
    final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
    final AST ast = cu.getAST();
    final ASTNode n = ASTutils.extractASTNode(javaFragment, cu);
    if (n == null)
      return javaFragment;
    final ASTRewrite r = ASTRewrite.create(ast);
    final Map<String, String> renaming = new HashMap<>();
    n.accept(new ASTVisitor(true) {
      @Override public void preVisit(final ASTNode ¢) {
        if (!iz.simpleName(¢) && !iz.qualifiedName(¢))
          return;
        final String name = ((Name) ¢).getFullyQualifiedName();
        if (!renaming.containsKey(name))
          if (name.charAt(0) < 'A' || name.charAt(0) > 'Z') {
            id.set(renderIdentifier(id.get()));
            renaming.put(name, id.get());
          } else {
            Id.set(renderIdentifier(Id.get()));
            renaming.put(name, Id.get());
          }
        r.replace(¢, ast.newSimpleName(renaming.get(name)), null);
      }
    });
    applyChanges($, r);
    return ASTutils.extractCode(javaFragment, $);
  }

  private static void applyChanges(final IDocument d, final ASTRewrite r) {
    try {
      r.rewriteAST(d, null).apply(d);
    } catch (MalformedTreeException | IllegalArgumentException | BadLocationException ¢) {
      ¢.printStackTrace();
    }
  }

  public static void main(final String[] args) {
    Display display = new Display();
    Shell shell = new Shell(display);
    // the layout manager handle the layout
    // of the widgets in the container
    GridLayout gridLayout = new GridLayout();
    gridLayout.numColumns = 3;
    gridLayout.marginWidth = 10;
    gridLayout.marginHeight = 15;
    gridLayout.verticalSpacing = 20;
    shell.setLayout(gridLayout);
    // Shell can be used as container
    Label label = new Label(shell, SWT.BORDER);
    label.setText("Enter Whatever:");
    label.setForeground(display.getSystemColor(SWT.COLOR_BLUE));
    Text textBox = new Text(shell, SWT.BORDER);
    textBox.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
    GridData gridData = new GridData();
    gridData.horizontalSpan = 1;
    gridData.horizontalAlignment = SWT.FILL;
    gridData.grabExcessHorizontalSpace = true;
    gridData.verticalAlignment = SWT.FILL;
    gridData.grabExcessVerticalSpace = true;
    textBox.setLayoutData(gridData);
    Button button = new Button(shell, SWT.WRAP);
    button.setText("Go!");
    Label resLabel = new Label(shell, SWT.BORDER);
    resLabel.setText("Auto Generated Test:");
    resLabel.setForeground(display.getSystemColor(SWT.COLOR_BLUE));
    Text result = new Text(shell, SWT.READ_ONLY);
    // result.setText("result will be written here");
    gridData = new GridData();
    gridData.horizontalSpan = 1;
    gridData.horizontalAlignment = SWT.FILL;
    gridData.grabExcessHorizontalSpace = true;
    gridData.verticalAlignment = SWT.FILL;
    gridData.grabExcessVerticalSpace = true;
    result.setLayoutData(gridData);
    // register listener for the selection event
    button.addSelectionListener(new SelectionAdapter() {
      /** [[SuppressWarningsSpartan]] */
      @Override public void widgetSelected(@SuppressWarnings("unused") SelectionEvent e) {
        String s = textBox.getText();
        result.setText("1s tipper: " + theSpartanizer.firstTipper(s) + "\n" + //
        "once: " + theSpartanizer.once(s) + "\n" + //
        "twice: " + theSpartanizer.twice(s) + "\n" + //
        "thrice: " + theSpartanizer.thrice(s) + "\n" + //
        "fixed: " + theSpartanizer.repetitively(s) + "\n" + //
        JUnitTestMethodFacotry.from(namer.signature(s), s) + "\n" + //
        "");
      }
    });
    // set widgets size to their preferred size
    label.pack();
    textBox.pack();
    button.pack();
    shell.open();
    while (!shell.isDisposed())
      if (!display.readAndDispatch())
        display.sleep();
    display.dispose();
  }

  public static String makeTipperUnitTest(final String codeFragment) {
    final String $ = squeeze(removeComments(code(essence(codeFragment))));
    return comment() + format("  @Test public void test_%s() {\n %s\n}\n", signature($), tipperBody($));
  }

  public static String makeBloaterUnitTest(final String codeFragment) {
    final String $ = squeeze(removeComments(code(essence(codeFragment))));
    return comment() + format("@Test public void test_%s() {\n %s\n}\n", signature($), bloaterBody($));
  }

  static String comment() {
    return format(
        "/** Introduced by %s on %s \n" + //
            "(code automatically in class '%s')*/\n", //
        system.userName(), //
        system.now(), //
        system.callinClassLastName());
  }

  static String tipperBody(final String input) {
    for (String $ = format("    trimmingOf(\"%s\") //\n", input), from = input;;) {
      final String to = theSpartanizer.once(from);
      if (theSpartanizer.same(to, from))
        return $ + "         .stays() //\n  ;";
      final Tipper<?> t = theSpartanizer.firstTipper(from);
      assert t != null;
      $ += //
          format("") + // Stub for alignment of strings below
              format(//
                  "         .using(%s.class, new %s()) //\n", operandClass(t), tipperClass(t))
              + //
              format(//
                  "         .gives(\"%s\") //\n", escapeQuotes(trivia.essence(to)));
      from = to;
    }
  }

  static String bloaterBody(final String input) {
    for (String $ = format("  bloatingOf(\"%s\") //\n", input), from = input;;) {
      final String to = OperandBloating.bloat(from);
      if (to.equals(from))
        return $ + "  .stays() //\n  ;";
      $ += format(" .gives(\"%s\") //\n", escapeQuotes(trivia.essence(to)));
      from = to;
    }
  }

  private static String operandClass(final Tipper<?> ¢) {
    return English.name(¢.current());
  }

  private static String tipperClass(final Tipper<?> ¢) {
    return ¢.nanoName() + format(¢.getClass().getTypeParameters().length <= 0 ? "" : "<%s>", operandClass(¢));
  }
}
