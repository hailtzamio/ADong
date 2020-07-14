package com.zamio.adong.ui.map

import RestClient
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import android.graphics.drawable.BitmapDrawable
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.elcom.com.quizupapp.ui.fragment.BaseFragment
import com.elcom.com.quizupapp.ui.network.RestData
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.Projection
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.zamio.adong.R
import com.zamio.adong.model.Project
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * A simple [Fragment] subclass.
 */
class MapFragment : BaseFragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    val ZOOM_LEVEL = 5f
    var locationManager: LocationManager? = null
    var latitude = 21.0278
    var longitude = 105.8342

    private val DEFAULT_FPS = 10
    private val DEFAULT_FREQUENCY = 2000
    val BUDAPEST = LatLng(47.504265, 19.046098)
    private val MOVE_DIFF = 0.0003

    private val latlng = SampleActivity.BUDAPEST
    private var marker: BlinkingMarker? = null

    private val mFrequency = DEFAULT_FREQUENCY
    private val mFps = DEFAULT_FPS
    private val mSyncMove = false


    companion object {
        var mapFragment: SupportMapFragment? = null
        val TAG: String = MapFragment::class.java.simpleName
        fun newInstance() = MapFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var rootView = inflater.inflate(R.layout.fragment_map, container, false)

        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        animationTest()
        return rootView
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap!!

        val location = LatLng(17.787203, 105.605202)
        with(mMap) {
            moveCamera(
                com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(
                    location,
                    ZOOM_LEVEL
                )
            )
//            addMarker(com.google.android.gms.maps.model.MarkerOptions().position(location))
        }

        getProjects()
    }

    private fun getBitmapIcon(type: Int): Bitmap {
        val height = 24
        val width = 24
        var bitmapdraw =
            ResourcesCompat.getDrawable(resources, R.drawable.busy_dot12, null) as BitmapDrawable
        when (type) {
            1 -> bitmapdraw =
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.busy_dot12,
                    null
                ) as BitmapDrawable
            2 -> bitmapdraw =
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.green_dot12,
                    null
                ) as BitmapDrawable
            3 -> bitmapdraw =
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.blue_icon12,
                    null
                ) as BitmapDrawable
        }

        val b = bitmapdraw.bitmap
        return Bitmap.createScaledBitmap(b, width, height, false)
    }

    private fun addMarkerBlink(position: LatLng, fps: Int, duration: Int, type: Int) {

        var bitmap = BitmapFactory.decodeResource(resources, R.drawable.blue_icon12)

        when (type) {
            1 -> bitmap =
                BitmapFactory.decodeResource(resources, R.drawable.busy_dot12)
            2 -> bitmap =
                BitmapFactory.decodeResource(resources, R.drawable.green_dot12)
            3 -> bitmap =
                BitmapFactory.decodeResource(resources, R.drawable.blue_icon12)
        }

        marker = BlinkingMarker(bitmap, mMap, fps, duration)
        marker!!.addToMap(position)
        marker!!.startBlinking()

    }

    private fun getProjects() {

        showProgessDialog()
        RestClient().getInstance().getRestService().getProjects(0, "", "")
            .enqueue(object :
                Callback<RestData<List<Project>>> {
                override fun onFailure(call: Call<RestData<List<Project>>>?, t: Throwable?) {
                    dismisProgressDialog()
                }

                override fun onResponse(
                    call: Call<RestData<List<Project>>>?,
                    response: Response<RestData<List<Project>>>?
                ) {
                    dismisProgressDialog()
                    if (response!!.body() != null && response!!.body().status == 1) {
                        val mList = response.body().data
                        if (mList != null) {
                            mList.forEach {
                                val location = LatLng(it.latitude ?: 0.0, it.longitude ?: 0.0)
//                                val marker = com.google.android.gms.maps.model.MarkerOptions().position(location).title(getProjectTitle(it)).icon(
//                                    com.google.android.gms.maps.model.BitmapDescriptorFactory.fromBitmap(getBitmapIcon(2)))


                                Log.d("Come Here", " onResponse ")

                                with(mMap) {

//                                    marker!!.removeMarker()

                                    when (it.status) {
                                        "PROCESSING" -> {
                                            if (it.teamType == "ADONG") {
                                                addMarkerBlink(location, mFps, mFrequency, 2)
//                                                addMarker(
//                                                    com.google.android.gms.maps.model.MarkerOptions()
//                                                        .position(location)
//                                                        .title(getProjectTitle(it)).icon(
//                                                        com.google.android.gms.maps.model.BitmapDescriptorFactory.fromBitmap(
//                                                            getBitmapIcon(2)
//                                                        )
//                                                    )
//                                                )
                                            } else {
                                                addMarkerBlink(location, mFps, mFrequency, 3)
//                                                addMarker(
//                                                    com.google.android.gms.maps.model.MarkerOptions()
//                                                        .position(location)
//                                                        .title(getProjectTitle(it)).icon(
//                                                        com.google.android.gms.maps.model.BitmapDescriptorFactory.fromBitmap(
//                                                            getBitmapIcon(3)
//                                                        )
//                                                    )
//                                                )
                                            }

                                        }
                                        "NEW" -> {
                                            addMarkerBlink(location, mFps, mFrequency, 1)
//                                            addMarker(
//                                                com.google.android.gms.maps.model.MarkerOptions()
//                                                    .position(location).title(getProjectTitle(it))
//                                                    .icon(
//                                                        com.google.android.gms.maps.model.BitmapDescriptorFactory.fromBitmap(
//                                                            getBitmapIcon(1)
//                                                        )
//                                                    )
//                                            )
                                        }
                                        else -> {

                                        }


                                    }
                                }

                            }
                        }
                    }
                }
            })
    }

    private fun getProjectTitle(project: Project): String {
        return project.name ?: "" + " \n ${project.address ?: ""}"
    }


    fun animateMarker(marker: Marker, toPosition: LatLng, hideMarker: Boolean) {
        val handler = Handler()
        val start = SystemClock.uptimeMillis()
        val proj: Projection = mMap.projection
        val startPoint: Point = proj.toScreenLocation(marker.position)
        val startLatLng = proj.fromScreenLocation(startPoint)
        val duration: Long = 500
        val interpolator: Interpolator = LinearInterpolator()
        handler.post(object : Runnable {
            override fun run() {
                val elapsed = SystemClock.uptimeMillis() - start
                val t: Float = interpolator.getInterpolation(
                    elapsed.toFloat()
                            / duration
                )
                val lng = t * toPosition.longitude + (1 - t) * startLatLng.longitude
                val lat = t * toPosition.latitude + (1 - t) * startLatLng.latitude
                marker.position = LatLng(lat, lng)
                if (t < 1.0) { // Post again 16ms later.
                    handler.postDelayed(this, 16)
                } else {
                    marker.isVisible = !hideMarker
                }
            }
        })
    }

    fun animationTest() {
//        val colorAnim: ObjectAnimator  = ObjectAnimator.ofInt(tvTitle, "backgroundColor", Color.BLACK, Color.TRANSPARENT)
//        colorAnim.duration = 500
//        colorAnim.setEvaluator(ArgbEvaluator())
//        colorAnim.repeatCount = ValueAnimator.INFINITE
//        colorAnim.repeatMode = ValueAnimator.REVERSE
//        colorAnim.start()
    }

}

