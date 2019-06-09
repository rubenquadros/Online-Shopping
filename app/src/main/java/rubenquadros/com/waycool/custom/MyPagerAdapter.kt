package rubenquadros.com.waycool.custom

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import com.nineoldandroids.view.ViewHelper
import kotlinx.android.synthetic.main.fragment_shopping.*
import rubenquadros.com.waycool.R
import rubenquadros.com.waycool.fragments.Offers
import rubenquadros.com.waycool.fragments.Shopping
import rubenquadros.com.waycool.models.OffersData

class MyPagerAdapter(val context: Shopping, private val fm: FragmentManager?) : FragmentStatePagerAdapter(fm), ViewPager.OnPageChangeListener {

    private var swipedLeft = false
    private var lastPage: Int = 10
    private var cur: MyLinearLayout? = null
    private var next: MyLinearLayout? = null
    private var prev: MyLinearLayout? = null
    private var nextnext: MyLinearLayout? = null
    private val minAlpha = 0.6f
    private val maxAlpha = 1f
    private val minDegree = 60.0f
    val BIG_SCALE = 1.0f
    val SMALL_SCALE = 0.8f
    val DIFF_SCALE = BIG_SCALE - SMALL_SCALE
    private val offersData = OffersData()

    override fun getItem(p0: Int): Fragment {
        val curFragment = Offers.newInstance(context, p0, offersData.offerImages, offersData.offersHeading, offersData.offersDescription, offersData.backgroundColor)
        cur = getRootView(position = p0)
        next = getRootView(position = p0+1)
        prev = getRootView(position = p0-1)

        return curFragment
    }

    override fun getCount(): Int {
        return offersData.offerImages.size
    }

    override fun onPageScrollStateChanged(p0: Int) {

    }

    override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
        if(p1 in 0f..1f) {
            cur =getRootView(p0)
            next = getRootView(p0+1)
            prev = getRootView(p0-1)
            nextnext = getRootView(p0+2)

            ViewHelper.setAlpha(cur, maxAlpha-0.5f*p1)
            ViewHelper.setAlpha(next, minAlpha+0.5f*p1)
            ViewHelper.setAlpha(prev, minAlpha+0.5f*p1)

            if (nextnext != null) {
                ViewHelper.setAlpha(nextnext, minAlpha)
                ViewHelper.setRotationY(nextnext, -minDegree)
            }
            if (cur != null) {
                cur!!.setScaleBoth(BIG_SCALE - DIFF_SCALE * p1)

                ViewHelper.setRotationY(cur, 0f)
            }

            if (next != null) {
                next!!.setScaleBoth(SMALL_SCALE + DIFF_SCALE * p1)
                ViewHelper.setRotationY(next, -minDegree)
            }
            if (prev != null) {
                ViewHelper.setRotationY(prev, minDegree)
            }

            if (swipedLeft) {
                if (next != null)
                    ViewHelper.setRotationY(next, -minDegree + minDegree * p1)
                if (cur != null)
                    ViewHelper.setRotationY(cur, 0 + minDegree * p1)
            } else {
                if (next != null)
                    ViewHelper.setRotationY(next, -minDegree + minDegree * p1)
                if (cur != null) {
                    ViewHelper.setRotationY(cur, 0 + minDegree * p1)
                }
            }
        }
        if (p1 >= 1f) {
            ViewHelper.setAlpha(cur, maxAlpha)
        }
    }

    override fun onPageSelected(p0: Int) {
        if (lastPage <= p0) {
            swipedLeft = true
        } else if (lastPage > p0) {
            swipedLeft = false
        }
        lastPage = p0
    }

    private fun getRootView(position:Int) : MyLinearLayout? {
        val mLinearLayout: MyLinearLayout
        try {
            mLinearLayout = fm!!.findFragmentByTag(getFragmentTag(position))!!.view!!.findViewById(R.id.root)
        }catch (e: Exception){
            return null
        }
        if (mLinearLayout != null) {
            return mLinearLayout
        }
        return null
    }

    private fun getFragmentTag(position: Int): String {
        return "android:switcher:" + context.viewPager + ":" + position
    }
}