package kz.sapasoft.emark.app.ui.photo

import kz.sapasoft.emark.app.domain.model.ImageDataModel

interface OnPhotoViewListener {

    fun onPhotoClick(imageDataModel: ImageDataModel)
}