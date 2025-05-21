package kz.sapasoft.emark.app.data.local.room.marker_sync;

import java.util.List;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kz.sapasoft.emark.app.domain.model.MarkerModelSync;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0016\u0010\u0005\u001a\u00020\u00062\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bH\u0016J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\n\u001a\u00020\tH\u0016J\u0010\u0010\u000b\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\rH\u0016J\u0016\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\t0\b2\u0006\u0010\u000f\u001a\u00020\rH\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0010"}, d2 = {"Lkz/sapasoft/emark/app/data/local/room/marker_sync/MarkerSyncDataSource;", "Lkz/sapasoft/emark/app/data/local/room/marker_sync/MarkerSyncRepository;", "markerDao", "Lkz/sapasoft/emark/app/data/local/room/marker_sync/MarkerSyncDao;", "(Lkz/sapasoft/emark/app/data/local/room/marker_sync/MarkerSyncDao;)V", "addWithReplace", "", "markerModelList", "", "Lkz/sapasoft/emark/app/domain/model/MarkerModelSync;", "markerModel", "deleteById", "id", "", "findByProjectId", "projectId", "app_release"}, k = 1, mv = {1, 1, 16})
/* compiled from: MarkerSyncDataSource.kt */
public final class MarkerSyncDataSource implements MarkerSyncRepository {
    private final MarkerSyncDao markerDao;

    @Inject
    public MarkerSyncDataSource(MarkerSyncDao markerSyncDao) {
        Intrinsics.checkParameterIsNotNull(markerSyncDao, "markerDao");
        this.markerDao = markerSyncDao;
    }

    public void addWithReplace(MarkerModelSync markerModelSync) {
        Intrinsics.checkParameterIsNotNull(markerModelSync, "markerModel");
        this.markerDao.insertWithReplace(markerModelSync);
    }

    public List<Long> addWithReplace(List<MarkerModelSync> list) {
        Intrinsics.checkParameterIsNotNull(list, "markerModelList");
        return this.markerDao.insertWithReplace(list);
    }

    public List<MarkerModelSync> findByProjectId(String str) {
        Intrinsics.checkParameterIsNotNull(str, "projectId");
        return this.markerDao.findByProjectId(str);
    }

    public void deleteById(String str) {
        Intrinsics.checkParameterIsNotNull(str, "id");
        this.markerDao.deleteById(str);
    }
}
