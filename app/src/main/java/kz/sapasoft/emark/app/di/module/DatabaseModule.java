package kz.sapasoft.emark.app.di.module;

import androidx.room.Room;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kz.sapasoft.emark.app.core.App;
import kz.sapasoft.emark.app.data.local.room.LocalDatabase;
import kz.sapasoft.emark.app.data.local.room.image.ImageDao;
import kz.sapasoft.emark.app.data.local.room.image.ImageDataSource;
import kz.sapasoft.emark.app.data.local.room.image.ImageRepository;
import kz.sapasoft.emark.app.data.local.room.marker.MarkerDao;
import kz.sapasoft.emark.app.data.local.room.marker.MarkerDataSource;
import kz.sapasoft.emark.app.data.local.room.marker.MarkerRepository;
import kz.sapasoft.emark.app.data.local.room.marker_sync.MarkerSyncDao;
import kz.sapasoft.emark.app.data.local.room.marker_sync.MarkerSyncDataSource;
import kz.sapasoft.emark.app.data.local.room.marker_sync.MarkerSyncRepository;
import kz.sapasoft.emark.app.data.local.room.project.ProjectDao;
import kz.sapasoft.emark.app.data.local.room.project.ProjectDataSource;
import kz.sapasoft.emark.app.data.local.room.project.ProjectRepository;
import kz.sapasoft.emark.app.data.local.room.tag.TagDao;
import kz.sapasoft.emark.app.data.local.room.tag.TagDataSource;
import kz.sapasoft.emark.app.data.local.room.tag.TagRepository;
import kz.sapasoft.emark.app.data.local.room.template.TemplateDao;
import kz.sapasoft.emark.app.data.local.room.template.TemplateDataSource;
import kz.sapasoft.emark.app.data.local.room.template.TemplateRepository;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000b\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0007J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0007J\u0010\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012H\u0007J\u0010\u0010\u0013\u001a\u00020\u00062\u0006\u0010\u0014\u001a\u00020\u0015H\u0007J\u0010\u0010\u0016\u001a\u00020\n2\u0006\u0010\u0014\u001a\u00020\u0015H\u0007J\u0010\u0010\u0017\u001a\u00020\u000e2\u0006\u0010\u0014\u001a\u00020\u0015H\u0007J\u0010\u0010\u0018\u001a\u00020\u00122\u0006\u0010\u0014\u001a\u00020\u0015H\u0007J\b\u0010\u0019\u001a\u00020\u0015H\u0007J\u0010\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u0014\u001a\u00020\u0015H\u0007J\u0010\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u0014\u001a\u00020\u0015H\u0007J\u0010\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020\u001bH\u0007J\u0010\u0010!\u001a\u00020\"2\u0006\u0010#\u001a\u00020\u001dH\u0007¨\u0006$"}, d2 = {"Lkz/sapasoft/emark/app/di/module/DatabaseModule;", "", "()V", "imageRepository", "Lkz/sapasoft/emark/app/data/local/room/image/ImageRepository;", "imageDao", "Lkz/sapasoft/emark/app/data/local/room/image/ImageDao;", "markerRepository", "Lkz/sapasoft/emark/app/data/local/room/marker/MarkerRepository;", "markerDao", "Lkz/sapasoft/emark/app/data/local/room/marker/MarkerDao;", "markerSyncRepository", "Lkz/sapasoft/emark/app/data/local/room/marker_sync/MarkerSyncRepository;", "markerSyncDao", "Lkz/sapasoft/emark/app/data/local/room/marker_sync/MarkerSyncDao;", "projectRepository", "Lkz/sapasoft/emark/app/data/local/room/project/ProjectRepository;", "projectDao", "Lkz/sapasoft/emark/app/data/local/room/project/ProjectDao;", "providesImageDao", "database", "Lkz/sapasoft/emark/app/data/local/room/LocalDatabase;", "providesMarkerDao", "providesMarkerSyncDao", "providesProjectDao", "providesRoomDatabase", "providesTagDao", "Lkz/sapasoft/emark/app/data/local/room/tag/TagDao;", "providesTemplateDao", "Lkz/sapasoft/emark/app/data/local/room/template/TemplateDao;", "tagRepository", "Lkz/sapasoft/emark/app/data/local/room/tag/TagRepository;", "tagDao", "templateRepository", "Lkz/sapasoft/emark/app/data/local/room/template/TemplateRepository;", "templateDao", "app_release"}, k = 1, mv = {1, 1, 16})
@Module
/* compiled from: DatabaseModule.kt */
public final class DatabaseModule {
    @Singleton
    @Provides
    public final LocalDatabase providesRoomDatabase() {
        return Room.databaseBuilder(App.Companion.getInstance(), LocalDatabase.class, "emark_data").allowMainThreadQueries().build();
    }

    @Singleton
    @Provides
    public final ProjectDao providesProjectDao(LocalDatabase localDatabase) {
        return localDatabase.getProjectDao();
    }

    @Singleton
    @Provides
    public final TagDao providesTagDao(LocalDatabase localDatabase) {
        return localDatabase.getTagDao();
    }

    @Singleton
    @Provides
    public final TemplateDao providesTemplateDao(LocalDatabase localDatabase) {
        return localDatabase.getTemplateDao();
    }

    @Singleton
    @Provides
    public final MarkerDao providesMarkerDao(LocalDatabase localDatabase) {
        return localDatabase.getMarkerDao();
    }

    @Singleton
    @Provides
    public final MarkerSyncDao providesMarkerSyncDao(LocalDatabase localDatabase) {
        return localDatabase.getMarkerSyncDao();
    }

    @Singleton
    @Provides
    public final ImageDao providesImageDao(LocalDatabase localDatabase) {
        return localDatabase.getImageDao();
    }

    @Singleton
    @Provides
    public final ProjectRepository projectRepository(ProjectDao projectDao) {
        return new ProjectDataSource(projectDao);
    }

    @Singleton
    @Provides
    public final TagRepository tagRepository(TagDao tagDao) {
        return new TagDataSource(tagDao);
    }

    @Singleton
    @Provides
    public final TemplateRepository templateRepository(TemplateDao templateDao) {
        return new TemplateDataSource(templateDao);
    }

    @Singleton
    @Provides
    public final MarkerRepository markerRepository(MarkerDao markerDao) {
        return new MarkerDataSource(markerDao);
    }

    @Singleton
    @Provides
    public final MarkerSyncRepository markerSyncRepository(MarkerSyncDao markerSyncDao) {
        return new MarkerSyncDataSource(markerSyncDao);
    }

    @Singleton
    @Provides
    public final ImageRepository imageRepository(ImageDao imageDao) {
        return new ImageDataSource(imageDao);
    }
}
