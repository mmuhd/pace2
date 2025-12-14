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
        Schema::create('women_training_sessions', function (Blueprint $table) {
            $table->id();
            $table->string('title');
            $table->date('date');
            $table->string('start_time')->nullable();
            $table->string('end_time')->nullable();
            $table->string('lga');
            $table->string('community');
            $table->string('venue_type');
            $table->string('facilitator_name');
            $table->string('organisation')->nullable();
            $table->integer('total_women');
            $table->integer('age18_25')->nullable();
            $table->integer('age26_35')->nullable();
            $table->integer('age36_45')->nullable();
            $table->integer('age46_plus')->nullable();
            $table->integer('households_represented')->nullable();
            $table->string('attendance_type')->nullable();
            $table->json('topics');
            $table->json('methods');
            $table->string('duration');
            $table->boolean('pretest_used')->default(false);
            $table->integer('knowledge_sorting_before')->nullable();
            $table->integer('knowledge_sorting_after')->nullable();
            $table->integer('categories_correct_before')->nullable();
            $table->integer('categories_correct_after')->nullable();
            $table->integer('burn_false_before')->nullable();
            $table->integer('burn_false_after')->nullable();
            $table->integer('confidence_avg_before')->nullable();
            $table->integer('confidence_avg_after')->nullable();
            $table->integer('importance_avg_before')->nullable();
            $table->integer('importance_avg_after')->nullable();
            $table->integer('committed_to_sorting');
            $table->string('followup_type')->nullable();
            $table->date('followup_date')->nullable();
            $table->text('notes')->nullable();
            $table->string('created_by_user_id');
            $table->string('session_code');
            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('women_training_sessions');
    }
};
