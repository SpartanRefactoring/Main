package il.org.spartan.plugin.revision;

import static il.org.spartan.lisp.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;

public class TrackerSelection extends Selection {
  ASTNode track;
  ITrackedNodePosition position;

  public TrackerSelection(final WrappedCompilationUnit compilationUnit, final ITextSelection textSelection, final String name) {
    super(asList(compilationUnit), textSelection, name);
  }

  public static TrackerSelection empty() {
    return new TrackerSelection(null, null, null);
  }

  public TrackerSelection track(final ASTNode ¢) {
    track = ¢;
    return this;
  }

  public void acknowledge(final ASTRewrite ¢) {
    if (track != null)
      position = ¢.track(track);
  }

  public void update() {
    if (track == null || compilationUnits == null || compilationUnits.size() != 1)
      compilationUnits.clear(); // empty selection
    else {
      textSelection = new TextSelection(position.getStartPosition(), position.getLength());
      first(compilationUnits).compilationUnit = null; // manual dispose
      track = new NodeFinder(first(compilationUnits).build().compilationUnit, textSelection.getOffset(), textSelection.getLength()).getCoveringNode();
      if (track == null || track.getStartPosition() != textSelection.getOffset() || track.getLength() != textSelection.getLength())
        compilationUnits.clear(); // empty selection
    }
  }

  private static List<WrappedCompilationUnit> asList(final WrappedCompilationUnit ¢) {
    final List<WrappedCompilationUnit> $ = new ArrayList<>();
    if (¢ != null)
      $.add(¢);
    return $;
  }
}
