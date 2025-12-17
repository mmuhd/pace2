package com.cleancall.mz

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import org.json.JSONArray

object ApiClient {
    private val client = OkHttpClient.Builder().build()

    private fun baseUrl(context: Context): String {
        val prefs = context.getSharedPreferences("clean_call", Context.MODE_PRIVATE)
        val cfg = prefs.getString("api_base_url", BuildConfig.BASE_URL) ?: BuildConfig.BASE_URL
        return if (cfg.endsWith("/")) cfg.dropLast(1) else cfg
    }

    private fun authToken(context: Context): String? {
        val prefs = context.getSharedPreferences("clean_call", Context.MODE_PRIVATE)
        return prefs.getString("api_token", null)
    }

    fun postStakeholderCommitment(context: Context, item: StakeholderCommitment): Boolean {
        val url = baseUrl(context) + "/commitments"
        val json = JSONObject().apply {
            put("stakeholder_name", item.stakeholderName)
            put("stakeholder_type", item.stakeholderType)
            put("lga", item.lga)
            put("contact_person", item.contactPerson)
            put("phone", item.phone)
            put("engagement_date", item.engagementDate)
            put("engagement_type", item.engagementType)
            put("engagement_description", item.engagementDescription)
            put("commitment_text", item.commitmentText)
            put("commitment_category", item.commitmentCategory)
            put("due_date", item.dueDate)
            put("priority_level", item.priorityLevel)
            put("action_taken", item.actionTaken)
            put("status", item.status)
            put("followup_required", item.followupRequired)
            put("followup_type", item.followupType)
            put("followup_date", item.followupDate)
            put("followup_assigned_to", item.followupAssignedTo)
            put("remarks", item.remarks)
            put("evidence", JSONArray(item.evidence))
        }.toString()
        val body: RequestBody = json.toRequestBody("application/json; charset=utf-8".toMediaType())
        val reqBuilder = Request.Builder()
            .url(url)
            .post(body)
            .addHeader("Accept", "application/json")
        val token = authToken(context)
        if (!token.isNullOrEmpty()) reqBuilder.addHeader("Authorization", "Bearer $token")
        val resp = client.newCall(reqBuilder.build()).execute()
        return resp.isSuccessful
    }

    fun postSchoolWasteBankRecord(context: Context, rec: SchoolWasteBankRecord): Boolean {
        val url = baseUrl(context) + "/schools"
        val json = JSONObject().apply {
            put("school_id", rec.schoolId)
            put("school_name", rec.schoolName)
            put("lga", rec.lga)
            put("reporting_period_type", rec.reportingPeriodType)
            put("reporting_date", rec.reportingDate)
            put("reporting_week_start", rec.reportingWeekStart)
            put("reporting_month", rec.reportingMonth)
            put("status", rec.status)
            put("plastic_collected_kg", rec.plasticCollectedKg)
            put("plastic_recycled_kg", rec.plasticRecycledKg)
            put("paper_collected_kg", rec.paperCollectedKg)
            put("paper_recycled_kg", rec.paperRecycledKg)
            put("metal_collected_kg", rec.metalCollectedKg)
            put("metal_recycled_kg", rec.metalRecycledKg)
            put("glass_collected_kg", rec.glassCollectedKg)
            put("glass_recycled_kg", rec.glassRecycledKg)
            put("organic_collected_kg", rec.organicCollectedKg)
            put("organic_recycled_kg", rec.organicRecycledKg)
            put("other_type", rec.otherType)
            put("other_collected_kg", rec.otherCollectedKg)
            put("other_recycled_kg", rec.otherRecycledKg)
            put("sold_to_recycler", rec.soldToRecycler)
            put("income_from_sale", rec.incomeFromSale)
            put("buyer_name", rec.buyerName)
            put("challenges", JSONArray(rec.challenges))
            put("student_participation_level", rec.studentParticipationLevel)
            put("remarks", rec.remarks)
            put("photo_base64s", JSONArray(rec.photoBase64s))
            put("recorded_by_user_id", rec.recordedByUserId)
        }.toString()
        val body: RequestBody = json.toRequestBody("application/json; charset=utf-8".toMediaType())
        val reqBuilder = Request.Builder().url(url).post(body).addHeader("Accept", "application/json")
        val token = authToken(context)
        if (!token.isNullOrEmpty()) reqBuilder.addHeader("Authorization", "Bearer $token")
        val resp = client.newCall(reqBuilder.build()).execute()
        return resp.isSuccessful
    }

