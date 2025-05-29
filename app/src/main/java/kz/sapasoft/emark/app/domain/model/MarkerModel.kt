package kz.sapasoft.emark.app.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import kz.sapasoft.emark.app.utils.Constants

@Entity("marker")
data class MarkerModel(
    @PrimaryKey var id: String,
    var updated: Long? = 0L,
    var templateId: String? = "",
    var markerId: String? = "",
    var markerModel: String? = "",
    var color: String? = "",
    var passiveId: String? = "",
    var projectIds: List<String>? = emptyList(),
    var depth: Double? = 0.0,
    var location: List<Double>? = emptyList(),
    var fields: List<FieldModel>? = emptyList(),
    var generalId: String? = "",
    var markerType: String? = "",
    var idLocal: String?,
    var status: Constants.MarkerStatus? = null,
    var isNotSynced: Boolean = false
) : Serializable {

    fun toNullable(): MarkerModelNullable {
        return MarkerModelNullable(
            id, updated, templateId, markerId, markerModel, color, passiveId,
            projectIds, depth, location, fields, generalId, markerType, idLocal, status ?: Constants.MarkerStatus.NORMAL
        )
    }

    fun toSync(notSynced: Boolean): MarkerModelSync {
        return MarkerModelSync(
            id, updated, templateId, markerId, markerModel, color, passiveId,
            projectIds, depth, location, fields, generalId, markerType, idLocal, status ?: Constants.MarkerStatus.NORMAL,
            notSynced
        )
    }
}