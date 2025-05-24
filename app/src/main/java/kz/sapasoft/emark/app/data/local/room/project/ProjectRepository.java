package kz.sapasoft.emark.app.data.local.room.project;

import java.util.List;
import kotlin.Metadata;
import kz.sapasoft.emark.app.domain.model.ProjectModel;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0016\u0010\u0006\u001a\u00020\u00032\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00050\bH&J\b\u0010\t\u001a\u00020\u0003H&J\u000e\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00050\bH&¨\u0006\u000b"}, d2 = {"Lkz/sapasoft/emark/app/data/local/room/project/ProjectRepository;", "", "addProject", "", "projectModel", "Lkz/sapasoft/emark/app/domain/model/ProjectModel;", "addProjectList", "projectModelList", "", "deleteAll", "findAll", "app_release"}, k = 1, mv = {1, 1, 16})
/* compiled from: ProjectRepository.kt */
public interface ProjectRepository {
    long addProject(ProjectModel projectModel);

    void addProjectList(List<ProjectModel> list);

    void deleteAll();

    List<ProjectModel> findAll();
}
