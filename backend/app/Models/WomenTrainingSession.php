<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class WomenTrainingSession extends Model
{
    protected $fillable = [
        'title','date','start_time','end_time','lga','community','venue_type','facilitator_name','organisation','total_women',
        'age18_25','age26_35','age36_45','age46_plus','households_represented','attendance_type','topics','methods','duration',
        'pretest_used','knowledge_sorting_before','knowledge_sorting_after','categories_correct_before','categories_correct_after',
        'burn_false_before','burn_false_after','confidence_avg_before','confidence_avg_after','importance_avg_before','importance_avg_after',
        'committed_to_sorting','followup_type','followup_date','notes','created_by_user_id','session_code'
    ];

    protected $casts = [
        'date' => 'date',
        'topics' => 'array',
        'methods' => 'array',
        'pretest_used' => 'boolean',
        'followup_date' => 'date',
    ];
}
