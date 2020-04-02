package com.briot.balmerlawrie.implementor.ui.main

//import com.briot.balmerlawrie.implementor.repository.remote.RoleAccessRelation
import android.Manifest
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.briot.balmerlawrie.implementor.R
import kotlinx.android.synthetic.main.home_fragment.*

import android.content.Context;
import android.content.pm.PackageManager
import android.telephony.TelephonyManager;
import androidx.core.app.ActivityCompat


class HomeFragment : androidx.fragment.app.Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel
    private val RECORD_REQUEST_CODE = 101

    lateinit var cardView: CardView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
//        val rootView = inflater.inflate(R.layout.home_fragment, container, false)
//        this.cardView = rootView.findViewById(R.id.materialPutaway)
//        this.cardView.setOnClickListener {
//            // your code to perform when the user clicks on the ImageView
//            Log.d(TAG, ".............")
//        }
//        return rootView
        return inflater.inflate(R.layout.home_fragment, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        (this.activity as AppCompatActivity).setTitle("Dashboard")

        Log.d(TAG, "////////////////")

        materialPutaway.setOnClickListener { Navigation.findNavController(it).navigate(R.id.action_homeFragment_to_putawayFragment) }
        Log.d(TAG, " materialPutaway.setOnClickListener { Navigation.findNavController(it).navigate(R.id.action_homeFragment_to_putawayFragment) }")
        //  val recyclerView = findViewById<CardView>(R.id.materialPutaway)
        materialPicking.setOnClickListener { Navigation.findNavController(it).navigate(R.id.action_homeFragment_to_pickingFragment) }
        vendorMaterialScan.setOnClickListener { Navigation.findNavController(it).navigate(R.id.action_homeFragment_to_vendorMaterialScanFragment) }
       physicalStock.setOnClickListener { Navigation.findNavController(it).navigate(R.id.action_homeFragment_to_physicalStockVerificationFragment) }


        try {
            val TelephonyManager = context!!.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

            // Log.d(ContentValues.TAG, "-----get------" + TelephonyManager)
            if (ActivityCompat.checkSelfPermission(requireContext(),
                            Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            Log.d(ContentValues.TAG, "-----deviceID2------" + TelephonyManager.getDeviceId(1))
            Log.d(ContentValues.TAG, "-----deviceID1------" + TelephonyManager.getDeviceId(0))
            Log.d(ContentValues.TAG, "-----meid------" + TelephonyManager.deviceId)


            }
            catch ( exception: Throwable ){
                Log.d(ContentValues.TAG, "-----exception------" + exception)
            }


    }

    }
