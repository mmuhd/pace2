<?php

use Illuminate\Support\Facades\Route;
use App\Http\Controllers\StakeholderCommitmentController;
use App\Http\Controllers\WastePickerController;
use App\Http\Controllers\WomenTrainingSessionController;
use App\Http\Controllers\SchoolWasteBankRecordController;
use App\Http\Controllers\WasteAggregationController;
use App\Http\Controllers\EvacuationController;
use App\Http\Controllers\ReportController;
use App\Http\Controllers\AuthController;

Route::middleware('api')->group(function () {
    Route::apiResource('commitments', StakeholderCommitmentController::class)->only(['index','show']);
    Route::apiResource('pickers', WastePickerController::class)->only(['index','show']);
    Route::apiResource('women-trainings', WomenTrainingSessionController::class)->only(['index','show']);
    Route::apiResource('schools', SchoolWasteBankRecordController::class)->only(['index','show']);
    Route::apiResource('aggregations', WasteAggregationController::class)->only(['index','show']);
    Route::apiResource('evacuations', EvacuationController::class)->only(['index','show']);
    Route::get('reports/overview', [ReportController::class, 'overview']);
    Route::post('auth/login', [AuthController::class, 'login']);
    Route::post('auth/register', [AuthController::class, 'register']);
    Route::middleware('auth:sanctum')->group(function () {
        Route::get('auth/me', [AuthController::class, 'me']);
        Route::post('auth/logout', [AuthController::class, 'logout']);

        // Protect mutations; keep reads open if desired
        Route::apiResource('commitments', StakeholderCommitmentController::class)->only(['store','update','destroy']);
        Route::apiResource('pickers', WastePickerController::class)->only(['store','update','destroy']);
        Route::apiResource('women-trainings', WomenTrainingSessionController::class)->only(['store','update','destroy']);
        Route::apiResource('schools', SchoolWasteBankRecordController::class)->only(['store','update','destroy']);
        Route::apiResource('aggregations', WasteAggregationController::class)->only(['store','update','destroy']);
        Route::apiResource('evacuations', EvacuationController::class)->only(['store','update','destroy']);
    });
});
