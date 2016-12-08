package il.org.spartan.athenizer.inflate;

import java.util.*;
import java.util.List;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.ltk.core.refactoring.*;
import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.text.edits.*;
import org.eclipse.ui.*;

import il.org.spartan.athenizer.inflate.expanders.*;
import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.utils.*;

public class InflaterListener implements MouseWheelListener, KeyListener {
  static final int CURSOR_IMAGE = SWT.CURSOR_CROSS;
  final StyledText text;
  final List<Listener> externalListeners;
  final Cursor activeCursor;
  final Cursor inactiveCursor;
  boolean active;

  public InflaterListener(final StyledText text) {
    this.text = text;
    externalListeners = new LinkedList<>();
    for (final Listener ¢ : text.getListeners(SWT.MouseWheel))
      externalListeners.add(¢);
    final Display display = PlatformUI.getWorkbench().getDisplay();
    activeCursor = new Cursor(display, CURSOR_IMAGE);
    inactiveCursor = text.getCursor();
  }

  @Override public void mouseScrolled(final MouseEvent ¢) {
    if (active)
      if (¢.count > 0)
        inflate();
      else if (¢.count < 0)
        deflate();
  }

  /** Main function of the application.  
   * @param r JD
   * @param statementsListList selection as list of lists of statements
   * @param g JD
   * @return true iff rewrite object should be applied For now - we only have a
   *         few expanders (1) so we will not infrastructure a full toolbox as
   *         in the spartanizer But we should definitely implement it one day
   * @author Raviv Rachmiel
   * @since 12-5-16 */
  private static boolean rewrite(final ASTRewrite r, final List<ASTNode> nl, final TextEditGroup __) {
    boolean $ = false;
    if (nl.isEmpty())
      return false;
    for (final ASTNode statement : nl) {  
      final ASTNode change = new TernaryExpander().replacement(az.statement(statement));
      if (change != null) {
        r.replace(statement, change, __);
        $ = true; 
      }
    }
    return $;
  }

  public static void commitChanges(final WrappedCompilationUnit u, final List<ASTNode> ns) {
    try {
      final TextFileChange textChange = new TextFileChange(u.descriptor.getElementName(), (IFile) u.descriptor.getResource());
      textChange.setTextType("java");
      final ASTRewrite r = ASTRewrite.create(u.compilationUnit.getAST());
      if (rewrite(r, ns, null)) {
        textChange.setEdit(r.rewriteAST());
        if (textChange.getEdit().getLength() != 0)
          textChange.perform(new NullProgressMonitor());
      }
    } catch (final CoreException ¢) {
      monitor.log(¢);
    }
  }

  private static List<ASTNode> getConditionalStatements(WrappedCompilationUnit u) {
    List<ASTNode> $ = new ArrayList<>();
    u.compilationUnit.accept(new ASTVisitor() {
      @Override public boolean visit(ConditionalExpression node) {
        if(az.statement(node.getParent()) != null)
          $.add(node.getParent());        
        return true;
      }
   });
    return $;
  }
  
  
  private static boolean intervalsIntersect(int startChar1,int length1,int startChar2,int length2) {
    System.out.println("========================");
    System.out.println("startChar1 - " + startChar1);
    System.out.println("length1 - " + length1);
    System.out.println("startLine2 - " + startChar2);
    System.out.println("length2 - " + length2);
    System.out.println("========================");
    return true;
  }
  
  private static List<ASTNode> selectedStatements(List<ASTNode> ns) {
    List<ASTNode> $ = new ArrayList<>();
    for(ASTNode ¢ : ns)
      if(intervalsIntersect(¢.getStartPosition(),¢.getLength(), Selection.Util.current().textSelection.getOffset(), Selection.Util.current().textSelection.getLength()))
        $.add(¢);
    return $;
  }
  
  
  private static void inflate() {
    WrappedCompilationUnit wcu = Selection.Util.current().inner.get(0).build();
    commitChanges(wcu, selectedStatements(getConditionalStatements(wcu)));
    }
    



  private static void deflate() {
    System.out.println("deflating " + Selection.Util.current());
  }

  @Override public void keyPressed(final KeyEvent ¢) {
    if (¢.keyCode == SWT.CTRL && !active)
      activate();
  }

  @Override public void keyReleased(final KeyEvent ¢) {
    if (¢.keyCode == SWT.CTRL && active)
      deactivate();
  }

  private void activate() {
    active = true;
    InflateHandler.removeListeners(text, externalListeners, SWT.MouseWheel);
    if (!text.isDisposed())
      text.setCursor(activeCursor);
  }

  private void deactivate() {
    active = false;
    InflateHandler.addListeners(text, externalListeners, SWT.MouseWheel);
    if (!text.isDisposed())
      text.setCursor(inactiveCursor);
  }

  public void finilize() {
    if (active)
      deactivate();
  }
}
