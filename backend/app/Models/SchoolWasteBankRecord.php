<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class SchoolWasteBankRecord extends Model
{
    protected $fillable = [
        'school_id','school_name','lga','reporting_period_type','reporting_date','reporting_week_start','reporting_month','status',
        'plastic_collected_kg','plastic_recycled_kg','paper_collected_kg','paper_recycled_kg','metal_collected_kg','metal_recycled_kg',
        'glass_collected_kg','glass_recycled_kg','organic_collected_kg','organic_recycled_kg','other_type','other_collected_kg','other_recycled_kg',
        'sold_to_recycler','income_from_sale','buyer_name','challenges','student_participation_level','remarks','photo_base64s','recorded_by_user_id'
    ];

    protected $casts = [
        'reporting_date' => 'date',
        'reporting_week_start' => 'date',
        'sold_to_recycler' => 'boolean',
        'challenges' => 'array',
        'photo_base64s' => 'array',
    ];
}
