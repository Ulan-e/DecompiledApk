package kz.sapasoft.emark.app.data.local.room.marker_sync;

import java.util.List;
import kotlin.Metadata;
import kz.sapasoft.emark.app.domain.model.MarkerModelSync;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H&J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\u0006H&J\u0010\u0010\b\u001a\u00020\u00032\u0006\u0010\t\u001a\u00020\nH&J\u0016\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00060\u00052\u0006\u0010\f\u001a\u00020\nH&¨\u0006\r"}, d2 = {"Lkz/sapasoft/emark/app/data/local/room/marker_sync/MarkerSyncRepository;", "", "addWithReplace", "", "markerModelList", "", "Lkz/sapasoft/emark/app/domain/model/MarkerModelSync;", "markerModel", "deleteById", "id", "", "findByProjectId", "projectId", "app_release"}, k = 1, mv = {1, 1, 16})
/* compiled from: MarkerSyncRepository.kt */
public interface MarkerSyncRepository {
    List<Long> addWithReplace(List<MarkerModelSync> list);

    void addWithReplace(MarkerModelSync markerModelSync);

    void deleteById(String str);

    List<MarkerModelSync> findByProjectId(String str);
}
