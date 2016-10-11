package il.org.spartan.plugin.revision;

import java.util.*;

import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;

/** An {@link Applicator} suitable for eclipse GUI.
 * @author Ori Roth
 * @since 2016 */
public class GUIApplicator extends Applicator {
  /** Possible events during spartanization process */
  enum event {
    run_start, run_finish, run_pass, //
    visit_project, visit_cu, visit_node, //
  }

  /** Spartanization process. */
  @Override public void go() {
    System.out.println(selection());
  }

  /** Default listener configuration of {@link GUIApplicator}.
   * @return this applicator */
  public GUIApplicator defaultListener() {
    listener(EventMapper.empty(event.class) //
        .expend(EventMapper.recorderOf(event.visit_cu).rememberBy(ICompilationUnit.class)) //
        .expend(EventMapper.recorderOf(event.visit_node).rememberBy(ASTNode.class)) //
        .expend(EventMapper.recorderOf(event.visit_project).rememberLast(IJavaProject.class)) //
        .expend(EventMapper.recorderOf(event.run_pass).counter()) //
        .expend(EventMapper.inspectorOf(event.run_start).does(¢ -> {
          // TODO Roth: open a dialog box etc etc...
          System.out.println("Spartanizing " + ¢.get(event.visit_project));
        })) //
        .expend(EventMapper.inspectorOf(event.run_finish).does(¢ -> {
          // TODO Roth: open a dialog box etc etc...
          System.out.println("Done spartanizing " + ¢.get(event.visit_project));
          System.out.println("Spartanized " + ¢.get(event.visit_project) //
              + " with " + ((Collection<?>) ¢.get(event.visit_cu)).size() + " files" //
              + " in " + ¢.get(event.run_pass) + " passes");
        })));
    return this;
  }

  /** Default selection configuration of {@link GUIApplicator}.
   * @return this applicator */
  public GUIApplicator defaultSelection() {
    selection(Selection.Util.get());
    return this;
  }
}
