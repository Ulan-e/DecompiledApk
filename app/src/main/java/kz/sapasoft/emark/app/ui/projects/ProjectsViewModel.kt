package kz.sapasoft.emark.app.ui.projects

import androidx.lifecycle.MutableLiveData
import kz.sapasoft.emark.app.core.BaseViewModel
import kz.sapasoft.emark.app.data.cloud.ResultWrapper
import kz.sapasoft.emark.app.data.cloud.repository.BaseCloudRepository
import kz.sapasoft.emark.app.data.local.prefs.PrefsImpl
import kz.sapasoft.emark.app.data.local.room.project.ProjectRepository
import kz.sapasoft.emark.app.data.local.room.tag.TagRepository
import kz.sapasoft.emark.app.domain.model.ProjectModel
import kz.sapasoft.emark.app.domain.model.TagModel
import java.util.Locale
import javax.inject.Inject
import kotlin.coroutines.Continuation
import kotlin.jvm.internal.Intrinsics

class ProjectsViewModel @Inject constructor(
    prefsImpl2: PrefsImpl,
    baseCloudRepository2: BaseCloudRepository,
    projectRepository2: ProjectRepository,
    tagRepository2: TagRepository
) : BaseViewModel() {
    private val TAG = javaClass.simpleName

    /* access modifiers changed from: private */
    @JvmField
    val baseCloudRepository: BaseCloudRepository
    private val `error$delegate`: MutableLiveData<ResultWrapper.Error> by lazy {
        MutableLiveData<ResultWrapper.Error>()
    }
    private val `isRefreshing$delegate`: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }


    /* access modifiers changed from: private */
    @JvmField
    var pageProject = 0

    /* access modifiers changed from: private */
    @JvmField
    var pageTag = 1

    /* access modifiers changed from: private */
    @JvmField
    val prefsImpl: PrefsImpl

    /* access modifiers changed from: private */
    @JvmField
    val projectRepository: ProjectRepository

    /* access modifiers changed from: private */
    @JvmField
    val projectsAll = ArrayList<ProjectModel>()
    private val `projectsData$delegate`: MutableLiveData<List<ProjectModel>> by lazy {
        MutableLiveData<List<ProjectModel>>()
    }
    private val `searchString$delegate`: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    private val tagRepository: TagRepository

    /* access modifiers changed from: private */
    @JvmField
    val tagsAll: ArrayList<TagModel> = ArrayList<TagModel>()
    val error: MutableLiveData<ResultWrapper.Error>
        get() = `error$delegate`
    val projectsData: MutableLiveData<List<ProjectModel>>
        get() = `projectsData$delegate`
    val searchString: MutableLiveData<String>
        get() = `searchString$delegate`
    val isRefreshing: MutableLiveData<Boolean>
        get() = `isRefreshing$delegate`

    init {
        Intrinsics.checkParameterIsNotNull(prefsImpl2, "prefsImpl")
        Intrinsics.checkParameterIsNotNull(baseCloudRepository2, "baseCloudRepository")
        Intrinsics.checkParameterIsNotNull(projectRepository2, "projectRepository")
        Intrinsics.checkParameterIsNotNull(tagRepository2, "tagRepository")
        prefsImpl = prefsImpl2
        baseCloudRepository = baseCloudRepository2
        projectRepository = projectRepository2
        tagRepository = tagRepository2
        projects
        getTags()
    }

    /* access modifiers changed from: private */
    private fun insertTagEntityList(list: List<TagModel?>?) {
        tagRepository.deleteAll()
        tagRepository.addTagList(list)
    }

    private fun getTags() {
        launchIO {
            isRefreshing.postValue(true)
            if (!prefsImpl.offline) {
                val result = baseCloudRepository.getTagList(pageTag)

                when (result) {
                    is ResultWrapper.Error -> error.postValue(result)
                    is ResultWrapper.Success -> {
                        val tags = result.value
                        if (tags?.isNotEmpty() == true) {
                            tagsAll.addAll(tags)
                          //  getTags()  // Recursive call for additional tags
                            insertTagEntityList(tagsAll)
                        } else {
                            insertTagEntityList(tagsAll)
                        }
                    }
                }
            }
            isRefreshing.postValue(false)
        }
    }

    val projects: Unit
        get() {
            if (pageProject == 1) {
                projectsAll.clear()
            }

            launchIO {
                isRefreshing.postValue(true)

                if (prefsImpl.offline) {
                    val allProjects = projectRepository.findAll()
                    projectsAll.addAll(allProjects)
                    projectsData.postValue(allProjects)
                    isRefreshing.postValue(false)
                } else {
                    pageProject++
                    val result = baseCloudRepository.getProjectList(pageProject)

                    when (result) {
                        is ResultWrapper.Error -> error.postValue(result)
                        is ResultWrapper.Success -> {
                            val newProjects = result.value
                            if (newProjects?.isNotEmpty() == true) {
                                projectsAll.addAll(newProjects)
                                projects // recursively calls itself to load more projects
                            } else {
                                projectsData.postValue(projectsAll)
                            }
                        }
                    }
                    isRefreshing.postValue(false)
                }
            }
        }

    fun getFilteredProjects(filter: String) {
        val filteredProjects = projectsAll.filter { project ->
            val projectName = project.name?.toLowerCase(Locale.ROOT)
            val filterText = filter.toLowerCase(Locale.ROOT)

            if (projectName != null && projectName.contains(filterText)) {
                true
            } else {
                val regionName = project.regionName?.toLowerCase(Locale.ROOT)
                regionName != null && regionName.contains(filterText)
            }
        }

        projectsData.postValue(filteredProjects)
    }
}
