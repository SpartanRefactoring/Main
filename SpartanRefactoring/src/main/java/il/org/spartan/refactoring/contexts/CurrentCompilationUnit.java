package il.org.spartan.refactoring.contexts;

import static il.org.spartan.lazy.Cookbook.cook;

import il.org.spartan.*;
import static il.org.spartan.azzert.positive;

import org.junit.Test;
import org.mockito.internal.matchers.*;
import org.eclipse.core.resources.*;
import org.eclipse.jdt.core.*;
import org.eclipse.ui.*;

/** @author Yossi Gil
 * @since 2016 */
@SuppressWarnings("javadoc") public class CurrentCompilationUnit extends Monitored {
  // @formatter:off 
  // VIM: +,/formatter:on/-!sort -u | column -t | sed "s/^/  /"
  public  final  ICompilationUnit  compilationUnit()        {  return  compilationUnit.get();        }
  public  final  IEditorInput      editorInput()            {  return  editorInput.get();            }
  public  final  IEditorPart       activeEditor()           {  return  activeEditor.get();           }
  public  final  IFile             iFile()                  {  return  iFile.get();                  }
  public  final  IJavaProject      javaProject()            {  return  javaProject.get();            }
  public  final  IResource         resource()               {  return  resource.get();               }
  public  final  IWorkbenchPage    activePage()             {  return  activePage.get();             }
  public  final  IWorkbenchWindow  activeWorkBenchWindow()  {  return  activeWorkBenchWindow.get();  }
  public  final  IWorkbench        workbench()              {  return  workbench.get();              }
  // @formatter:on
  
  // @formatter:off 
  // VIM: +,/formatter:on/- | column -t | sed "s/^/  /"
  final  Cell<IWorkbench>        workbench              =  cook(()->  PlatformUI.getWorkbench());
  final  Cell<IWorkbenchWindow>  activeWorkBenchWindow  =  cook(()->  workbench().getActiveWorkbenchWindow());
  final  Cell<IWorkbenchPage>    activePage             =  cook(()->  activeWorkBenchWindow().getActivePage());
  final  Cell<IEditorPart>       activeEditor           =  cook(()->  activePage().getActiveEditor());
  final  Cell<IEditorInput>      editorInput            =  cook(()->  activeEditor().getEditorInput());
  final  Cell<IResource>         resource               =  cook(()->  (IResource)editorInput().getAdapter(IResource.class));
  final  Cell<IFile>             iFile                  =  cook(()->  (IFile)resource());
  final  Cell<ICompilationUnit>  compilationUnit        =  cook(()->  JavaCore.createCompilationUnitFrom(iFile()));
  final  Cell<IJavaProject>      javaProject            =  cook(()->  compilationUnit().getJavaProject());
  // @formatter:on
  /** Inner class, inheriting all of its container's {@link Cell}s, and adding
   * some of its own. Access to container's c {@link Cells} is through the
   * {@link #context} variable.
   * @author Yossi Gil
   * @since 2016` */
  public abstract class Context extends Described.Monitored {
    /** instantiates this class */
    public Context() {
      new Described().super();
    }

    /** the containing instance */
    protected final CurrentCompilationUnit context = CurrentCompilationUnit.this;
  }

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
      IWorkbenchWindow ¢ = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
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
      IWorkbenchPage ¢ = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
      azzert.notNull(¢);
      azzert.isNull(¢.getActiveEditor());
    }
    @Test public void sessionA08() {
      azzert.isNull(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor());
    }
  }
}