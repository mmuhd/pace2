<?php

namespace App\Http\Controllers;

use App\Models\WasteAggregation;
use Illuminate\Http\Request;

class WasteAggregationController extends Controller
{
    /**
     * Display a listing of the resource.
     */
    public function index()
    {
        $q = WasteAggregation::query();
        if ($lga = request('lga')) $q->where('lga', $lga);
        if ($source = request('waste_source')) $q->where('waste_source', $source);
        if ($disposal = request('final_disposal_site')) $q->where('final_disposal_site', $disposal);
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
            'lga' => ['required','string','max:100'],
            'waste_source' => ['required','string','max:100'],
            'site_name' => ['required','string','max:255'],
            'collection_date' => ['required','date'],
            'team' => ['nullable','string','max:255'],
            'total_waste_kg' => ['required','numeric','min:0'],
            'recyclable_percentage' => ['required','integer','min:0','max:100'],
            'plastic_kg' => ['nullable','numeric','min:0'],
            'paper_kg' => ['nullable','numeric','min:0'],
            'metal_kg' => ['nullable','numeric','min:0'],
            'glass_kg' => ['nullable','numeric','min:0'],
            'organic_kg' => ['nullable','numeric','min:0'],
            'other_kg' => ['nullable','numeric','min:0'],
            'final_disposal_site' => ['required','string','max:255'],
            'transport_used' => ['nullable','string','max:100'],
            'recyclers_involved' => ['nullable','string','max:255'],
            'trip_count' => ['nullable','integer','min:0'],
            'vehicle_count' => ['nullable','integer','min:0'],
            'hours_worked' => ['nullable','numeric','min:0'],
            'avg_load_kg' => ['nullable','numeric','min:0'],
            'staff_count' => ['nullable','integer','min:0'],
            'moisture_percent' => ['nullable','integer','min:0','max:100'],
            'contamination_percent' => ['nullable','integer','min:0','max:100'],
            'weather' => ['nullable','string','max:100'],
            'hazardous_found' => ['sometimes','boolean'],
            'hazardous_description' => ['nullable','string','max:255'],
            'challenges' => ['required','array'],'challenges.*' => ['string'],
            'remarks' => ['nullable','string'],
            'photos' => ['required','array'],'photos.*' => ['string'],
            'recorded_by_user_id' => ['required','string','max:100'],
            'gps_lat' => ['nullable','numeric'],
            'gps_long' => ['nullable','numeric'],
        ]);
        $rec = WasteAggregation::create($validated);
        return response()->json($rec, 201);
    }

    /**
     * Display the specified resource.
     */
    public function show(WasteAggregation $wasteAggregation)
    {
        return response()->json($wasteAggregation);
    }

    /**
     * Update the specified resource in storage.
     */
    public function update(Request $request, WasteAggregation $wasteAggregation)
    {
        $validated = $request->validate([
            'lga' => ['sometimes','string','max:100'],
            'waste_source' => ['sometimes','string','max:100'],
            'site_name' => ['sometimes','string','max:255'],
            'collection_date' => ['sometimes','date'],
            'team' => ['nullable','string','max:255'],
            'total_waste_kg' => ['sometimes','numeric','min:0'],
            'recyclable_percentage' => ['sometimes','integer','min:0','max:100'],
            'plastic_kg' => ['nullable','numeric','min:0'],
            'paper_kg' => ['nullable','numeric','min:0'],
            'metal_kg' => ['nullable','numeric','min:0'],
            'glass_kg' => ['nullable','numeric','min:0'],
            'organic_kg' => ['nullable','numeric','min:0'],
            'other_kg' => ['nullable','numeric','min:0'],
            'final_disposal_site' => ['sometimes','string','max:255'],
            'transport_used' => ['nullable','string','max:100'],
            'recyclers_involved' => ['nullable','string','max:255'],
            'trip_count' => ['nullable','integer','min:0'],
            'vehicle_count' => ['nullable','integer','min:0'],
            'hours_worked' => ['nullable','numeric','min:0'],
            'avg_load_kg' => ['nullable','numeric','min:0'],
            'staff_count' => ['nullable','integer','min:0'],
            'moisture_percent' => ['nullable','integer','min:0','max:100'],
            'contamination_percent' => ['nullable','integer','min:0','max:100'],
            'weather' => ['nullable','string','max:100'],
            'hazardous_found' => ['sometimes','boolean'],
            'hazardous_description' => ['nullable','string','max:255'],
            'challenges' => ['nullable','array'],'challenges.*' => ['string'],
            'remarks' => ['nullable','string'],
            'photos' => ['nullable','array'],'photos.*' => ['string'],
            'recorded_by_user_id' => ['sometimes','string','max:100'],
            'gps_lat' => ['nullable','numeric'],
            'gps_long' => ['nullable','numeric'],
        ]);
        $wasteAggregation->update($validated);
        return response()->json($wasteAggregation);
    }

    /**
     * Remove the specified resource from storage.
     */
    public function destroy(WasteAggregation $wasteAggregation)
    {
        $wasteAggregation->delete();
        return response()->json(['deleted'=>true]);
    }
}
