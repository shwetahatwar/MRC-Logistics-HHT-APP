package com.briot.balmerlawrie.implementor.ui.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity

import com.briot.balmerlawrie.implementor.R

class VendorMaterialScanFragment : Fragment() {

    companion object {
        fun newInstance() = VendorMaterialScanFragment()
    }

    private lateinit var viewModel: VendorMaterialScanViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.vendor_material_scan_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(VendorMaterialScanViewModel::class.java)
        // TODO: Use the ViewModel
        (this.activity as AppCompatActivity).setTitle("Vendor Material Scan")

    }

}
