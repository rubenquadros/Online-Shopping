package rubenquadros.com.waycool.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.TypedValue
import kotlinx.android.synthetic.main.activity_splash.*
import rubenquadros.com.waycool.R
import rubenquadros.com.waycool.utility.ApplicationConstants
import rubenquadros.com.waycool.utility.ApplicationUtility



class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        setupUI()
        setTransition()
    }

    private fun setupUI() {
        logoLabel.text = getString(rubenquadros.com.waycool.R.string.app_name)
        logoLabel.typeface = ApplicationUtility.fontBold(this)
        logoLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, ApplicationConstants.FONT_SPLASH)
    }

    private fun setTransition() {
        parentLayout.animate().alpha(1f).duration = 2000L
        logoLabel.animate().alpha(1f).setStartDelay(2000L).duration = 3000L

        Handler().postDelayed({
            goToHome()
        }, 5000)
    }

    private fun goToHome() {
        Handler().postDelayed({
            startActivity(Intent(this, HomeScreen::class.java))
            finish()
        }, 500)
    }
}
