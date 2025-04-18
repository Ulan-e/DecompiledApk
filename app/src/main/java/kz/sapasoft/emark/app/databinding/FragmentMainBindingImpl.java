package kz.sapasoft.emark.app.databinding;

import android.util.SparseIntArray;
import android.view.View;
import android.widget.FrameLayout;

import androidx.databinding.DataBindingComponent;
import androidx.databinding.ViewDataBinding;
import kz.sapasoft.emark.app.ui.projects.ProjectsViewModel;

public class FragmentMainBindingImpl extends FragmentMainBinding {
    private static final ViewDataBinding.IncludedLayouts sIncludes = null;
    private static final SparseIntArray sViewsWithIds = null;
    private long mDirtyFlags;

    /* access modifiers changed from: protected */
    public boolean onFieldChange(int i, Object obj, int i2) {
        return false;
    }

    public FragmentMainBindingImpl(DataBindingComponent dataBindingComponent, View view) {
        this(dataBindingComponent, view, mapBindings(dataBindingComponent, view, 1, sIncludes, sViewsWithIds));
    }

    private FragmentMainBindingImpl(DataBindingComponent dataBindingComponent, View view, Object[] objArr) {
        super((Object) dataBindingComponent, view, 0, (FrameLayout) objArr[0]);
        this.mDirtyFlags = -1;
        this.flContent.setTag((Object) null);
        setRootTag(view);
        invalidateAll();
    }

    public void invalidateAll() {
        synchronized (this) {
            this.mDirtyFlags = 2;
        }
        requestRebind();
    }

    public boolean hasPendingBindings() {
        synchronized (this) {
            if (this.mDirtyFlags != 0) {
                return true;
            }
            return false;
        }
    }

    public boolean setVariable(int i, Object obj) {
        if (6 != i) {
            return false;
        }
        setViewModel((ProjectsViewModel) obj);
        return true;
    }

    public void setViewModel(ProjectsViewModel projectsViewModel) {
        this.mViewModel = projectsViewModel;
    }

    /* access modifiers changed from: protected */
    public void executeBindings() {
        synchronized (this) {
            this.mDirtyFlags = 0;
        }
    }
}
