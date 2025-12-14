<?php

namespace App\Http\Controllers;

use App\Models\SchoolWasteBankRecord;
use Illuminate\Http\Request;

class SchoolWasteBankRecordController extends Controller
{
    /**
     * Display a listing of the resource.
     */
    public function index()
    {
        $q = SchoolWasteBankRecord::query();
        if ($lga = request('lga')) $q->where('lga', $lga);
        if ($school = request('school_name')) $q->where('school_name', 'like', "%$school%");
        if ($status = request('status')) $q->where('status', $status);
        $from = request('from'); $to = request('to');
        if ($from) $q->whereDate('created_at', '>=', $from);
        if ($to) $q->whereDate('created_at', '<=', $to);
        $perPage = (int) (request('per_page', 20));
        $data = $q->orderByDesc('created_at')->paginate($perPage);
        return response()->json($data);
    }

    /**
     * Store a newly created resource in storage.
     */
    public function store(Request $request)
    {
        $validated = $request->validate([
            'school_id' => ['nullable','string','max:100'],
            'school_name' => ['required','string','max:255'],
            'lga' => ['required','string','max:100'],
            'reporting_period_type' => ['required','string','max:50'],
            'reporting_date' => ['nullable','date'],
            'reporting_week_start' => ['nullable','date'],
            'reporting_month' => ['nullable','string','max:20'],
            'status' => ['required','string','max:50'],
            'plastic_collected_kg' => ['nullable','numeric','min:0'],
            'plastic_recycled_kg' => ['nullable','numeric','min:0'],
            'paper_collected_kg' => ['nullable','numeric','min:0'],
            'paper_recycled_kg' => ['nullable','numeric','min:0'],
            'metal_collected_kg' => ['nullable','numeric','min:0'],
            'metal_recycled_kg' => ['nullable','numeric','min:0'],
            'glass_collected_kg' => ['nullable','numeric','min:0'],
            'glass_recycled_kg' => ['nullable','numeric','min:0'],
            'organic_collected_kg' => ['nullable','numeric','min:0'],
            'organic_recycled_kg' => ['nullable','numeric','min:0'],
            'other_type' => ['nullable','string','max:50'],
            'other_collected_kg' => ['nullable','numeric','min:0'],
            'other_recycled_kg' => ['nullable','numeric','min:0'],
            'sold_to_recycler' => ['sometimes','boolean'],
            'income_from_sale' => ['nullable','numeric','min:0'],
            'buyer_name' => ['nullable','string','max:255'],
            'challenges' => ['required','array'], 'challenges.*' => ['string'],
            'student_participation_level' => ['nullable','integer','min:0'],
            'remarks' => ['nullable','string'],
            'photo_base64s' => ['required','array'], 'photo_base64s.*' => ['string'],
            'recorded_by_user_id' => ['required','string','max:100'],
        ]);
        $rec = SchoolWasteBankRecord::create($validated);
        return response()->json($rec, 201);
    }

    /**
     * Display the specified resource.
     */
    public function show(SchoolWasteBankRecord $schoolWasteBankRecord)
    {
        return response()->json($schoolWasteBankRecord);
    }

    /**
     * Update the specified resource in storage.
     */
    public function update(Request $request, SchoolWasteBankRecord $schoolWasteBankRecord)
    {
        $validated = $request->validate([
            'school_id' => ['nullable','string','max:100'],
            'school_name' => ['sometimes','string','max:255'],
            'lga' => ['sometimes','string','max:100'],
            'reporting_period_type' => ['sometimes','string','max:50'],
            'reporting_date' => ['nullable','date'],
            'reporting_week_start' => ['nullable','date'],
            'reporting_month' => ['nullable','string','max:20'],
            'status' => ['sometimes','string','max:50'],
            'plastic_collected_kg' => ['nullable','numeric','min:0'],
            'plastic_recycled_kg' => ['nullable','numeric','min:0'],
            'paper_collected_kg' => ['nullable','numeric','min:0'],
            'paper_recycled_kg' => ['nullable','numeric','min:0'],
            'metal_collected_kg' => ['nullable','numeric','min:0'],
            'metal_recycled_kg' => ['nullable','numeric','min:0'],
            'glass_collected_kg' => ['nullable','numeric','min:0'],
            'glass_recycled_kg' => ['nullable','numeric','min:0'],
            'organic_collected_kg' => ['nullable','numeric','min:0'],
            'organic_recycled_kg' => ['nullable','numeric','min:0'],
            'other_type' => ['nullable','string','max:50'],
            'other_collected_kg' => ['nullable','numeric','min:0'],
            'other_recycled_kg' => ['nullable','numeric','min:0'],
            'sold_to_recycler' => ['sometimes','boolean'],
            'income_from_sale' => ['nullable','numeric','min:0'],
            'buyer_name' => ['nullable','string','max:255'],
            'challenges' => ['nullable','array'], 'challenges.*' => ['string'],
            'student_participation_level' => ['nullable','integer','min:0'],
            'remarks' => ['nullable','string'],
            'photo_base64s' => ['nullable','array'], 'photo_base64s.*' => ['string'],
            'recorded_by_user_id' => ['sometimes','string','max:100'],
        ]);
        $schoolWasteBankRecord->update($validated);
        return response()->json($schoolWasteBankRecord);
    }

    /**
     * Remove the specified resource from storage.
     */
    public function destroy(SchoolWasteBankRecord $schoolWasteBankRecord)
    {
        $schoolWasteBankRecord->delete();
        return response()->json(['deleted'=>true]);
    }
}
