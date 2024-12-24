package com.zhair.stitchwell

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
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
    lateinit var tokenOfUser:String
    lateinit var  authViewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        tokenOfUser=""
        viewModel= OrderDetailViewModel()
        binding= ActivityDetailsOfOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val user: Users = user!!
        var isAdmin = false
        if (user.role.equals("Admin"))
            isAdmin = true

        order = Gson().fromJson(intent.getStringExtra("data"),Order::class.java)

        binding.namee.text=order.status
        binding.textView16.text=order.date
        binding.textView30.text=order.customInstr
        binding.textView32.text=order.tailorComment
        binding.textView28.text=order.expectedDate
        binding.textView7.text=order.price.toString()

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

        binding.name.editText!!.setText(order.tailorComment)

        progressDialog= ProgressDialog(this)
        progressDialog.setMessage("Updating the Order Status...Please Update")
        progressDialog.setCancelable(false)
        authViewModel= AuthViewModel()
        authViewModel.getTokenOf(order.phoneNumber!!)

        binding.workStarted.setOnClickListener(){
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
        binding.workStarted.visibility = View.GONE
        binding.completedd.visibility = View.GONE
        binding.cancelOrder.visibility = View.GONE
        binding.name.visibility= View.GONE
        binding.UpdateComment.visibility = View.GONE
        if(isAdmin){
            binding.name.visibility= View.VISIBLE
            binding.UpdateComment.visibility = View.VISIBLE
        }
        binding.UpdateComment.setOnClickListener(){
            progressDialog.show()
            if(binding.name.editText!!.text.toString() != ""){
                order.tailorComment=binding.name.editText!!.text.toString()
                viewModel.updateOrder(order)
            }

        }

        if((isAdmin && order.status.equals("Pending")) || (isAdmin && order.status.equals("Order Cancelled"))){
            binding.workStarted.visibility = View.VISIBLE
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
                    if(tokenOfUser != ""){
                        if(order.status.equals("Work Started")){
                            NotificationsRepository().sendNotification(tokenOfUser, "Order Status Updated", "Tailor Started the work on your Order", this@DetailsOfOrder)
                        }

                    } else{
                        Log.i("FMC", "Notification not send because token is null")
                    }

                    finish()

                }
            }
        }
        lifecycleScope.launch {
            authViewModel.token.collect{
                it?.let {
                    tokenOfUser=it
                }
            }
        }
    }
}
