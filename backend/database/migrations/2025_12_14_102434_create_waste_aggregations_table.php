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
        Schema::create('waste_aggregations', function (Blueprint $table) {
            $table->id();
            $table->string('lga');
            $table->string('waste_source');
            $table->string('site_name');
            $table->date('collection_date');
            $table->string('team')->nullable();
            $table->double('total_waste_kg');
            $table->integer('recyclable_percentage');
            $table->double('plastic_kg')->nullable();
            $table->double('paper_kg')->nullable();
            $table->double('metal_kg')->nullable();
            $table->double('glass_kg')->nullable();
            $table->double('organic_kg')->nullable();
            $table->double('other_kg')->nullable();
            $table->string('final_disposal_site');
            $table->string('transport_used')->nullable();
            $table->string('recyclers_involved')->nullable();
            $table->integer('trip_count')->nullable();
            $table->integer('vehicle_count')->nullable();
            $table->double('hours_worked')->nullable();
            $table->double('avg_load_kg')->nullable();
            $table->integer('staff_count')->nullable();
            $table->integer('moisture_percent')->nullable();
            $table->integer('contamination_percent')->nullable();
            $table->string('weather')->nullable();
            $table->boolean('hazardous_found')->default(false);
            $table->string('hazardous_description')->nullable();
            $table->json('challenges');
            $table->text('remarks')->nullable();
            $table->json('photos');
            $table->string('recorded_by_user_id');
            $table->double('gps_lat')->nullable();
            $table->double('gps_long')->nullable();
            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('waste_aggregations');
    }
};
