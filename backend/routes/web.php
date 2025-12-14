<?php

use Illuminate\Support\Facades\Route;
use App\Http\Controllers\WebAuthController;

Route::get('/', function () {
    return view('landing');
})->name('landing');

Route::get('/login', [WebAuthController::class, 'show'])->name('login');
Route::post('/login', [WebAuthController::class, 'login']);
Route::post('/logout', [WebAuthController::class, 'logout'])->name('logout');

Route::middleware('auth')->group(function () {
    Route::view('/home', 'home')->name('home');
    Route::view('/dashboard', 'home')->name('dashboard');
    Route::view('/dashboards', 'dashboards')->name('dashboards');
});
