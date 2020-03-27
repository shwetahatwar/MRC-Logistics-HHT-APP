package com.briot.balmerlawrie.implementor.ui.main

import android.content.ContentValues.TAG
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.briot.balmerlawrie.implementor.R
import com.briot.balmerlawrie.implementor.UiHelper
import com.briot.balmerlawrie.implementor.repository.remote.PickingItems
import io.github.pierry.progress.Progress
import kotlinx.android.synthetic.main.picking_fragment.*
import kotlinx.android.synthetic.main.picking_fragment.picking_materialBarcode
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PickingFragment : Fragment() {

    companion object {
        fun newInstance() = PickingFragment()
    }

    lateinit var pickingMaterialTextValue: EditText
    lateinit var binMaterialTextValue: EditText
    lateinit var rackMaterialTextValue: EditText
    lateinit var materialBarcodeScanButton: Button
    lateinit var rackBarcodeScanButton: Button
    lateinit var binBarcodeScanButton: Button
    lateinit var picking_submitItemButton: Button

private lateinit var viewModel: PickingViewModel
private var progress: Progress? = null
private var oldPickingItems: Array<PickingItems?>? = null
lateinit var recyclerView: RecyclerView

override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                          savedInstanceState: Bundle?): View? {
    val rootView = inflater.inflate(R.layout.picking_fragment, container, false)
    this.recyclerView = rootView.findViewById(R.id.pickingItems)
    recyclerView.layoutManager = LinearLayoutManager(this.activity)
    pickingMaterialTextValue = rootView.findViewById(R.id.picking_materialBarcode)
    binMaterialTextValue = rootView.findViewById(R.id.picking_binBarcode)
    rackMaterialTextValue = rootView.findViewById(R.id.picking_rackBarcode)
    materialBarcodeScanButton = rootView.findViewById(R.id.picking_material_scanButton)
    rackBarcodeScanButton = rootView.findViewById(R.id.picking_bin_scanButton)
    binBarcodeScanButton = rootView.findViewById(R.id.picking_rack_scanButton)
    picking_submitItemButton = rootView.findViewById(R.id.picking_submit_button)

    // Log.d("materialbtn: ", "materialbtn")


    return rootView
}

override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel = ViewModelProviders.of(this).get(PickingViewModel::class.java)
    // TODO: Use the ViewModel

    (this.activity as AppCompatActivity).setTitle("Picking")

    if (this.arguments != null) {
        viewModel.rackBarcodeSerial = this.arguments!!.getString("rackBarcodeSerial")
        viewModel.binBarcodeSerial = this.arguments!!.getString("binBarcodeSerial")
        viewModel.materialBarcodeSerial = this.arguments!!.getString("materialBarcodeSerial")

    }
    recyclerView.adapter = SimplePickingItemAdapter(recyclerView, viewModel.pickingItems)
    viewModel.pickingItems.observe(viewLifecycleOwner, Observer<Array<PickingItems?>> {
        if (it != null) {
            UiHelper.hideProgress(this.progress)
            this.progress = null

            if (viewModel.pickingItems.value.orEmpty().isNotEmpty() && viewModel.pickingItems.value?.first() == null) {
                UiHelper.showSomethingWentWrongSnackbarMessage(this.activity as AppCompatActivity)
            } else if (it != oldPickingItems) {
                pickingItems.adapter?.notifyDataSetChanged()
            }
        }

        oldPickingItems = viewModel.pickingItems.value
    })

    viewModel.networkError.observe(viewLifecycleOwner, Observer<Boolean> {
        if (it == true) {
            UiHelper.hideProgress(this.progress)
            this.progress = null

            UiHelper.showNoInternetSnackbarMessage(this.activity as AppCompatActivity)
        }
    })

    picking_materialBarcode.setOnEditorActionListener { _, i, keyEvent ->
        var handled = false
        if (keyEvent == null) {
            Log.d("picking: ", "event is null")
        } else if ((picking_materialBarcode.text != null && picking_materialBarcode.text!!.isNotEmpty())
                && i == EditorInfo.IME_ACTION_DONE || ((keyEvent.keyCode == KeyEvent.KEYCODE_ENTER ||
                        keyEvent.keyCode == KeyEvent.KEYCODE_TAB) && keyEvent.action == KeyEvent.ACTION_DOWN)) {
            this.progress = UiHelper.showProgressIndicator(this.activity as AppCompatActivity, "Please wait")
            UiHelper.hideProgress(this.progress)
            this.progress = null
            handled = true
        }
        handled
    }
    this.progress = UiHelper.showProgressIndicator(activity!!, "Picking Items")
    viewModel.loadPickingItems("In progress")

    // After click on submit button need to call put method to update database
    picking_submit_button.setOnClickListener({
        viewModel.binBarcodeSerial = binMaterialTextValue.getText().toString()
        viewModel.materialBarcodeSerial = pickingMaterialTextValue.getText().toString()
        viewModel.rackBarcodeSerial = rackMaterialTextValue.getText().toString()

        GlobalScope.launch {
            viewModel.handleSubmitPicking()
        }

    });
}
}



open class SimplePickingItemAdapter(private val recyclerView: androidx.recyclerview.widget.RecyclerView,
                                    private val pickingItems: LiveData<Array<PickingItems?>>) : androidx.recyclerview.widget.
RecyclerView.Adapter<SimplePickingItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.picking_row, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()

        val pickingsItems = pickingItems.value!![position]!!
        holder.itemView.setOnClickListener{

        }
    }

    override fun getItemCount(): Int {
        return pickingItems.value?.size ?: 0
    }


    open inner class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        protected val rackBarcodeSerial: TextView
        protected val binBarcodeSerial: TextView
        protected val materialBarcodeSerial: TextView

        init {
            Log.d(TAG, "..............rack_barcode" + R.id.rack_barcode)
            rackBarcodeSerial = itemView.findViewById(R.id.rack_barcode)
            binBarcodeSerial = itemView.findViewById(R.id.bin_barcode)
            materialBarcodeSerial = itemView.findViewById(R.id.material_barcode)
        }

        fun bind() {
            val pickingItems = pickingItems.value!![adapterPosition]!!
            Log.d(TAG, "..............." + pickingItems.toString())

            rackBarcodeSerial.text = pickingItems.rackBarcodeSerial
            binBarcodeSerial.text = pickingItems.binBarcodeSerial
            materialBarcodeSerial.text = pickingItems.materialBarcodeSerial
        }
    }
}

