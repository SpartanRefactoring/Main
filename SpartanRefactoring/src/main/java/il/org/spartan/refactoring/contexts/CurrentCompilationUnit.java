package il.org.spartan.refactoring.contexts;

import static il.org.spartan.lazy.Environment.*;

import org.eclipse.core.resources.*;
import org.eclipse.jdt.core.*;
import org.eclipse.ui.*;
import org.junit.*;

import il.org.spartan.*;

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
  final  Property<IWorkbench>        workbench              =  function(()->  PlatformUI.getWorkbench());
  final  Property<IWorkbenchWindow>  activeWorkBenchWindow  =  bind((IWorkbench ¢) ->  ¢.getActiveWorkbenchWindow()).to(workbench);

  //    ( ¢)->  ¢.getActiveWorkbenchWindow()).to(workbench);
 final  Property<IWorkbenchPage>    activePage             = bind((IWorkbenchWindow ¢)-> ¢.getActivePage()).to(activeWorkBenchWindow);
  final  Property<IEditorPart>       activeEditor           =  function(()->  activePage().getActiveEditor());
  final  Property<IEditorInput>      editorInput            =  function(()->  activeEditor().getEditorInput());
  final  Property<IResource>         resource               =  function(()->  editorInput().getAdapter(IResource.class));
  final  Property<IFile>             iFile                  =  function(()->  (IFile)resource());
  final  Property<ICompilationUnit>  compilationUnit        =  function(()->  JavaCore.createCompilationUnitFrom(iFile()));
  final  Property<IJavaProject>      javaProject            =  function(()->  compilationUnit().getJavaProject());
  // @formatter:on
  /** Inner class, inheriting all of its container's {@link Property}s, and
   * adding some of its own. Access to container's c {@link Cells} is through
   * the {@link #context} variable.
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