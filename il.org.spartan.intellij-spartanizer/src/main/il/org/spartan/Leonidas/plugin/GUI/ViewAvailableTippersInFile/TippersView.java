package il.org.spartan.Leonidas.plugin.GUI.ViewAvailableTippersInFile;

import com.intellij.psi.JavaRecursiveElementVisitor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import il.org.spartan.Leonidas.plugin.GUI.LeonidasIcon;
import il.org.spartan.Leonidas.plugin.Toolbox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Amir on 20-06-2017.
 */
public class TippersView extends JFrame{
    private static boolean active;
    private JPanel mainPanel;
    private JScrollPane scrollPane;
    private JList list;
    private TippersList cl;

    public TippersView(PsiFile pf){
        super("Available Tippers In Current File");
        if(active)
			return;
        active = true;
        LeonidasIcon.apply(this);

        Set<String> tippers= new HashSet<>();
        Toolbox toolbox = Toolbox.getInstance();
        pf.accept(new JavaRecursiveElementVisitor() {
            @Override
            public void visitElement(PsiElement e) {
                super.visitElement(e);
                tippers.addAll(toolbox.getAvailableTipsInfo(e));
            }
        });

        setContentPane(mainPanel);
        cl = new TippersList(this);
        for(String tip : tippers)
		 cl.addTipper(new JLabel(tip));

        this.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                active = false;
            }
        });
        scrollPane.setViewportView(cl);
        setPreferredSize(new Dimension(800, 800));
        setResizable(false);
        pack();
        setVisible(true);
    }

}
