package kz.sapasoft.emark.app.ui.welcome;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\b\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, d2 = {"<anonymous>", "Lkz/sapasoft/emark/app/ui/welcome/WelcomeViewModel;", "invoke"}, k = 3, mv = {1, 1, 16})
/* compiled from: WelcomeActivity.kt */
final class WelcomeActivity$viewModel$2 extends Lambda implements Function0<WelcomeViewModel> {
    final /* synthetic */ WelcomeActivity this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    WelcomeActivity$viewModel$2(WelcomeActivity welcomeActivity) {
        super(0);
        this.this$0 = welcomeActivity;
    }

    public final WelcomeViewModel invoke() {
        WelcomeActivity welcomeActivity = this.this$0;
        return (WelcomeViewModel) new ViewModelProvider((ViewModelStoreOwner) welcomeActivity, welcomeActivity.getViewModelFactory()).get(WelcomeViewModel.class);
    }
}
