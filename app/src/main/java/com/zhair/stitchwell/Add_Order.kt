package com.zhair.stitchwell

import android.app.ProgressDialog
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.zhair.stitchwell.databinding.ActivityAddOrderBinding
import kotlinx.coroutines.launch

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
                 if (it!=null)
                     Toast.makeText(this@Add_Order,it,Toast.LENGTH_LONG).show()
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

            val size = Size(collar, length, shoulder, waist, sleeve, chest, type, cuff, length1, bottom, kunda, legs, asan)

            // Create a Handcraft object with the entered data
             val order = Order()
            order.status = status
             order.expectedDate = delivaryDate
             order.phoneNumber = phone
             order.size = size

             viewModel.saveOrder(order)
             // Save the Handcraft object (this would be a database operation, Firestore, etc.)
             // For now, just display the success message
             Toast.makeText(this, "Handcraft Added Successfully!", Toast.LENGTH_SHORT).show()
         }
     }


    }
