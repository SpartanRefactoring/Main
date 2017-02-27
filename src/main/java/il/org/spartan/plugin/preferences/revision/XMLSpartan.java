package il.org.spartan.plugin.preferences.revision;

import java.io.*;
import java.util.*;

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

import il.org.spartan.*;
import il.org.spartan.plugin.preferences.PreferencesResources.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.utils.*;

/** Support for plugin's XML configurations file for projects. Currently
 * describes what tippers are enabled for the project.
 * @author Ori Roth <tt>ori.rothh@gmail.com</tt>
 * @since 2017-02-01 */
public class XMLSpartan {
  private static final String CURRENT_VERSION = "1.0";
  private static final String FILE_NAME = "spartan.xml";
  private static final String TIPPER = "tipper";
  private static final String CATEGORY = "category";
  private static final String ENABLED = "enabled";
  private static final String TIPPER_ID = "id";
  private static final String CATEGORY_ID = "id";
  @SuppressWarnings("unused") @Deprecated private static final String TIPPER_DESCRIPTION = "description";
  private static final Set<Class<Tipper<? extends ASTNode>>> NON_CORE = new HashSet<>();
  // TODO Roth: decide what tippers are non-core
  // Collections.addAll(NON_CORE, stuff);

  /** Computes enabled tippers by categories for the project. If some error
   * occur (such as a corrupted XML file), an empty map is returned.
   * @param p JD
   * @param includeEmptyCategories if set, includes categories with no tippers.
   * @return enabled tippers for the project */
  public static Map<SpartanCategory, SpartanTipper[]> getTippersByCategories(final IProject p, final boolean includeEmptyCategories) {
    final Map<SpartanCategory, SpartanTipper[]> $ = new HashMap<>();
    final Document d = getFile(p);
    if (d == null)
      return $;
    final NodeList l = d.getElementsByTagName(CATEGORY);
    if (l == null)
      return $;
    for (int i = 0; i < l.getLength(); ++i) {
      final Element e = (Element) l.item(i);
      // categories are disabled because we use container mode in dialog
      final SpartanCategory se = new SpartanCategory(e.getAttribute(CATEGORY_ID), false);
      final List<SpartanTipper> ts = new ArrayList<>();
      final NodeList ll = e.getElementsByTagName(TIPPER);
      if (ll != null)
        for (int j = 0; j < ll.getLength(); ++j) {
          final Element ee = (Element) ll.item(j);
          final Class<?> tc = Toolbox.Tables.TipperIDClassTranslationTable.get(ee.getAttribute(TIPPER_ID));
          if (tc == null)
            continue;
          final String description = Toolbox.Tables.TipperdescriptionCache.get(tc);
          final SpartanTipper st = new SpartanTipper(tc.getSimpleName(), Boolean.parseBoolean(ee.getAttribute(ENABLED)), se,
              description == null ? "No available description" : description);
          ts.add(st);
          se.addChild(st);
        }
      if (includeEmptyCategories || !ts.isEmpty())
        $.put(se, ts.toArray(new SpartanTipper[ts.size()]));
    }
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
    final Map<SpartanCategory, SpartanTipper[]> m = getTippersByCategories(p, false);
    if (m == null)
      return $;
    final Set<String> ets = m.values().stream().flatMap(Arrays::stream).filter(SpartanElement::enabled).map(SpartanElement::name).collect(toSet());
    final List<Class<Tipper<? extends ASTNode>>> l = new ArrayList<>();
    l.addAll($);
    for (final Class<Tipper<? extends ASTNode>> ¢ : l)
      if (!ets.contains(¢.getSimpleName()))
        $.remove(¢);
    return $;
  }

