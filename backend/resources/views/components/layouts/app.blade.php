<!DOCTYPE html>
<html lang="{{ str_replace('_', '-', app()->getLocale()) }}">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>{{ $title ?? config('app.name', 'CleanCall') }}</title>
        <style>
            :root { --brand-green: #5DA646; --brand-dark: #1D2D47; }
            * { box-sizing: border-box; }
            body { margin: 0; font-family: ui-sans-serif, system-ui, -apple-system, Segoe UI, Roboto, Helvetica Neue, Arial, Noto Sans, sans-serif; background: #FCFDFF; color: var(--brand-dark); }
            .header { display: flex; align-items: center; justify-content: space-between; padding: 12px 16px; background: var(--brand-green); color: #fff; }
            .brand { font-weight: 700; font-size: 20px; }
            .nav a { color: #fff; text-decoration: none; margin-left: 12px; padding: 6px 10px; border-radius: 6px; }
            .nav a:hover { background: #4a8e38; }
            .container { max-width: 1024px; margin: 0 auto; padding: 16px; }
            .card { background: #fff; border: 1px solid rgba(29,45,71,0.2); border-radius: 12px; padding: 16px; box-shadow: 0 1px 2px rgba(0,0,0,0.04); }
            .grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(240px, 1fr)); gap: 16px; }
            .btn { display: inline-block; padding: 10px 14px; background: var(--brand-green); color: #fff; border-radius: 8px; text-decoration: none; }
            .btn.secondary { background: #fff; color: var(--brand-dark); border: 1px solid rgba(29,45,71,0.2); }
            input, select { width: 100%; padding: 10px; border: 1px solid rgba(29,45,71,0.3); border-radius: 8px; }
            label { font-size: 12px; color: rgba(29,45,71,0.8); display: block; margin-bottom: 6px; }
        </style>
        {{ $head ?? '' }}
    </head>
    <body>
        <header class="header">
            <div class="brand">CleanCall</div>
            <nav class="nav">
                <a href="/">Landing</a>
                @auth
                    <a href="/home">Home</a>
                    <a href="/dashboards">Dashboards</a>
                    <form action="/logout" method="POST" style="display:inline">
                        @csrf
                        <button type="submit" class="btn" style="margin-left:12px;background:#fff;color:#1D2D47;border:1px solid rgba(29,45,71,0.2)">Logout</button>
                    </form>
                @else
                    <a href="/login">Login</a>
                @endauth
            </nav>
        </header>
        <main class="container">
            {{ $slot }}
        </main>
    </body>
</html>