    fun fetchSchoolWasteBanks(context: Context): List<SchoolWasteBankRecord> {
        val url = baseUrl(context) + "/schools?per_page=100"
        val req = Request.Builder().url(url).get().addHeader("Accept", "application/json").build()
        val resp = client.newCall(req).execute()
        if (!resp.isSuccessful) return emptyList()
        val s = resp.body?.string().orEmpty()
        val arr = try {
            val obj = JSONObject(s)
            if (obj.has("data")) obj.getJSONArray("data") else JSONArray(s)
        } catch (_: Exception) { JSONArray() }
        val out = mutableListOf<SchoolWasteBankRecord>()
        for (i in 0 until arr.length()) {
            val item = arr.optJSONObject(i) ?: continue
            val mapped = JSONObject().apply {
                put("id", item.optString("id"))
                put("schoolId", item.optString("school_id"))
                put("schoolName", item.optString("school_name"))
                put("lga", item.optString("lga"))
                put("reportingPeriodType", item.optString("reporting_period_type"))
                put("reportingDate", item.optString("reporting_date"))
                put("reportingWeekStart", item.optString("reporting_week_start"))
                put("reportingMonth", item.optString("reporting_month"))
                put("status", item.optString("status"))
                put("plasticCollectedKg", item.optDouble("plastic_collected_kg"))
                put("plasticRecycledKg", item.optDouble("plastic_recycled_kg"))
                put("paperCollectedKg", item.optDouble("paper_collected_kg"))
                put("paperRecycledKg", item.optDouble("paper_recycled_kg"))
                put("metalCollectedKg", item.optDouble("metal_collected_kg"))
                put("metalRecycledKg", item.optDouble("metal_recycled_kg"))
                put("glassCollectedKg", item.optDouble("glass_collected_kg"))
                put("glassRecycledKg", item.optDouble("glass_recycled_kg"))
                put("organicCollectedKg", item.optDouble("organic_collected_kg"))
                put("organicRecycledKg", item.optDouble("organic_recycled_kg"))
                put("otherType", item.optString("other_type"))
                put("otherCollectedKg", item.optDouble("other_collected_kg"))
                put("otherRecycledKg", item.optDouble("other_recycled_kg"))
                put("soldToRecycler", item.optBoolean("sold_to_recycler"))
                put("incomeFromSale", item.optDouble("income_from_sale"))
                put("buyerName", item.optString("buyer_name"))
                put("challenges", item.optJSONArray("challenges") ?: JSONArray())
                put("studentParticipationLevel", item.optInt("student_participation_level"))
                put("remarks", item.optString("remarks"))
                put("photoBase64s", item.optJSONArray("photo_base64s") ?: JSONArray())
                put("recordedByUserId", item.optString("recorded_by_user_id"))
                put("createdAt", 0)
                put("updatedAt", 0)
            }
            out.add(SchoolWasteBankRecord.fromJson(mapped))
        }
        return out
    }

    fun postWasteAggregation(context: Context, report: WasteAggregationReport): Boolean {
        val url = baseUrl(context) + "/aggregations"
        val json = JSONObject().apply {
            put("lga", report.lga)
            put("waste_source", report.wasteSource)
            put("site_name", report.siteName)
            put("collection_date", report.collectionDate)
            put("team", report.team)
            put("total_waste_kg", report.totalWasteKg)
            put("recyclable_percentage", report.recyclablePercentage)
            put("plastic_kg", report.plasticKg)
            put("paper_kg", report.paperKg)
            put("metal_kg", report.metalKg)
            put("glass_kg", report.glassKg)
            put("organic_kg", report.organicKg)
            put("other_kg", report.otherKg)
            put("final_disposal_site", report.finalDisposalSite)
            put("transport_used", report.transportUsed)
            put("recyclers_involved", report.recyclersInvolved)
            put("trip_count", report.tripCount)
            put("vehicle_count", report.vehicleCount)
            put("hours_worked", report.hoursWorked)
            put("avg_load_kg", report.avgLoadKg)
            put("staff_count", report.staffCount)
            put("moisture_percent", report.moisturePercent)
            put("contamination_percent", report.contaminationPercent)
            put("weather", report.weather)
            put("hazardous_found", report.hazardousFound)
            put("hazardous_description", report.hazardousDescription)
            put("challenges", JSONArray(report.challenges))
            put("remarks", report.remarks)
            put("photos", JSONArray(report.photos))
            put("recorded_by_user_id", report.recordedByUserId)
            put("gps_lat", report.gpsLat)
            put("gps_long", report.gpsLong)
        }.toString()
        val body: RequestBody = json.toRequestBody("application/json; charset=utf-8".toMediaType())
        val reqBuilder = Request.Builder().url(url).post(body).addHeader("Accept", "application/json")
        val token = authToken(context)
        if (!token.isNullOrEmpty()) reqBuilder.addHeader("Authorization", "Bearer $token")
        val resp = client.newCall(reqBuilder.build()).execute()
        return resp.isSuccessful
    }

