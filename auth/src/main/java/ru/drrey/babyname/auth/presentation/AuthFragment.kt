package ru.drrey.babyname.auth.presentation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import ru.drrey.babyname.auth.R

const val RC_SIGN_IN = 111

class AuthFragment : Fragment() {
    companion object {
        fun newInstance() = AuthFragment()
    }

    private val authViewModel: AuthViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.auth_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        authViewModel.getState().observe(viewLifecycleOwner, Observer {
            when (it) {
                AuthUndefined -> {
                    authViewModel.loadAuth()
                }
                AuthLoading -> {

                }
                AuthNone -> {
                    val providers = arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build())
                    startActivityForResult(
                        AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build(),
                        RC_SIGN_IN
                    )
                }
                is AuthError -> {
                    Toast.makeText(context!!, it.message ?: "Auth error", Toast.LENGTH_LONG).show()
                }
                is AuthComplete -> {
                    Toast.makeText(context!!, it.userId, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                FirebaseAuth.getInstance().currentUser?.uid?.let { authViewModel.onAuthComplete(it) }
                    ?: authViewModel.onAuthError(response?.error)
            } else {
                authViewModel.onAuthError(response?.error)
            }
        }
    }
}
