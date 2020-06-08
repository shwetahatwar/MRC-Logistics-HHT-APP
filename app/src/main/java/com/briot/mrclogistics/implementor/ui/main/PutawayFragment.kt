package com.briot.mrclogistics.implementor.ui.main

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.briot.mrclogistics.implementor.R
import com.briot.mrclogistics.implementor.UiHelper
import com.briot.mrclogistics.implementor.repository.local.PrefConstants
import com.briot.mrclogistics.implementor.repository.remote.PutawayItems
import com.briot.mrclogistics.implementor.ui.main.SimplePutawayItemAdapter.ViewHolder
import io.github.pierry.progress.Progress
import kotlinx.android.synthetic.main.dispatch_picking_list_fragment.*
import kotlinx.android.synthetic.main.dispatch_slip_loading_fragment.*
import kotlinx.android.synthetic.main.home_fragment.*
import kotlinx.android.synthetic.main.picking_fragment.*
import kotlinx.android.synthetic.main.putaway_fragment.*
import kotlinx.android.synthetic.main.putaway_row.*
import kotlinx.coroutines.*

class PutawayFragment : Fragment() {

    companion object {
        fun newInstance() = PutawayFragment()
    }

    lateinit var putawayMaterialTextValue: EditText
    lateinit var binMaterialTextValue: EditText
    lateinit var rackMaterialTextValue: EditText
    lateinit var materialbtn: Button
    lateinit var rackbtn: Button
    lateinit var binbtn: Button
    lateinit var putawaysubmit: Button

