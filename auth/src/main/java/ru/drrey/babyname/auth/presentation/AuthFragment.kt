package ru.drrey.babyname.auth.presentation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import ru.drrey.babyname.auth.R
import ru.drrey.babyname.common.presentation.base.NonNullObserver
import ru.drrey.babyname.common.presentation.router

const val RC_SIGN_IN = 111

class AuthFragment : Fragment() {
    companion object {
        fun newInstance() = AuthFragment()
    }

    private val authViewModel: AuthViewModel by sharedViewModel(from = { parentFragment!! })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_auth, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authViewModel.getViewState().observe(viewLifecycleOwner, NonNullObserver {
            renderState(it)
        })
    }

    private fun renderState(viewState: AuthViewState) {
        if (!viewState.isLoading && !viewState.isLoaded && viewState.error == null && viewState.userId == null) {
            authViewModel.loadAuth()
        } else if (!viewState.isLoading && viewState.isLoaded && viewState.error == null && viewState.userId == null) {
            val providers = arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build())
            startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .build(),
                RC_SIGN_IN
            )
        } else if (!viewState.isLoading && viewState.isLoaded && viewState.error != null) {
            Toast.makeText(context!!, viewState.error, Toast.LENGTH_LONG).show()
        } else if (!viewState.isLoading && viewState.isLoaded && viewState.error == null && viewState.userId != null) {
            router?.exit()
        }
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
