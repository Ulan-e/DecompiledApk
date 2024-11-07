package kz.sapasoft.emark.app.ui.custom_views;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import java.util.HashMap;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kz.sapasoft.emark.app.R;
import kz.sapasoft.emark.app.domain.model.FieldModel;
import kz.sapasoft.emark.app.domain.model.TagModel;
import kz.sapasoft.emark.app.ui.marker.OnFieldValueChangeListener;
import kz.sapasoft.emark.app.utils.DateConverter;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\u0018\u00002\u00020\u0001B5\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\t\u0012\b\b\u0002\u0010\n\u001a\u00020\u000b¢\u0006\u0002\u0010\fJ\b\u0010\u0010\u001a\u00020\u0005H\u0016J\b\u0010\u0011\u001a\u00020\u0012H\u0016J\b\u0010\u0013\u001a\u00020\u0012H\u0016J\u0010\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0004\u001a\u00020\u0005H\u0016R\u000e\u0010\r\u001a\u00020\u000eX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0005X\u000e¢\u0006\u0002\n\u0000¨\u0006\u0016"}, d2 = {"Lkz/sapasoft/emark/app/ui/custom_views/FieldViewDate;", "Lkz/sapasoft/emark/app/ui/custom_views/FieldView;", "context", "Landroid/content/Context;", "field", "Lkz/sapasoft/emark/app/domain/model/FieldModel;", "listener", "Lkz/sapasoft/emark/app/ui/marker/OnFieldValueChangeListener;", "attrs", "Landroid/util/AttributeSet;", "defStyleAttr", "", "(Landroid/content/Context;Lkz/sapasoft/emark/app/domain/model/FieldModel;Lkz/sapasoft/emark/app/ui/marker/OnFieldValueChangeListener;Landroid/util/AttributeSet;I)V", "dialogListener", "Landroid/app/DatePickerDialog$OnDateSetListener;", "mField", "getFieldModel", "hasValue", "", "isRequired", "setFieldModel", "", "app_release"}, k = 1, mv = {1, 1, 16})
/* compiled from: FieldViewDate.kt */
public final class FieldViewDate extends FieldView {
    private HashMap _$_findViewCache;
    /* access modifiers changed from: private */
    public final DatePickerDialog.OnDateSetListener dialogListener;
    private FieldModel mField;

    public FieldViewDate(Context context, FieldModel fieldModel, OnFieldValueChangeListener onFieldValueChangeListener) {
        this(context, fieldModel, onFieldValueChangeListener, (AttributeSet) null, 0, 24, (DefaultConstructorMarker) null);
    }

