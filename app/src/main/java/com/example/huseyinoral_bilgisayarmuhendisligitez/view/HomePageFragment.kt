package com.example.huseyinoral_bilgisayarmuhendisligitez.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.huseyinoral_bilgisayarmuhendisligitez.databinding.FragmentHomePageBinding
import com.google.firebase.auth.FirebaseAuth

class HomePageFragment : Fragment() {
    private var _binding: FragmentHomePageBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        val guncelkullanici=auth.currentUser
        if(guncelkullanici==null){
            val action = HomePageFragmentDirections.actionHomePageFragmentToLoginPageActivity2()
            findNavController().navigate(action)
            activity?.finish()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomePageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.antrenorlerText.setOnClickListener {
            homePageToAntrenorList()
        }
        binding.antrenorlerImageButton.setOnClickListener {
            homePageToAntrenorList()
        }
        binding.ipuclarText.setOnClickListener {
            homePageToTipsAndAdvice()
        }
        binding.ipuclarImageButton.setOnClickListener {
            homePageToTipsAndAdvice()
        }
        binding.egzersizText.setOnClickListener {
            homePageToSportsExcercise()
        }
        binding.egzersizImageButton.setOnClickListener {
            homePageToSportsExcercise()
        }
        binding.vucutkitleindeksiText.setOnClickListener {
            homePageToBMI()
        }
        binding.vucutkitleindeksiImageButton.setOnClickListener {
            homePageToBMI()
        }
        binding.userprofileText.setOnClickListener {
            homePageToUserProfile()
        }
        binding.userprofileImageButton.setOnClickListener {
            homePageToUserProfile()
        }
    }
    private fun homePageToAntrenorList(){
        val action=HomePageFragmentDirections.actionHomePageFragmentToAntrenorListFragment2()
        findNavController().navigate(action)
    }
    private fun homePageToBMI(){
        val action=HomePageFragmentDirections.actionHomePageFragmentToBodyMassIndexFragment()
        findNavController().navigate(action)
    }
    private fun homePageToSportsExcercise(){
        val action=HomePageFragmentDirections.actionHomePageFragmentToSportsExerciseFragment()
        findNavController().navigate(action)
    }
    private fun homePageToTipsAndAdvice(){
        val action=HomePageFragmentDirections.actionHomePageFragmentToTipsAndAdviceFragment2()
        findNavController().navigate(action)
    }
    private fun homePageToUserProfile(){
        val action=HomePageFragmentDirections.actionHomePageFragmentToUserProfileFragment()
        findNavController().navigate(action)
    }
}