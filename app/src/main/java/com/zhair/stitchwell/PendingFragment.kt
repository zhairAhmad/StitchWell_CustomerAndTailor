package com.zhair.stitchwell

import OrderAdapter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.coderobust.handcraftsshop.ui.main.home.PendingFragmentViewModel
import com.zhair.stitchwell.MainActivity.Companion.user
import com.zhair.stitchwell.databinding.FragmentHomeBinding
import com.zhair.stitchwell.databinding.FragmentPendingBinding
import kotlinx.coroutines.launch

class PendingFragment : Fragment() {
    lateinit var adapter: OrderAdapter
    lateinit var binding: FragmentPendingBinding
    lateinit var viewModel: PendingFragmentViewModel
    lateinit var authViewModel: AuthViewModel
    lateinit var CurrentUser:Users
    val items=ArrayList<Order>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentPendingBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authViewModel=AuthViewModel()
        CurrentUser=Users("","","","", "")
        authViewModel.loadUser()

        lifecycleScope.launch {
            authViewModel.currentUser.collect {
                it?.let {
                    CurrentUser = it
                    user =it
                    viewModel.readOrders()
                    if(!CurrentUser.role.equals("admin")){
                        viewModel.readAllPending()

                    } else{
                        viewModel.readOrders()
                    }
                }
            }
        }


        adapter= OrderAdapter(items)
        binding.recyclerview.adapter=adapter
        binding.recyclerview.layoutManager= LinearLayoutManager(context)

        viewModel= PendingFragmentViewModel()
//        viewModel.readOrders()
//        viewModel.readHandcrafts()
        lifecycleScope.launch {
            viewModel.failureMessage.collect {
                it?.let {
                    Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                }
            }
        }
        lifecycleScope.launch {
            viewModel.data.collect {
                it?.let {
                    items.clear()
                    items.addAll(it)
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }
}