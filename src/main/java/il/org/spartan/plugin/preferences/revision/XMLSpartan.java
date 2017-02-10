package il.org.spartan.plugin.preferences.revision;

import java.io.*;
import java.util.*;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.w3c.dom.*;
import org.xml.sax.*;

import il.org.spartan.plugin.preferences.PreferencesResources.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.utils.*;

/** TODO Ori Roth: document class {@link }
 * @author Ori Roth <tt>ori.rothh@gmail.com</tt>
 * @since 2017-02-01 */
public class XMLSpartan {
  private static final String CURRENT_VERSION = "1";
  private static final String FILE_NAME = "spartan.xml";
  private static final String TIPPER = "tipper";
  private static final String CATEGORY = "category";
  private static final String ENABLED = "enabled";
  private static final String TIPPER_ID = "tipper_id";
  private static final String CATEGORY_ID = "category_id";

  /** TODO Ori Roth: Stub 'XMLSpartan::getTippersByCategories' (created on
   * 2017-02-10)." );
   * <p>
   * @param p
   * @param includeEmptyCategories
   * @return
   *         <p>
   *         [[SuppressWarningsSpartan]] */
  public static Map<SpartanCategory, SpartanTipper[]> getTippersByCategories(final IProject p, final boolean includeEmptyCategories) {
    final Map<SpartanCategory, SpartanTipper[]> $ = new HashMap<>();
    final Document d = getFile(p);
    if (d == null)
      return $;
    final NodeList l = d.getElementsByTagName(CATEGORY);
    if (l == null)
      return $;
    for (int i = 0; i < l.getLength(); ++i) {
      Element e = (Element) l.item(i);
      // categories are disabled because we use container mode in dialog
      SpartanCategory se = new SpartanCategory(e.getAttribute(CATEGORY_ID), false);
      List<SpartanTipper> ts = new ArrayList<>();
      NodeList ll = e.getElementsByTagName(TIPPER);
      if (ll != null)
        for (int j = 0; j < ll.getLength(); ++j) {
          Element ee = (Element) ll.item(j);
          SpartanTipper st = new SpartanTipper(ee.getAttribute(TIPPER_ID), Boolean.parseBoolean(ee.getAttribute(ENABLED)), se);
          ts.add(st);
          se.addChild(st);
        }
      if (includeEmptyCategories || !ts.isEmpty())
        $.put(se, ts.toArray(new SpartanTipper[ts.size()]));
    }
    return $;
  }

  /** TODO Ori Roth: Stub 'XMLSpartan::updateEnabledTippers' (created on
   * 2017-02-10)." );
   * <p>
   * @param p
   * @param es
   *        <p>
   *        [[SuppressWarningsSpartan]] */
  public static void updateEnabledTippers(final IProject p, Set<String> es) {
    final Document d = getFile(p);
    if (d == null)
      return;
    final NodeList l = d.getElementsByTagName(CATEGORY);
    if (l == null)
      return;
    for (int i = 0; i < l.getLength(); ++i) {
      NodeList ll = ((Element) l.item(i)).getElementsByTagName(TIPPER);
      if (ll != null)
        for (int j = 0; j < ll.getLength(); ++j) {
          Element ee = (Element) ll.item(j);
          if (es.contains(ee.getAttribute(TIPPER_ID)))
            ee.setAttribute(ENABLED, "true");
          else
            ee.setAttribute(ENABLED, "false");
        }
    }
    commit(p, d);
  }

  /** TODO Ori Roth: Stub 'XMLSpartanParser::getFile' (created on 2017-02-01)."
   * );
   * <p>
   * @return
   *         <p>
   *         [[SuppressWarningsSpartan]] */
  private static Document getFile(final IProject p) {
    try {
      return getFileInner(p);
    } catch (final ParserConfigurationException | CoreException | SAXException | IOException ¢) {
      monitor.log(¢);
      ¢.printStackTrace();
      return null;
    }
  }

