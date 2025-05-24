package kz.sapasoft.emark.app.domain.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kz.sapasoft.emark.app.utils.Constants
import java.io.Serializable

@Entity(
    "marker_sync",
    indices = [Index(
        value = ["markerId"], unique = true
    )]
)
data class MarkerModelSync(
    @PrimaryKey var id: String,
    var updated: Long? = null,
    var templateId: String? = null,
    var markerId: String? = null,
    var markerModel: String? = null,
    var color: String? = null,
    var passiveId: String? = null,
    var projectIds: List<String>? = null,
    var depth: Double? = null,
    var location: List<Double>? = null,
    var fields: List<FieldModel>? = null,
    var generalId: String? = null,
    var markerType: String? = null,
    var idLocal: String? = null,
    var status: Constants.MarkerStatus = Constants.MarkerStatus.NORMAL
) : Serializable {

    fun toModel(): MarkerModel {
        return MarkerModel(
            id = this.id,
            updated = this.updated,
            templateId = this.templateId,
            markerId = this.markerId,
            markerModel = this.markerModel,
            color = this.color,
            passiveId = this.passiveId,
            projectIds = this.projectIds,
            depth = this.depth,
            location = this.location,
            fields = this.fields,
            generalId = this.generalId,
            markerType = this.markerType,
            idLocal = this.idLocal,
            status = this.status
        )
    }
}