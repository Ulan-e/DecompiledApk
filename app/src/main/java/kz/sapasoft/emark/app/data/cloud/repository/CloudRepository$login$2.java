/*
package kz.sapasoft.emark.app.data.cloud.repository;

import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kz.sapasoft.emark.app.data.cloud.rest.ApiService;
import okhttp3.ResponseBody;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\n\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001H@¢\u0006\u0004\b\u0002\u0010\u0003"}, d2 = {"<anonymous>", "Lokhttp3/ResponseBody;", "invoke", "(Ljava/lang/Object;)Ljava/lang/Object;"}, k = 3, mv = {1, 1, 16})
@DebugMetadata(c = "kz.sapasoft.emark.app.data.cloud.repository.CloudRepository$login$2", f = "CloudRepository.kt", i = {}, l = {25}, m = "invokeSuspend", n = {}, s = {})
*/
/* compiled from: CloudRepository.kt *//*

final class CloudRepository$login$2 extends SuspendLambda implements Function1<Continuation<? super ResponseBody>, Object> {
    final */
/* synthetic *//*
 String $password;
    final */
/* synthetic *//*
 String $url;
    final */
/* synthetic *//*
 String $username;
    int label;
    final */
/* synthetic *//*
 CloudRepository this$0;

    */
/* JADX INFO: super call moved to the top of the method (can break code semantics) *//*

    CloudRepository$login$2(CloudRepository cloudRepository, String str, String str2, String str3, Continuation continuation) {
        super(1, continuation);
        this.this$0 = cloudRepository;
        this.$url = str;
        this.$username = str2;
        this.$password = str3;
    }

    public final Continuation<Unit> create(Continuation<?> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        return new CloudRepository$login$2(this.this$0, this.$url, this.$username, this.$password, continuation);
    }

    public final Object invoke(Object obj) {
        return ((CloudRepository$login$2) create((Continuation) obj)).invokeSuspend(Unit.INSTANCE);
    }

    public final Object invokeSuspend(Object obj) {
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            ApiService access$getApIs$p = this.this$0.apIs;
            String str = this.$url;
            String str2 = this.$username;
            String str3 = this.$password;
            this.label = 1;
            obj = access$getApIs$p.login(str, str2, str3, this);
            if (obj == coroutine_suspended) {
                return coroutine_suspended;
            }
        } else if (i == 1) {
            ResultKt.throwOnFailure(obj);
        } else {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        return obj;
    }
}
*/
