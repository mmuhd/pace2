@php($title = 'Login')
<x-layouts.app :title="$title">
    <div class="card" style="max-width:480px;margin:0 auto">
        <h2 style="margin:0 0 12px 0">Login</h2>
        <form action="/login" method="POST">
            @csrf
            <div style="margin-bottom:12px">
                <label for="email">Email</label>
                <input id="email" name="email" type="email" required value="{{ old('email') }}" />
            </div>
            <div style="margin-bottom:12px">
                <label for="password">Password</label>
                <input id="password" name="password" type="password" required />
            </div>
            @if ($errors->any())
                <div style="color:#c0392b;margin-bottom:8px">{{ $errors->first() }}</div>
            @endif
            <button class="btn" type="submit">Login</button>
        </form>
    </div>
</x-layouts.app>
