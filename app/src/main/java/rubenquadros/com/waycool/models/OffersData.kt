package rubenquadros.com.waycool.models

data class OffersData (
   val offerImages: ArrayList<String> = arrayListOf("cashback", "fruitsdiscount", "electronicsdiscount", "shoesdiscount", "snacksdiscount"),
   val offersHeading: ArrayList<String> = arrayListOf("Rs. 500 Cashback", "Minimum 30% Off!", "Flat 20% Off!", "Minimum 15% Off!", "Buy 1 Get 1 Free!"),
   val offersDescription: ArrayList<String> = arrayListOf("On first order, min of Rs. 2500", "On all fruits and veggies",
                                                "On Laptops & Accessories", "On all Nike shoes", "On all Chips"),
   val backgroundColor: ArrayList<String> = arrayListOf("#9ae5a1", "#f9bb85", "#a7d6f1", "#f5948a", "#f9d886")
) {
    override fun toString(): String {
        return "OffersData(offerImages=$offerImages, offersHeading=$offersHeading, offersDescription=$offersDescription)"
    }
}