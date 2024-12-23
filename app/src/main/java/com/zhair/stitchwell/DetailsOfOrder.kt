package com.zhair.stitchwell

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.zhair.stitchwell.MainActivity.Companion.user
import com.zhair.stitchwell.databinding.ActivityDetailsOfOrderBinding
import com.zhair.stitchwell.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch

class DetailsOfOrder : AppCompatActivity() {
    lateinit var binding: ActivityDetailsOfOrderBinding
    lateinit var order:Order
    lateinit var viewModel:OrderDetailViewModel
    lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        viewModel= OrderDetailViewModel()
        binding= ActivityDetailsOfOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val user: Users = user!!
        var isAdmin = false
        if (user.role.equals("admin"))
            isAdmin = true

        order = Gson().fromJson(intent.getStringExtra("data"),Order::class.java)

        binding.namee.text=order.status
        binding.number.text=order.expectedDate
        binding.collar.text=order.size?.collar.toString()
        binding.length.text=order.size?.length.toString()
        binding.waist.text=order.size?.waist.toString()
        binding.shoulder.text=order.size?.shoulder.toString()
        binding.sleeve.text=order.size?.sleeve.toString()
        binding.chest.text=order.size?.chest.toString()
        binding.shirt.text=order.size?.type
        binding.cuff.text=order.size?.cuff.toString()
        binding.length1.text=order.size?.length1.toString()
        binding.bottom.text=order.size?.bottom.toString()
        binding.kunda.text=order.size?.kunda.toString()
        binding.length.text=order.size?.length.toString()

        progressDialog= ProgressDialog(this)
        progressDialog.setMessage("Updating the Order Status...Please Update")
        progressDialog.setCancelable(false)
        binding.workStarted2.setOnClickListener(){
            progressDialog.show()
            order.status="Work Started"
            viewModel.updateOrder(order)
        }
        binding.completedd.setOnClickListener(){
            progressDialog.show()
            order.status="Completed"
            viewModel.updateOrder(order)
        }
        binding.cancelOrder.setOnClickListener(){
            progressDialog.show()
            order.status="Order Cancelled"
            viewModel.updateOrder(order)
        }
        binding.workStarted2.visibility = View.GONE
        binding.completedd.visibility = View.GONE
        binding.cancelOrder.visibility = View.GONE

        if((isAdmin && order.status.equals("Pending")) || (isAdmin && order.status.equals("Order Cancelled"))){
            binding.workStarted2.visibility = View.VISIBLE
        }

        if(isAdmin && order.status.equals("Work Started")){
            binding.completedd.visibility = View.VISIBLE
        }
        if(isAdmin && !order.status.equals("Completed")){
            binding.cancelOrder.visibility = View.VISIBLE
        }

        lifecycleScope.launch {
            viewModel.isSaving.collect {
                if (it==true) {
//                    progressDialog.show()
                    Toast.makeText(
                        this@DetailsOfOrder,
                        "Status Updated Successfully.",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()

                }
            }
        }
        lifecycleScope.launch {
            viewModel.isSaved.collect {
//                        progressDialog.show()
                if (it==true) {
                    progressDialog.dismiss()

                    Toast.makeText(
                        this@DetailsOfOrder,
                        "Status Updated Successfully.",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()

                }
            }
        }
    }
}
