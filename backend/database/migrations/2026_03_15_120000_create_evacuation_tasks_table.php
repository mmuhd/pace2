<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up(): void
    {
        Schema::create('evacuation_tasks', function (Blueprint $table) {
            $table->id();
            $table->string('source_type');
            $table->string('source_name')->nullable();
            $table->string('lga');
            $table->string('address')->nullable();
            $table->string('scheduled_at')->nullable();
            $table->string('assigned_to')->nullable();
            $table->string('status')->default('pending');
            $table->double('total_kg')->nullable();
            $table->json('breakdown')->nullable();
            $table->integer('contamination_score')->nullable();
            $table->json('photo_base64s')->nullable();
            $table->double('gps_lat')->nullable();
            $table->double('gps_long')->nullable();
            $table->string('created_by_user_id')->nullable();
            $table->timestamps();
        });
    }

    public function down(): void
    {
        Schema::dropIfExists('evacuation_tasks');
    }
};
