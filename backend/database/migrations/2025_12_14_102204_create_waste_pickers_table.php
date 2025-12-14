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
        Schema::create('waste_pickers', function (Blueprint $table) {
            $table->id();
            $table->string('full_name');
            $table->string('nickname')->nullable();
            $table->string('gender');
            $table->string('age_range');
            $table->string('phone')->nullable();
            $table->string('id_number')->nullable();
            $table->string('lga');
            $table->string('community');
            $table->string('cluster_name')->nullable();
            $table->string('primary_location');
            $table->json('waste_types');
            $table->string('years_experience');
            $table->string('selling_mode')->nullable();
            $table->string('income_range')->nullable();
            $table->string('ppe_usage');
            $table->boolean('had_training')->default(false);
            $table->string('training_provider')->nullable();
            $table->string('willing_to_join');
            $table->string('special_needs')->nullable();
            $table->text('photo_base64')->nullable();
            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('waste_pickers');
    }
};