    public FieldViewDate(Context context, FieldModel fieldModel, OnFieldValueChangeListener onFieldValueChangeListener, AttributeSet attributeSet) {
        this(context, fieldModel, onFieldValueChangeListener, attributeSet, 0, 16, (DefaultConstructorMarker) null);
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
    public /* synthetic */ FieldViewDate(Context context, FieldModel fieldModel, OnFieldValueChangeListener onFieldValueChangeListener, AttributeSet attributeSet, int i, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, fieldModel, onFieldValueChangeListener, (i2 & 8) != 0 ? null : attributeSet, (i2 & 16) != 0 ? 0 : i);
    }

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public FieldViewDate(Context r28, FieldModel r29, OnFieldValueChangeListener r30, AttributeSet r31, int r32) {
        /*
            r27 = this;
            r0 = r27
            r1 = r28
            r2 = r30
            java.lang.String r3 = "context"
            kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r1, r3)
            java.lang.String r3 = "field"
            r4 = r29
            kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r4, r3)
            java.lang.String r3 = "listener"
            kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r2, r3)
            r3 = r31
            r5 = r32
            r0.<init>(r1, r3, r5)
            java.lang.String r3 = "layout_inflater"
            java.lang.Object r3 = r1.getSystemService(r3)
            android.view.LayoutInflater r3 = (android.view.LayoutInflater) r3
            if (r3 == 0) goto L_0x0031
            r5 = 2131558527(0x7f0d007f, float:1.8742372E38)
            r6 = r0
            android.view.ViewGroup r6 = (android.view.ViewGroup) r6
            r3.inflate(r5, r6)
        L_0x0031:
            r5 = 0
            r6 = 0
            r7 = 0
            r8 = 0
            r9 = 0
            r10 = 0
            r11 = 0
            r12 = 0
            r13 = 0
            r14 = 0
            r15 = 0
            r16 = 0
            r17 = 0
            r18 = 0
            r19 = 0
            r20 = 0
            r21 = 0
            r22 = 0
            r23 = 0
            r24 = 0
            r25 = 1048575(0xfffff, float:1.469367E-39)
            r26 = 0
            r4 = r29
            kz.sapasoft.emark.app.domain.model.FieldModel r3 = kz.sapasoft.emark.app.domain.model.FieldModel.copy$default(r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18, r19, r20, r21, r22, r23, r24, r25, r26)
            r0.mField = r3
            int r3 = kz.sapasoft.emark.app.R.id.tv_title
            android.view.View r3 = r0._$_findCachedViewById(r3)
            com.google.android.material.textview.MaterialTextView r3 = (com.google.android.material.textview.MaterialTextView) r3
            java.lang.String r4 = "tv_title"
            kotlin.jvm.internal.Intrinsics.checkExpressionValueIsNotNull(r3, r4)
            java.lang.String r4 = r29.getName()
            java.lang.CharSequence r4 = (java.lang.CharSequence) r4
            r3.setText(r4)
            int r3 = kz.sapasoft.emark.app.R.id.tv_text
            android.view.View r3 = r0._$_findCachedViewById(r3)
            android.widget.TextView r3 = (android.widget.TextView) r3
            java.lang.String r4 = "tv_text"
            kotlin.jvm.internal.Intrinsics.checkExpressionValueIsNotNull(r3, r4)
            kz.sapasoft.emark.app.utils.DateConverter r5 = kz.sapasoft.emark.app.utils.DateConverter.INSTANCE
            java.lang.Long r6 = r29.getDateValue()
            java.lang.String r7 = "dd.MM.yyyy"
            java.lang.String r5 = r5.millisToString(r6, r7)
            java.lang.CharSequence r5 = (java.lang.CharSequence) r5
            r3.setText(r5)
            int r3 = kz.sapasoft.emark.app.R.id.tv_text
            android.view.View r3 = r0._$_findCachedViewById(r3)
            android.widget.TextView r3 = (android.widget.TextView) r3
            kotlin.jvm.internal.Intrinsics.checkExpressionValueIsNotNull(r3, r4)
            kz.sapasoft.emark.app.ui.custom_views.FieldViewDate$$special$$inlined$addTextChangedListener$1 r4 = new kz.sapasoft.emark.app.ui.custom_views.FieldViewDate$$special$$inlined$addTextChangedListener$1
            r4.<init>(r2)
            android.text.TextWatcher r4 = (android.text.TextWatcher) r4
            r3.addTextChangedListener(r4)
            int r2 = kz.sapasoft.emark.app.R.id.ll_root
            android.view.View r2 = r0._$_findCachedViewById(r2)
            android.widget.LinearLayout r2 = (android.widget.LinearLayout) r2
            kz.sapasoft.emark.app.ui.custom_views.FieldViewDate$2 r3 = new kz.sapasoft.emark.app.ui.custom_views.FieldViewDate$2
            r3.<init>(r0, r1)
            android.view.View$OnClickListener r3 = (android.view.View.OnClickListener) r3
            r2.setOnClickListener(r3)
            kz.sapasoft.emark.app.ui.custom_views.FieldViewDate$dialogListener$1 r1 = new kz.sapasoft.emark.app.ui.custom_views.FieldViewDate$dialogListener$1
            r1.<init>(r0)
            android.app.DatePickerDialog$OnDateSetListener r1 = (android.app.DatePickerDialog.OnDateSetListener) r1
            r0.dialogListener = r1
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: kz.sapasoft.emark.app.ui.custom_views.FieldViewDate.<init>(android.content.Context, kz.sapasoft.emark.app.domain.model.FieldModel, kz.sapasoft.emark.app.ui.marker.OnFieldValueChangeListener, android.util.AttributeSet, int):void");
    }

    public FieldModel getFieldModel() {
        FieldModel fieldModel = this.mField;
        DateConverter dateConverter = DateConverter.INSTANCE;
        TextView textView = (TextView) _$_findCachedViewById(R.id.tv_text);
        Intrinsics.checkExpressionValueIsNotNull(textView, "tv_text");
        fieldModel.setDateValue(dateConverter.stringToMillis(textView.getText().toString(), "dd.MM.yyyy"));
        return this.mField;
    }

    public void setFieldModel(FieldModel fieldModel) {
        Intrinsics.checkParameterIsNotNull(fieldModel, "field");
        this.mField = FieldModel.copy$default(fieldModel, (String) null, (Long) null, (String) null, (Long) null, (String) null, (String) null, (String) null, (String) null, (String) null, (Boolean) null, (Boolean) null, (String) null, (String) null, (String) null, (Long) null, (Double) null, (String) null, (String) null, (TagModel) null, (Long) null, 1048575, (Object) null);
        TextView textView = (TextView) _$_findCachedViewById(R.id.tv_text);
        Intrinsics.checkExpressionValueIsNotNull(textView, "tv_text");
        textView.setText(DateConverter.INSTANCE.millisToString(fieldModel.getDateValue(), "dd.MM.yyyy"));
    }

    public boolean isRequired() {
        Boolean required = this.mField.getRequired();
        if (required != null) {
            return required.booleanValue();
        }
        return false;
    }

    public boolean hasValue() {
        TextView textView = (TextView) _$_findCachedViewById(R.id.tv_text);
        Intrinsics.checkExpressionValueIsNotNull(textView, "tv_text");
        CharSequence text = textView.getText();
        return !(text == null || text.length() == 0);
    }
}
