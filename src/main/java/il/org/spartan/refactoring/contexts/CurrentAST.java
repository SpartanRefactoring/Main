package il.org.spartan.refactoring.contexts;

import static il.org.spartan.lazy.Environment.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.ui.*;

/** @author Yossi Gil
 * @since 2016 */
@SuppressWarnings("javadoc") public class CurrentAST extends Monitored {
  public CurrentAST clone() {
    try {
      return (CurrentAST) super.clone();
    } catch (CloneNotSupportedException x) {
      bug(x);
      return this;
    }
  }
  // @formatter:off
  // Suppliers: may be sorted.
  // Sort alphabetically, organize in columns, indent. VIM: /^\s*[^*\/][^*\/]/,/^\s*\/\//-!sort -u | column -t | sed "s/^/  /"
  final  Property<IWorkbench>        workbench        =  function(()             ->  PlatformUI.getWorkbench());
  // Bindings: must not be sorted 
  // Organize in columns, indent, but do not sort. VIM: /^\s*[^*\/][^*\/]/,/^\s*\/\//-!column -t | sed "s/^/  /"
  final  Property<AST>  ast  =  bind((IWorkbench        ¢)  ->  ¢.getActiveWorkbenchWindow()).to(workbench);
  final  Property<IWorkbenchPage>    activePage       =  bind((IWorkbenchWindow  ¢)  ->  ¢.getActivePage()).to(activeWorkbench);
  final  Property<IEditorPart>       activeEditor     =  bind((IWorkbenchPage    ¢)  ->  ¢.getActiveEditor()).to(activePage);
  final  Property<IEditorInput>      editorInput      =  bind((IEditorPart       ¢)  ->  ¢.getEditorInput()).to(activeEditor);
  public  final  IEditorPart       activeEditor()     {  return  activeEditor.get();     }
  public  final  IWorkbenchPage    activePage()       {  return  activePage.get();       }
  public  final  IWorkbenchWindow  activeWorkbench()  {  return  activeWorkbench.get();  }
  // Getters: can be sorter
  // Sort alphabetically, organize in columns, indent. VIM: /^\s*[^*\/][^*\/]/,/^\s*\/\//-!sort -u | column -t | sed "s/^/  /"
  public  final  IEditorInput      editorInput()      {  return  editorInput.get();      }
  public  final  IWorkbench        workbench()        {  return  workbench.get();        }
  public static class __META {
    /**Place holder**/
}
  // @formatter:on
  /** Inner class, inheriting all of its container's {@link Property}s, and
   * adding some of its own. Access to container's {@link Properties} is through
   * the {@link #parent} variable.
   * @author Yossi Gil
   * @since 2016` */
  public abstract class ¢ extends Described.Monitored {
    /** the containing instance */
    protected final CurrentAST parent = CurrentAST.this;

    /** instantiates this class */
    public ¢() {
      new Described().super();
    }
  }
}
