package il.org.spartan.refactoring.contexts;

import static il.org.spartan.lazy.Environment.*;

import java.util.*;

import org.eclipse.core.resources.*;
import org.eclipse.jdt.core.*;
import org.eclipse.ui.*;
import org.junit.*;

import il.org.spartan.*;

/** @author Yossi Gil
 * @since 2016 */
@SuppressWarnings("javadoc") public class CurrentCompilationUnit extends Monitored {
  public static class __META {
    @SuppressWarnings("static-method") public static class TEST {
      @Test public void sessionA01() {
        azzert.notNull(new CurrentCompilationUnit());
      }
      @Test public void sessionA02() {
        azzert.notNull(PlatformUI.getWorkbench());
        azzert.notNull(PlatformUI.getWorkbench().getActiveWorkbenchWindow());
        azzert.notNull(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor());
        azzert.notNull(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor().getEditorInput());
        azzert.notNull(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor().getEditorInput().getAdapter(IResource.class));
        azzert.that(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor().getEditorInput().getAdapter(IResource.class), azzert.instanceOf(IFile.class));
        azzert.notNull(JavaCore.createCompilationUnitFrom((IFile) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor().getEditorInput().getAdapter(IResource.class)));
      }
      @Test public void sessionA03() {
        final IWorkbenchWindow ¢ = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        azzert.notNull(¢);
        azzert.notNull(¢.getPages());
        azzert.positive(¢.getPages().length);
      }
      @Test public void sessionA04() {
        azzert.notNull(PlatformUI.getWorkbench().getActiveWorkbenchWindow());
      }
      @Test public void sessionA05() {
        PlatformUI.getWorkbench().getActiveWorkbenchWindow().getPages();
      }
      @Test public void sessionA06() {
        azzert.positive(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getPages().length);
      }
      @Test public void sessionA07() {
        final IWorkbenchPage ¢ = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        azzert.notNull(¢);
        azzert.isNull(¢.getActiveEditor());
      }
      @Test public void sessionA08() {
        azzert.isNull(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor());
      }
    }
  }

  // @formatter:on
  /** Inner class, inheriting all of its container's {@link Property}s, and
   * adding some of its own. Access to container's {@link Properties} is through
   * the {@link #parent} variable.
   * @author Yossi Gil
   * @since 2016` */
  public abstract class ¢ extends Described.Monitored {
    /** the containing instance */
    protected final CurrentCompilationUnit parent = CurrentCompilationUnit.this;

    /** instantiates this class */
    public ¢() {
      new Described().super();
    }
  }

  // @formatter:off
  // Suppliers: may be sorted.
  // Sort alphabetically, organize in columns, indent. VIM: /^\s*[^*\/][^*\/]/,/^\s*\/\//-!sort -u | column -t | sed "s/^/  /"
  final  Property<IWorkbench>        workbench        =  function(()             ->  PlatformUI.getWorkbench());
  // Bindings: must not be sorted 
  // Organize in columns, indent, but do not sort. VIM: /^\s*[^*\/][^*\/]/,/^\s*\/\//-!column -t | sed "s/^/  /"
  final  Property<IWorkbenchWindow>  activeWorkbench  =  bind((IWorkbench        ¢)  ->  ¢.getActiveWorkbenchWindow()).to(workbench);
  final  Property<IWorkbenchPage>    activePage       =  bind((IWorkbenchWindow  ¢)  ->  ¢.getActivePage()).to(activeWorkbench);
  final  Property<IEditorPart>       activeEditor     =  bind((IWorkbenchPage    ¢)  ->  ¢.getActiveEditor()).to(activePage);
  final  Property<IEditorInput>      editorInput      =  bind((IEditorPart       ¢)  ->  ¢.getEditorInput()).to(activeEditor);
  final  Property<IResource>         resource         =  bind((IEditorInput      ¢)  ->  ¢.getAdapter(IResource.class)).to(editorInput);
  final  Property<IFile>             iFile            =  bind((IResource         ¢)  ->  (IFile)¢).to(resource);
  final  Property<ICompilationUnit>  compilationUnit  =  bind((IFile             ¢)  ->  JavaCore.createCompilationUnitFrom(¢)).to(iFile);
  final  Property<IJavaProject>      javaProject      =  bind((ICompilationUnit  ¢)  ->  ¢.getJavaProject()).to(compilationUnit);
}
