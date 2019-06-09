package rubenquadros.com.waycool.models

import com.github.mikephil.charting.data.BarEntry

data class DashboardData(
    val firstMonth: ArrayList<BarEntry> = arrayListOf(BarEntry(1f, 50f), BarEntry(2f, 68f), BarEntry(3f, 32f), BarEntry(4f, 43f)),
    val secondMonth: ArrayList<BarEntry> = arrayListOf(BarEntry(1f, 72f), BarEntry(2f, 28f), BarEntry(3f, 33f), BarEntry(4f, 68f))
) {
    override fun toString(): String {
        return "DashboardData(firstMonth=$firstMonth)"
    }
}