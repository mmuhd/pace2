<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class WastePicker extends Model
{
    protected $fillable = [
        'full_name',
        'nickname',
        'gender',
        'age_range',
        'phone',
        'id_number',
        'lga',
        'community',
        'cluster_name',
        'primary_location',
        'waste_types',
        'years_experience',
        'selling_mode',
        'income_range',
        'ppe_usage',
        'had_training',
        'training_provider',
        'willing_to_join',
        'special_needs',
        'photo_base64',
    ];

    protected $casts = [
        'waste_types' => 'array',
        'had_training' => 'boolean',
    ];
}
