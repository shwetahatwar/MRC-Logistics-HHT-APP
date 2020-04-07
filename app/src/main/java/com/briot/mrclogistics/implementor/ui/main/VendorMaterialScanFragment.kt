package com.briot.mrclogistics.implementor.ui.main

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import io.github.pierry.progress.Progress
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.briot.mrclogistics.implementor.R
import com.briot.mrclogistics.implementor.repository.local.PrefConstants
import com.briot.mrclogistics.implementor.repository.local.PrefRepository
import com.briot.mrclogistics.implementor.repository.remote.VendorMaterialInward
import kotlinx.android.synthetic.main.vendor_material_scan_fragment.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import com.briot.mrclogistics.implementor.ui.main.SimpleVendorItemAdapter.ViewHolder
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.login_fragment.*
import kotlinx.android.synthetic.main.picking_row.*


class VendorMaterialScanFragment : Fragment() {

    companion object {
        fun newInstance() = VendorMaterialScanFragment()
    }

    lateinit var vendorMaterialTextValue: EditText
    lateinit var vendorsubmit: Button
    private lateinit var viewModel: VendorMaterialScanViewModel
    private var oldVendorItems: Array<VendorMaterialInward?>? = null
    var inputData = VendorMaterialInward()
    private var progress: Progress? = null
    lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.vendor_material_scan_fragment, container, false)
        this.recyclerView = rootView.findViewById(R.id.vendorItems)
        recyclerView.layoutManager = LinearLayoutManager(this.activity)
        vendorMaterialTextValue = rootView.findViewById(R.id.vendor_materialBarcode)
        vendorsubmit = rootView.findViewById(R.id.vendor_items_submit_button)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(VendorMaterialScanViewModel::class.java)

        viewModel.getUsers()
        (this.activity as AppCompatActivity).setTitle("Vendor Material Scan")

        if (this.arguments != null) {
            viewModel.materialBarcode = this.arguments!!.getString("materialBarcode")
            viewModel.materialBarcode = vendorMaterialTextValue.getText().toString()
        }
        vendor_items_submit_button.setOnClickListener {
            viewModel.materialBarcode = vendorMaterialTextValue.getText().toString()

            val logedInUsername = PrefRepository.singleInstance.getValueOrDefault(PrefConstants().username,"")
            viewModel.logedInUsername = logedInUsername

            GlobalScope.launch {
                viewModel.handleSubmitVendor()
            }
        };
        recyclerView.adapter = SimpleVendorItemAdapter(recyclerView, viewModel.vendorItems, viewModel)
    }
}
open class SimpleVendorItemAdapter(private val recyclerView: androidx.recyclerview.widget.RecyclerView,
                                    private val vendorItems: LiveData<Array<VendorMaterialInward?>>,
                                   private val viewModel: VendorMaterialScanViewModel) :
        androidx.recyclerview.widget.RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.vendor_material_scan_fragment, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
        val vendorItems = vendorItems.value!![position]!!
    }

    override fun getItemCount(): Int {
        Log.d(ContentValues.TAG, "getItemCount" + vendorItems.value)
        return vendorItems.value?.size ?: 0
    }
    open inner class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        protected val materialBarcode: TextView
        init {
            materialBarcode = itemView.findViewById(R.id.vendor_materialBarcode)
        }
        fun bind() {
            val item = vendorItems.value!![adapterPosition]!!
            materialBarcode.text = item.materialBarcode
        }
    }
}