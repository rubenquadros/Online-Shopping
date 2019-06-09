package rubenquadros.com.waycool.custom

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.LinearLayout

class MyLinearLayout : LinearLayout {
    private var scale = 1.0f

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context) : super(context) {}

    fun setScaleBoth(scale: Float) {
        this.scale = scale
        this.invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        val w = this.width
        val h = this.height
        canvas.scale(scale, scale, (w / 2).toFloat(), (h / 2).toFloat())

        super.onDraw(canvas)
    }
}
