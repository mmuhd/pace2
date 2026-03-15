<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\EvacuationTask;

class EvacuationController extends Controller
{
    public function index()
    {
        $q = EvacuationTask::query();
        if ($lga = request('lga')) $q->where('lga', $lga);
        if ($status = request('status')) $q->where('status', $status);
        $perPage = (int) (request('per_page', 20));
        $data = $q->orderByDesc('created_at')->paginate($perPage);
        return response()->json($data);
    }

    public function store(Request $request)
    {
        $validated = $request->validate([
            'source_type' => ['required','string','max:100'],
            'source_name' => ['nullable','string','max:255'],
            'lga' => ['required','string','max:100'],
            'address' => ['nullable','string','max:255'],
            'scheduled_at' => ['nullable','string','max:255'],
            'assigned_to' => ['nullable','string','max:255'],
            'status' => ['nullable','string','max:50'],
            'total_kg' => ['nullable','numeric'],
            'breakdown' => ['nullable','array'],
            'breakdown.*.material' => ['string'],
            'breakdown.*.kg' => ['numeric'],
            'contamination_score' => ['nullable','integer','min:0','max:100'],
            'photo_base64s' => ['nullable','array'],
            'photo_base64s.*' => ['string'],
            'gps_lat' => ['nullable','numeric'],
            'gps_long' => ['nullable','numeric'],
            'created_by_user_id' => ['nullable','string','max:255'],
        ]);
        $task = EvacuationTask::create($validated);
        return response()->json($task, 201);
    }

    public function show(EvacuationTask $evacuationTask)
    {
        return response()->json($evacuationTask);
    }
}
