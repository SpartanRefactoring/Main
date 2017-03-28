package il.org.spartan.plugin;

import static il.org.spartan.plugin.Dialogs.*;
import static il.org.spartan.plugin.Eclipse.*;

import java.util.*;
import java.util.function.*;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.part.*;

public class SpartanView extends ViewPart {
  private static final String VIEW_ID = "il.org.spartan.spartanizer.view";
  private static final int BUTTON_SIZE = 100;
  TableViewer viewer;
  Table table;
  static Function<ImageData, ImageData> scaler = λ -> λ.scaledTo(BUTTON_SIZE, BUTTON_SIZE);
  final Map<SpartanViewSection, ToolTip> tooltips = new HashMap<>();
  final Map<ToolTip, Rectangle> bounds = new HashMap<>();
  static final SpartanViewSection[] sections = { //
      section("Spartanize1", ICON, mouseListener(λ -> System.out.println("!"), λ -> System.out.println("@"), λ -> System.out.println("#"))), //
      section("Spartanize2", LOGO, mouseListener(λ -> {/**/}, λ -> {/**/}, λ -> {/**/})), //
      section("Spartanize3", DELTA, mouseListener(λ -> {/**/}, λ -> {/**/}, λ -> {/**/})), //
      section("Spartanize4", CATEGORY, mouseListener(λ -> {/**/}, λ -> {/**/}, λ -> {/**/})), //
  };

  @Override public void createPartControl(final Composite parent) {
    viewer = new TableViewer(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
    table = viewer.getTable();
    viewer.setContentProvider(ArrayContentProvider.getInstance());
    viewer.setLabelProvider(new LabelProvider() {
      @Override public String getText(@SuppressWarnings("unused") final Object element) {
        return "";
      }

      @Override public Image getImage(final Object element) {
        return ((SpartanViewSection) element).image;
      }
    });
    viewer.setInput(sections);
    getSite().setSelectionProvider(viewer);
    table.addMouseListener(new MouseListener() {
      @Override public void mouseUp(final MouseEvent e) {
        viewer.setSelection(null);
        Optional.ofNullable(getSVS(e)).ifPresent(λ -> λ.ml.mouseUp(e));
      }

      @Override public void mouseDown(final MouseEvent e) {
        Optional.ofNullable(getSVS(e)).ifPresent(λ -> λ.ml.mouseDown(e));
      }

      @Override public void mouseDoubleClick(final MouseEvent e) {
        Optional.ofNullable(getSVS(e)).ifPresent(λ -> λ.ml.mouseDoubleClick(e));
      }

      SpartanViewSection getSVS(final MouseEvent e) {
        return Optional.of(e).map(λ -> new Point(λ.x, λ.y)) //
            .map(λ -> table.getItem(λ)).map(λ -> λ.getData()) //
            .filter(λ -> λ instanceof SpartanViewSection).map(SpartanViewSection.class::cast).orElse(null);
      }
    });
    table.addFocusListener(new FocusListener() {
      @Override public void focusLost(@SuppressWarnings("unused") final FocusEvent __) {
        tooltips.values().forEach(λ -> λ.setVisible(false));
      }

      @Override public void focusGained(@SuppressWarnings("unused") final FocusEvent __) {
        //
      }
    });
    table.addListener(SWT.MouseHover, e -> {
      final Rectangle r = e.getBounds();
      if (r == null)
        return;
      final TableItem ti = table.getItem(new Point(e.x, e.y));
      if (ti == null)
        return;
      final SpartanViewSection svs = Optional.of(ti) //
          .map(λ1 -> λ1.getData()).filter(λ2 -> λ2 instanceof SpartanViewSection) //
          .map(SpartanViewSection.class::cast).orElse(null);
      if (svs == null)
        return;
      tooltips.values().forEach(λ3 -> λ3.setVisible(false));
      if (!tooltips.containsKey(svs)) {
        final ToolTip tt1 = new ToolTip(viewer.getControl().getShell(), SWT.ICON_INFORMATION);
        tt1.setMessage(svs.tooltip);
        tt1.setAutoHide(true);
        tooltips.put(svs, tt1);
      }
      final Rectangle tp = table.getBounds();
      final Point tl = table.toDisplay(tp.x + r.x, tp.y + r.y);
      final Rectangle tr = new Rectangle(tl.x, tl.y, r.width, r.height);
      final ToolTip tt2 = tooltips.get(svs);
      bounds.put(tt2, tr);
      final Point m = Eclipse.mouseLocation();
      tt2.setLocation(m.x, m.y - Eclipse.TOOLTIP_HEIGHT);
      tt2.setVisible(true);
    });
    final Listener killTooltips = e -> tooltips.values().forEach(λ -> λ.setVisible(false));
    table.addListener(SWT.MouseMove, killTooltips);
    table.addListener(SWT.MouseWheel, killTooltips);
  }

  @Override public void setFocus() {
    viewer.getControl().setFocus();
  }

  public static SpartanViewSection section(final String tooltip, final String image, final MouseListener l) {
    return new SpartanViewSection(tooltip, image, l);
  }

  protected static class SpartanViewSection {
    public final String tooltip;
    public final Image image;
    public final String id;
    public final MouseListener ml;
    private final int idi;
    private static int idGen;

    public SpartanViewSection(final String tooltip, final String image, final MouseListener ml) {
      this.tooltip = tooltip;
      this.image = Dialogs.image(image, VIEW_ID + tooltip, scaler);
      idi = idGen++;
      id = idi + "";
      this.ml = ml;
    }

    @Override public boolean equals(final Object ¢) {
      return ¢ instanceof SpartanViewSection && ((SpartanViewSection) ¢).id == id;
    }

    @Override public int hashCode() {
      return idi;
    }
  }
}
