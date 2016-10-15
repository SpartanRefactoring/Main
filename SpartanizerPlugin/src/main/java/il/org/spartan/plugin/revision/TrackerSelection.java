package il.org.spartan.plugin.revision;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.*;

public class TrackerSelection extends Selection {
  ASTNode track;

  public TrackerSelection(CU compilationUnit, ITextSelection textSelection, String name) {
    super(asList(compilationUnit), textSelection, name);
  }

  public static TrackerSelection empty() {
    return new TrackerSelection(null, null, null);
  }

  public TrackerSelection track(final ASTNode ¢) {
    track = ¢;
    return this;
  }

  // TODO Roth: check safety of tracking
  public void update() {
    final ASTNode newTrack = new NodeFinder(compilationUnits.get(0).build().compilationUnit, textSelection.getOffset(), textSelection.getLength()).getCoveringNode();
    if (newTrack == null || track.getStartPosition() != newTrack.getStartPosition()) {
      compilationUnits.clear(); // empty selection
      return;
    }
    track = newTrack;
    textSelection = new TextSelection(track.getStartPosition(), track.getLength());
  }

  private static List<CU> asList(CU ¢) {
    List<CU> $ = new ArrayList<>();
    if (¢ != null)
      $.add(¢);
    return $;
  }
}