    fun fetchWasteAggregations(context: Context): List<WasteAggregationReport> {
        val url = baseUrl(context) + "/aggregations?per_page=100"
        val req = Request.Builder().url(url).get().addHeader("Accept", "application/json").build()
        val resp = client.newCall(req).execute()
        if (!resp.isSuccessful) return emptyList()
        val s = resp.body?.string().orEmpty()
        val arr = try {
            val obj = JSONObject(s)
            if (obj.has("data")) obj.getJSONArray("data") else JSONArray(s)
        } catch (_: Exception) { JSONArray() }
        val out = mutableListOf<WasteAggregationReport>()
        for (i in 0 until arr.length()) {
            val item = arr.optJSONObject(i) ?: continue
            val mapped = JSONObject().apply {
                put("id", item.optString("id"))
                put("lga", item.optString("lga"))
                put("wasteSource", item.optString("waste_source"))
                put("siteName", item.optString("site_name"))
                put("collectionDate", item.optString("collection_date"))
                put("team", item.optString("team"))
                put("totalWasteKg", item.optDouble("total_waste_kg"))
                put("recyclablePercentage", item.optInt("recyclable_percentage"))
                put("plasticKg", item.optDouble("plastic_kg"))
                put("paperKg", item.optDouble("paper_kg"))
                put("metalKg", item.optDouble("metal_kg"))
                put("glassKg", item.optDouble("glass_kg"))
                put("organicKg", item.optDouble("organic_kg"))
                put("otherKg", item.optDouble("other_kg"))
                put("finalDisposalSite", item.optString("final_disposal_site"))
                put("transportUsed", item.optString("transport_used"))
                put("recyclersInvolved", item.optString("recyclers_involved"))
                put("tripCount", item.optInt("trip_count"))
                put("vehicleCount", item.optInt("vehicle_count"))
                put("hoursWorked", item.optDouble("hours_worked"))
                put("avgLoadKg", item.optDouble("avg_load_kg"))
                put("staffCount", item.optInt("staff_count"))
                put("moisturePercent", item.optInt("moisture_percent"))
                put("contaminationPercent", item.optInt("contamination_percent"))
                put("weather", item.optString("weather"))
                put("hazardousFound", item.optBoolean("hazardous_found"))
                put("hazardousDescription", item.optString("hazardous_description"))
                put("challenges", item.optJSONArray("challenges") ?: JSONArray())
                put("remarks", item.optString("remarks"))
                put("photos", item.optJSONArray("photos") ?: JSONArray())
                put("recordedByUserId", item.optString("recorded_by_user_id"))
                put("gpsLat", item.optDouble("gps_lat"))
                put("gpsLong", item.optDouble("gps_long"))
                put("createdAt", 0)
                put("updatedAt", 0)
            }
            out.add(WasteAggregationReport.fromJson(mapped))
        }
        return out
    }

