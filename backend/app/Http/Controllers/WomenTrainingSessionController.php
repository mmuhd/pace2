<?php

namespace App\Http\Controllers;

use App\Models\WomenTrainingSession;
use Illuminate\Http\Request;

class WomenTrainingSessionController extends Controller
{
    /**
     * Display a listing of the resource.
     */
    public function index()
    {
        $q = WomenTrainingSession::query();
        if ($lga = request('lga')) $q->where('lga', $lga);
        if ($venue = request('venue_type')) $q->where('venue_type', $venue);
        $from = request('from'); $to = request('to');
        if ($from) $q->whereDate('date', '>=', $from);
        if ($to) $q->whereDate('date', '<=', $to);
        if ($follow = request('followup_type')) {
            if ($follow === 'None') $q->whereNull('followup_type'); else $q->where('followup_type', $follow);
        }
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
            'title' => ['required','string','max:255'],
            'date' => ['required','date'],
            'start_time' => ['nullable','string','max:20'],
            'end_time' => ['nullable','string','max:20'],
            'lga' => ['required','string','max:100'],
            'community' => ['required','string','max:100'],
            'venue_type' => ['required','string','max:100'],
            'facilitator_name' => ['required','string','max:255'],
            'organisation' => ['nullable','string','max:255'],
            'total_women' => ['required','integer','min:0'],
            'age18_25' => ['nullable','integer','min:0'],
            'age26_35' => ['nullable','integer','min:0'],
            'age36_45' => ['nullable','integer','min:0'],
            'age46_plus' => ['nullable','integer','min:0'],
            'households_represented' => ['nullable','integer','min:0'],
            'attendance_type' => ['nullable','string','max:50'],
            'topics' => ['required','array'],
            'topics.*' => ['string'],
            'methods' => ['required','array'],
            'methods.*' => ['string'],
            'duration' => ['required','string','max:50'],
            'pretest_used' => ['sometimes','boolean'],
            'knowledge_sorting_before' => ['nullable','integer','min:0'],
            'knowledge_sorting_after' => ['nullable','integer','min:0'],
            'categories_correct_before' => ['nullable','integer','min:0'],
            'categories_correct_after' => ['nullable','integer','min:0'],
            'burn_false_before' => ['nullable','integer','min:0'],
            'burn_false_after' => ['nullable','integer','min:0'],
            'confidence_avg_before' => ['nullable','integer','min:0'],
            'confidence_avg_after' => ['nullable','integer','min:0'],
            'importance_avg_before' => ['nullable','integer','min:0'],
            'importance_avg_after' => ['nullable','integer','min:0'],
            'committed_to_sorting' => ['required','integer','min:0'],
            'followup_type' => ['nullable','string','max:100'],
            'followup_date' => ['nullable','date'],
            'notes' => ['nullable','string'],
            'created_by_user_id' => ['required','string','max:100'],
            'session_code' => ['required','string','max:50'],
        ]);
        $s = WomenTrainingSession::create($validated);
        return response()->json($s, 201);
    }

    /**
     * Display the specified resource.
     */
    public function show(WomenTrainingSession $womenTrainingSession)
    {
        return response()->json($womenTrainingSession);
    }

    /**
     * Update the specified resource in storage.
     */
    public function update(Request $request, WomenTrainingSession $womenTrainingSession)
    {
        $validated = $request->validate([
            'title' => ['sometimes','string','max:255'],
            'date' => ['sometimes','date'],
            'start_time' => ['nullable','string','max:20'],
            'end_time' => ['nullable','string','max:20'],
            'lga' => ['sometimes','string','max:100'],
            'community' => ['sometimes','string','max:100'],
            'venue_type' => ['sometimes','string','max:100'],
            'facilitator_name' => ['sometimes','string','max:255'],
            'organisation' => ['nullable','string','max:255'],
            'total_women' => ['sometimes','integer','min:0'],
            'age18_25' => ['nullable','integer','min:0'],
            'age26_35' => ['nullable','integer','min:0'],
            'age36_45' => ['nullable','integer','min:0'],
            'age46_plus' => ['nullable','integer','min:0'],
            'households_represented' => ['nullable','integer','min:0'],
            'attendance_type' => ['nullable','string','max:50'],
            'topics' => ['nullable','array'], 'topics.*' => ['string'],
            'methods' => ['nullable','array'], 'methods.*' => ['string'],
            'duration' => ['sometimes','string','max:50'],
            'pretest_used' => ['sometimes','boolean'],
            'knowledge_sorting_before' => ['nullable','integer','min:0'],
            'knowledge_sorting_after' => ['nullable','integer','min:0'],
            'categories_correct_before' => ['nullable','integer','min:0'],
            'categories_correct_after' => ['nullable','integer','min:0'],
            'burn_false_before' => ['nullable','integer','min:0'],
            'burn_false_after' => ['nullable','integer','min:0'],
            'confidence_avg_before' => ['nullable','integer','min:0'],
            'confidence_avg_after' => ['nullable','integer','min:0'],
            'importance_avg_before' => ['nullable','integer','min:0'],
            'importance_avg_after' => ['nullable','integer','min:0'],
            'committed_to_sorting' => ['sometimes','integer','min:0'],
            'followup_type' => ['nullable','string','max:100'],
            'followup_date' => ['nullable','date'],
            'notes' => ['nullable','string'],
            'created_by_user_id' => ['sometimes','string','max:100'],
            'session_code' => ['sometimes','string','max:50'],
        ]);
        $womenTrainingSession->update($validated);
        return response()->json($womenTrainingSession);
    }

    /**
     * Remove the specified resource from storage.
     */
    public function destroy(WomenTrainingSession $womenTrainingSession)
    {
        $womenTrainingSession->delete();
        return response()->json(['deleted'=>true]);
    }
}
