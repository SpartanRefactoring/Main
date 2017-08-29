package op.traverse;

import java.io.*;
import java.util.*;
import java.util.function.*;

import fluent.ly.*;
import il.org.spartan.collections.*;
import il.org.spartan.external.*;
import il.org.spartan.spartanizer.cmdline.library.*;
import il.org.spartan.spartanizer.traversal.*;
import op.traverse.Traverse.*;

class Run<Self extends Run<Self, S>, S extends Events.Set> //
    implements Arguments<Self> {
  /** Transient variables */
  /** Default command line argument list, used when the arguments is empty */
  public static final String[] DEFAULT_ARGUMENTS = as.array("..");
  final List<BooleanSupplier> filters = new ArrayList<>();

  /** runs the class arguments are corpora to be searched */
  public final void go() {
    doBatch();
  }
  @Override public Self withArguments(String[] arguments) {
    corpora.addAll(External.Introspector.extract(de.fault(arguments).to(DEFAULT_ARGUMENTS), this));
    return self();
  }
  @Override public Self withFilter(BooleanSupplier filter) {
    filters.add(filter);
    return self();
  }
  @Override public Self withHook(@SuppressWarnings("unused") Events h) {
    return self();
  }
  private void doBatch() {
    if (corpora.isEmpty())
      corpora.addAll(as.list(system.foldersIn(inputAbsolutePath())));
    for (corpusIndex = 0; corpusIndex < corpora.size(); ++corpusIndex)
      doCorpus();
    corpusIndex = -1;
  }
  private void doCorpus() {
    projects.clear();
    projects.addAll(as.list(system.foldersIn(corpusAbsolutePath())));
    listeners.beginCorpus();
    for (projectIndex = 0; projectIndex < projects.size(); ++projectIndex)
      doProject();
    listeners.endCorpus();
  }
  private void doFile(File f) {
    file = f;
    listeners.beginFile();
    if (FileHeuristics.noTestMethods(file()))
      try {
        fileContents = FileUtils.read(file());
        listeners.beginFile();
        listeners.endFile();
      } catch (final IOException ¢) {
        note.io(¢, "File = " + file());
      }
    listeners.endFile();
    file = null;
  }
  private void doProject() {
    listeners.beginProject();
    new FilesGenerator(".java").from(projectAbsolutePath()).forEach(λ -> doFile(λ));
    listeners.endProject();
  }

  public class Hook extends Traverse.Events.Delegator.ToInner {
    @Override public final Hook inner() {
      return self();
    }
  }
}
