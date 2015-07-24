package com.hhh.platform.ops.perspective;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import com.hhh.platform.ops.view.LogRunView;
import com.hhh.platform.ops.view.ServerRunView;

public class ServerRunPerspective implements IPerspectiveFactory {
	public static final String ID = "com.hhh.platform.ops.perspective.server.run";

	@Override
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);
		layout.setFixed(true);
		layout.addView(ServerRunView.ID, IPageLayout.LEFT, 1.0f, editorArea);
	}

}
