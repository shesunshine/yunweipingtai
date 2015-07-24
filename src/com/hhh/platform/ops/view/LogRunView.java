package com.hhh.platform.ops.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchWindow;

import com.hhh.platform.advisors.framework.binding.CommonEntity;
import com.hhh.platform.advisors.framework.binding.EntityViewPart;
import com.hhh.platform.ops.entity.LogPerformanceEntity;
import com.hhh.platform.ops.entity.LogRunEntity;

public class LogRunView extends EntityViewPart {
	
	public static final String ID = "com.hhh.platform.ops.view.log.run";

	private LogRunEntity currentEntity;

	public static String TABLE_ROW_ID_KEY = "a.log_id";

	private Composite top;

	private Composite parent;

	@Override
	public void createPartControl2(Composite parent) {
		this.parent = parent;
		this.currentEntity = new LogRunEntity(TABLE_ROW_ID_KEY);
		parent.setLayout(new FillLayout());
		top = new Composite(parent, SWT.NONE);
		this.currentEntity.open(top, this);
		this.rebuildActionBars();
		this.setViewActive();
	}

	@Override
	public CommonEntity getEntity() {
		return currentEntity;
	}

	private void setViewActive() {
		IWorkbenchWindow window = this.getSite().getWorkbenchWindow();
		window.getActivePage().activate(this);
		parent.layout(true, true);
	}

	@Override
	public void setFocus() {
	}
	
	@Override
	public void dispose() {
		if (this.currentEntity != null) {
			this.currentEntity.close(top, this);
			this.currentEntity = null;
		}
		if (top != null && !top.isDisposed()) {
			top.dispose();
		}
		super.dispose();
	}
	@Override
	protected boolean isAuthzToolItem() {
		return true;
	}
}
