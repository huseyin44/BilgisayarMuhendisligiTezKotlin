package com.example.huseyinoral_bilgisayarmuhendisligitez.view.mapsPage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.huseyinoral_bilgisayarmuhendisligitez.adapter.NearByLocationRecyclerAdapter
import com.example.huseyinoral_bilgisayarmuhendisligitez.databinding.FragmentLocationListBinding
import com.example.huseyinoral_bilgisayarmuhendisligitez.model.NearByLocationData


class LocationListFragment : Fragment() {
    private var _binding: FragmentLocationListBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerNearByAdapter: NearByLocationRecyclerAdapter

    private val args : LocationListFragmentArgs by navArgs()
    var nearlbyLocationList= java.util.ArrayList<NearByLocationData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        nearlbyLocationList=args.locationList.toCollection(ArrayList())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLocationListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager= LinearLayoutManager(context)
        binding.nearByLocationRecycler.layoutManager=layoutManager
        recyclerNearByAdapter= NearByLocationRecyclerAdapter(nearlbyLocationList)
        binding.nearByLocationRecycler.adapter=recyclerNearByAdapter
    }

}