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
    @Query("SELECT * FROM marker_sync WHERE id = :projectId")
    List<MarkerModelSync> findByProjectId(String projectId);

    // Вставка списка с заменой при конфликте
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertWithReplace(List<MarkerModelSync> list);

    // Вставка одного объекта с заменой при конфликте
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertWithReplace(MarkerModelSync markerModelSync);
}

