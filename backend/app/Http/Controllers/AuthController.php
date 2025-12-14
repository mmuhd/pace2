<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Hash;
use App\Models\User;

class AuthController extends Controller
{
    public function register(Request $request)
    {
        $validated = $request->validate([
            'name' => ['required','string','max:255'],
            'phone' => ['required','string','max:50','unique:users,phone'],
            'password' => ['required','string','min:6'],
            'lga' => ['nullable','string','max:100'],
            'role' => ['required','string','max:100'],
            'email' => ['nullable','email','max:255'],
            'invitation_code' => ['nullable','string','max:100'],
        ]);

        $email = $validated['email'] ?? ($validated['phone'].'@user.local');

        $user = new \App\Models\User();
        $user->name = $validated['name'];
        $user->email = $email; // keep unique non-null
        $user->phone = $validated['phone'];
        $user->lga = $validated['lga'] ?? null;
        $user->role = $validated['role'];
        $user->password = Hash::make($validated['password']);
        $user->save();

        $token = $user->createToken('mobile')->plainTextToken;
        return response()->json(['token' => $token, 'user' => $user]);
    }

    public function login(Request $request)
    {
        if ($request->has('phone')) {
            $request->validate([
                'phone' => ['required','string'],
                'password' => ['required','string'],
            ]);
            $user = User::where('phone', $request->phone)->first();
        } else {
            $request->validate([
                'email' => ['required','email'],
                'password' => ['required','string'],
            ]);
            $user = User::where('email', $request->email)->first();
        }
        if (!$user || !Hash::check($request->password, $user->password)) {
            return response()->json(['message' => 'Invalid credentials'], 422);
        }
        $token = $user->createToken('mobile')->plainTextToken;
        return response()->json(['token' => $token, 'user' => $user]);
    }

    public function me(Request $request)
    {
        return response()->json($request->user());
    }

    public function logout(Request $request)
    {
        $request->user()->currentAccessToken()->delete();
        return response()->json(['logged_out' => true]);
    }
}
