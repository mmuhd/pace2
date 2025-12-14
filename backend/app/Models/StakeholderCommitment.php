<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class StakeholderCommitment extends Model
{
    protected $fillable = [
        'stakeholder_name',
        'stakeholder_type',
        'lga_id',
        'lga',
        'contact_person',
        'phone',
        'engagement_date',
        'engagement_type',
        'engaged_by_user_id',
        'engagement_description',
        'commitment_text',
        'commitment_category',
        'due_date',
        'priority_level',
        'action_taken',
        'status',
        'followup_required',
        'followup_type',
        'followup_date',
        'followup_assigned_to',
        'remarks',
        'evidence',
        'recorded_by_user_id',
        'is_system_flagged',
    ];

    protected $casts = [
        'engagement_date' => 'date',
        'due_date' => 'date',
        'followup_date' => 'date',
        'followup_required' => 'boolean',
        'is_system_flagged' => 'boolean',
        'evidence' => 'array',
    ];
}