    fun fetchStakeholderCommitments(context: Context): List<StakeholderCommitment> {
        val url = baseUrl(context) + "/commitments?per_page=100"
        val req = Request.Builder().url(url).get().addHeader("Accept", "application/json").build()
        val resp = client.newCall(req).execute()
        if (!resp.isSuccessful) return emptyList()
        val s = resp.body?.string().orEmpty()
        val arr = try {
            val obj = JSONObject(s)
            if (obj.has("data")) obj.getJSONArray("data") else JSONArray(s)
        } catch (_: Exception) { JSONArray() }
        val out = mutableListOf<StakeholderCommitment>()
        for (i in 0 until arr.length()) {
            val item = arr.optJSONObject(i) ?: continue
            val mapped = JSONObject().apply {
                put("id", item.optString("id"))
                put("stakeholderName", item.optString("stakeholder_name"))
                put("stakeholderType", item.optString("stakeholder_type"))
                put("lga", item.optString("lga"))
                put("contactPerson", item.optString("contact_person"))
                put("phone", item.optString("phone"))
                put("engagementDate", item.optString("engagement_date"))
                put("engagementType", item.optString("engagement_type"))
                put("engagedByUserId", item.optString("engaged_by_user_id"))
                put("engagementDescription", item.optString("engagement_description"))
                put("commitmentText", item.optString("commitment_text"))
                put("commitmentCategory", item.optString("commitment_category"))
                put("dueDate", item.optString("due_date"))
                put("priorityLevel", item.optString("priority_level"))
                put("actionTaken", item.optString("action_taken"))
                put("status", item.optString("status"))
                put("followupRequired", item.optBoolean("followup_required"))
                put("followupType", item.optString("followup_type"))
                put("followupDate", item.optString("followup_date"))
                put("followupAssignedTo", item.optString("followup_assigned_to"))
                put("remarks", item.optString("remarks"))
                put("evidence", item.optJSONArray("evidence") ?: JSONArray())
                put("evidenceTypes", JSONArray())
                put("recordedByUserId", item.optString("recorded_by_user_id"))
                put("isSystemFlagged", if (item.has("is_system_flagged")) item.optBoolean("is_system_flagged") else null)
                put("createdAt", 0)
                put("updatedAt", 0)
            }
            out.add(StakeholderCommitment.fromJson(mapped))
        }
        return out
    }

    fun postWomenTrainingSession(context: Context, s: WomenTrainingSession): Boolean {
        val url = baseUrl(context) + "/women-trainings"
        val json = JSONObject().apply {
            put("title", s.title)
            put("date", s.date)
            put("start_time", s.startTime)
            put("end_time", s.endTime)
            put("lga", s.lga)
            put("community", s.community)
            put("venue_type", s.venueType)
            put("facilitator_name", s.facilitatorName)
            put("organisation", s.organisation)
            put("total_women", s.totalWomen)
            put("age18_25", s.age18_25)
            put("age26_35", s.age26_35)
            put("age36_45", s.age36_45)
            put("age46_plus", s.age46Plus)
            put("households_represented", s.householdsRepresented)
            put("attendance_type", s.attendanceType)
            put("topics", JSONArray(s.topics))
            put("methods", JSONArray(s.methods))
            put("duration", s.duration)
            put("pretest_used", s.pretestUsed)
            put("knowledge_sorting_before", s.knowledgeSortingBefore)
            put("knowledge_sorting_after", s.knowledgeSortingAfter)
            put("categories_correct_before", s.categoriesCorrectBefore)
            put("categories_correct_after", s.categoriesCorrectAfter)
            put("burn_false_before", s.burnFalseBefore)
            put("burn_false_after", s.burnFalseAfter)
            put("confidence_avg_before", s.confidenceAvgBefore)
            put("confidence_avg_after", s.confidenceAvgAfter)
            put("importance_avg_before", s.importanceAvgBefore)
            put("importance_avg_after", s.importanceAvgAfter)
            put("committed_to_sorting", s.committedToSorting)
            put("followup_type", s.followupType)
            put("followup_date", s.followupDate)
            put("notes", s.notes)
            put("created_by_user_id", s.createdByUserId)
            put("session_code", s.sessionCode)
        }.toString()
        val body: RequestBody = json.toRequestBody("application/json; charset=utf-8".toMediaType())
        val reqBuilder = Request.Builder().url(url).post(body).addHeader("Accept", "application/json")
        val token = authToken(context)
        if (!token.isNullOrEmpty()) reqBuilder.addHeader("Authorization", "Bearer $token")
        val resp = client.newCall(reqBuilder.build()).execute()
        return resp.isSuccessful
    }

