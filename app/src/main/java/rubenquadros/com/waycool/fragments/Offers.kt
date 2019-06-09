package rubenquadros.com.waycool.fragments

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.CardView
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import rubenquadros.com.waycool.R
import rubenquadros.com.waycool.utility.ApplicationConstants
import rubenquadros.com.waycool.utility.ApplicationUtility

@Suppress("UNCHECKED_CAST")
class Offers : Fragment() {

    private var listener: OnFragmentInteractionListener? = null
    private lateinit var offersImages: ArrayList<String>
    private lateinit var offersTitles: ArrayList<String>
    private lateinit var offersDescp: ArrayList<String>
    private lateinit var offersColor: ArrayList<String>
    private var position:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            offersImages = it.getSerializable(ApplicationConstants.OFFERSIMG) as ArrayList<String>
            offersTitles = it.getSerializable(ApplicationConstants.OFFERSTITLE) as ArrayList<String>
            offersDescp = it.getSerializable(ApplicationConstants.OFFERSDESCP) as ArrayList<String>
            offersColor = it.getSerializable(ApplicationConstants.OFFERSCOLOR) as ArrayList<String>
            position = it.getInt(ApplicationConstants.POSITION)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_offers, container, false)
        val mCardView = view.findViewById<CardView>(R.id.content)
        mCardView.radius = 20F
        mCardView.setBackgroundColor(Color.parseColor(offersColor[position]))
        val imageView = view.findViewById<ImageView>(R.id.img)
        imageView.setImageResource(context!!.resources.getIdentifier(offersImages[position], "drawable", context!!.packageName))
        val firstTv = view.findViewById<TextView>(R.id.firstText)
        firstTv.typeface = activity?.let { ApplicationUtility.fontBold(it) }
        firstTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, ApplicationConstants.OFFERS_SIZE)
        firstTv.text = offersTitles[position]
        val secondTv = view.findViewById<TextView>(R.id.secondText)
        secondTv.typeface = activity?.let { ApplicationUtility.fontBold(it) }
        secondTv.text = offersDescp[position]
        return view
    }

    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        @JvmStatic
        fun newInstance(context: Shopping, position: Int, imagesArr: ArrayList<String>, titleArr: ArrayList<String>, descpArr: ArrayList<String>, colorArr: ArrayList<String>) =
            Offers().apply {
                arguments = Bundle().apply {
                    putInt(ApplicationConstants.POSITION, position)
                    putSerializable(ApplicationConstants.OFFERSIMG, imagesArr)
                    putSerializable(ApplicationConstants.OFFERSTITLE, titleArr)
                    putSerializable(ApplicationConstants.OFFERSDESCP, descpArr)
                    putSerializable(ApplicationConstants.OFFERSCOLOR, colorArr)
                }
            }
    }
}
