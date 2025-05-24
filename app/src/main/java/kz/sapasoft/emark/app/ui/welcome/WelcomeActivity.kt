package kz.sapasoft.emark.app.ui.welcome

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.example.decompiledapk.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import dagger.android.support.DaggerAppCompatActivity
import kz.sapasoft.emark.app.ui.MainActivity
import javax.inject.Inject

class WelcomeActivity : DaggerAppCompatActivity() {


    private val `viewModel$delegate`: WelcomeViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(WelcomeViewModel::class.java)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    val viewModel: WelcomeViewModel
        /* access modifiers changed from: private */
        get() = `viewModel$delegate` as WelcomeViewModel


    /* access modifiers changed from: protected */
    public override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_welcome)
        setOnClickListeners()
        setObservers()
    }

    private fun setOnClickListeners() {
        findViewById<MaterialButton>(R.id.btn_start).setOnClickListener { view ->
        //    Utils.hideKeyboard(view)
            val username = findViewById<TextInputEditText>(R.id.et_username).text.toString()
            val password = findViewById<TextInputEditText>(R.id.et_password).text.toString()
            val serverUrl = findViewById<TextInputEditText>(R.id.et_server).text.toString()

            viewModel.login(username, password, serverUrl)
        }
    }

    private fun setObservers() {
        val lifecycleOwner: LifecycleOwner = this
        viewModel.isRefreshing.observe(this as LifecycleOwner) { isRefreshing ->
            val materialButton = findViewById<MaterialButton>(R.id.btn_start)
            materialButton.isEnabled = !isRefreshing
        }

        viewModel.loginData.observe(this as LifecycleOwner) { isLoggedIn ->
            if (isLoggedIn) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }

        viewModel.error.observe(this as LifecycleOwner) { error ->
            val errorMessage = error.message ?: getString(R.string.error)
            val materialButton: MaterialButton = findViewById(R.id.btn_start)
            Snackbar.make(materialButton, errorMessage, Snackbar.LENGTH_SHORT).show()
        }

        viewModel.server.observe(this as LifecycleOwner) { serverUrl ->
            //findViewById<TextInputEditText>(R.id.et_server).setText(serverUrl)
        }
    }
}
