package kz.sapasoft.emark.app.data.local.room.marker;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.room.RoomDatabase;
import java.util.List;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kz.sapasoft.emark.app.domain.model.MarkerModel;

@Metadata(bv = {1, 0, 3}, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0016\u0010\u0005\u001a\u00020\u00062\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bH\u0016J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\n\u001a\u00020\tH\u0016J\b\u0010\u000b\u001a\u00020\u0006H\u0016J\u0010\u0010\f\u001a\u00020\u00062\u0006\u0010\r\u001a\u00020\u000eH\u0016J\u0016\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\t0\b2\u0006\u0010\u0010\u001a\u00020\u0011H\u0016J\u0016\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\t0\b2\u0006\u0010\r\u001a\u00020\u000eH\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0013"}, d2 = {"Lkz/sapasoft/emark/app/data/local/room/marker/MarkerDataSource;", "Lkz/sapasoft/emark/app/data/local/room/marker/MarkerRepository;", "markerDao", "Lkz/sapasoft/emark/app/data/local/room/marker/MarkerDao;", "(Lkz/sapasoft/emark/app/data/local/room/marker/MarkerDao;)V", "addWithReplace", "", "markerModelList", "", "Lkz/sapasoft/emark/app/domain/model/MarkerModel;", "markerModel", "deleteAll", "deleteByProjectId", "projectId", "", "findAll", "page", "", "findByProjectId", "app_release"}, k = 1, mv = {1, 1, 16})
/* compiled from: MarkerDataSource.kt */
public final class MarkerDataSource implements MarkerRepository {
    private final MarkerDao markerDao;

    @Inject
    public MarkerDataSource(MarkerDao markerDao2) {
        this.markerDao = markerDao2;
    }

    public void addWithReplace(MarkerModel markerModel) {
        this.markerDao.insertWithReplace(markerModel);
    }

    public void addWithReplace(List<MarkerModel> list) {
        Intrinsics.checkParameterIsNotNull(list, "markerModelList");
        this.markerDao.insertWithReplace(list);
    }

    public List<MarkerModel> findByProjectId(String str) {
        List<MarkerModel> markers = this.markerDao.findByProjectId(str);
        Log.d("MMarker", "bd findByProjectId markersSize" + markers.size() + "str " + str);
        return markers;
    }

    @SuppressLint("RestrictedApi")
    public List<MarkerModel> findAll(int i) {
        return this.markerDao.findAll(RoomDatabase.MAX_BIND_PARAMETER_CNT, (i - 1) * RoomDatabase.MAX_BIND_PARAMETER_CNT);
    }

    public void deleteAll() {
        this.markerDao.deleteAll();
    }

    public void deleteByProjectId(String str) {
        this.markerDao.deleteByProjectId(str);
    }
}
