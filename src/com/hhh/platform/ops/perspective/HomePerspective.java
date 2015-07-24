package com.hhh.platform.ops.perspective;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import com.hhh.platform.ops.view.HomeLeftView;

public class HomePerspective implements IPerspectiveFactory {
	public static final String ID = "com.hhh.platform.ops.perspective.home";

	@Override
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);
		layout.setFixed(true);
		layout.addStandaloneView(HomeLeftView.ID, false, IPageLayout.LEFT, 0.25f, editorArea);
	}

}
