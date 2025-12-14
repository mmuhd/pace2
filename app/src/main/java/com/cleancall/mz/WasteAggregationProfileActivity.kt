package com.cleancall.mz

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.util.Base64

class WasteAggregationProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waste_aggregation_profile)

        val id = intent.getStringExtra("report_id") ?: ""
        val report = WasteAggregationStore.find(this, id)

        findViewById<View>(R.id.backButton).setOnClickListener { finish() }

        findViewById<TextView>(R.id.profileTitle).text = report?.siteName ?: "Aggregation Report"
        findViewById<TextView>(R.id.profileSubtitle).text = listOfNotNull(report?.lga, report?.collectionDate).joinToString(" • ")

        findViewById<TextView>(R.id.valueLga).text = report?.lga ?: "—"
        findViewById<TextView>(R.id.valueSource).text = report?.wasteSource ?: "—"
        findViewById<TextView>(R.id.valueSite).text = report?.siteName ?: "—"
        findViewById<TextView>(R.id.valueDate).text = report?.collectionDate ?: "—"
        findViewById<TextView>(R.id.valueTeam).text = report?.team ?: "—"

        findViewById<TextView>(R.id.valueTotal).text = report?.totalWasteKg?.toString() ?: "—"
        findViewById<TextView>(R.id.valuePct).text = report?.recyclablePercentage?.let { "$it%" } ?: "—"
        fun fmtD(d: Double?): String = d?.toString() ?: "—"
        findViewById<TextView>(R.id.valuePlastic).text = fmtD(report?.plasticKg)
        findViewById<TextView>(R.id.valuePaper).text = fmtD(report?.paperKg)
        findViewById<TextView>(R.id.valueMetal).text = fmtD(report?.metalKg)
        findViewById<TextView>(R.id.valueGlass).text = fmtD(report?.glassKg)
        findViewById<TextView>(R.id.valueOrganic).text = fmtD(report?.organicKg)
        findViewById<TextView>(R.id.valueOther).text = fmtD(report?.otherKg)
        findViewById<TextView>(R.id.valueDisposal).text = report?.finalDisposalSite ?: "—"

        findViewById<TextView>(R.id.valueTransport).text = report?.transportUsed ?: "—"
        findViewById<TextView>(R.id.valueRecyclers).text = report?.recyclersInvolved ?: "—"
        findViewById<TextView>(R.id.valueWeather).text = report?.weather ?: "—"
        findViewById<TextView>(R.id.valueHazard).text = if (report?.hazardousFound == true) "Yes" else "No"
        findViewById<TextView>(R.id.valueHazardDesc).text = report?.hazardousDescription ?: "—"
        findViewById<TextView>(R.id.valueChallenges).text = report?.challenges?.joinToString(", ") ?: "—"
        findViewById<TextView>(R.id.valueRemarks).text = report?.remarks ?: "—"

        fun setPhoto(iv: ImageView, base64: String?) {
            if (base64.isNullOrEmpty()) return
            val bytes = Base64.decode(base64, Base64.DEFAULT)
            val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            iv.setImageBitmap(bmp)
        }
        val photos = report?.photos ?: emptyList()
        setPhoto(findViewById(R.id.photo1), photos.getOrNull(0))
        setPhoto(findViewById(R.id.photo2), photos.getOrNull(1))
        setPhoto(findViewById(R.id.photo3), photos.getOrNull(2))

        val total = report?.totalWasteKg ?: 0.0
        fun fmt(d: Double?): String = d?.toString() ?: "—"
        val recyclableWeight = report?.recyclablePercentage?.let { total * (it / 100.0) }
        val nonRecyclableWeight = recyclableWeight?.let { total - it }
        val kgPerTrip = report?.tripCount?.let { if (it > 0) total / it else null }
        val kgPerVehicle = report?.vehicleCount?.let { if (it > 0) total / it else null }
        val kgPerStaff = report?.staffCount?.let { if (it > 0) total / it else null }
        val kgPerHour = report?.hoursWorked?.let { if (it > 0.0) total / it else null }
        val tripsPerVehicle = if ((report?.tripCount ?: 0) > 0 && (report?.vehicleCount ?: 0) > 0) {
            (report?.tripCount ?: 0).toDouble() / (report?.vehicleCount ?: 0).toDouble()
        } else null

        findViewById<TextView>(R.id.valueRecyclableWeight).text = fmt(recyclableWeight)
        findViewById<TextView>(R.id.valueNonRecyclableWeight).text = fmt(nonRecyclableWeight)
        findViewById<TextView>(R.id.valueKgPerTrip).text = fmt(kgPerTrip)
        findViewById<TextView>(R.id.valueKgPerVehicle).text = fmt(kgPerVehicle)
        findViewById<TextView>(R.id.valueKgPerStaff).text = fmt(kgPerStaff)
        findViewById<TextView>(R.id.valueKgPerHour).text = fmt(kgPerHour)
        findViewById<TextView>(R.id.valueTripsPerVehicle).text = fmt(tripsPerVehicle)
    }
}