    var getResponsePutwayData: Array<PutawayItems?> = arrayOf(null)
    private lateinit var viewModel: PutawayViewModel
    private var progress: Progress? = null
    private var oldPutawayItems: Array<PutawayItems?>? = null
    var inputData = PutawayItems()
    lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.putaway_fragment, container, false)
        this.recyclerView = rootView.findViewById(R.id.putawayItems)
        recyclerView.layoutManager = LinearLayoutManager(this.activity)
        putawayMaterialTextValue = rootView.findViewById(R.id.putaway_materialBarcode)
        binMaterialTextValue = rootView.findViewById(R.id.bin_materialBarcode)
        rackMaterialTextValue = rootView.findViewById(R.id.rack_materialBarcode)
        materialbtn = rootView.findViewById(R.id.putaway_scanButton)
        rackbtn = rootView.findViewById(R.id.bin_scanButton)
        binbtn = rootView.findViewById(R.id.rack_scanButton)
        putawaysubmit = rootView.findViewById(R.id.putaway_items_submit_button)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PutawayViewModel::class.java)
        (this.activity as AppCompatActivity).setTitle("Putaway")

        if (this.arguments != null) {
            viewModel.rackBarcodeSerial = this.arguments!!.getString("rackBarcodeSerial")
            viewModel.binBarcodeSerial = this.arguments!!.getString("binBarcodeSerial")
            viewModel.materialBarcodeSerial = this.arguments!!.getString("materialBarcodeSerial")
            viewModel.rackBarcodeSerial = binMaterialTextValue.getText().toString()
        }

        this.progress = UiHelper.showProgressIndicator(activity!!, "Putaway Items")
        viewModel.loadPutawayItems()
        viewModel.loadPutawayScannedItems()
        recyclerView.adapter = SimplePutawayItemAdapter(recyclerView, viewModel.putawayItems, viewModel)
        viewModel.putawayItems.observe(viewLifecycleOwner, Observer<Array<PutawayItems?>> {
            if (it != null) {
                UiHelper.hideProgress(this.progress)
                this.progress = null

                if (viewModel.putawayItems.value.orEmpty().isNotEmpty() && viewModel.putawayItems.value?.first() == null) {
                    UiHelper.showSomethingWentWrongSnackbarMessage(this.activity as AppCompatActivity)
                } else if (it != oldPutawayItems) {
                    putawayItems.adapter?.notifyDataSetChanged()
                }
            }
            oldPutawayItems = viewModel.putawayItems.value
        })

        viewModel.networkError.observe(viewLifecycleOwner, Observer<Boolean> {
            if (it == true) {
                UiHelper.hideProgress(this.progress)
                this.progress = null
                if (viewModel.messageContent != null) {
                    UiHelper.showErrorToast(this.activity as AppCompatActivity, viewModel.messageContent)
                } else {
                    UiHelper.showNoInternetSnackbarMessage(this.activity as AppCompatActivity)
                }
            }
        })

        viewModel.itemSubmissionSuccessful.observe(viewLifecycleOwner, Observer<Boolean> {
            // println("---it--->"+it)
            // recyclerView.adapter = SimplePutawayItemAdapter(recyclerView, viewModel.putawayItems, viewModel)
            if (it == true) {
                    UiHelper.hideProgress(this.progress)
                    this.progress = null
                    var thisObject = this
                    UiHelper.showSuccessToast(this.activity as AppCompatActivity,
                            "Scan Successful")
//                     viewModel.loadPutawayRefreshItems()
                }
            })

        // On click on BIN Barcode scan button
        putaway_scanButton.setOnClickListener {
            // User input MATERIAL barcode value
            val inputMaterialBarcode = putawayMaterialTextValue.getText().toString()
            if (inputMaterialBarcode == "") {
                UiHelper.showErrorToast(this.activity as AppCompatActivity,
                        "Please enter MATERIAL Barcode value")
                putaway_materialBarcode.requestFocus()
            }else{
                bin_materialBarcode.requestFocus()}
        }


        rack_scanButton.setOnClickListener {
            // User input MATERIAL barcode value
            val inputRackBarcode = rackMaterialTextValue.getText().toString()
            if (inputRackBarcode == "") {
                UiHelper.showErrorToast(this.activity as AppCompatActivity,
                        "Please enter RACK Barcode value")
                rack_materialBarcode.requestFocus()
            }
        }

        bin_scanButton.setOnClickListener {
            // User input MATERIAL barcode value
            val inputBinBarcode = binMaterialTextValue.getText().toString()
            if (inputBinBarcode == "") {
                UiHelper.showErrorToast(this.activity as AppCompatActivity,
                        "Please enter BIN Barcode value")
                bin_materialBarcode.requestFocus()
            }else{
                rack_materialBarcode.requestFocus()}
        }


        putaway_materialBarcode.setOnEditorActionListener { _, i, keyEvent ->
            var handled = false
          //  var value = loading_materialBarcode.text!!.toString()
            var materialBarcodeSerial = putaway_materialBarcode.text!!.toString()

            if (keyEvent == null) {
                Log.d("putaway: ", "event is null")
                UiHelper.showErrorToast(this.activity as AppCompatActivity, "event is null")
            }else if ((putaway_materialBarcode.text != null && putaway_materialBarcode.text!!.isNotEmpty()) && i
                    == EditorInfo.IME_ACTION_DONE || ((keyEvent.keyCode == KeyEvent.KEYCODE_ENTER || keyEvent.keyCode == KeyEvent.KEYCODE_TAB)
                            && keyEvent.action == KeyEvent.ACTION_DOWN)) {
                this.progress = UiHelper.showProgressIndicator(this.activity as AppCompatActivity, "Please wait")
                UiHelper.hideProgress(this.progress)
                this.progress = null
                handled = true
            }
            handled
        }

        putaway_items_submit_button.setOnClickListener {
            var thisObject = this
            var foundFlag: Boolean = false
            val inputBin = binMaterialTextValue.getText().toString()
            val inputMaterial = putawayMaterialTextValue.getText().toString()
            val inputRack = rackMaterialTextValue.getText().toString()

            if (inputBin != "" && inputMaterial != "" && inputRack != ""){
                if (inputBin == viewModel.binBarcodeSerial && inputMaterial == viewModel.materialBarcodeSerial &&
                    inputRack == viewModel.rackBarcodeSerial){
                    UiHelper.showErrorToast(this.activity as AppCompatActivity, "Already Scanned item!!")
                    foundFlag = true
                }
            }
            viewModel.binBarcodeSerial = inputBin
            viewModel.materialBarcodeSerial = inputMaterial
            viewModel.rackBarcodeSerial = inputRack

            if (putawayMaterialTextValue.getText().toString() != "") {
                val found = viewModel.putawayScannedItems.value!!.filter {
                    it!!.materialBarcodeSerial == putawayMaterialTextValue.getText().toString() &&
                            it.binBarcodeSerial == binMaterialTextValue.getText().toString() &&
                            it.rackBarcodeSerial == rackMaterialTextValue.getText().toString()
                }
                if (found.isNotEmpty()) {
                    UiHelper.showErrorToast(this.activity as AppCompatActivity, "Already Scanned item!!")
                    foundFlag = true
                }
            }

            if (foundFlag == false) {
                GlobalScope.launch {
                    viewModel.handleSubmitPutaway()
                }
//                viewModel.loadPutawayRefreshItems()
                // viewModel.loadPutawayRefreshItems()
//                putaway_materialBarcode.text?.clear()
//                bin_materialBarcode.text?.clear()
//                rack_materialBarcode.text?.clear()
            }
            putaway_materialBarcode.requestFocus()
            // viewModel.loadPutawayRefreshItems()
//            viewModel.loadPutawayItems()
//            viewModel.loadPutawayScannedItems()
            // Log.d(TAG,"after submit call -->"+viewModel.putawayScannedItems.value!!.size)
            };
        }
}
open class SimplePutawayItemAdapter(private val recyclerView: androidx.recyclerview.widget.RecyclerView,
                                    private val putawayItems: LiveData<Array<PutawayItems?>>,
                                    private val viewModel: PutawayViewModel) :
        androidx.recyclerview.widget.RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.putaway_row, parent, false)
        return ViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
        val putawayItems = putawayItems.value!![position]!!
        holder.itemView.setOnClickListener{

            if (viewModel.putawayItems.toString().toLowerCase().contains("complete")) {
                return@setOnClickListener
            }
        }
    }

    override fun getItemCount(): Int {
        return putawayItems.value?.size ?: 0
    }

    open inner class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        protected val rackBarcodeSerial: TextView
        protected val binBarcodeSerial: TextView
        protected val materialBarcodeSerial: TextView
        protected val linearLayout: LinearLayout

        init {
            rackBarcodeSerial = itemView.findViewById(R.id.rack_barcode)
            binBarcodeSerial = itemView.findViewById(R.id.bin_barcode)
            materialBarcodeSerial = itemView.findViewById(R.id.material_barcode)
            linearLayout = itemView.findViewById(R.id.putaway_layout)
        }

        fun bind() {
            val item = putawayItems.value!![adapterPosition]!!
            rackBarcodeSerial.text = item.rackBarcodeSerial
            binBarcodeSerial.text = item.binBarcodeSerial
            val barcodeComplete = item.materialBarcodeSerial
            val barcodeValue = barcodeComplete?.split(",");

            val scannedMaterialBarcodeValue = viewModel.materialBarcodeSerial
            val scannedSplitedValue = scannedMaterialBarcodeValue?.split(",")
            val materialScanValueToCompare = (scannedSplitedValue?.get(0) ?: "NA")
            materialBarcodeSerial.text = (barcodeValue?.get(0) ?: "NA")

            if (materialScanValueToCompare !=""){
                if (viewModel.rackBarcodeSerial == item!!.rackBarcodeSerial  &&
                        viewModel.binBarcodeSerial == item!!.binBarcodeSerial &&
                        materialScanValueToCompare == (barcodeValue?.get(0) ?: "NA")){
                    linearLayout.setBackgroundColor(PrefConstants().lightGreenColor)
                }
                else{
                    linearLayout.setBackgroundColor(PrefConstants().lightGrayColor)
                }
            }
            if (materialScanValueToCompare == ""){
                if (viewModel.rackBarcodeSerial == item!!.rackBarcodeSerial  &&
                        viewModel.binBarcodeSerial == item!!.binBarcodeSerial){
                    linearLayout.setBackgroundColor(PrefConstants().lightGreenColor)
                }
                else{
                    linearLayout.setBackgroundColor(PrefConstants().lightGrayColor)
                }
            }
        }
    }
}

