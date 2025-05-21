package kz.sapasoft.emark.app.ui.custom_views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.decompiledapk.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kz.sapasoft.emark.app.domain.model.ImageDataModel;
import kz.sapasoft.emark.app.ui.marker.OnFieldValueChangeListener;
import kz.sapasoft.emark.app.ui.marker.OnImageClickListener;
import kz.sapasoft.emark.app.ui.photo.OnPhotoViewListener;
import kz.sapasoft.emark.app.ui.photo.PhotoAdapter;
import pl.aprilapps.easyphotopicker.MediaFile;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\n\u0018\u00002\u00020\u00012\u00020\u0002B%\b\u0007\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b¢\u0006\u0002\u0010\tJ\u000e\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0014J\f\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00170\u0016J\b\u0010\u0018\u001a\u00020\u0012H\u0016J\u0010\u0010\u0019\u001a\u00020\u00122\u0006\u0010\u001a\u001a\u00020\u0017H\u0016J\b\u0010\u001b\u001a\u00020\u0012H\u0016J\u0014\u0010\u001c\u001a\u00020\u00122\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u00170\u0016J\u0016\u0010\u001e\u001a\u00020\u00122\u0006\u0010\u001f\u001a\u00020\u00102\u0006\u0010 \u001a\u00020\u000eR\u000e\u0010\n\u001a\u00020\u000bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u0004\u0018\u00010\u000eX\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u000e¢\u0006\u0002\n\u0000¨\u0006!"}, d2 = {"Lkz/sapasoft/emark/app/ui/custom_views/MarkerPhotoView;", "Landroid/widget/LinearLayout;", "Lkz/sapasoft/emark/app/ui/photo/PhotoAdapter$OnPhotoClickListener;", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "defStyleAttr", "", "(Landroid/content/Context;Landroid/util/AttributeSet;I)V", "adapter", "Lkz/sapasoft/emark/app/ui/photo/PhotoAdapter;", "mContext", "mFieldChangeListener", "Lkz/sapasoft/emark/app/ui/marker/OnFieldValueChangeListener;", "mImageClickListener", "Lkz/sapasoft/emark/app/ui/marker/OnImageClickListener;", "addImage", "", "mediaFile", "Lpl/aprilapps/easyphotopicker/MediaFile;", "getChangedModelList", "", "Lkz/sapasoft/emark/app/domain/model/ImageDataModel;", "onAddPhotoClick", "onPhotoClick", "imageDataModel", "onPhotoDeleted", "setImageDataModelList", "imageDataModelList", "setListeners", "imageClickListener", "fieldChangeListener", "app_release"}, k = 1, mv = {1, 1, 16})
/* compiled from: MarkerPhotoView.kt */
public final class MarkerPhotoView extends LinearLayout implements PhotoAdapter.OnPhotoClickListener {
    private HashMap _$_findViewCache;
    private final PhotoAdapter adapter;
    private Context mContext;
    private OnFieldValueChangeListener mFieldChangeListener;
    private OnImageClickListener mImageClickListener;

    private OnPhotoViewListener onPhotoViewListener;

    public MarkerPhotoView(Context context) {
        this(context, (AttributeSet) null, 0, 6, (DefaultConstructorMarker) null);
    }

    public MarkerPhotoView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0, 4, (DefaultConstructorMarker) null);
    }

    public void _$_clearFindViewByIdCache() {
        HashMap hashMap = this._$_findViewCache;
        if (hashMap != null) {
            hashMap.clear();
        }
    }

    public View _$_findCachedViewById(int i) {
        if (this._$_findViewCache == null) {
            this._$_findViewCache = new HashMap();
        }
        View view = (View) this._$_findViewCache.get(Integer.valueOf(i));
        if (view != null) {
            return view;
        }
        View findViewById = findViewById(i);
        this._$_findViewCache.put(Integer.valueOf(i), findViewById);
        return findViewById;
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ MarkerPhotoView(Context context, AttributeSet attributeSet, int i, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i2 & 2) != 0 ? null : attributeSet, (i2 & 4) != 0 ? 0 : i);
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public MarkerPhotoView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.adapter = new PhotoAdapter(new ArrayList(), this, context);
        LayoutInflater.from(context).inflate(R.layout.view_marker_photo, this, true);
        this.mContext = context;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_photoss);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(this.adapter);
    }

    public final void setImageDataModelList(List<ImageDataModel> list) {
        Intrinsics.checkParameterIsNotNull(list, "imageDataModelList");
        this.adapter.add(list);
    }

    public void setOnPhotoClickListener(OnPhotoViewListener onPhotoViewListener){
        this.onPhotoViewListener = onPhotoViewListener;
    }

    public final List<ImageDataModel> getChangedModelList() {
        return this.adapter.getChangedModelList();
    }

    public final void setListeners(OnImageClickListener onImageClickListener, OnFieldValueChangeListener onFieldValueChangeListener) {
        Intrinsics.checkParameterIsNotNull(onImageClickListener, "imageClickListener");
        Intrinsics.checkParameterIsNotNull(onFieldValueChangeListener, "fieldChangeListener");
        this.mImageClickListener = onImageClickListener;
        this.mFieldChangeListener = onFieldValueChangeListener;
    }

    public final void addImage(MediaFile mediaFile) {
        Intrinsics.checkParameterIsNotNull(mediaFile, "mediaFile");
        PhotoAdapter photoAdapter = this.adapter;
        String uuid = UUID.randomUUID().toString();
        Intrinsics.checkExpressionValueIsNotNull(uuid, "UUID.randomUUID().toString()");
        photoAdapter.add(new ImageDataModel(uuid, (String) null, (ImageDataModel.MetaData) null, mediaFile.getFile()));
        OnFieldValueChangeListener onFieldValueChangeListener = this.mFieldChangeListener;
        if (onFieldValueChangeListener != null) {
            onFieldValueChangeListener.onFieldValueChange();
        }
    }

    public void onAddPhotoClick() {
        OnImageClickListener onImageClickListener = this.mImageClickListener;
        if (onImageClickListener != null) {
            onImageClickListener.onAddPhotoClick();
        }
    }

    @Override
    public void onPhotoClick(ImageDataModel imageDataModel) {
        onPhotoViewListener.onPhotoClick(imageDataModel);
    }

    public void onPhotoDeleted() {
        OnFieldValueChangeListener onFieldValueChangeListener = this.mFieldChangeListener;
        if (onFieldValueChangeListener != null) {
            onFieldValueChangeListener.onFieldValueChange();
        }
    }
}
