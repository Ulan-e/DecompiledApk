package kz.sapasoft.emark.app.ui.map

//import com.hoho.android.usbserial.driver.UsbSerialDriver
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.internal.view.SupportMenu
import androidx.core.os.bundleOf
import androidx.core.view.InputDeviceCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.decompiledapk.R
import com.felhr.usbserial.UsbSerialInterface
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kz.sapasoft.emark.app.BuildConfig
import kz.sapasoft.emark.app.core.BluetoothService
import kz.sapasoft.emark.app.core.BluetoothServiceCallback
import kz.sapasoft.emark.app.domain.model.MarkerModel
import kz.sapasoft.emark.app.domain.model.ProjectModel
import kz.sapasoft.emark.app.ui.MainActivity
import kz.sapasoft.emark.app.ui.OnNewDeviceAttached
import kz.sapasoft.emark.app.ui.base.DaggerFragmentExtended
import kz.sapasoft.emark.app.ui.marker.MarkerFragment
import kz.sapasoft.emark.app.ui.marker.OnMarkerChangeListener
import kz.sapasoft.emark.app.utils.Constants
import kz.sapasoft.emark.app.utils.MarkerDrawer
import kz.sapasoft.emark.app.utils.Utils
import kz.sapasoft.emark.app.utils.showToast
import okhttp3.internal.cache.DiskLruCache
import org.osmdroid.api.IGeoPoint
import org.osmdroid.bonuspack.clustering.RadiusMarkerClusterer
import org.osmdroid.config.Configuration
import org.osmdroid.config.IConfigurationProvider
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import java.io.ByteArrayOutputStream
import java.nio.charset.Charset
import java.util.UUID
import javax.inject.Inject
import kotlin.jvm.internal.Intrinsics

