package com.hhh.platform.ops.perspective;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import com.hhh.platform.ops.view.ProductDeployView;

public class ProductDeployPerspective implements IPerspectiveFactory {
	public static final String ID = "com.hhh.platform.ops.perspective.product.deploy";

	@Override
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);
		layout.setFixed(true);
		layout.addView(ProductDeployView.ID, IPageLayout.LEFT, 1.0f, editorArea);
	}

}
