package kz.sapasoft.emark.app.ui.custom_views;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.decompiledapk.R;
import com.google.android.gms.common.internal.ServiceSpecificExtraArgs;
import java.util.HashMap;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kz.sapasoft.emark.app.ui.marker.OnFieldValueChangeListener;

@Metadata(bv = {1, 0, 3}, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B%\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\u0006\u0010\u000b\u001a\u00020\nJ\u0018\u0010\f\u001a\u00020\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\n2\u0006\u0010\u000f\u001a\u00020\u0010R\u0010\u0010\t\u001a\u0004\u0018\u00010\nX\u000e¢\u0006\u0002\n\u0000¨\u0006\u0011"}, d2 = {"Lkz/sapasoft/emark/app/ui/custom_views/MarkerIdentifierView;", "Landroid/widget/LinearLayout;", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "defStyleAttr", "", "(Landroid/content/Context;Landroid/util/AttributeSet;I)V", "mValue", "", "getValue", "setData", "", "value", "listener", "Lkz/sapasoft/emark/app/ui/marker/OnFieldValueChangeListener;", "app_release"}, k = 1, mv = {1, 1, 16})
/* compiled from: MarkerIdentifierView.kt */
public final class MarkerIdentifierView extends LinearLayout {
    private HashMap _$_findViewCache;
    private String mValue;

    public MarkerIdentifierView(Context context) {
        this(context, (AttributeSet) null, 0, 6, (DefaultConstructorMarker) null);
    }

    public MarkerIdentifierView(Context context, AttributeSet attributeSet) {
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
    public /* synthetic */ MarkerIdentifierView(Context context, AttributeSet attributeSet, int i, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i2 & 2) != 0 ? null : attributeSet, (i2 & 4) != 0 ? 0 : i);
    }


    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public MarkerIdentifierView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        Intrinsics.checkParameterIsNotNull(context, "context");
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService("layout_inflater");
        if (layoutInflater != null) {
            layoutInflater.inflate(R.layout.view_marker_identifier, this);
        }

        addMask();
    }

    public void addMask(){
        EditText etText = (EditText) _$_findCachedViewById(R.id.et_text);
        etText.addTextChangedListener(new TextWatcher() {
            boolean isUpdating = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (isUpdating) return;
                isUpdating = true;

                // Удалим всё, кроме цифр
                String digits = s.toString().replaceAll("[^\\d]", "");

                // Ограничим длину (максимум 10 цифр)
                if (digits.length() > 10) {
                    digits = digits.substring(0, 10);
                }

                // Строим маску #:000-000-0000
                StringBuilder formatted = new StringBuilder("#:");
                for (int i = 0; i < digits.length(); i++) {
                    if (i == 3 || i == 6) {
                        formatted.append('-');
                    }
                    formatted.append(digits.charAt(i));
                }

                etText.setText(formatted.toString());
                etText.setSelection(formatted.length());

                isUpdating = false;
            }
        });
    }

    public void enableEdit(Boolean enable){
        EditText etText = (EditText) _$_findCachedViewById(R.id.et_text);
        etText.setEnabled(enable);
    }

    public final void setData(String str, OnFieldValueChangeListener onFieldValueChangeListener) {
        Intrinsics.checkParameterIsNotNull(onFieldValueChangeListener, ServiceSpecificExtraArgs.CastExtraArgs.LISTENER);
        this.mValue = str;
        ((EditText) _$_findCachedViewById(R.id.et_text)).setText(str);
        EditText editText = (EditText) _$_findCachedViewById(R.id.et_text);
        Intrinsics.checkExpressionValueIsNotNull(editText, "et_text");
        editText.addTextChangedListener(new MarkerIdentifierView$setData$$inlined$addTextChangedListener$1(onFieldValueChangeListener));
    }

    public final boolean isIdentifierEmpty() {
        EditText editText = (EditText) _$_findCachedViewById(R.id.et_text);
        String text = editText.getText().toString().trim();
        return text.length() < 14;
    }

    public final String getValue() {
        EditText editText = (EditText) _$_findCachedViewById(R.id.et_text);
        Intrinsics.checkExpressionValueIsNotNull(editText, "et_text");
        return editText.getText().toString();
    }
}
