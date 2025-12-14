<?php

namespace App\Http\Controllers;

use App\Models\StakeholderCommitment;
use Illuminate\Http\Request;

class StakeholderCommitmentController extends Controller
{
    /**
     * Display a listing of the resource.
     */
    public function index()
    {
        $q = StakeholderCommitment::query();

        // Filters: lga, status, date range, overdue
        if ($lga = request('lga')) {
            $q->where(function ($qq) use ($lga) {
                $qq->where('lga', $lga)->orWhere('lga_id', $lga);
            });
        }
        if ($status = request('status')) {
            $q->where('status', $status);
        }
        $from = request('from');
        $to = request('to');
        if ($from) {
            $q->whereDate('engagement_date', '>=', $from);
        }
        if ($to) {
            $q->whereDate('engagement_date', '<=', $to);
        }
        if (request()->boolean('overdue')) {
            $q->whereNotNull('due_date')
              ->whereDate('due_date', '<', now()->toDateString())
              ->where('status', '!=', 'Completed');
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
            'stakeholder_name' => ['required', 'string', 'max:255'],
            'stakeholder_type' => ['required', 'string', 'max:100'],
            'lga_id' => ['nullable', 'integer'],
            'lga' => ['nullable', 'string', 'max:100'],
            'contact_person' => ['nullable', 'string', 'max:255'],
            'phone' => ['nullable', 'string', 'max:50'],

            'engagement_date' => ['required', 'date'],
            'engagement_type' => ['required', 'string', 'max:100'],
            'engaged_by_user_id' => ['nullable', 'integer', 'exists:users,id'],
            'engagement_description' => ['nullable', 'string'],

            'commitment_text' => ['required', 'string'],
            'commitment_category' => ['nullable', 'string', 'max:100'],
            'due_date' => ['nullable', 'date'],
            'priority_level' => ['nullable', 'string', 'max:20'],

            'action_taken' => ['nullable', 'string'],
            'status' => ['required', 'string', 'max:30'],
            'followup_required' => ['sometimes', 'boolean'],
            'followup_type' => ['nullable', 'string', 'max:100'],
            'followup_date' => ['nullable', 'date'],
            'followup_assigned_to' => ['nullable', 'string', 'max:255'],
            'remarks' => ['nullable', 'string'],

            'evidence' => ['nullable', 'array'],
            'evidence.*' => ['string'],
            'recorded_by_user_id' => ['nullable', 'integer', 'exists:users,id'],
        ]);

        $rec = StakeholderCommitment::create($validated);
        return response()->json($rec, 201);
    }

    /**
     * Display the specified resource.
     */
    public function show(StakeholderCommitment $stakeholderCommitment)
    {
        return response()->json($stakeholderCommitment);
    }

    /**
     * Update the specified resource in storage.
     */
    public function update(Request $request, StakeholderCommitment $stakeholderCommitment)
    {
        $validated = $request->validate([
            'stakeholder_name' => ['sometimes', 'string', 'max:255'],
            'stakeholder_type' => ['sometimes', 'string', 'max:100'],
            'lga_id' => ['nullable', 'integer'],
            'lga' => ['nullable', 'string', 'max:100'],
            'contact_person' => ['nullable', 'string', 'max:255'],
            'phone' => ['nullable', 'string', 'max:50'],

            'engagement_date' => ['sometimes', 'date'],
            'engagement_type' => ['sometimes', 'string', 'max:100'],
            'engaged_by_user_id' => ['nullable', 'integer', 'exists:users,id'],
            'engagement_description' => ['nullable', 'string'],

            'commitment_text' => ['sometimes', 'string'],
            'commitment_category' => ['nullable', 'string', 'max:100'],
            'due_date' => ['nullable', 'date'],
            'priority_level' => ['nullable', 'string', 'max:20'],

            'action_taken' => ['nullable', 'string'],
            'status' => ['sometimes', 'string', 'max:30'],
            'followup_required' => ['sometimes', 'boolean'],
            'followup_type' => ['nullable', 'string', 'max:100'],
            'followup_date' => ['nullable', 'date'],
            'followup_assigned_to' => ['nullable', 'string', 'max:255'],
            'remarks' => ['nullable', 'string'],

            'evidence' => ['nullable', 'array'],
            'evidence.*' => ['string'],
            'recorded_by_user_id' => ['nullable', 'integer', 'exists:users,id'],
            'is_system_flagged' => ['nullable', 'boolean'],
        ]);

        $stakeholderCommitment->update($validated);
        return response()->json($stakeholderCommitment);
    }

    /**
     * Remove the specified resource from storage.
     */
    public function destroy(StakeholderCommitment $stakeholderCommitment)
    {
        $stakeholderCommitment->delete();
        return response()->json(['deleted' => true]);
    }
}
