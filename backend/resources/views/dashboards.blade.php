@php($title = 'Dashboards')
@php($grafana = rtrim(config('services.grafana.url', ''), '/'))
<x-layouts.app :title="$title">
    <style>
        .fullpage-container { padding: 0; margin: 0; }
        .dash-toolbar {
            display:flex; gap:8px; align-items:center; padding:8px 12px;
            border-bottom:1px solid rgba(29,45,71,0.12); background:#fff;
        }
        .dash-toolbar h2 { margin:0; font-size:16px; color:#1d2d47; }
        .dash-toolbar .btn {
            border:1px solid rgba(29,45,71,0.2); border-radius:8px; padding:6px 10px; background:#fff;
            color:#1d2d47; cursor:pointer;
        }
        .dash-toolbar .btn.active { background:#1d2d47; color:#fff; }
        .dash-frame { width:100%; height: calc(100vh - 64px); border:0; }
        @media (max-width: 640px) { .dash-frame { height: calc(100vh - 96px); } }
    </style>
    <div class="fullpage-container">
        @if(empty($grafana) || str_contains($grafana, 'localhost'))
            <div class="card" style="margin:12px">
                <h2 style="margin:0 0 8px 0">Grafana not configured</h2>
                <p style="margin:0 0 6px 0">Set GRAFANA_URL in your backend .env to your Grafana base URL (e.g. https://grafana.yourdomain.com), then reload this page.</p>
                <code>GRAFANA_URL=https://grafana.yourdomain.com</code>
            </div>
        @else
            @php($dash1 = $grafana.'/d/pace/clean-call-overview?orgId=1&kiosk&refresh=30s')
            @php($dash2 = $grafana.'/d/pace2/waste-aggregation?orgId=1&kiosk&refresh=30s')
            <div class="dash-toolbar">
                <h2 style="flex:1">REMASAB Grafana</h2>
                <button id="btnOverview" class="btn active" onclick="switchDash('overview')">Overview</button>
                <button id="btnEvac" class="btn" onclick="switchDash('evac')">Waste Evacuation</button>
            </div>
            <iframe id="dashFrame" class="dash-frame" src="{{ request()->query('d') === 'evac' ? $dash2 : $dash1 }}"></iframe>
            <script>
                const urls = { overview: @json($dash1), evac: @json($dash2) };
                function switchDash(which) {
                    const f = document.getElementById('dashFrame');
                    f.src = urls[which] || urls.overview;
                    document.getElementById('btnOverview').classList.toggle('active', which === 'overview');
                    document.getElementById('btnEvac').classList.toggle('active', which === 'evac');
                    const u = new URL(window.location.href);
                    u.searchParams.set('d', which);
                    history.replaceState({}, '', u.toString());
                }
            </script>
        @endif
    </div>
</x-layouts.app>
