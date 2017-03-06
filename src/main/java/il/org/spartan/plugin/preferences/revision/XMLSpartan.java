package il.org.spartan.plugin.preferences.revision;

import java.io.*;
import java.util.*;
import java.util.Map.*;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.dom.*;
import org.w3c.dom.*;
import org.xml.sax.*;

import static java.util.stream.Collectors.*;

import il.org.spartan.plugin.preferences.revision.PreferencesResources.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tippers.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.utils.*;

/** Support for plugin's XML configurations file for projects. Currently
 * describes what tippers are enabled for the project.
 * @author Ori Roth <tt>ori.rothh@gmail.com</tt>
 * @since 2017-02-01 */
public class XMLSpartan {
  private static final String CURRENT_VERSION = "2.0";
  private static final String BASE = "spartan";
  private static final String VERSION = "version";
  private static final String FILE_NAME = "spartan.xml";
  private static final String TIPPER = "tipper";
  private static final String ENABLED = "enabled";
  private static final String TIPPER_ID = "id";
  private static final String[][] EMPTY_PREVIEW = { { "[no available preview]", "[no available preview]" } };
  private static final Collection<Class<? extends Tipper<? extends ASTNode>>> NON_CORE = new HashSet<>();
  static {
    Collections.addAll(NON_CORE, //
        CatchClauseRenameParameterToCent.class, //
        EnhancedForParameterRenameToCent.class, //
        ForRenameInitializerToCent.class, //
        ForMoveLastIntoUpdaters.class, //
        InfixExpressionConcatentateCompileTime.class, //
        LambdaRenameSingleParameterToLambda.class, //
        MethodDeclarationRenameReturnToDollar.class, //
        MethodDeclarationRenameSingleParameterToCent.class, //
        MethodInvocationToStringToEmptyStringAddition.class, //
        ModifierRedundant.class, //
        SingelVariableDeclarationUnderscoreDoubled.class, //
        SingleVariableDeclarationEnhancedForRenameParameterToCent.class //
    );
  }

  /** Computes enabled tippers by categories for the project. If some error
   * occur (such as a corrupted XML file), an empty map is returned.
   * @param p JD
   * @return enabled tippers for the project */
  public static Map<SpartanCategory, SpartanTipper[]> getTippersByCategories(final IProject p) {
    final Map<SpartanCategory, SpartanTipper[]> $ = new HashMap<>();
    final Document d = getFile(p);
    if (d == null)
      return $;
    final NodeList ns = d.getElementsByTagName(TIPPER);
    if (ns == null)
      return $;
    final Map<TipperGroup, SpartanCategory> tcs = new HashMap<>();
    final Map<TipperGroup, List<SpartanTipper>> tgs = new HashMap<>();
    for (int i = 0; i < ns.getLength(); ++i) {
      final Element e = (Element) ns.item(i);
      final Class<?> tc = Toolbox.Tables.TipperIDClassTranslationTable.get(e.getAttribute(TIPPER_ID));
      if (tc == null)
        continue;
      final String description = Toolbox.Tables.TipperDescriptionCache.get(tc);
      final String[][] preview = Toolbox.Tables.TipperExamplesCache.get(tc);
      final TipperGroup g = Toolbox.Tables.TipperObjectByClassCache.get(tc).tipperGroup();
      if (!tgs.containsKey(g)) {
        tgs.put(g, new ArrayList<>());
        tcs.put(g, new SpartanCategory(g.name(), false));
      }
      final SpartanTipper st = new SpartanTipper(tc.getSimpleName(), Boolean.parseBoolean(e.getAttribute(ENABLED)), tcs.get(g),
          description != null ? description : "No description available", preview != null && preview.length > 0 ? preview : EMPTY_PREVIEW);
      tcs.get(g).addChild(st);
      tgs.get(g).add(st);
    }
    for (final Entry<TipperGroup, List<SpartanTipper>> ¢ : tgs.entrySet())
      $.put(tcs.get(¢.getKey()), ¢.getValue().toArray(new SpartanTipper[¢.getValue().size()]));
    return $;
  }

