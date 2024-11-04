package kz.sapasoft.emark.app.ui.map;

import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.CoroutineScope;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H@¢\u0006\u0004\b\u0003\u0010\u0004"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"}, k = 3, mv = {1, 1, 16})
@DebugMetadata(c = "kz.sapasoft.emark.app.ui.map.MapViewModel$synchronizeMarkers$1", f = "MapViewModel.kt", i = {0}, l = {88}, m = "invokeSuspend", n = {"$this$launchIO"}, s = {"L$0"})
/* compiled from: MapViewModel.kt */
final class MapViewModel$synchronizeMarkers$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    final /* synthetic */ String $projectId;
    Object L$0;
    int label;
    private CoroutineScope p$;
    final /* synthetic */ MapViewModel this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    MapViewModel$synchronizeMarkers$1(MapViewModel mapViewModel, String str, Continuation continuation) {
        super(2, continuation);
        this.this$0 = mapViewModel;
        this.$projectId = str;
    }

    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        MapViewModel$synchronizeMarkers$1 mapViewModel$synchronizeMarkers$1 = new MapViewModel$synchronizeMarkers$1(this.this$0, this.$projectId, continuation);
        mapViewModel$synchronizeMarkers$1.p$ = (CoroutineScope) obj;
        return mapViewModel$synchronizeMarkers$1;
    }

    public final Object invoke(Object obj, Object obj2) {
        return ((MapViewModel$synchronizeMarkers$1) create(obj, (Continuation) obj2)).invokeSuspend(Unit.INSTANCE);
    }

    public final Object invokeSuspend(Object obj) {
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            CoroutineScope coroutineScope = this.p$;
            MapViewModel mapViewModel = this.this$0;
            String str = this.$projectId;
            this.L$0 = coroutineScope;
            this.label = 1;
            if (mapViewModel.saveMarkerList(str, this) == coroutine_suspended) {
                return coroutine_suspended;
            }
        } else if (i == 1) {
            CoroutineScope coroutineScope2 = (CoroutineScope) this.L$0;
            ResultKt.throwOnFailure(obj);
        } else {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        this.this$0.getMarkerEntityList(this.$projectId);
        return Unit.INSTANCE;
    }
}
