package vn.edu.vlu.lab09

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import vn.edu.vlu.lab09.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding
    private val auth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (auth.currentUser != null) {
            openMain()
            return
        }

        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val safeArea = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or
                        WindowInsetsCompat.Type.displayCutout() or
                        WindowInsetsCompat.Type.ime()
            )
            view.setPadding(safeArea.left, safeArea.top, safeArea.right, safeArea.bottom)
            insets
        }

        binding.toggleAuthMode.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                val registering = checkedId == R.id.btnTabRegister
                binding.tilConfirmPassword.visibility =
                    if (registering) View.VISIBLE else View.GONE
                binding.tilConfirmPassword.error = null
                binding.btnSubmit.text = if (registering) "Create account" else "Login"
            }
        }

        binding.btnSubmit.setOnClickListener { submit() }
    }

    private fun submit() {
        val registering = binding.toggleAuthMode.checkedButtonId == R.id.btnTabRegister
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString()
        val confirmation = binding.etConfirmPassword.text.toString()

        binding.tilEmail.error = null
        binding.tilPassword.error = null
        binding.tilConfirmPassword.error = null

        when {
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                binding.tilEmail.error = "Enter a valid email address"
                return
            }
            password.length < 6 -> {
                binding.tilPassword.error = "Use at least 6 characters"
                return
            }
            registering && password != confirmation -> {
                binding.tilConfirmPassword.error = "Passwords do not match"
                return
            }
        }

        setLoading(true)
        val task = if (registering) {
            auth.createUserWithEmailAndPassword(email, password)
        } else {
            auth.signInWithEmailAndPassword(email, password)
        }

        task.addOnCompleteListener(this) { result ->
            setLoading(false)
            if (result.isSuccessful) {
                openMain()
            } else {
                showAuthError(result.exception)
            }
        }
    }

    private fun setLoading(loading: Boolean) {
        binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        binding.btnSubmit.isEnabled = !loading
        binding.btnTabLogin.isEnabled = !loading
        binding.btnTabRegister.isEnabled = !loading
    }

    private fun showAuthError(error: Exception?) {
        when (error) {
            is FirebaseAuthWeakPasswordException ->
                binding.tilPassword.error = "Choose a stronger password"
            is FirebaseAuthUserCollisionException ->
                binding.tilEmail.error = "This email is already registered"
            is FirebaseAuthInvalidCredentialsException,
            is FirebaseAuthInvalidUserException ->
                binding.tilPassword.error = "Incorrect email or password"
            is FirebaseNetworkException ->
                Toast.makeText(this, "Check your internet connection", Toast.LENGTH_LONG).show()
            else ->
                Toast.makeText(this, "Authentication failed. Try again.", Toast.LENGTH_LONG).show()
        }
    }

    private fun openMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}