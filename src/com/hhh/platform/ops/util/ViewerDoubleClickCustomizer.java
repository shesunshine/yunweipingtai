package com.hhh.platform.ops.util;

import com.hhh.platform.advisors.framework.binding.EntityViewerBindingHandler;
import com.hhh.platform.advisors.table.CheckboxTableViewerWithDoubleClick;
import com.hhh.platform.advisors.table.DoubleClickCustomizer;

public class ViewerDoubleClickCustomizer extends DoubleClickCustomizer {

	private EntityViewerBindingHandler handler;
	public ViewerDoubleClickCustomizer(EntityViewerBindingHandler handler) {
		this.handler = handler;
	}

	@Override
	public void doubleClick(CheckboxTableViewerWithDoubleClick viewer) {
	}
}