  /** Computes enabled tippers for project. If some error occur (such as a
   * corrupted XML file), full tippers collection is returned.
   * @param p JD
   * @return enabled tippers for project */
  @SuppressWarnings("unchecked") public static Set<Class<Tipper<? extends ASTNode>>> enabledTippers(final IProject p) {
    final Set<Class<Tipper<? extends ASTNode>>> $ = Toolbox.freshCopyOfAllTippers().getAllTippers().stream()
        .map(λ -> (Class<Tipper<? extends ASTNode>>) λ.getClass()).collect(toSet());
    if (p == null)
      return $;
    final Map<SpartanCategory, SpartanTipper[]> m = getTippersByCategories(p);
    if (m == null)
      return $;
    final Set<String> ets = m.values().stream().flatMap(Arrays::stream).filter(SpartanElement::enabled).map(SpartanElement::name).collect(toSet());
    final Collection<Class<Tipper<? extends ASTNode>>> l = new ArrayList<>($);
    for (final Class<Tipper<? extends ASTNode>> ¢ : l)
      if (!ets.contains(¢.getSimpleName()))
        $.remove(¢);
    return $;
  }

  /** Updates the project's XML file to enable given tippers.
   * @param p JD
   * @param ss enabled tippers by name */
  public static void updateEnabledTippers(final IProject p, final Collection<String> ss) {
    final Document d = getFile(p);
    if (d == null)
      return;
    final NodeList l = d.getElementsByTagName(TIPPER);
    if (l == null)
      return;
    for (int i = 0; i < l.getLength(); ++i) {
      final Element e = (Element) l.item(i);
      final String nameByID = Toolbox.Tables.TipperIDNameTranslationTable.get(e.getAttribute(TIPPER_ID));
      e.setAttribute(ENABLED, nameByID != null && ss.contains(nameByID) ? "true" : "false");
    }
    commit(p, d);
  }

  /** Return XML file for given project. Creates one if absent.
   * @param p JD
   * @return XML file for project */
  private static Document getFile(final IProject $) {
    try {
      return getFileInner($);
    } catch (final ParserConfigurationException | CoreException | SAXException | IOException ¢) {
      monitor.log(¢);
      ¢.printStackTrace();
      return null;
    }
  }

  /** Return XML file for given project. Creates one if absent.
   * @param p JD
   * @return XML file for project
   * @throws ParserConfigurationException
   * @throws SAXException
   * @throws IOException
   * @throws CoreException
   * @return XML file for project */
  private static Document getFileInner(final IProject p) throws CoreException, ParserConfigurationException, SAXException, IOException {
    if (p == null || !p.exists() || !p.isOpen())
      return null;
    final IFile fl = p.getFile(FILE_NAME);
    if (fl == null)
      return null;
    final DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
    if (df == null)
      return null;
    final DocumentBuilder b = df.newDocumentBuilder();
    if (b == null)
      return null;
    if (!fl.exists()) {
      final Document i = initialize(b.newDocument());
      if (i == null)
        return null;
      fl.create(new ByteArrayInputStream("".getBytes()), false, new NullProgressMonitor());
      if (!commit(fl, i) || !fl.exists())
        return null;
    }
    Document $ = b.parse(fl.getContents());
    if ($ == null)
      return null;
    final Element e = $.getDocumentElement();
    if (e == null)
      return null;
    e.normalize();
    final NodeList bs = $.getElementsByTagName(BASE);
    if (bs == null || bs.getLength() != 1 || !validate($, ((Element) bs.item(0)).getAttribute(VERSION))) {
      $ = initialize(b.newDocument());
      commit(fl, $);
    }
    return $;
  }

  /** Initialize XML document. Enables all tippers, except declared non core
   * tippers.
   * @param d JD
   * @return given document */
  private static Document initialize(final Document $) {
    if ($ == null)
      return null;
    if ($.getElementById(BASE) != null)
      return $;
    final Element e = $.createElement("spartan");
    e.setAttribute(VERSION, CURRENT_VERSION);
    final Collection<String> seen = new HashSet<>();
    Toolbox.freshCopyOfAllTippers().getAllTippers().forEach(λ -> createEnabledNodeChild($, λ, seen, e));
    $.appendChild(e);
    $.setXmlStandalone(true); // TODO Roth: does not seem to work
    return $;
  }

  /** Adds a new tipper to the XML document.
   * @param d JD
   * @param t JD
   * @param seen seen tippers by name. Tippers can appear multiple times in the
   *        {@link Toolbox}, so we should avoid duplications
   * @param e base element "spartan" */
  private static void createEnabledNodeChild(final Document d, final Tipper<?> t, final Collection<String> seen, final Node e) {
    if (d == null || t == null || seen == null || e == null)
      return;
    final String n = t.getClass().getSimpleName();
    if (seen.contains(n))
      return;
    final Element $ = d.createElement(TIPPER);
    if ($ == null)
      return;
    $.setAttribute(ENABLED, !NON_CORE.contains(t.getClass()) + "");
    $.setAttribute(TIPPER_ID, ObjectStreamClass.lookup(t.getClass()).getSerialVersionUID() + "");
    seen.add(n);
    e.appendChild($);
  }