@Suppress("UNREACHABLE_CODE")
class MapFragment : DaggerFragmentExtended(), OnMarkerChangeListener,
    UsbSerialInterface.UsbReadCallback,
    OnNewDeviceAttached {

    private val REQUEST_PERMISSIONS_CODE = 101
    private val TAG: String = "BLEq"
    private var `_$_findViewCache`: HashMap<*, *>? = null
    private val buffer = ByteArrayOutputStream()
    private val kzGeoPoint: GeoPoint = GeoPoint(48.005284, 66.9045435)
    private var mIsManualNavigation = false

    private var rootView: View? = null
    private var mapView: MapView? = null

    private var bluetoothService: BluetoothService? = null

    /* access modifiers changed from: private */
    @JvmField
    val mMarkerList = ArrayList<Marker>()

    /* access modifiers changed from: private */
    @JvmField
    var mMarkerModelList: ArrayList<MarkerModel> = ArrayList<MarkerModel>()

    /* access modifiers changed from: private */
    @JvmField
    var mMyLocationOverlay: MyLocationNewOverlay? = null

    private val onMarkerClickListener =
        Marker.OnMarkerClickListener { marker, mapView ->
            val markerModel = this@MapFragment.mMarkerModelList.firstOrNull {
                it.id == marker.id
            }
            if (markerModel != null) {
                this@MapFragment.openMarkerFragment(markerModel)
            }
            false
        }

    /* access modifiers changed from: private */
    var poiMarkers: RadiusMarkerClusterer? = null
    private val `viewModel$delegate`: MapViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(MapViewModel::class.java)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    // @Metadata(bv = [1, 0, 3], k = 3, mv = [1, 1, 16])
    object WhenMappings {
        /* synthetic */ val `$EnumSwitchMapping$0`: IntArray

        init {
            val iArr = IntArray(Constants.MarkerStatus.entries.toTypedArray().size)
            `$EnumSwitchMapping$0` = iArr
            iArr[Constants.MarkerStatus.NEW.ordinal] = 1
            `$EnumSwitchMapping$0`[Constants.MarkerStatus.EDITED.ordinal] = 2
        }
    }

    val viewModel: MapViewModel
        /* access modifiers changed from: private */
        get() = `viewModel$delegate` as MapViewModel

    /* synthetic */ override fun onDestroyView() {
        super.onDestroyView()
        `_$_clearFindViewByIdCache`()
    }

    init {
        val simpleName = javaClass.simpleName
        Intrinsics.checkExpressionValueIsNotNull(simpleName, "this::class.java.simpleName")
    }

    override fun onCreateView(
        layoutInflater: LayoutInflater,
        viewGroup: ViewGroup?,
        bundle: Bundle?
    ): View? {
        rootView = layoutInflater.inflate(R.layout.fragment_map, viewGroup, false)
        mapView = rootView?.findViewById(R.id.map_view)
        return rootView
    }

    override fun onViewCreated(view: View, bundle: Bundle?) {
        Intrinsics.checkParameterIsNotNull(view, "view")
        super.onViewCreated(view, bundle)
        setHasOptionsMenu(true);
        setFloatingActionButton()
        handleBluetoothConnectionAndRead()
        val textView = view.findViewById(R.id.tv_toolbar) as TextView
        Intrinsics.checkExpressionValueIsNotNull(textView, "tv_toolbar")
        textView.setText(getString(R.string.map))
        val activity: FragmentActivity = requireActivity()
        if (activity != null) {
            (activity as MainActivity).setSupportActionBar(view.findViewById(R.id.toolbar) as Toolbar)
            val utils = Utils.INSTANCE
            val textView2 = view.findViewById(R.id.tv_toolbar) as TextView
            Intrinsics.checkExpressionValueIsNotNull(textView2, "tv_toolbar")
            utils.hideKeyboard(textView2)
            viewModel.getMarkerList(arrayListOf(projectModel.id))
            viewModel.getTemplateList(projectModel.markerTemplateIds)

            val activity2: FragmentActivity = requireActivity()
            if (activity2 != null) {
                (activity2 as MainActivity).setDeviceListener(this)
                mapInit(view)
                drawPin(view)
                setObservers()
                //setListeners()
                // requestGpsPermission()
                checkAndRequestPermissions()
                return
            }
            throw TypeCastException("null cannot be cast to non-null type kz.sapasoft.emark.app.ui.MainActivity")
        }
        throw TypeCastException("null cannot be cast to non-null type kz.sapasoft.emark.app.ui.MainActivity")
    }

    override fun onDestroy() {
        super.onDestroy()
        bluetoothService?.disconnectAndClose()
        bluetoothService = null // Clear reference
    }

    private fun setFloatingActionButton(){
        val fab = rootView?.findViewById<FloatingActionButton>(R.id.fab)
        val markerId = generateRandom10DigitString()
        fab?.setOnClickListener {
            val markerLocation = this.getMapLocation()
            if(markerLocation != null) {
                openMarkerFragment(
                    MarkerModel(
                        projectIds = arrayListOf(projectModel.id),
                        idLocal = UUID.randomUUID().toString(),
                        markerModel = "1405",
                        generalId = markerId,
                        markerId = markerId,
                        id = projectModel.id,
                        location = listOf(markerLocation.latitude, markerLocation.longitude),
                        status = Constants.MarkerStatus.NEW
                    )
                )
            }
        }
    }

    fun generateRandom10DigitString(): String {
        val number = (0..999_999_999).random()
        return number.toString().padStart(10, '0')
    }

    private val projectModel: ProjectModel
        private get() {
            val arguments: Bundle = requireArguments()
            val serializable = if (arguments != null) arguments.getSerializable("project") else null
            if (serializable != null) {
                return serializable as ProjectModel
            }
            throw TypeCastException("null cannot be cast to non-null type kz.sapasoft.emark.app.domain.model.ProjectModel")
        }

    @SuppressLint("MissingPermission")
    private fun handleBluetoothConnectionAndRead() {
        bluetoothService = BluetoothService(
            requireContext(),
            object : BluetoothServiceCallback {
                override fun onDiscovered(value: String?) {
                    Log.d(TAG, "BS onDiscovered value $value ")
                    activity?.runOnUiThread {
                        if (isAdded) { // Ensure fragment is still added and has a context
                            context?.let { ctx ->
                                ctx.showToast("BS onDiscovered value $value")
                            }
                        }
                    }
                }

                override fun onChanged(
                    serviceId: UUID?,
                    characteristicId: UUID?,
                    characteristicValue: String?
                ) {
                    Log.d(
                        TAG, "BS onChanged " +
                                "serviceId: $serviceId " +
                                "characteristicId $characteristicId " +
                                "characteristicValue $characteristicValue"
                    )

                    if (characteristicValue != null) {
                        Log.d(TAG, "BS connecting $characteristicValue")
                        if (viewModel.checkId(characteristicValue)) {
                            addMarker(characteristicValue)
                        } else {
                            showSnackBar("checkId return false")
                        }
                    }
                }

                override fun onError(e: Exception?) {
                    Log.d(TAG, "BS onError e: ${e?.message}")
                    e?.printStackTrace()
                    activity?.runOnUiThread {
                        if (isAdded) {
                            context?.let { ctx ->
                                requireContext().showToast("BS onError e: ${e?.message}")
                            }
                        }
                    }
                }
            }
        )
        bluetoothService?.searchAndConnectDevice()
    }

//    private fun setListeners() {
//        val systemService: Any = requireContext().getSystemService(android.content.Context.USB_SERVICE)
//        if (systemService != null) {
//            val usbManager: UsbManager = systemService as UsbManager
//            val findAllDrivers: List<UsbSerialDriver> =
//                UsbSerialProber.getDefaultProber().findAllDrivers(usbManager)
//            if (!findAllDrivers.isEmpty()) {
//                val usbSerialDriver: UsbSerialDriver = findAllDrivers[0]
//                Intrinsics.checkExpressionValueIsNotNull(usbSerialDriver, "availableDrivers[0]")
//                val openDevice: UsbDeviceConnection =
//                    usbManager.openDevice(usbSerialDriver.getDevice())
//                val usbSerialDriver2: UsbSerialDriver = findAllDrivers[0]
//                Intrinsics.checkExpressionValueIsNotNull(usbSerialDriver2, "availableDrivers[0]")
//                val createUsbSerialDevice: UsbSerialDevice =
//                    UsbSerialDevice.createUsbSerialDevice(usbSerialDriver2.getDevice(), openDevice)
//                createUsbSerialDevice.open()
//                createUsbSerialDevice.setBaudRate(4800)
//                createUsbSerialDevice.setDataBits(8)
//                createUsbSerialDevice.setParity(0)
//                createUsbSerialDevice.setFlowControl(0)
//                createUsbSerialDevice.setStopBits(1)
//                createUsbSerialDevice.read(this)
//                return
//            }
//            return
//        }
//        throw TypeCastException("null cannot be cast to non-null type android.hardware.usb.UsbManager")
//    }

    private fun setObservers() {
        val viewModel = viewModel
        viewModel.markerModelListData.observe(viewLifecycleOwner) { markerModels ->
            mMarkerModelList.clear()
            mMarkerList.clear()

            mapView?.overlays?.clear()
            mapView?.overlays?.add(mMyLocationOverlay)
            mapView?.invalidate()

            // Add map markers from the updated list
            addMapMarkers(markerModels)
        }
        viewModel.error.observe(viewLifecycleOwner) { error ->
            val errorMessage = error?.toString() ?: "Error ______"
            Log.e("MapFragment","errorMessage while syncing with server $errorMessage")
            // showSnackBar(errorMessage)
        }
    }

    /* access modifiers changed from: private */
    fun showSnackBar(str: String?) {
        (str as CharSequence?)?.let {
            Snackbar.make(
                mapView!!,
                it,
                BaseTransientBottomBar.LENGTH_INDEFINITE
            ).show()
        }
    }

    private fun drawPin(view: View) {
        val drawable: Drawable = getResources().getDrawable(R.drawable.ic_pin)
        Intrinsics.checkExpressionValueIsNotNull(
            drawable,
            "resources.getDrawable(R.drawable.ic_pin)"
        )
        val `bitmap$default`: Bitmap =
            toBitmap(drawable, 0, 0, null as Bitmap.Config?, 7, null as Any?)
        (view.findViewById<ImageView>(R.id.iv_pin) as ImageView).setImageBitmap(
            `bitmap$default`
        )
        (view.findViewById<ImageView>(R.id.iv_pin) as ImageView).setPadding(
            0,
            0,
            0,
            `bitmap$default`.height
        )
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    @SuppressLint("CutPasteId")
    private fun mapInit(view: View) {
        Configuration.getInstance()
            .load(getContext(), PreferenceManager.getDefaultSharedPreferences(getContext()))
        val instance: IConfigurationProvider = Configuration.getInstance()
        Intrinsics.checkExpressionValueIsNotNull(instance, "Configuration.getInstance()")
        instance.setUserAgentValue(BuildConfig.APPLICATION_ID)
        val myLocationNewOverlay =
            MyLocationNewOverlay(mapView)
        mMyLocationOverlay = myLocationNewOverlay
        if (myLocationNewOverlay != null) {
            myLocationNewOverlay.enableMyLocation()
        }
        mapView?.getOverlays()?.clear()
        mapView?.getOverlays()?.add(mMyLocationOverlay)
        mapView?.setTileSource(TileSourceFactory.MAPNIK)
        mapView?.setMultiTouchControls(true)
        mapView?.setMaxZoomLevel(java.lang.Double.valueOf(22.0))
        mapView?.setMinZoomLevel(java.lang.Double.valueOf(5.0))
        mapView?.getController()?.setZoom(5.0)
        mapView?.getController()?.setCenter(kzGeoPoint)
        mapView?.getZoomController()
            ?.setVisibility(CustomZoomButtonsController.Visibility.SHOW_AND_FADEOUT)
    }

    private fun moveCamera(geoPoint: GeoPoint?, d: Double?) {
        if (geoPoint != null) {
            if (d != null) {
                mapView?.getController()?.setZoom(12.0)
            }
            mapView?.getController()?.setCenter(geoPoint)
        }
    }

    /* access modifiers changed from: private */
    @SuppressLint("RestrictedApi", "UseCompatLoadingForDrawables")
    fun addMapMarkers(list: List<MarkerModel>) {
        poiMarkers = RadiusMarkerClusterer(getContext())
        mMarkerModelList.addAll(list)
        val drawable: Drawable = getResources().getDrawable(R.drawable.ic_menu_compass)
        val bitmap: Bitmap = (drawable as BitmapDrawable).getBitmap()
        val radiusMarkerClusterer: RadiusMarkerClusterer? = poiMarkers
        if (radiusMarkerClusterer != null) {
            radiusMarkerClusterer.setIcon(bitmap)
        }
        val radiusMarkerClusterer2: RadiusMarkerClusterer? = poiMarkers
        radiusMarkerClusterer2?.setRadius(ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION)

        mapView?.overlays?.add(poiMarkers)
        for (next in list) {
            // val status: Constants.MarkerStatus = next.status
            val status = Constants.MarkerStatus.NORMAL
            val i = WhenMappings.`$EnumSwitchMapping$0`[status.ordinal]
            if (i == 1) {
                addGasMarkerToMapMarkers(next, -16711936)
            } else if (i == 2) {
                addGasMarkerToMapMarkers(next, SupportMenu.CATEGORY_MASK)
            }
            addGasMarkerToMapMarkers(next, InputDeviceCompat.SOURCE_ANY)
        }
        return
        throw TypeCastException("null cannot be cast to non-null type android.graphics.drawable.BitmapDrawable")
    }

    private fun addGasMarkerToMapMarkers(markerModel: MarkerModel, i: Int) {
        val marker = Marker(mapView)
        val location: List<Double>? = markerModel.location
        if (location == null) {
            Intrinsics.throwNpe()
        }
        val doubleValue = location?.get(0)
        val location2: List<Double>? = markerModel.location
        if (location2 == null) {
            Intrinsics.throwNpe()
        }
        marker.position = doubleValue?.let { GeoPoint(it, location2?.get(1) ?: 0.0) }
        marker.setOnMarkerClickListener(onMarkerClickListener)
        marker.icon = MarkerDrawer.INSTANCE.makeCircle(i, viewModel.markerSize)
        marker.id = markerModel.id
        mMarkerList.add(marker)
        val radiusMarkerClusterer: RadiusMarkerClusterer? = poiMarkers
        radiusMarkerClusterer?.add(marker)
    }

    private fun enableManualNavigation(z: Boolean) {
        mIsManualNavigation = z
        if (z) {
            val imageView = rootView?.findViewById(R.id.iv_pin) as ImageView
            Intrinsics.checkExpressionValueIsNotNull(imageView, "iv_pin")
            imageView.visibility = View.VISIBLE
            return
        }
        val imageView2 = rootView?.findViewById(R.id.iv_pin) as ImageView
        Intrinsics.checkExpressionValueIsNotNull(imageView2, "iv_pin")
        imageView2.visibility = View.GONE
    }

    override fun onMarkerChange() {
        viewModel.getMarkerEntityList(projectModel.id)
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        Intrinsics.checkParameterIsNotNull(menu, "menu")
        Intrinsics.checkParameterIsNotNull(menuInflater, "inflater")
        menuInflater.inflate(R.menu.map_menu, menu)
        super.onCreateOptionsMenu(menu, menuInflater)
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        Intrinsics.checkParameterIsNotNull(menuItem, "item")
        when (menuItem.itemId) {
            16908332 -> {
                val activity: FragmentActivity = requireActivity()
                if (activity != null) {
                    activity.onBackPressed()
                }
            }

            R.id.action_manual_nav -> {
                menuItem.setChecked(!menuItem.isChecked)
                enableManualNavigation(menuItem.isChecked)
            }

            R.id.action_synchronize -> viewModel.synchronizeMarkers(projectModel.id)
            R.id.action_to_last_marker -> {
                `moveCamera$default`(
                    this,
                    mMarkerList.lastOrNull()?.position,
                    null as Double?,
                    2,
                    null as Any?
                )
            }
        }
        return super.onOptionsItemSelected(menuItem)
    }

    override fun onReceivedData(bArr: ByteArray) {
        Log.e("SCAN_MARKER", DiskLruCache.VERSION_1)
        buffer.write(bArr)
        val byteArray = buffer.toByteArray()
        Intrinsics.checkExpressionValueIsNotNull(byteArray, "buffer.toByteArray()")
        val str = String(byteArray, Charset.defaultCharset())
        Log.e("SCAN_MARKER", str)
        var d: Double? = null
        if (str.contains("DL,08,", ignoreCase = false)) {
            Log.e("SCAN_MARKER", str)
            buffer.reset()
            if (getMapLocation() != null) {
                val location = getMapLocation()
                if (location != null) {
                    d = java.lang.Double.valueOf(location.latitude)
                }
                if (d != null) {
                    val location2 = this.getMapLocation()
                    if (location2 == null) {
                        Intrinsics.throwNpe()
                    }
                    if (location2!!.accuracy > 5f) {
                        Log.e("SCAN_MARKER", "4")
                        val activity: FragmentActivity? = getActivity()
                        activity?.runOnUiThread {
                            val location =
                                getMapLocation() ?: throw NullPointerException("Location is null")
                            alertLowAccuracy(str, location)
                        }
                        return
                    }
                    Log.e("SCAN_MARKER", "5")
                    val viewModel = viewModel
                    val location3 = this.getMapLocation()
                    if (location3 == null) {
                        Intrinsics.throwNpe()
                    }
                    openMarkerFragment(viewModel.getMarkerModelFromByteStr(str, location3)!!)
                    return
                }
            }
            val string: String = getString(R.string.location_not_found)
            Intrinsics.checkExpressionValueIsNotNull(
                string,
                "getString(R.string.location_not_found)"
            )
            showSnackBar(string)
        }
    }

    private fun addMarker(str: String) {
//        val location2 = this.getMapLocation()
//        Log.e("addMarker", " location2 $location2")
//        if (location2!!.accuracy > 5f) {
//            Log.e("addMarker", "4")
//            val activity: FragmentActivity? = getActivity()
//            activity?.runOnUiThread {
//                val location = getMapLocation() ?: throw NullPointerException("Location is null")
//                alertLowAccuracy(str, location)
//            }
//            return
//        }
        Log.e("addMarker", "New marker from scanner")
        val viewModel = viewModel
        val location3 = this.getMapLocation()
        val marker = viewModel.getMarkerModelFromByteStr(str, location3)
        if (marker != null) {
            addGasMarkerToMapMarkers(marker,1)
            openMarkerFragment(marker)
        }
    }

    fun getMapLocation(): Location? {
        return if (mIsManualNavigation) {
            val location = Location("map_center").apply {
                accuracy = 0.0f
                val mapCenter = mapView?.mapCenter
                latitude = mapCenter?.latitude!!
                longitude = mapCenter?.longitude!!
            }
            location
        } else {
            mMyLocationOverlay?.lastFix
        }
    }

    /* access modifiers changed from: private */
    fun alertLowAccuracy(str: String?, location: Location) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.low_gps_accuracy) as CharSequence?)
        builder.setCancelable(false)
        builder.setMessage(
            getString(
                R.string.low_accuracy_continue,
                Integer.valueOf(location.accuracy.toInt())
            ) as CharSequence?
        )
        builder.setPositiveButton(getString(R.string.yes)) { dialog, _ ->
            val location = getMapLocation() ?: throw NullPointerException("Location is null")
            val markerModel = viewModel.getMarkerModelFromByteStr(str, location)
            openMarkerFragment(markerModel!!)
            dialog.dismiss()
        }
        builder.setNegativeButton(getString(R.string.no)) { dialog, _ ->
            dialog.dismiss()
        }
        val create = builder.create()
        Intrinsics.checkExpressionValueIsNotNull(create, "builder.create()")
        create.show()
    }

    /* access modifiers changed from: private */
    fun openMarkerFragment(markerModel: MarkerModel) {
        getChildFragmentClickListener().onFragmentAdd(
            MarkerFragment.newInstance(
                projectModel, markerModel, this
            )
        )
    }

    override fun onRequestPermissionsResult(i: Int, strArr: Array<String>, iArr: IntArray) {
        Intrinsics.checkParameterIsNotNull(strArr, "permissions")
        Intrinsics.checkParameterIsNotNull(iArr, "grantResults")
        super.onRequestPermissionsResult(i, strArr, iArr)
        if (i == REQUEST_PERMISSIONS_CODE) {
            var allGranted = true
            for (result in iArr) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false
                    break
                }
            }
            if (allGranted) {
                Log.d(TAG, "All permissions granted.")
                // It's safer to re-initialize or ensure bluetoothService is ready here if it wasn't earlier
                // For now, let's assume handleBluetoothConnectionAndRead() was already called
                // and it will retry or the service is waiting.
                // A good place to initiate the scan if not already started:
                bluetoothService?.searchAndConnectDevice()
            } else {
                Log.e(TAG, "Not all permissions granted.")
            }
        }
    }

    val location: Location?
        /* access modifiers changed from: private */
        get() {
            if (mIsManualNavigation) {
                val location = Location("map_center")
                location.accuracy = 0.0f
                val mapCenter: IGeoPoint? = mapView?.getMapCenter()
                location.latitude = mapCenter?.getLatitude()!!
                val mapCenter2: IGeoPoint = mapView?.getMapCenter()!!
                location.longitude = mapCenter2.getLongitude()
                val mapCenter3: IGeoPoint = mapView!!.getMapCenter()
                val mapCenter4: IGeoPoint = mapView!!.getMapCenter()
                arrayListOf<Double>(
                    java.lang.Double.valueOf(mapCenter3.getLatitude()),
                    java.lang.Double.valueOf(mapCenter4.getLongitude())
                )
                return location
            }
            val myLocationNewOverlay: MyLocationNewOverlay? = mMyLocationOverlay
            return if (myLocationNewOverlay != null) {
                myLocationNewOverlay.getLastFix()
            } else null
        }

    override fun onNewDeviceAttached() {
        Log.d("SCAN_MARKER", "onNewDeviceAttached")
//        setListeners()
    }

    private fun checkAndRequestPermissions() {
        val permissionsToRequest = mutableListOf<String>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.BLUETOOTH_SCAN)
            }
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.BLUETOOTH_CONNECT)
            }
        } else {
            // For older versions, BLUETOOTH and BLUETOOTH_ADMIN are not runtime permissions
            // but ACCESS_FINE_LOCATION is, which you already handle.
        }

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.ACCESS_FINE_LOCATION)
            permissionsToRequest.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }

        if (permissionsToRequest.isNotEmpty()) {
            requestPermissions(permissionsToRequest.toTypedArray(), REQUEST_PERMISSIONS_CODE)
        } else {
            // All permissions granted, proceed with Bluetooth operations
            bluetoothService?.searchAndConnectDevice()
        }
    }

    companion object {

        fun newInstance(
            projectModel: ProjectModel,
            markerModel: MarkerModel?,
            mapFragment: MapFragment
        ): Fragment {
            Intrinsics.checkParameterIsNotNull(projectModel, "projectModel")
            val mapFragment = MapFragment()
            mapFragment.setArguments(bundleOf("project".to<String, ProjectModel>(projectModel)))
            return mapFragment
        }

        fun  /* synthetic */`moveCamera$default`(
            mapFragment: MapFragment,
            geoPoint: GeoPoint?,
            d: Double?,
            i: Int,
            obj: Any?
        ) {
            var d = d
            if (i and 2 != 0) {
                d = null
            }
            mapFragment.moveCamera(geoPoint, d)
        }
    }
}

fun toBitmap(
    drawable: Drawable,
    width: Int = 0,
    height: Int = 0,
    config: Bitmap.Config? = Bitmap.Config.ARGB_8888,
    i: Int,
    any: Any?
): Bitmap {
    val finalWidth = if (width > 0) width else drawable.intrinsicWidth
    val finalHeight = if (height > 0) height else drawable.intrinsicHeight
    val bitmap = Bitmap.createBitmap(finalWidth, finalHeight, config ?: Bitmap.Config.ARGB_8888)

    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, finalWidth, finalHeight)
    drawable.draw(canvas)

    return bitmap
}
