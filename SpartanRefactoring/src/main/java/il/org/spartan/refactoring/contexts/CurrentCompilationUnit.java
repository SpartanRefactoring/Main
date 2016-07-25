package il.org.spartan.refactoring.contexts;

import static il.org.spartan.lazy.Cookbook.cook;


import org.eclipse.core.resources.*;
import org.eclipse.jdt.core.*;
import org.eclipse.ui.*;

/** @author Yossi Gil
 * @since 2016` */
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
  // VIM: +,/formatter:on/-!sort -u | column -t | sed "s/^/  /"
  final  Cell<ICompilationUnit>  compilationUnit        =  cook(()->  JavaCore.createCompilationUnitFrom(iFile()));
  final  Cell<IEditorInput>      editorInput            =  cook(()->  activeEditor().getEditorInput());
  final  Cell<IEditorPart>       activeEditor           =  cook(()->  activePage().getActiveEditor());
  final  Cell<IFile>             iFile                  =  cook(()->  (IFile)resource());
  final  Cell<IJavaProject>      javaProject            =  cook(()->  compilationUnit().getJavaProject());
  final  Cell<IResource>         resource               =  cook(()->  (IResource)editorInput().getAdapter(IResource.class));
  final  Cell<IWorkbenchPage>    activePage             =  cook(()->  activeWorkBenchWindow().getActivePage());
  final  Cell<IWorkbenchWindow>  activeWorkBenchWindow  =  cook(()->  workbench().getActiveWorkbenchWindow());
  final  Cell<IWorkbench>        workbench              =  cook(()->  PlatformUI.getWorkbench());
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
}