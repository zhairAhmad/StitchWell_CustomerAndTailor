package com.zhair.stitchwell

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.zhair.stitchwell.databinding.ActivityAddOrderBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import kotlin.text.format

class Add_Order : AppCompatActivity() {
    lateinit var progressDialog:ProgressDialog
     lateinit var binding: ActivityAddOrderBinding;
     lateinit var viewModel: AddOrderViewModel
     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = AddOrderViewModel()

         progressDialog= ProgressDialog(this)
         progressDialog.setMessage("Saving your order...")
         progressDialog.setCancelable(false)

         lifecycleScope.launch {
             viewModel.isSaving.collect{
                 if (it==true)
                     progressDialog.show()
                 else
                     progressDialog.dismiss()
             }
         }
         lifecycleScope.launch {
             viewModel.failureMessage.collect{
                 if (it!=null) {
                     Toast.makeText(this@Add_Order, it, Toast.LENGTH_LONG).show()
                     Log.i("AddOrder", it)
                 }
             }
         }
         lifecycleScope.launch {
             viewModel.isSaved.collect{
                 if (it==true) {
                     Toast.makeText(
                         this@Add_Order,
                         "Order saved successfully",
                         Toast.LENGTH_LONG
                     ).show()
                     finish()
                 }
             }
         }

         binding.cancel.setOnClickListener(){
             finish()
         }
         binding.saveBtn3.setOnClickListener{
             val phone=binding.phone.editText?.text.toString()
             if (phone.isNullOrEmpty() || phone.length < 11) {
                 Toast.makeText(this, "Please Type Correct Phone", Toast.LENGTH_SHORT).show()
             } else {
                 Toast.makeText(this, "Getting Data", Toast.LENGTH_SHORT).show()
                 viewModel.getMostRecent(phone)
             }
         }
         binding.saveBtn.setOnClickListener {
//            val status = binding.namee.editText?.text.toString()
            val status = "Pending"
            val delivaryDate = binding.expectedDate.editText?.text.toString()
            val phone = binding.phone.editText?.text.toString()
            val collar = binding.collerSize.editText?.text.toString().toFloatOrNull() ?: 0f
            val length = binding.length.editText?.text.toString().toFloatOrNull() ?: 0f
            val shoulder = binding.shoulderSize.editText?.text.toString().toFloatOrNull() ?: 0f
            val waist = binding.waist.editText?.text.toString().toFloatOrNull() ?: 0f
            val sleeve = binding.sleeveLength.editText?.text.toString().toFloatOrNull() ?: 0f
            val chest = binding.chestSize.editText?.text.toString().toFloatOrNull() ?: 0f
            val type= binding.type.editText?.text.toString()
            val cuff = binding.cuff.editText?.text.toString().toFloatOrNull() ?: 0f

            val length1 = binding.length1.editText?.text.toString().toFloatOrNull() ?: 0f
            val bottom = binding.bottom.editText?.text.toString().toFloatOrNull() ?: 0f
            val kunda = binding.kunda.editText?.text.toString().toFloatOrNull() ?: 0f
            val legs = binding.legs.editText?.text.toString().toFloatOrNull() ?: 0f
            val asan = binding.asan.editText?.text.toString().toFloatOrNull() ?: 0f
             val customerIntra = binding.namee.editText?.text.toString()
             val price = binding.price.editText?.text.toString().toIntOrNull() ?: 0
             
             if(status.isEmpty() || delivaryDate.isEmpty() || phone.isEmpty() || collar == 0f || length == 0f || shoulder == 0f || waist == 0f || sleeve == 0f || chest == 0f || type.isEmpty() || cuff == 0f){
                 Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                 return@setOnClickListener
             }

            val size = Size(collar, length, shoulder, waist, sleeve, chest, type, cuff, length1, bottom, kunda, legs, asan)

            // Create a Handcraft object with the entered data
             val order = Order()
             order.status = status
             order.date= SimpleDateFormat("yyyy-MM-dd HH:mm a").format(System.currentTimeMillis())
             order.customInstr= customerIntra
             order.price = price
             order.expectedDate = delivaryDate
             order.phoneNumber = phone
             order.size = size


             viewModel.saveOrder(order)
//             Toast.makeText(this, "Order Saved Successfully!", Toast.LENGTH_SHORT).show()
         }

         lifecycleScope.launch {
             viewModel.dataSize.collect{
                 if(it!=null){
                     binding.chestSize.editText?.setText(it.size!!.chest.toString())
                     binding.shoulderSize.editText?.setText(it.size!!.shoulder.toString())
                     binding.chestSize.editText?.setText(it.size!!.chest.toString())
                     binding.sleeveLength.editText?.setText(it.size!!.sleeve.toString())
                     binding.collerSize.editText?.setText(it.size!!.collar.toString())
                     binding.waist.editText?.setText(it.size!!.waist.toString())
                     binding.length.editText?.setText(it.size!!.length.toString())
                     binding.cuff.editText?.setText(it.size!!.cuff.toString())
                     binding.length1.editText?.setText(it.size!!.length1.toString())
                     binding.bottom.editText?.setText(it.size!!.bottom.toString())
                     binding.kunda.editText?.setText(it.size!!.kunda.toString())
                     binding.legs.editText?.setText(it.size!!.legs.toString())
                     binding.asan.editText?.setText(it.size!!.asan.toString())

                 }
             }
         }
     }


    }
