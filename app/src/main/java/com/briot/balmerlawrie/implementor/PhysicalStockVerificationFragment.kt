package com.briot.balmerlawrie.implementor.ui.main

import android.content.ContentValues
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
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.briot.balmerlawrie.implementor.MainApplication
import com.briot.balmerlawrie.implementor.PhysicalStockVerificationViewModel

import com.briot.balmerlawrie.implementor.R
import com.briot.balmerlawrie.implementor.UiHelper
import com.briot.balmerlawrie.implementor.repository.local.PrefConstants
import com.briot.balmerlawrie.implementor.repository.local.PrefRepository
import com.briot.balmerlawrie.implementor.repository.remote.AuditItem
import com.briot.balmerlawrie.implementor.repository.remote.PickingItems
import com.briot.balmerlawrie.implementor.repository.remote.PutPickingResponse
import io.github.pierry.progress.Progress
import kotlinx.android.synthetic.main.material_details_scan_fragment.*
import kotlinx.android.synthetic.main.physical_stock_verification_fragment.*
import kotlinx.android.synthetic.main.picking_fragment.*
import kotlinx.android.synthetic.main.picking_fragment.picking_materialBarcode
import kotlinx.android.synthetic.main.user_profile_fragment.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PhysicalStockVerificationFragment : Fragment() {

    companion object {
        fun newInstance() = PhysicalStockVerificationFragment()
    }
    lateinit var auditScanMaterialTextValue: EditText
    lateinit var auditsubmitButton: Button
    private lateinit var viewModel: PhysicalStockVerificationViewModel
    private var oldAuditItems: Array<AuditItem?>? = null
    var inputData = AuditItem()
    private var progress: Progress? = null
    lateinit var recyclerView: RecyclerView
    private var userId = PrefRepository.singleInstance.getValueOrDefault(PrefConstants().USER_ID, "0").toInt()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.physical_stock_verification_fragment, container, false)
        this.recyclerView = rootView.findViewById(R.id.auditItems)
        recyclerView.layoutManager = LinearLayoutManager(this.activity)
        auditScanMaterialTextValue = rootView.findViewById(R.id.audit_materialBarcode)
        auditsubmitButton = rootView.findViewById(R.id.audit_submitItemsButton)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PhysicalStockVerificationViewModel::class.java)

        (this.activity as AppCompatActivity).setTitle("Physical stock verification Material Scan")

        if (this.arguments != null) {
            viewModel.materialBarcode = this.arguments!!.getString("materialBarcode")
            viewModel.materialBarcode = auditScanMaterialTextValue.getText().toString()

        }

        audit_submitItemsButton.setOnClickListener {
            viewModel.materialBarcode = auditScanMaterialTextValue.getText().toString()
           // PrefRepository.singleInstance.setKeyValue(PrefConstants().USER_ID, it.userNameId!!.toString())


            GlobalScope.launch {
                viewModel.handleSubmitAudit()
            }
        };
        recyclerView.adapter = SimpleAuditItemAdapter(recyclerView, viewModel.auditItems, viewModel)
    }
}
open class SimpleAuditItemAdapter(private val recyclerView: androidx.recyclerview.widget.RecyclerView,
                                  private val auditItems: LiveData<Array<AuditItem?>>,
                                  private val viewModel: PhysicalStockVerificationViewModel) :
        androidx.recyclerview.widget.RecyclerView.Adapter<SimpleAuditItemAdapter.ViewHolder>() {
    override fun onBindViewHolder(holder: SimpleAuditItemAdapter.ViewHolder, position: Int) {
        holder.bind()
        val auditItems = auditItems.value!![position]!!
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.physical_stock_verification_fragment, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        Log.d(ContentValues.TAG, "getItemCount" + auditItems.value)
        return auditItems.value?.size ?: 0
    }
    open inner class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        protected val materialBarcode: TextView
        init {
            materialBarcode = itemView.findViewById(R.id.audit_materialBarcode)
        }
        fun bind() {
            val item = auditItems.value!![adapterPosition]!!
            materialBarcode.text = item.materialBarcode
        }
    }
}