  /** Writes XML dom object to file.
   * @param f JD
   * @param d JD
   * @return true iff the operation has been completed successfully */
  private static boolean commit(final IFile f, final Document d) {
    final Source domSource = new DOMSource(d);
    final StringWriter writer = new StringWriter();
    final Result result = new StreamResult(writer);
    final TransformerFactory tf = TransformerFactory.newInstance();
    try {
      final Transformer t = tf.newTransformer();
      t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
      t.setOutputProperty(OutputKeys.METHOD, "xml");
      t.setOutputProperty(OutputKeys.INDENT, "yes");
      t.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
      t.setOutputProperty(OutputKeys.STANDALONE, "yes");
      t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
      t.transform(domSource, result);
      f.setContents(new ByteArrayInputStream((writer + "").getBytes()), false, false, new NullProgressMonitor());
      return true;
    } catch (CoreException | TransformerException ¢) {
      monitor.log(¢);
      return false;
    }
  }

  /** Writes XML dom object to project.
   * @param p JD
   * @param d JD
   * @return true iff the operation has been completed successfully */
  private static boolean commit(final IProject p, final Document d) {
    final IFile $ = p.getFile(FILE_NAME);
    return $ != null && $.exists() && commit($, d);
  }

  /** Manipulates documents with different version / corrupted files.
   * @param $ plugin's XML document
   * @param version document's version
   * @return true iff the document is valid, and does not require
   *         initialization */
  private static boolean validate(@SuppressWarnings("unused") final Document $, final String version) {
    return CURRENT_VERSION.equals(version);
  }

  /** Describes an XML element for plugin's XML file.
   * @author Ori Roth <tt>ori.rothh@gmail.com</tt>
   * @since 2017-02-25 */
  public abstract static class SpartanElement {
    public static SpartanElement[] EMPTY = new SpartanElement[0];
    private final String name;
    private boolean enabled;

    public SpartanElement(final String name, final boolean enabled) {
      this.name = name;
      this.enabled = enabled;
    }

    public String name() {
      return name;
    }

    public boolean enabled() {
      return enabled;
    }

    public void enable(final boolean enable) {
      enabled = enable;
    }

    @SuppressWarnings("static-method") public boolean hasChildren() {
      return false;
    }

    @SuppressWarnings("static-method") public SpartanElement[] getChildren() {
      return EMPTY;
    }
  }

  /** Describes an XML tipper element for plugin's XML file. The tipper is
   * connected to {@link SpartanCategory}, and has a description.
   * @author Ori Roth <tt>ori.rothh@gmail.com</tt>
   * @since 2017-02-25 */
  public static class SpartanTipper extends SpartanElement {
    private final SpartanCategory parent;
    private final String description;
    private final String[][] preview;

    public SpartanTipper(final String name, final boolean enabled, final SpartanCategory parent, final String description, final String[][] preview) {
      super(name, enabled);
      this.parent = parent;
      this.description = description;
      this.preview = preview;
    }

    public SpartanCategory parent() {
      return parent;
    }

    public String description() {
      return description;
    }

    public String[][] preview() {
      return preview;
    }
  }

  /** Describes an XML category element for plugin's XML file. The category has
   * a list of its {@link SpartanElement} children.
   * @author Ori Roth <tt>ori.rothh@gmail.com</tt>
   * @since 2017-02-25 */
  public static class SpartanCategory extends SpartanElement {
    private final List<SpartanElement> children;

    public SpartanCategory(final String name, final boolean enabled) {
      super(name, enabled);
      children = new ArrayList<>();
    }

    public void addChild(final SpartanTipper ¢) {
      children.add(¢);
    }

    /* (non-Javadoc)
     *
     * @see
     * il.org.spartan.plugin.preferences.revision.ProjectPreferencesHandler.
     * SpartanElement#hasChildren() */
    @Override public boolean hasChildren() {
      return !children.isEmpty();
    }

    /* (non-Javadoc)
     *
     * @see
     * il.org.spartan.plugin.preferences.revision.ProjectPreferencesHandler.
     * SpartanElement#getChildren() */
    @Override public SpartanElement[] getChildren() {
      return children.toArray(new SpartanElement[children.size()]);
    }
  }
}
