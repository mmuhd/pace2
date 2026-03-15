<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class EvacuationTask extends Model
{
    protected $fillable = [
        'source_type',
        'source_name',
        'lga',
        'address',
        'scheduled_at',
        'assigned_to',
        'status',
        'total_kg',
        'breakdown',
        'contamination_score',
        'photo_base64s',
        'gps_lat',
        'gps_long',
        'created_by_user_id',
    ];

    protected $casts = [
        'breakdown' => 'array',
        'photo_base64s' => 'array',
        'total_kg' => 'float',
        'gps_lat' => 'float',
        'gps_long' => 'float',
        'contamination_score' => 'int',
    ];
}