  /** Updates the project's XML file to enable given tippers.
   * @param p JD
   * @param es enabled tippers by name */
  public static void updateEnabledTippers(final IProject p, final Set<String> es) {
    final Document d = getFile(p);
    if (d == null)
      return;
    final NodeList l = d.getElementsByTagName(CATEGORY);
    if (l == null)
      return;
    for (int i = 0; i < l.getLength(); ++i) {
      final NodeList ll = ((Element) l.item(i)).getElementsByTagName(TIPPER);
      if (ll != null)
        for (int j = 0; j < ll.getLength(); ++j) {
          final Element ee = (Element) ll.item(j);
          final String nameByID = Toolbox.Tables.TipperIDNameTranslationTable.get(ee.getAttribute(TIPPER_ID));
          if (nameByID != null && es.contains(nameByID))
            ee.setAttribute(ENABLED, "true");
          else
            ee.setAttribute(ENABLED, "false");
        }
    }
    commit(p, d);
  }

  /** Return XML file for given project. Creates one if absent.
   * @param p JD
   * @return XML file for project */
  private static Document getFile(final IProject p) {
    try {
      return getFileInner(p);
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
    final Document $ = b.parse(fl.getContents());
    if ($ == null)
      return null;
    final Element e = $.getDocumentElement();
    if (e == null)
      return null;
    e.normalize();
    return $;
  }

  /** Initialize XML document. Enables all tippers, except declared non core
   * tippers.
   * @param d JD
   * @return given document */
  private static Document initialize(final Document d) {
    if (d == null)
      return null;
    if (d.getElementById("spartan") != null)
      return d;
    final Element e = d.createElement("spartan");
    e.setAttribute("version", CURRENT_VERSION);
    final Map<TipperGroup, Element> groups = new HashMap<>();
    as.list(TipperGroup.values()).forEach(g -> createEnabledNodeChild(d, e, g, groups));
    final Set<String> seen = new HashSet<>();
    Toolbox.freshCopyOfAllTippers().getAllTippers().forEach(t -> createEnabledNodeChild(d, t, seen, groups));
    d.appendChild(e);
    d.setXmlStandalone(true); // TODO Roth: does not seem to work
    return d;
  }

  /** Adds a new category to the XML document.
   * @param d JD
   * @param e base XML element (spartan)
   * @param g category to be added
   * @param groups documents created element for the category */
  private static void createEnabledNodeChild(final Document d, final Element e, final TipperGroup g, final Map<TipperGroup, Element> groups) {
    if (d == null || e == null || g == null || groups == null)
      return;
    final Element $ = d.createElement(CATEGORY);
    if ($ == null)
      return;
    $.setAttribute(CATEGORY_ID, g.name());
    e.appendChild($);
    groups.put(g, $);
  }

  /** Adds a new tipper to the XML document.
   * @param d JD
   * @param t JD
   * @param seen seen tippers by name. Tippers can appear multiple times in the
   *        {@link Toolbox}, so we should avoid duplications
   * @param groups maps tipper category to already created category element, to
   *        which we should add the new tipper element as a child */
  private static void createEnabledNodeChild(final Document d, final Tipper<?> t, final Set<String> seen, final Map<TipperGroup, Element> groups) {
    if (d == null || t == null || seen == null || groups == null)
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
    groups.get(Toolbox.groupFor(t.getClass())).appendChild($);
  }

  /** Writes XML dom object to file.
   * @param f JD
   * @param d JD
   * @return true iff the operation has been completed successfully */
  private static boolean commit(final IFile f, final Document d) {
    final DOMSource domSource = new DOMSource(d);
    final StringWriter writer = new StringWriter();
    final StreamResult result = new StreamResult(writer);
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
      f.setContents(new ByteArrayInputStream(writer.toString().getBytes()), false, false, new NullProgressMonitor());
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
    final IFile f = p.getFile(FILE_NAME);
    return !(f == null || !f.exists()) && commit(f, d);
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

    public SpartanTipper(final String name, final boolean enabled, final SpartanCategory parent, final String description) {
      super(name, enabled);
      this.parent = parent;
      this.description = description;
    }

    public SpartanCategory parent() {
      return parent;
    }

    public String description() {
      return description;
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
