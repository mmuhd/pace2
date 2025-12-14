<?php

namespace App\Http\Controllers;

use App\Models\WastePicker;
use Illuminate\Http\Request;

class WastePickerController extends Controller
{
    /**
     * Display a listing of the resource.
     */
    public function index()
    {
        $q = WastePicker::query();
        if ($lga = request('lga')) $q->where('lga', $lga);
        if ($cluster = request('cluster_name')) $q->where('cluster_name', $cluster);
        if ($ppe = request('ppe_usage')) $q->where('ppe_usage', $ppe);
        if ($willing = request('willing_to_join')) $q->where('willing_to_join', $willing);
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
            'full_name' => ['required','string','max:255'],
            'nickname' => ['nullable','string','max:255'],
            'gender' => ['required','string','max:30'],
            'age_range' => ['required','string','max:50'],
            'phone' => ['nullable','string','max:50'],
            'id_number' => ['nullable','string','max:100'],
            'lga' => ['required','string','max:100'],
            'community' => ['required','string','max:100'],
            'cluster_name' => ['nullable','string','max:100'],
            'primary_location' => ['required','string','max:100'],
            'waste_types' => ['required','array'],
            'waste_types.*' => ['string'],
            'years_experience' => ['required','string','max:50'],
            'selling_mode' => ['nullable','string','max:50'],
            'income_range' => ['nullable','string','max:50'],
            'ppe_usage' => ['required','string','max:50'],
            'had_training' => ['sometimes','boolean'],
            'training_provider' => ['nullable','string','max:255'],
            'willing_to_join' => ['required','string','max:50'],
            'special_needs' => ['nullable','string','max:255'],
            'photo_base64' => ['nullable','string'],
        ]);
        $picker = WastePicker::create($validated);
        return response()->json($picker, 201);
    }

    /**
     * Display the specified resource.
     */
    public function show(WastePicker $wastePicker)
    {
        return response()->json($wastePicker);
    }

    /**
     * Update the specified resource in storage.
     */
    public function update(Request $request, WastePicker $wastePicker)
    {
        $validated = $request->validate([
            'full_name' => ['sometimes','string','max:255'],
            'nickname' => ['nullable','string','max:255'],
            'gender' => ['sometimes','string','max:30'],
            'age_range' => ['sometimes','string','max:50'],
            'phone' => ['nullable','string','max:50'],
            'id_number' => ['nullable','string','max:100'],
            'lga' => ['sometimes','string','max:100'],
            'community' => ['sometimes','string','max:100'],
            'cluster_name' => ['nullable','string','max:100'],
            'primary_location' => ['sometimes','string','max:100'],
            'waste_types' => ['nullable','array'],
            'waste_types.*' => ['string'],
            'years_experience' => ['sometimes','string','max:50'],
            'selling_mode' => ['nullable','string','max:50'],
            'income_range' => ['nullable','string','max:50'],
            'ppe_usage' => ['sometimes','string','max:50'],
            'had_training' => ['sometimes','boolean'],
            'training_provider' => ['nullable','string','max:255'],
            'willing_to_join' => ['sometimes','string','max:50'],
            'special_needs' => ['nullable','string','max:255'],
            'photo_base64' => ['nullable','string'],
        ]);
        $wastePicker->update($validated);
        return response()->json($wastePicker);
    }

    /**
     * Remove the specified resource from storage.
     */
    public function destroy(WastePicker $wastePicker)
    {
        $wastePicker->delete();
        return response()->json(['deleted' => true]);
    }
}
