package com.example.huseyinoral_bilgisayarmuhendisligitez.view

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.huseyinoral_bilgisayarmuhendisligitez.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth


class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        //kullanici giriş yaptıysa
        val guncelkullanici=auth.currentUser
        if(guncelkullanici!=null){
            loginPageToAntrenorListPage()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.hesabinizyoksakayitolButton.setOnClickListener{
            loginPageToRegisterPage()
        }

        binding.loginButton.setOnClickListener {
            if(isInputCorrect()){
                signIn()
            }

        }
    }

    private fun loginPageToRegisterPage(){
        val action=LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
        findNavController().navigate(action)

    }

    private fun loginPageToAntrenorListPage(){
        val action=LoginFragmentDirections.actionLoginFragmentToAntrenorListFragment()
        findNavController().navigate(action)


    }
    private fun signIn() {
        auth.signInWithEmailAndPassword(
            binding.loginEmail.text.toString(),
            binding.loginPassword.text.toString()
        ).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                loginPageToAntrenorListPage()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(context,exception.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }
    //email kontrol
    private fun String.isValidEmail() = !TextUtils.isEmpty(this) && Patterns.EMAIL_ADDRESS.matcher(this).matches()
    //giriş ol kontrol
    private fun isInputCorrect(): Boolean {
        val email=binding.loginEmail.text
        val sifre =binding.loginPassword.text

        if (email.isNullOrBlank()) {
            Toast.makeText(activity, "Mail boş olmamalı", Toast.LENGTH_LONG).show()
            return false
        }
        if (email.toString().isValidEmail().not()) {
            Toast.makeText(activity, "Gecerli Email Adresi Olmalı", Toast.LENGTH_LONG).show()
            return false
        }
        if (sifre.isNullOrBlank()) {
            Toast.makeText(activity, "Şifre boş olmamalı", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }
}