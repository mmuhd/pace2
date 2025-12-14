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
        Schema::create('stakeholder_commitments', function (Blueprint $table) {
            $table->id();
            $table->string('stakeholder_name');
            $table->string('stakeholder_type');
            $table->unsignedBigInteger('lga_id')->nullable();
            $table->string('lga')->nullable();
            $table->string('contact_person')->nullable();
            $table->string('phone')->nullable();

            $table->date('engagement_date');
            $table->string('engagement_type');
            $table->foreignId('engaged_by_user_id')->nullable()->constrained('users')->nullOnDelete();
            $table->text('engagement_description')->nullable();

            $table->text('commitment_text');
            $table->string('commitment_category')->nullable();
            $table->date('due_date')->nullable();
            $table->string('priority_level')->nullable();

            $table->text('action_taken')->nullable();
            $table->string('status');
            $table->boolean('followup_required')->default(false);
            $table->string('followup_type')->nullable();
            $table->date('followup_date')->nullable();
            $table->string('followup_assigned_to')->nullable();
            $table->text('remarks')->nullable();

            $table->json('evidence')->nullable();
            $table->foreignId('recorded_by_user_id')->nullable()->constrained('users')->nullOnDelete();
            $table->boolean('is_system_flagged')->default(false);

            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('stakeholder_commitments');
    }
};