    fun fetchWomenTrainingSessions(context: Context): List<WomenTrainingSession> {
        val url = baseUrl(context) + "/women-trainings?per_page=100"
        val req = Request.Builder().url(url).get().addHeader("Accept", "application/json").build()
        val resp = client.newCall(req).execute()
        if (!resp.isSuccessful) return emptyList()
        val s = resp.body?.string().orEmpty()
        val arr = try {
            val obj = JSONObject(s)
            if (obj.has("data")) obj.getJSONArray("data") else JSONArray(s)
        } catch (_: Exception) { JSONArray() }
        val out = mutableListOf<WomenTrainingSession>()
        for (i in 0 until arr.length()) {
            val item = arr.optJSONObject(i) ?: continue
            val mapped = JSONObject().apply {
                put("id", item.optString("id"))
                put("title", item.optString("title"))
                put("date", item.optString("date"))
                put("startTime", item.optString("start_time"))
                put("endTime", item.optString("end_time"))
                put("lga", item.optString("lga"))
                put("community", item.optString("community"))
                put("venueType", item.optString("venue_type"))
                put("facilitatorName", item.optString("facilitator_name"))
                put("organisation", item.optString("organisation"))
                put("totalWomen", item.optInt("total_women"))
                put("age18_25", item.optInt("age18_25"))
                put("age26_35", item.optInt("age26_35"))
                put("age36_45", item.optInt("age36_45"))
                put("age46Plus", item.optInt("age46_plus"))
                put("householdsRepresented", item.optInt("households_represented"))
                put("attendanceType", item.optString("attendance_type"))
                put("topics", item.optJSONArray("topics") ?: JSONArray())
                put("methods", item.optJSONArray("methods") ?: JSONArray())
                put("duration", item.optString("duration"))
                put("pretestUsed", item.optBoolean("pretest_used"))
                put("knowledgeSortingBefore", item.optInt("knowledge_sorting_before"))
                put("knowledgeSortingAfter", item.optInt("knowledge_sorting_after"))
                put("categoriesCorrectBefore", item.optInt("categories_correct_before"))
                put("categoriesCorrectAfter", item.optInt("categories_correct_after"))
                put("burnFalseBefore", item.optInt("burn_false_before"))
                put("burnFalseAfter", item.optInt("burn_false_after"))
                put("confidenceAvgBefore", item.optInt("confidence_avg_before"))
                put("confidenceAvgAfter", item.optInt("confidence_avg_after"))
                put("importanceAvgBefore", item.optInt("importance_avg_before"))
                put("importanceAvgAfter", item.optInt("importance_avg_after"))
                put("committedToSorting", item.optInt("committed_to_sorting"))
                put("followupType", item.optString("followup_type"))
                put("followupDate", item.optString("followup_date"))
                put("notes", item.optString("notes"))
                put("createdByUserId", item.optString("created_by_user_id"))
                put("sessionCode", item.optString("session_code"))
                put("createdAt", 0)
                put("updatedAt", 0)
            }
            out.add(WomenTrainingSession.fromJson(mapped))
        }
        return out
    }

    fun postWastePicker(context: Context, p: WastePicker): Boolean {
        val url = baseUrl(context) + "/pickers"
        val json = JSONObject().apply {
            put("full_name", p.fullName)
            put("nickname", p.nickname)
            put("gender", p.gender)
            put("age_range", p.ageRange)
            put("phone", p.phone)
            put("id_number", p.idNumber)
            put("lga", p.lga)
            put("community", p.community)
            put("cluster_name", p.clusterName)
            put("primary_location", p.primaryLocation)
            put("waste_types", JSONArray(p.wasteTypes))
            put("years_experience", p.yearsExperience)
            put("selling_mode", p.sellingMode)
            put("income_range", p.incomeRange)
            put("ppe_usage", p.ppeUsage)
            put("had_training", p.hadTraining)
            put("training_provider", p.trainingProvider)
            put("willing_to_join", p.willingToJoin)
            put("special_needs", p.specialNeeds)
            put("photo_base64", p.photoBase64)
        }.toString()
        val body: RequestBody = json.toRequestBody("application/json; charset=utf-8".toMediaType())
        val reqBuilder = Request.Builder().url(url).post(body).addHeader("Accept", "application/json")
        val token = authToken(context)
        if (!token.isNullOrEmpty()) reqBuilder.addHeader("Authorization", "Bearer $token")
        val resp = client.newCall(reqBuilder.build()).execute()
        return resp.isSuccessful
    }

