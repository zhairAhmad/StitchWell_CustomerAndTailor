package com.zhair.stitchwell

import OrderAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.zhair.stitchwell.MainActivity.Companion.user
import com.zhair.stitchwell.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {
    lateinit var adapter: OrderAdapter
    lateinit var binding:FragmentHomeBinding
    lateinit var viewModel: HomeFragmentViewModel
    lateinit var authViewModel: AuthViewModel
    lateinit var CurrentUser:Users
    val items=ArrayList<Order>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentHomeBinding.inflate(inflater,container,false)
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
                    user=it

                    if(CurrentUser.role.equals("Admin")){

                        viewModel.readAllOrders()

                    } else{
                        binding.floatingActionButton.visibility=View.GONE
                        viewModel.readOrders()
                    }
                }
            }
        }

        binding.floatingActionButton.setOnClickListener(){
            startActivity(Intent(requireActivity(), Add_Order::class.java))
        }
        adapter= OrderAdapter(items)
        binding.recyclerview.adapter=adapter
        binding.recyclerview.layoutManager= LinearLayoutManager(context)

        viewModel= HomeFragmentViewModel()
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