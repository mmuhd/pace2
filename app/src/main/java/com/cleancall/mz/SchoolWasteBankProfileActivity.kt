package com.cleancall.mz

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.util.Base64

class SchoolWasteBankProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_school_waste_bank_profile)

        val id = intent.getStringExtra("record_id") ?: ""
        val record = SchoolWasteBankStore.find(this, id)

        findViewById<TextView>(R.id.profileTitle).text = record?.schoolName ?: "School Waste Bank"
        val sub = listOfNotNull(record?.lga, record?.status).joinToString(" • ")
        findViewById<TextView>(R.id.profileSubtitle).text = sub

        val periodStr = when (record?.reportingPeriodType?.lowercase()) {
            "daily" -> "Daily: ${record.reportingDate ?: "—"}"
            "weekly" -> "Weekly: ${record.reportingWeekStart ?: "—"}"
            "monthly" -> "Monthly: ${record.reportingMonth ?: "—"}"
            else -> "—"
        }
        findViewById<TextView>(R.id.valueSchool).text = record?.schoolName ?: "—"
        findViewById<TextView>(R.id.valueLga).text = record?.lga ?: "—"
        findViewById<TextView>(R.id.valuePeriod).text = periodStr
        findViewById<TextView>(R.id.valueStatus).text = record?.status ?: "—"

        fun pairD(a: Double?, b: Double?): String {
            val aStr = a?.toString() ?: "—"
            val bStr = b?.toString() ?: "—"
            return "$aStr collected, $bStr recycled"
        }
        findViewById<TextView>(R.id.valuePlastic).text = "Plastic: ${pairD(record?.plasticCollectedKg, record?.plasticRecycledKg)}"
        findViewById<TextView>(R.id.valuePaper).text = "Paper/Cardboard: ${pairD(record?.paperCollectedKg, record?.paperRecycledKg)}"
        findViewById<TextView>(R.id.valueMetal).text = "Metal: ${pairD(record?.metalCollectedKg, record?.metalRecycledKg)}"
        findViewById<TextView>(R.id.valueGlass).text = "Glass: ${pairD(record?.glassCollectedKg, record?.glassRecycledKg)}"
        findViewById<TextView>(R.id.valueOrganic).text = "Organic: ${pairD(record?.organicCollectedKg, record?.organicRecycledKg)}"
        val otherPair = pairD(record?.otherCollectedKg, record?.otherRecycledKg)
        val otherType = record?.otherType?.let { " ($it)" } ?: ""
        findViewById<TextView>(R.id.valueOther).text = "Other$otherType: $otherPair"

        findViewById<TextView>(R.id.valueSold).text = if (record?.soldToRecycler == true) "Sold to recycler" else "Not sold"
        findViewById<TextView>(R.id.valueIncome).text = record?.incomeFromSale?.let { "₦$it" } ?: "—"
        findViewById<TextView>(R.id.valueBuyer).text = record?.buyerName ?: "—"
        findViewById<TextView>(R.id.valueChallenges).text = record?.challenges?.joinToString(", ") ?: "—"
        findViewById<TextView>(R.id.valueParticipation).text = record?.studentParticipationLevel?.toString() ?: "—"
        findViewById<TextView>(R.id.valueRemarks).text = record?.remarks ?: "—"

        fun showPhoto(img: ImageView, b64: String?) {
            if (!b64.isNullOrEmpty()) {
                val bytes = Base64.decode(b64, Base64.NO_WRAP)
                val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                img.setImageBitmap(bmp)
            }
        }
        val photos = record?.photoBase64s ?: emptyList()
        showPhoto(findViewById(R.id.photo1), photos.getOrNull(0))
        showPhoto(findViewById(R.id.photo2), photos.getOrNull(1))
        showPhoto(findViewById(R.id.photo3), photos.getOrNull(2))

        findViewById<View>(R.id.backButton).setOnClickListener { finish() }
    }
}

