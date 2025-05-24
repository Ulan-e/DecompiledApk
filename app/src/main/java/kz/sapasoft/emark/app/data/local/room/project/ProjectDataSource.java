package kz.sapasoft.emark.app.data.local.room.project;

import java.util.List;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kz.sapasoft.emark.app.domain.model.ProjectModel;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0016J\u0016\u0010\t\u001a\u00020\u00062\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\b0\u000bH\u0016J\b\u0010\f\u001a\u00020\u0006H\u0016J\u000e\u0010\r\u001a\b\u0012\u0004\u0012\u00020\b0\u000bH\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\u000e"}, d2 = {"Lkz/sapasoft/emark/app/data/local/room/project/ProjectDataSource;", "Lkz/sapasoft/emark/app/data/local/room/project/ProjectRepository;", "projectDao", "Lkz/sapasoft/emark/app/data/local/room/project/ProjectDao;", "(Lkz/sapasoft/emark/app/data/local/room/project/ProjectDao;)V", "addProject", "", "projectModel", "Lkz/sapasoft/emark/app/domain/model/ProjectModel;", "addProjectList", "projectModelList", "", "deleteAll", "findAll", "app_release"}, k = 1, mv = {1, 1, 16})
/* compiled from: ProjectDataSource.kt */
public final class ProjectDataSource implements ProjectRepository {
    private final ProjectDao projectDao;

    @Inject
    public ProjectDataSource(ProjectDao projectDao2) {
        Intrinsics.checkParameterIsNotNull(projectDao2, "projectDao");
        this.projectDao = projectDao2;
    }

    public long addProject(ProjectModel projectModel) {
        Intrinsics.checkParameterIsNotNull(projectModel, "projectModel");
        return this.projectDao.insert(projectModel);
    }

    public void addProjectList(List<ProjectModel> list) {
        Intrinsics.checkParameterIsNotNull(list, "projectModelList");
        this.projectDao.insert(list);
    }

    public List<ProjectModel> findAll() {
        return this.projectDao.findAll();
    }

    public void deleteAll() {
        this.projectDao.deleteAll();
    }
}
