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
  TableViewer viewer;
  Function<ImageData, ImageData> scaler = λ -> λ.scaledTo(100, 100);
  final Map<SpartanViewSection, ToolTip> tooltips = new HashMap<>();
  final Map<ToolTip, Rectangle> bounds = new HashMap<>();
  static final SpartanViewSection[] sections = { //
      section("Spartanize1", ICON, mouseListener(λ -> System.out.println("!"), λ -> System.out.println("@"), λ -> System.out.println("#"))), //
      section("Spartanize2", ICON, mouseListener(λ -> {/**/}, λ -> {/**/}, λ -> {/**/})) //
  };

  @Override public void createPartControl(Composite parent) {
    viewer = new TableViewer(parent);
    viewer.setContentProvider(ArrayContentProvider.getInstance());
    viewer.setLabelProvider(new LabelProvider() {
      @Override public String getText(@SuppressWarnings("unused") Object element) {
        return "";
      }

      @Override public Image getImage(Object element) {
        return Dialogs.image(((SpartanViewSection) element).image, VIEW_ID + ((SpartanViewSection) element).tooltip, scaler);
      }
    });
    viewer.setInput(sections);
    getSite().setSelectionProvider(viewer);
    viewer.getTable().addMouseListener(new MouseListener() {
      @Override public void mouseUp(MouseEvent e) {
        Optional.ofNullable(getSVS(e)).ifPresent(λ -> λ.ml.mouseUp(e));
      }

      @Override public void mouseDown(MouseEvent e) {
        Optional.ofNullable(getSVS(e)).ifPresent(λ -> λ.ml.mouseDown(e));
      }

      @Override public void mouseDoubleClick(MouseEvent e) {
        Optional.ofNullable(getSVS(e)).ifPresent(λ -> λ.ml.mouseDoubleClick(e));
      }

      SpartanViewSection getSVS(MouseEvent e) {
        return Optional.of(e).map(λ -> new Point(λ.x, λ.y)) //
            .map(λ -> viewer.getTable().getItem(λ)).map(λ -> λ.getData()) //
            .filter(λ -> λ instanceof SpartanViewSection).map(SpartanViewSection.class::cast).orElse(null);
      }
    });
    viewer.getTable().addFocusListener(new FocusListener() {
      @Override public void focusLost(@SuppressWarnings("unused") FocusEvent __) {
        tooltips.values().forEach(λ -> λ.setVisible(false));
      }

      @Override public void focusGained(@SuppressWarnings("unused") FocusEvent __) {
        //
      }
    });
    viewer.getTable().addListener(SWT.MouseHover, new Listener() {
      @Override public void handleEvent(Event e) {
        final Rectangle r = e.getBounds();
        if (r == null)
          return;
        TableItem ti = viewer.getTable().getItem(new Point(e.x, e.y));
        if (ti == null)
          return;
        SpartanViewSection svs = Optional.of(ti) //
            .map(λ -> λ.getData()).filter(λ -> λ instanceof SpartanViewSection) //
            .map(SpartanViewSection.class::cast).orElse(null);
        if (svs == null)
          return;
        tooltips.values().forEach(λ -> λ.setVisible(false));
        if (!tooltips.containsKey(svs)) {
          final ToolTip tt = new ToolTip(viewer.getControl().getShell(), SWT.ICON_INFORMATION);
          tt.setMessage(svs.tooltip);
          tt.setAutoHide(true);
          tooltips.put(svs, tt);
        }
        final Rectangle tp = viewer.getTable().getBounds();
        final Point tl = viewer.getTable().toDisplay(tp.x + r.x, tp.y + r.y);
        final Rectangle tr = new Rectangle(tl.x, tl.y, r.width, r.height);
        final ToolTip tt = tooltips.get(svs);
        bounds.put(tt, tr);
        final Point m = Eclipse.mouseLocation();
        tt.setLocation(m.x, m.y - Eclipse.TOOLTIP_HEIGHT);
        tt.setVisible(true);
      }
    });
    final Listener killTooltips = e -> tooltips.values().forEach(λ -> λ.setVisible(false));
    viewer.getTable().addListener(SWT.MouseMove, killTooltips);
    viewer.getTable().addListener(SWT.MouseWheel, killTooltips);
  }

  @Override public void setFocus() {
    viewer.getControl().setFocus();
  }

  public static SpartanViewSection section(String tooltip, String image, MouseListener l) {
    return new SpartanViewSection(tooltip, image, l);
  }

  protected static class SpartanViewSection {
    public final String tooltip;
    public final String image;
    public final String id;
    public final MouseListener ml;
    private final int idi;
    private static int idGen;

    public SpartanViewSection(String tooltip, String image, MouseListener ml) {
      this.tooltip = tooltip;
      this.image = image;
      idi = idGen++;
      id = idi + "";
      this.ml = ml;
    }

    @Override public boolean equals(Object ¢) {
      return ¢ instanceof SpartanViewSection && ((SpartanViewSection) ¢).id == id;
    }

    @Override public int hashCode() {
      return idi;
    }
  }
}
