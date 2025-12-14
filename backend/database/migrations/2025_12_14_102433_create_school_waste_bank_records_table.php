<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    /**
     * Run the migrations.
     */
    public function up(): void
    {
        Schema::create('school_waste_bank_records', function (Blueprint $table) {
            $table->id();
            $table->string('school_id')->nullable();
            $table->string('school_name');
            $table->string('lga');
            $table->string('reporting_period_type');
            $table->date('reporting_date')->nullable();
            $table->date('reporting_week_start')->nullable();
            $table->string('reporting_month')->nullable();
            $table->string('status');
            $table->double('plastic_collected_kg')->nullable();
            $table->double('plastic_recycled_kg')->nullable();
            $table->double('paper_collected_kg')->nullable();
            $table->double('paper_recycled_kg')->nullable();
            $table->double('metal_collected_kg')->nullable();
            $table->double('metal_recycled_kg')->nullable();
            $table->double('glass_collected_kg')->nullable();
            $table->double('glass_recycled_kg')->nullable();
            $table->double('organic_collected_kg')->nullable();
            $table->double('organic_recycled_kg')->nullable();
            $table->string('other_type')->nullable();
            $table->double('other_collected_kg')->nullable();
            $table->double('other_recycled_kg')->nullable();
            $table->boolean('sold_to_recycler')->default(false);
            $table->double('income_from_sale')->nullable();
            $table->string('buyer_name')->nullable();
            $table->json('challenges');
            $table->integer('student_participation_level')->nullable();
            $table->text('remarks')->nullable();
            $table->json('photo_base64s');
            $table->string('recorded_by_user_id');
            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('school_waste_bank_records');
    }
};
