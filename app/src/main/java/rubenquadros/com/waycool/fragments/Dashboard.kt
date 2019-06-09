package rubenquadros.com.waycool.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.widget.ProgressBar
import android.widget.TextView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import rubenquadros.com.waycool.R
import rubenquadros.com.waycool.models.DashboardData
import rubenquadros.com.waycool.utility.ApplicationConstants
import rubenquadros.com.waycool.utility.ApplicationUtility
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


@Suppress("LocalVariableName")
class Dashboard : Fragment() {

    private lateinit var dashboardData: DashboardData
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var barChart: BarChart
    private lateinit var lineChart: LineChart
    private lateinit var dateText: TextView
    private lateinit var amountText: TextView
    private lateinit var expenditureTitle: TextView
    private lateinit var expenditureDesc: TextView
    private lateinit var growthTitle: TextView
    private lateinit var growthDesc: TextView
    private lateinit var monthTV1: TextView
    private lateinit var monthTV2: TextView
    private lateinit var monthTV3: TextView
    private lateinit var monthTV4: TextView
    private lateinit var mProgress: ProgressBar
    private val firstMonthLine: ArrayList<Entry> = arrayListOf()
    private val secondMonthLine: ArrayList<Entry> = arrayListOf()
    private lateinit var dataSet: LineDataSet
    private var didSwipeRight = false
    private var isFirstMonth = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        barChart = view.findViewById(R.id.barchart)
        lineChart = view.findViewById(R.id.lineChart)
        mProgress = view.findViewById(R.id.myProgress)
        dashboardData = DashboardData()
        dateText = view.findViewById(R.id.dateText)
        dateText.typeface = activity?.let { ApplicationUtility.fontBold(it) }
        dateText.setTextSize(TypedValue.COMPLEX_UNIT_SP, ApplicationConstants.FONT_DISCOUNT)
        amountText = view.findViewById(R.id.sumText)
        amountText.typeface = activity?.let { ApplicationUtility.fontBold(it) }
        amountText.setTextSize(TypedValue.COMPLEX_UNIT_SP, ApplicationConstants.FONT_DISCOUNT)
        expenditureTitle = view.findViewById(R.id.expenditureTitle)
        expenditureTitle.typeface = activity?.let { ApplicationUtility.fontBold(it) }
        expenditureTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, ApplicationConstants.FONT_REGULAR_SIZE)
        expenditureDesc = view.findViewById(R.id.expenditureDesc)
        expenditureDesc.typeface = activity?.let { ApplicationUtility.fontRegular(it) }
        growthTitle = view.findViewById(R.id.growthTitle)
        growthTitle.typeface = activity?.let { ApplicationUtility.fontBold(it) }
        growthTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, ApplicationConstants.FONT_REGULAR_SIZE)
        growthDesc = view.findViewById(R.id.growthDesc)
        growthDesc.typeface = activity?.let { ApplicationUtility.fontRegular(it) }
        monthTV1 = view.findViewById(R.id.month1)
        monthTV1.typeface = activity?.let { ApplicationUtility.fontRegular(it) }
        monthTV2 = view.findViewById(R.id.month2)
        monthTV2.typeface = activity?.let { ApplicationUtility.fontRegular(it) }
        monthTV3 = view.findViewById(R.id.month3)
        monthTV3.typeface = activity?.let { ApplicationUtility.fontRegular(it) }
        monthTV4 = view.findViewById(R.id.month4)
        monthTV4.typeface = activity?.let { ApplicationUtility.fontRegular(it) }
        getDate()
        setLineChart()
        val gesture = GestureDetector(
            activity,
            object : GestureDetector.SimpleOnGestureListener() {

                override fun onDown(e: MotionEvent): Boolean {
                    return true
                }

                override fun onFling(
                    e1: MotionEvent, e2: MotionEvent, velocityX: Float,
                    velocityY: Float
                ): Boolean {
                    val SWIPE_MIN_DISTANCE = 120
                    val SWIPE_MAX_OFF_PATH = 250
                    val SWIPE_THRESHOLD_VELOCITY = 200
                    try {
                        if(Math.abs(e1.y - e2.y) > SWIPE_MAX_OFF_PATH) {
                            return false
                        }
                        if(e1.x - e2.x > SWIPE_THRESHOLD_VELOCITY) {
                            if(didSwipeRight) {
                                didSwipeRight = false
                                showPrevMonthData()
                            }
                        }
                        if(e2.x - e1.x > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY){
                            if(!didSwipeRight) {
                                didSwipeRight = true
                                showNextMonthData()
                            }
                        }
                    }catch (e: Exception) {
                        Log.d(ApplicationConstants.TAG, e.message)
                    }
                    return super.onFling(e1, e2, velocityX, velocityY)
                }
            })

        view.setOnTouchListener { _, event -> gesture.onTouchEvent(event) }
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

    @SuppressLint("SetTextI18n")
    fun getDate() {
        val curDate = Calendar.getInstance().time
        val dateFormatter = SimpleDateFormat("dd-MM-YYYY", Locale.getDefault())
        dateText.text = activity!!.resources.getString(R.string.today) + " " + dateFormatter.format(curDate)
    }

    private fun setLineChart() {
        var i = 0f
        firstMonthLine.removeAll(firstMonthLine)
        secondMonthLine.removeAll(secondMonthLine)
        if(isFirstMonth){
            while (i < 7f) {
                i += 0.02f
                firstMonthLine.add(Entry(i, Math.sin(i.toDouble()).toFloat()))
            }
            dataSet = LineDataSet(firstMonthLine, "Spending")
            mProgress.visibility = View.GONE
        }else{
            while (i < 7f) {
                i += 0.02f
                secondMonthLine.add(Entry(i, Math.cos(i.toDouble()).toFloat()))
            }
            dataSet = LineDataSet(secondMonthLine, "Spending")
            mProgress.visibility = View.GONE
        }
        dataSet.color = activity!!.resources.getColor(R.color.colorPrimary)
        dataSet.setCircleColor(activity!!.resources.getColor(R.color.colorPrimary))
        dataSet.lineWidth = 1f
        dataSet.setDrawCircleHole(false)
        dataSet.isHighlightEnabled = false
        val xAxis = lineChart.xAxis
        xAxis.isEnabled = false
        val yAxisRight = lineChart.axisRight
        yAxisRight.setDrawGridLines(false)
        yAxisRight.isEnabled = false
        val yAxisLeft = lineChart.axisLeft
        yAxisLeft.isEnabled = false
        yAxisLeft.setDrawGridLines(false)
        val data = LineData(dataSet)
        lineChart.data = data
        lineChart.animateY(1000)
        lineChart.setScaleEnabled(false)
        lineChart.invalidate()
        lineChart.setOnClickListener {
            barChart.visibility = View.VISIBLE
            if(isFirstMonth) {
                setBarChart(dashboardData.firstMonth)
            }else{
                setBarChart(dashboardData.secondMonth)
            }
        }
    }

    private fun setBarChart(mData: ArrayList<BarEntry>) {
        barChart.setDrawBarShadow(false)
        barChart.setScaleEnabled(false)
        val description = Description()
        description.text = ""
        barChart.description = description
        barChart.setDrawValueAboveBar(false)
        val xAxis = barChart.xAxis
        xAxis.setDrawGridLines(false)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.isEnabled = true
        xAxis.setDrawAxisLine(true)
        xAxis.labelCount = 4
        val yRight = barChart.axisRight
        yRight.isEnabled = false
        yRight.setDrawGridLines(false)
        val yLeft = barChart.axisLeft
        yLeft.isEnabled = false
        val barDataSet = BarDataSet(mData, "")
        barDataSet.color = Color.parseColor("#CFD8DC")
        barDataSet.isHighlightEnabled = false
        val data = BarData(barDataSet)
        data.barWidth = 0.8f
        barChart.animateY(1000)
        barChart.data = data
        barChart.invalidate()
    }

    private fun showNextMonthData() {
        mProgress.visibility = View.VISIBLE
        barChart.visibility = View.GONE
        monthTV1.setTextColor(activity!!.resources.getColor(R.color.monthColor))
        monthTV2.setTextColor(activity!!.resources.getColor(R.color.colorBlack))
        isFirstMonth = false
        setLineChart()
    }

    private fun showPrevMonthData() {
        mProgress.visibility = View.VISIBLE
        barChart.visibility = View.GONE
        monthTV1.setTextColor(activity!!.resources.getColor(R.color.colorBlack))
        monthTV2.setTextColor(activity!!.resources.getColor(R.color.monthColor))
        isFirstMonth = true
        setLineChart()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Dashboard().apply {
                arguments = Bundle().apply {

                }
            }
    }
}
