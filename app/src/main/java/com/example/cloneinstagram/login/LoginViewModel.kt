package com.example.cloneinstagram.login

import android.app.Application
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.cloneinstagram.R
import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class LoginViewModel(application: Application) : AndroidViewModel(application) {


    val context = getApplication<Application>().applicationContext

    var auth: FirebaseAuth = FirebaseAuth.getInstance()

    var googleSignInClient: GoogleSignInClient

    var id : MutableLiveData<String> = MutableLiveData("")
    var password : MutableLiveData<String> = MutableLiveData("")
    var showInputNumberActivity: MutableLiveData<Boolean> = MutableLiveData(false)
    var showFindIdActivity: MutableLiveData<Boolean> = MutableLiveData(false)



    init{
        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(context, gso)
    }

    // id 있으면 로그인, 없으면 회원가입
    fun loginWithSignupEmail(){
        println("Email")
        auth.createUserWithEmailAndPassword(id.value.toString(), password.value.toString()).addOnCompleteListener {
            if(it.isSuccessful){
                showInputNumberActivity.value = true
            }else{
                // 아이디가 있을 경우
            }
        }
    }

    // 구글 로그인
    fun loginGoogle(view: View){
        // LoginActivity와 연결(캐스팅)
         var i = googleSignInClient.signInIntent
        (view.context as? LoginActivity)?.googleLoginResult?.launch(i)



    }

    fun firebaseAuthWithGoogle(idToken: String?){
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if(it.isSuccessful){
                showInputNumberActivity.value = true
            }else{
                // 아이디가 있을 경우
            }
        }
    }

    fun firebaseAuthWithFacebook(accessToken: AccessToken){
        println("Token : $accessToken")
        val credential = FacebookAuthProvider.getCredential(accessToken.token)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if(it.isSuccessful){
                println("success")
                showInputNumberActivity.value = true
            }else{
                println("fail")
                // 아이디가 있을 경우
            }
        }
    }
}