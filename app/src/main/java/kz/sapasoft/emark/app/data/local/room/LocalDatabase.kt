package kz.sapasoft.emark.app.data.local.room

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kz.sapasoft.emark.app.data.local.room.convertors.DoublesContainer
import kz.sapasoft.emark.app.data.local.room.convertors.FieldModelConverters
import kz.sapasoft.emark.app.data.local.room.convertors.FileConverter
import kz.sapasoft.emark.app.data.local.room.convertors.MapConverter
import kz.sapasoft.emark.app.data.local.room.convertors.MarkerStatusConverter
import kz.sapasoft.emark.app.data.local.room.convertors.MetaDataConverter
import kz.sapasoft.emark.app.data.local.room.convertors.StringListConvertor
import kz.sapasoft.emark.app.data.local.room.convertors.StringsConverters
import kz.sapasoft.emark.app.data.local.room.image.ImageDao
import kz.sapasoft.emark.app.data.local.room.marker.MarkerDao
import kz.sapasoft.emark.app.data.local.room.marker_sync.MarkerSyncDao
import kz.sapasoft.emark.app.data.local.room.project.ProjectDao
import kz.sapasoft.emark.app.data.local.room.tag.TagDao
import kz.sapasoft.emark.app.data.local.room.template.TemplateDao
import kz.sapasoft.emark.app.domain.model.ImageDataModel
import kz.sapasoft.emark.app.domain.model.MarkerModel
import kz.sapasoft.emark.app.domain.model.MarkerModelSync
import kz.sapasoft.emark.app.domain.model.ProjectModel
import kz.sapasoft.emark.app.domain.model.TagModel
import kz.sapasoft.emark.app.domain.model.TemplateModel
import java.util.concurrent.Executors

@Database(
    entities = [TagModel::class, ProjectModel::class, MarkerModelSync::class, MarkerModel::class, ImageDataModel::class, TemplateModel::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(StringsConverters::class, FieldModelConverters::class, DoublesContainer::class, MetaDataConverter::class, FileConverter::class, MapConverter::class, MarkerStatusConverter::class, StringListConvertor::class)
abstract class LocalDatabase : RoomDatabase() {

    abstract val imageDao: ImageDao

    abstract val markerDao: MarkerDao

    abstract val markerSyncDao: MarkerSyncDao

    abstract val projectDao: ProjectDao

    abstract val tagDao: TagDao

    abstract val templateDao: TemplateDao

    companion object {
        private var instance: LocalDatabase? = null

        @Synchronized
        fun getInstance(ctx: Context): LocalDatabase {
            if (instance == null)
                instance = Room.databaseBuilder(
                    ctx.applicationContext, LocalDatabase::class.java,
                    "note_database_1"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onOpen(db: SupportSQLiteDatabase) {
                            super.onOpen(db)
                            db.execSQL("PRAGMA journal_mode = WAL") // optional
                            Log.d("MRoom", "DB Opened")
                        }
                    })
                    .build()

            return instance!!

        }
    }
}
