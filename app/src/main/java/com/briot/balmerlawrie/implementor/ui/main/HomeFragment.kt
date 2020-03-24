package com.briot.balmerlawrie.implementor.ui.main

//import com.briot.balmerlawrie.implementor.repository.remote.RoleAccessRelation
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.Navigation.createNavigateOnClickListener
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.briot.balmerlawrie.implementor.BuildConfig
import com.briot.balmerlawrie.implementor.R
import com.briot.balmerlawrie.implementor.UiHelper
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.home_fragment.*


class HomeFragment : androidx.fragment.app.Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel

    lateinit var cardView: CardView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.home_fragment, container, false)
        this.cardView = rootView.findViewById(R.id.materialPutaway)
        Log.d(TAG, ">>>>>>>>>>>>>>>>>")
        this.cardView.setOnClickListener {
            // your code to perform when the user clicks on the ImageView
            Log.d(TAG, ".............")
        }
        return rootView
        // return inflater.inflate(R.layout.home_fragment, container, false)
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

       // putawayBtn.setOnClickListener { Navigation.findNavController(it).navigate(R.id.action_homeFragment_to_putawayFragment) }
        //navigate to putaway fragment
        //Navigation.findNavController(materialPutaway).navigate(R.id.action_homeFragment_to_putawayFragment)

//        materialPicking.isEnabled = true
//        materialPutaway.isEnabled = true
//        materialPicking.isEnabled = false
//        materialPutaway.isEnabled = false

//        fun onClick(v: View) {
//            materialPutaway.setOnClickListener { view ->
//               // view.findNavController().navigate(R.id.action_homeFragment_to_putawayFragment)
//                view.findNavController(materialPutaway).navigate(R.id.action_homeFragment_to_putawayFragment)
//            }

        //}
//        materialPutaway.setOnClickListener(clickListener)
//        val clickListener: View.OnClickListener = View.OnClickListener { view ->
//            when (view.id) {
//                R.id.materialPutaway -> gotoXScreent()
//            }
//        }
//        fun goToXScreen() {
//
//            val intent = Intent(this, PutawayFragment)
//            startActivity(intent)
//        }
//        fun onClick(v: View) {
//            Navigation.findNavController(materialPutaway).navigate(R.id.action_homeFragment_to_putawayFragment)
//        }
    }
}