package com.hhh.platform.ops.perspective;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import com.hhh.platform.ops.view.LogExceptionView;

public class LogExceptionPerspective implements IPerspectiveFactory {
	public static final String ID = "com.hhh.platform.ops.perspective.log.exception";

	@Override
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);
		layout.setFixed(true);
		layout.addView(LogExceptionView.ID, IPageLayout.LEFT, 1.0f, editorArea);
	}

}