    fun postWastePickerStatus(context: Context, p: WastePicker): Int {
        val url = baseUrl(context) + "/pickers"
        val json = JSONObject().apply {
            put("full_name", p.fullName)
            put("nickname", p.nickname)
            put("gender", p.gender)
            put("age_range", p.ageRange)
            put("phone", p.phone)
            put("id_number", p.idNumber)
            put("lga", p.lga)
            put("community", p.community)
            put("cluster_name", p.clusterName)
            put("primary_location", p.primaryLocation)
            put("waste_types", JSONArray(p.wasteTypes))
            put("years_experience", p.yearsExperience)
            put("selling_mode", p.sellingMode)
            put("income_range", p.incomeRange)
            put("ppe_usage", p.ppeUsage)
            put("had_training", p.hadTraining)
            put("training_provider", p.trainingProvider)
            put("willing_to_join", p.willingToJoin)
            put("special_needs", p.specialNeeds)
            put("photo_base64", p.photoBase64)
        }.toString()
        val body: RequestBody = json.toRequestBody("application/json; charset=utf-8".toMediaType())
        val reqBuilder = Request.Builder().url(url).post(body).addHeader("Accept", "application/json")
        val token = authToken(context)
        if (!token.isNullOrEmpty()) reqBuilder.addHeader("Authorization", "Bearer $token")
        val resp = client.newCall(reqBuilder.build()).execute()
        return resp.code
    }

    fun postWastePickerResponse(context: Context, p: WastePicker): Pair<Int, String> {
        val url = baseUrl(context) + "/pickers"
        val json = JSONObject().apply {
            put("full_name", p.fullName)
            put("nickname", p.nickname)
            put("gender", p.gender)
            put("age_range", p.ageRange)
            put("phone", p.phone)
            put("id_number", p.idNumber)
            put("lga", p.lga)
            put("community", p.community)
            put("cluster_name", p.clusterName)
            put("primary_location", p.primaryLocation)
            put("waste_types", JSONArray(p.wasteTypes))
            put("years_experience", p.yearsExperience)
            put("selling_mode", p.sellingMode)
            put("income_range", p.incomeRange)
            put("ppe_usage", p.ppeUsage)
            put("had_training", p.hadTraining)
            put("training_provider", p.trainingProvider)
            put("willing_to_join", p.willingToJoin)
            put("special_needs", p.specialNeeds)
            put("photo_base64", p.photoBase64)
        }.toString()
        val body: RequestBody = json.toRequestBody("application/json; charset=utf-8".toMediaType())
        val reqBuilder = Request.Builder().url(url).post(body).addHeader("Accept", "application/json")
        val token = authToken(context)
        if (!token.isNullOrEmpty()) reqBuilder.addHeader("Authorization", "Bearer $token")
        val resp = client.newCall(reqBuilder.build()).execute()
        val status = resp.code
        val s = try { resp.body?.string().orEmpty() } catch (_: Exception) { "" }
        return status to s
    }

    fun fetchWastePickers(context: Context): List<WastePicker> {
        val url = baseUrl(context) + "/pickers?per_page=100"
        val req = Request.Builder().url(url).get().addHeader("Accept", "application/json").build()
        val resp = client.newCall(req).execute()
        if (!resp.isSuccessful) return emptyList()
        val s = resp.body?.string().orEmpty()
        val arr = try {
            val obj = JSONObject(s)
            if (obj.has("data")) obj.getJSONArray("data") else JSONArray(s)
        } catch (_: Exception) { JSONArray() }
        val out = mutableListOf<WastePicker>()
        for (i in 0 until arr.length()) {
            val item = arr.optJSONObject(i) ?: continue
            val mapped = JSONObject().apply {
                put("id", item.optString("id"))
                put("fullName", item.optString("full_name"))
                put("nickname", item.optString("nickname"))
                put("gender", item.optString("gender"))
                put("ageRange", item.optString("age_range"))
                put("phone", item.optString("phone"))
                put("idNumber", item.optString("id_number"))
                put("lga", item.optString("lga"))
                put("community", item.optString("community"))
                put("clusterName", item.optString("cluster_name"))
                put("primaryLocation", item.optString("primary_location"))
                put("wasteTypes", item.optJSONArray("waste_types") ?: JSONArray())
                put("yearsExperience", item.optString("years_experience"))
                put("sellingMode", item.optString("selling_mode"))
                put("incomeRange", item.optString("income_range"))
                put("ppeUsage", item.optString("ppe_usage"))
                put("hadTraining", item.optBoolean("had_training"))
                put("trainingProvider", item.optString("training_provider"))
                put("willingToJoin", item.optString("willing_to_join"))
                put("specialNeeds", item.optString("special_needs"))
                put("photoBase64", item.optString("photo_base64"))
                put("createdAt", 0)
            }
            out.add(WastePicker.fromJson(mapped))
        }
        return out
    }
}
