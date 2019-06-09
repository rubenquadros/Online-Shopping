package rubenquadros.com.waycool.utility

import android.content.Context
import android.graphics.Typeface
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.view.View
import android.widget.FrameLayout

class ApplicationUtility {
    companion object {
        fun fontRegular(context: Context): Typeface {
            return Typeface.createFromAsset(context.assets, ApplicationConstants.FONT_REGULAR)
        }

        fun fontBold(context: Context): Typeface {
            return Typeface.createFromAsset(context.assets, ApplicationConstants.FONT_BOLD)
        }

        fun fontLight(context: Context): Typeface {
            return Typeface.createFromAsset(context.assets, ApplicationConstants.FONT_LIGHT)
        }
    }
}