package il.org.spartan.plugin.revision;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;

public class TrackerSelection extends Selection {
  ASTNode track;
  ITrackedNodePosition position;

  public TrackerSelection(CU compilationUnit, ITextSelection textSelection, String name) {
    super(compilationUnit == null ? null : Collections.singletonList(CU.nonDisposal(compilationUnit.descriptor)), textSelection, name);
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
      compilationUnits.get(0).compilationUnit = null; // manual dispose
      track = new NodeFinder(compilationUnits.get(0).build().compilationUnit, textSelection.getOffset(), textSelection.getLength()).getCoveringNode();
      if (track == null || track.getStartPosition() != textSelection.getOffset() || track.getLength() != textSelection.getLength())
        compilationUnits.clear(); // empty selection
    }
  }
}
