package kz.sapasoft.emark.app.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.File
import java.io.Serializable

@Entity("image_data")
data class ImageDataModel(
    @PrimaryKey var id: String,
    var localIdParent: String? = null,
    var metadata: MetaData? = null,
    var file: File? = null
) : Serializable {

    data class MetaData(
        var parentId: String? = null,
        var queryKey: String? = null
    ): Serializable
}