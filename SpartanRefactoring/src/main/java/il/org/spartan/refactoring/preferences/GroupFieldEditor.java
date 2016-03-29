package il.org.spartan.refactoring.preferences;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.DialogPage;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

/**
 * A {@link FieldEditor} designed to store multiple controls within a group panel widget,
 * to be used in conjunction with an {@link FieldEditorPreferencePage} instance.
 * <p>
 * <u>Usage:</u><br>
 * 1) Create a new {@link GroupFieldEditor} object.<br>
 * 2) Add {@link FieldEditor} objects using the {@link GroupFieldEditor#add(FieldEditor)} method.
 *    Each {@link FieldEditor} should be initialized to have the return value of {@link GroupFieldEditor#getFieldEditor()} as its parent.<br>
 * 3) Add the {@link GroupFieldEditor} to the parent as usual
 * 
 * @author alf (original)
 * @author Daniel Mittelman (fixed and revised)
 * @since 29/03/2016
 * 
 */
public class GroupFieldEditor extends FieldEditor {
	private String title;
	private int	numColumns;
	private List<FieldEditor> members = new ArrayList<>();
	private Group group;
	private Composite parent;
	
	private boolean	initialized = false;
	
	private static final int GROUP_MARGIN = 5;
	private static final int GROUP_PADDING = 8;
	
	/**
	 * Create a group of {@link FieldEditor} objects
	 * 
	 * @param labelText (optional) the text that will appear in the top label. For no label, pass {@code null}
	 * @param fieldEditorParent the widget's parent, usually {@link FieldEditorPreferencePage#getFieldEditorParent()}
	 */
	public GroupFieldEditor(final String labelText, final Composite fieldEditorParent) {
		this.title = labelText == null ? "" : labelText;
		this.parent = fieldEditorParent;
		this.numColumns = 0;
		
		group = new Group(this.parent, SWT.SHADOW_OUT);
		group.setText(this.title);
	}
	
	/**
	 * Returns the parent for all the FieldEditors inside of this group.
	 * In this class, the actual {@link Group} object is returned
	 * 
	 * @return the parent {@link Composite} object
	 */
	public Composite getFieldEditor() {
		return group;
	}
	
	/**
	 * Adds a new {@link FieldEditor} object to the group.
	 * Controls must be added before the group is drawn to the parent.
	 */
	public void add(FieldEditor fieldEditor) {
		if(initialized)
			throw new RuntimeException("The GroupFieldEditor has already been drawn, new fields cannot be added at this time");
		
		members.add(fieldEditor);
	}
	
	/**
	 * Initializes using the currently added field editors. 
	 */
	public void init() {
		if(!initialized) {
			doFillIntoGrid(getFieldEditor(), numColumns);
			initialized = true;
		}
	}
	
	/*
	 * (non-Javadoc) Method declared on FieldEditor.
	 */
	@Override
	protected void adjustForNumColumns(int numColumns) {
		this.numColumns = numColumns;
	}
	
	/*
	 * (non-Javadoc) Method declared on FieldEditor.
	 */
	@Override
	protected void doFillIntoGrid(Composite parentParam, int numColumns) {
		if(members == null || members.isEmpty())
			return;
		
		if(numColumns == 0) {
			for(FieldEditor fe : members)
				numColumns = Math.max(numColumns, fe.getNumberOfControls());
		}
		
		GridData data = new GridData(SWT.FILL, SWT.CENTER, true, false);
		data.horizontalIndent = 2;
		data.verticalIndent = GROUP_PADDING;
		data.horizontalSpan = numColumns;
		this.group.setLayoutData(data);
		
		GridLayout groupLayout = new GridLayout();
		groupLayout.marginHeight = GROUP_PADDING;
		groupLayout.marginWidth = GROUP_PADDING;
		groupLayout.numColumns = numColumns;
		this.group.setLayout(groupLayout);
			
		for(FieldEditor editor : members)
			editor.fillIntoGrid(parentParam, numColumns);
		
		this.parent.layout();
		this.parent.redraw();
	}
	
	
	/*
	 * (non-Javadoc) Method declared on FieldEditor. Loads the value from the
	 * preference store and sets it to the check box.
	 */
	@Override
	protected void doLoad() {
		for(FieldEditor editor : members)
			editor.load();
	}
	
	/*
	 * (non-Javadoc) Method declared on FieldEditor. Loads the default value
	 * from the preference store and sets it to the check box.
	 */
	@Override
	protected void doLoadDefault() {
		for(FieldEditor editor : members)
			editor.loadDefault();
	}
	
	
	/*
	 * (non-Javadoc) Method declared on FieldEditor.
	 */
	@Override
	protected void doStore()
	{
		for(FieldEditor editor : members)
			editor.store();
	}
	
	@Override
	public void store() {
		doStore();
	}
	
	
	/*
	 * (non-Javadoc) Method declared on FieldEditor.
	 */
	@Override
	public int getNumberOfControls() {
		return members.size();
	}
	
	/*
	 * (non-Javadoc) Method declared on FieldEditor.
	 */
	@Override
	public void setFocus() {
		if(members != null && !members.isEmpty())
			members.iterator().next().setFocus();
	}
	
	
	/*
	 * @see FieldEditor.setEnabled
	 */
	@Override
	public void setEnabled(boolean enabled, Composite parentParam)
	{
		for(FieldEditor editor : members)
			editor.setEnabled(enabled, parentParam);
	}
	
	@Override
	public void setPreferenceStore(IPreferenceStore store) {
		super.setPreferenceStore(store);

		for(FieldEditor editor : members)
			editor.setPreferenceStore(store);
	}
	
	@Override
	public void setPage(DialogPage dialogPage)
	{
		for(FieldEditor editor : members)
			editor.setPage(dialogPage);
	}
	
	@Override
	public boolean isValid() {
		for(FieldEditor editor : members) {
			if(!editor.isValid())
				return false;
		}
		return true;
	}
}
