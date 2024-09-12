package com.shahriar.shop.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.shahriar.shop.R
import com.shahriar.shop.data.products.Product
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ExploreFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ExploreFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: ExploreAdapter
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_explore, container, false)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        progressBar = view.findViewById(R.id.progressBar2)
        recyclerView = view.findViewById(R.id.recyclerView2)
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        recyclerView.addItemDecoration(ItemSpacingDecoration(horizontal = 4, vertical = 16))
        recyclerView.setPadding(0, 0, 0, 0)
        adapter = ExploreAdapter(emptyList()) { item ->
            navigateToDetails(item)
        }
        recyclerView.adapter = adapter

        val categories = listOf("electronics", "jewelery", "men's clothing", "women's clothing")

        val spinner = view.findViewById<Spinner>(R.id.category)
        val spinnerAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Set the adapter to the Spinner
        spinner.adapter = spinnerAdapter

        handleLoading()

        lifecycleScope.launch {
            viewModel.productsByCategory.collect { response ->
                if (response != null) {
                    adapter.setNewList(response)
                }
            }
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent.getItemAtPosition(position).toString()

                viewModel.selectCategory(selectedItem)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Optional action
            }
        }

        return view
    }

    private fun navigateToDetails(item: Product) {
        val intent = Intent(requireContext(), ProductDetails::class.java).apply {
            putExtra("PRODUCT", item)
        }
        startActivity(intent)
    }

    private fun handleLoading() {
        lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                if (isLoading) {
                    progressBar.visibility = View.VISIBLE
                } else {
                    progressBar.visibility = View.GONE
                }
            }
        }
    }

}