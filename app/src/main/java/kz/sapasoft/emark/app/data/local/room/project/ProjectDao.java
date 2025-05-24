package kz.sapasoft.emark.app.data.local.room.project;

import androidx.room.Dao;

import java.util.List;
import kotlin.Metadata;
import kz.sapasoft.emark.app.domain.model.ProjectModel;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ProjectDao {

    // Удаление всех записей из таблицы
    @Query("DELETE FROM project")
    void deleteAll();

    // Поиск всех записей в таблице
    @Query("SELECT * FROM project")
    List<ProjectModel> findAll();

    // Вставка списка записей с игнорированием конфликта
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(List<ProjectModel> list);

    // Вставка одной записи с игнорированием конфликта
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(ProjectModel projectModel);
}

