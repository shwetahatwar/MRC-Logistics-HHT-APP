package com.briot.balmerlawrie.implementor.ui.main

import android.content.ContentValues.TAG
import android.content.Context
import androidx.lifecycle.ViewModelProviders
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
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.briot.balmerlawrie.implementor.R
import com.briot.balmerlawrie.implementor.UiHelper
import com.briot.balmerlawrie.implementor.repository.remote.DispatchSlipItem
import com.briot.balmerlawrie.implementor.repository.remote.PutawayItems
import com.briot.balmerlawrie.implementor.ui.main.SimplePutawayItemAdapter.ViewHolder
import io.github.pierry.progress.Progress
import kotlinx.android.synthetic.main.dispatch_picking_list_fragment.*
import kotlinx.android.synthetic.main.dispatch_slip_loading_fragment.*
import kotlinx.android.synthetic.main.home_fragment.*
import kotlinx.android.synthetic.main.putaway_fragment.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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

    private lateinit var viewModel: PutawayViewModel
    private var progress: Progress? = null
    private var oldPutawayItems: Array<PutawayItems?>? = null
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

        // Log.d("materialbtn: ", "materialbtn")
        return rootView

//        val material_barcode_edit_text = rootView.findViewById<EditText>(R.id.putaway_materialBarcode)
//        val bin_barcode_edit_text = rootView.findViewById<EditText>(R.id.bin_materialBarcode)
//        val rack_barcode_edit_text = rootView.findViewById<EditText>(R.id.rack_materialBarcode)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProviders.of(this).get(PutawayViewModel::class.java)
//        (this.activity as AppCompatActivity).setTitle("Putaway")

        viewModel = ViewModelProvider(this).get(PutawayViewModel::class.java)
        (this.activity as AppCompatActivity).setTitle("Putaway")

        if (this.arguments != null) {
            viewModel.rackBarcodeSerial = this.arguments!!.getString("rackBarcodeSerial")
            viewModel.binBarcodeSerial = this.arguments!!.getString("binBarcodeSerial")
            viewModel.materialBarcodeSerial = this.arguments!!.getString("materialBarcodeSerial")

        }
        recyclerView.adapter = SimplePutawayItemAdapter(recyclerView, viewModel.putawayItems)
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

        // Log.d(TAG, "putaway text values ----- in fragment-----"+ viewModel.putawayItems.value)
        viewModel.networkError.observe(viewLifecycleOwner, Observer<Boolean> {
            if (it == true) {
                UiHelper.hideProgress(this.progress)
                this.progress = null

                UiHelper.showNoInternetSnackbarMessage(this.activity as AppCompatActivity)
            }
        })

        putaway_materialBarcode.setOnEditorActionListener { _, i, keyEvent ->
            var handled = false
            if (keyEvent == null) {
                Log.d("putaway: ", "event is null")
            } else if ((putaway_materialBarcode.text != null && putaway_materialBarcode.text!!.isNotEmpty()) && i == EditorInfo.IME_ACTION_DONE || ((keyEvent.keyCode == KeyEvent.KEYCODE_ENTER || keyEvent.keyCode == KeyEvent.KEYCODE_TAB) && keyEvent.action == KeyEvent.ACTION_DOWN)) {
                this.progress = UiHelper.showProgressIndicator(this.activity as AppCompatActivity, "Please wait")
                UiHelper.hideProgress(this.progress)
                this.progress = null
                handled = true
            }
            handled
        }
        this.progress = UiHelper.showProgressIndicator(activity!!, "Putaway Items")
        viewModel.loadPutawayItems("In progress")


        // --------------------------------------------------------------------------------------------------------------------
        // After click on submit button need to call put method to update database
        putaway_items_submit_button.setOnClickListener({
            viewModel.binBarcodeSerial = binMaterialTextValue.getText().toString()
            viewModel.materialBarcodeSerial = putawayMaterialTextValue.getText().toString()
            viewModel.rackBarcodeSerial = rackMaterialTextValue.getText().toString()
            // viewModel.id = this.arguments!!.getInt("id")
//            Log.d(TAG, "putaway text values -----"+ putawayMaterialTextValue.getText())
//            Log.d(TAG, "putaway text values -----"+ binMaterialTextValue.getText())
//            Log.d(TAG, "putaway text values -----"+ rackMaterialTextValue.getText())

            GlobalScope.launch {
                viewModel.handleSubmitPutaway()
            }

        });

        // --------------------------------------------------------------------------------------------------------------------


    }
}

open class SimplePutawayItemAdapter(private val recyclerView: androidx.recyclerview.widget.RecyclerView,
                                    private val putawayItems: LiveData<Array<PutawayItems?>>) :
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

        }
    }

    override fun getItemCount(): Int {
        return putawayItems.value?.size ?: 0
    }


    open inner class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        protected val rackBarcodeSerial: TextView
        protected val binBarcodeSerial: TextView
        protected val materialBarcodeSerial: TextView

        init {
            // Log.d(TAG, "..............rack_barcode" + R.id.rack_barcode)

            rackBarcodeSerial = itemView.findViewById(R.id.rack_barcode)
            binBarcodeSerial = itemView.findViewById(R.id.bin_barcode)
            materialBarcodeSerial = itemView.findViewById(R.id.material_barcode)
        }

        fun bind() {
            val putawayItems = putawayItems.value!![adapterPosition]!!
             Log.d(TAG, ">>>>>>>>>>>>>>>>>" + putawayItems.materialBarcodeSerial)

            rackBarcodeSerial.text = putawayItems.rackBarcodeSerial
            binBarcodeSerial.text = putawayItems.binBarcodeSerial
            val barcodeComplete = putawayItems.materialBarcodeSerial
            val barcodeValue = barcodeComplete?.split(",");
            //Log.d(TAG, "////////////////" + (barcodeValue?.get(0) ?: 1))
            materialBarcodeSerial.text = (barcodeValue?.get(0) ?:"NA")
        }
    }
}
