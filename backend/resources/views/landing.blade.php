@php($title = 'Welcome')
<x-layouts.app :title="$title">
    <div class="card" style="display:grid;grid-template-columns:1fr 1fr;gap:16px;align-items:center">
        <div>
            <h1 style="margin:0 0 8px 0">CleanCall</h1>
            <p style="margin:0 0 16px 0;color:rgba(29,45,71,0.8)">Waste management insights and stakeholder engagement for Kano State.</p>
            <div style="display:flex;gap:12px;flex-wrap:wrap">
                @auth
                    <a class="btn" href="/home">Enter Home</a>
                    <a class="btn secondary" href="/dashboards">Dashboards</a>
                @else
                    <a class="btn" href="/login">Login</a>
                @endauth
            </div>
        </div>
        <div>
            <svg viewBox="0 0 480 320" xmlns="http://www.w3.org/2000/svg" style="width:100%;height:auto;border-radius:12px">
                <defs>
                    <linearGradient id="g" x1="0" y1="0" x2="1" y2="1">
                        <stop offset="0%" stop-color="#5DA646"/>
                        <stop offset="100%" stop-color="#1D2D47"/>
                    </linearGradient>
                </defs>
                <rect x="0" y="0" width="480" height="320" fill="url(#g)" opacity="0.15"/>
                <g fill="#5DA646">
                    <circle cx="80" cy="100" r="28"/>
                    <rect x="130" y="80" width="220" height="140" rx="14" fill="#ffffff" stroke="#1D2D47" stroke-opacity="0.2"/>
                    <rect x="145" y="95" width="120" height="14" rx="7" fill="#5DA646"/>
                    <rect x="145" y="117" width="180" height="10" rx="5" fill="#1D2D47" opacity="0.4"/>
                    <rect x="145" y="137" width="160" height="10" rx="5" fill="#1D2D47" opacity="0.3"/>
                    <rect x="145" y="157" width="140" height="10" rx="5" fill="#1D2D47" opacity="0.2"/>
                    <rect x="145" y="177" width="200" height="10" rx="5" fill="#1D2D47" opacity="0.25"/>
                    <rect x="145" y="197" width="120" height="10" rx="5" fill="#1D2D47" opacity="0.2"/>
                    <rect x="360" y="80" width="60" height="140" rx="10" fill="#ffffff" stroke="#1D2D47" stroke-opacity="0.2"/>
                    <rect x="372" y="110" width="36" height="18" rx="4" fill="#5DA646"/>
                </g>
            </svg>
        </div>
    </div>
</x-layouts.app>
