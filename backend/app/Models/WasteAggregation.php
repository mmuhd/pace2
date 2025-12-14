<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class WasteAggregation extends Model
{
    protected $fillable = [
        'lga','waste_source','site_name','collection_date','team','total_waste_kg','recyclable_percentage','plastic_kg','paper_kg','metal_kg','glass_kg','organic_kg','other_kg',
        'final_disposal_site','transport_used','recyclers_involved','trip_count','vehicle_count','hours_worked','avg_load_kg','staff_count','moisture_percent','contamination_percent',
        'weather','hazardous_found','hazardous_description','challenges','remarks','photos','recorded_by_user_id','gps_lat','gps_long'
    ];

    protected $casts = [
        'collection_date' => 'date',
        'hazardous_found' => 'boolean',
        'challenges' => 'array',
        'photos' => 'array',
    ];
}
