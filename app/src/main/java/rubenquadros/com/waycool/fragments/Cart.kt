package rubenquadros.com.waycool.fragments

import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_cart.*
import org.w3c.dom.Text
import rubenquadros.com.waycool.R
import rubenquadros.com.waycool.custom.CartAdapter
import rubenquadros.com.waycool.models.CartData
import rubenquadros.com.waycool.utility.ApplicationConstants
import rubenquadros.com.waycool.utility.ApplicationUtility

class Cart : Fragment() {

    private var listener: OnFragmentInteractionListener? = null
    private lateinit var cartData: CartData
    private lateinit var items: TextView
    private lateinit var total: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_cart, container, false)
        items = view.findViewById(R.id.items)
        total = view.findViewById(R.id.total)
        items.typeface = activity?.let { ApplicationUtility.fontRegular(it) }
        items.setTextSize(TypedValue.COMPLEX_UNIT_SP, ApplicationConstants.FONT_REGULAR_SIZE)
        total.typeface = activity?.let { ApplicationUtility.fontRegular(it) }
        total.setTextSize(TypedValue.COMPLEX_UNIT_SP, ApplicationConstants.FONT_REGULAR_SIZE)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val linearLayoutManager = LinearLayoutManager(activity)
        cartData = CartData()
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = activity?.let { CartAdapter(it,cartData.nameArray, cartData.imageArray, cartData.amountArray, cartData.weightArray,
                                                            cartData.actualPriceArray, cartData.priceArray) }
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
        fun newInstance() =
            Cart().apply {
                arguments = Bundle().apply {

                }
            }
    }
}
