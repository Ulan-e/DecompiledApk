package kz.sapasoft.emark.app.data.local.room.marker_sync;

import androidx.room.Dao;

import java.util.List;
import kotlin.Metadata;
import kz.sapasoft.emark.app.domain.model.MarkerModelSync;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MarkerSyncDao {

    // Удаление записи по ID
    @Query("DELETE FROM marker_sync WHERE id = :id")
    void deleteById(String id);

    // Поиск записей по projectId
    @Query("SELECT * FROM marker_sync WHERE :projectId IN (projectIds)")
    List<MarkerModelSync> findByProjectId(String projectId);

    // Вставка списка с заменой при конфликте
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertWithReplace(List<MarkerModelSync> list);

    // Вставка одного объекта с заменой при конфликте
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertWithReplace(MarkerModelSync markerModelSync);

    @Query("SELECT EXISTS(SELECT 1 FROM marker_sync WHERE markerId = :markerId LIMIT 1)")
    boolean exists(String markerId);
}