  /** TODO Ori Roth: Stub 'XMLSpartanParser::getFileInner' (created on
   * 2017-02-01)." );
   * <p>
   * @return
   * @throws ParserConfigurationException
   * @throws SAXException
   * @throws IOException
   *         <p>
   *         [[SuppressWarningsSpartan]]
   * @throws CoreException */
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
      commit(fl, i);
    }
    Document $ = b.parse(fl.getContents());
    if ($ == null)
      return null;
    final Element e = $.getDocumentElement();
    if (e == null)
      return null;
    e.normalize();
    return $;
  }

  /** TODO Ori Roth: Stub 'XMLSpartan::initialize' (created on 2017-02-01)." );
   * <p>
   * @param di 
   * @param di 
   * @param newDocument
   * @return
   *         <p>
   *         [[SuppressWarningsSpartan]] */
  private static Document initialize(final Document d) {
    if (d == null)
      return null;
    if (d.getElementById("spartan") != null)
      return d;
    final Element e = d.createElement("spartan");
    e.setAttribute("version", CURRENT_VERSION);
    Map<TipperGroup, Element> groups = new HashMap<>();
    for (final TipperGroup g : TipperGroup.values())
      createEnabledNodeChild(d, e, g, groups);
    Set<String> seen = new HashSet<>();
    for (final Tipper<?> t : Toolbox.freshCopyOfAllTippers().getAllTippers())
      createEnabledNodeChild(d, t, seen, groups);
    d.appendChild(e);
    d.setXmlStandalone(true); // TODO Roth: does not seam to work
    return d;
  }

  /** TODO Ori Roth: Stub 'XMLSpartan::createEnabledNodeChild' (created on
   * 2017-02-01)." );
   * <p>
   * @param e
   * @param t
   *        <p>
   *        [[SuppressWarningsSpartan]]
   * @param seen
   * @param groups */
  private static void createEnabledNodeChild(final Document d, Tipper<?> t, Set<String> seen, Map<TipperGroup, Element> groups) {
    if (d == null || t == null || seen == null || groups == null)
      return;
    final String n = t.getClass().getSimpleName();
    if (seen.contains(n))
      return;
    final Element $ = d.createElement(TIPPER);
    if ($ == null)
      return;
    $.setAttribute(ENABLED, (t instanceof Core) + "");
    $.setAttribute(TIPPER_ID, n);
    seen.add(n);
    groups.get(Toolbox.groupFor(t.getClass())).appendChild($);
  }

  /** TODO Ori Roth: Stub 'XMLSpartan::createEnabledNodeChild' (created on
   * 2017-02-09)." );
   * <p>
   * @param d
   * @param e
   * @param g
   *        <p>
   *        [[SuppressWarningsSpartan]] */
  private static void createEnabledNodeChild(Document d, Element e, TipperGroup g, Map<TipperGroup, Element> groups) {
    if (d == null || e == null || g == null || groups == null)
      return;
    final Element $ = d.createElement(CATEGORY);
    if ($ == null)
      return;
    $.setAttribute(CATEGORY_ID, g.name());
    e.appendChild($);
    groups.put(g, $);
  }

  /** TODO Ori Roth: Stub 'XMLSpartan::commit' (created on 2017-02-01)." );
   * <p>
   * @param f
   * @param d
   * @return
   *         <p>
   *         [[SuppressWarningsSpartan]] */
  private static boolean commit(final IFile f, final Document d) {
    final DOMSource domSource = new DOMSource(d);
    final StringWriter writer = new StringWriter();
    final StreamResult result = new StreamResult(writer);
    final TransformerFactory tf = TransformerFactory.newInstance();
    try {
      Transformer t = tf.newTransformer();
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
  
  /** TODO Ori Roth: Stub 'XMLSpartan::commit' (created on 2017-02-01)." );
   * <p>
   * @param p
   * @param d
   * @return
   *         <p>
   *         [[SuppressWarningsSpartan]] */
  private static boolean commit(final IProject p, final Document d) {
    final IFile f = p.getFile(FILE_NAME);
    return !(f == null || !f.exists()) && commit(f, d);
  }

  public abstract static class SpartanElement {
    public static SpartanElement[] EMPTY = new SpartanElement[0];
    private String name;
    private boolean enabled;

    public SpartanElement(String name, boolean enabled) {
      this.name = name;
      this.enabled = enabled;
    }

    public String name() {
      return name;
    }

    public boolean enabled() {
      return enabled;
    }

    @SuppressWarnings("static-method") public boolean hasChildren() {
      return false;
    }

    @SuppressWarnings("static-method") public SpartanElement[] getChildren() {
      return EMPTY;
    }
  }

  public static class SpartanTipper extends SpartanElement {
    private SpartanCategory parent;

    public SpartanTipper(String name, boolean enabled, SpartanCategory parent) {
      super(name, enabled);
      this.parent = parent;
    }

    public SpartanCategory parent() {
      return parent;
    }
  }

  public static class SpartanCategory extends SpartanElement {
    private List<SpartanElement> children;

    public SpartanCategory(String name, boolean enabled) {
      super(name, enabled);
      children = new ArrayList<>();
    }

    public void addChild(SpartanTipper ¢) {
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
