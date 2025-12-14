@php($title = 'Dashboards')
<x-layouts.app :title="$title">
    <div class="card">
        <h2 style="margin:0 0 12px 0">Grafana Dashboards</h2>
        <div style="display:grid;grid-template-columns:1fr;gap:16px">
            <iframe src="{{ rtrim(config('services.grafana.url', 'http://localhost:3000'), '/') }}/d/pace/clean-call-overview?orgId=1&kiosk=tv" style="width:100%;height:480px;border:1px solid rgba(29,45,71,0.2);border-radius:12px"></iframe>
            <iframe src="{{ rtrim(config('services.grafana.url', 'http://localhost:3000'), '/') }}/d/pace2/waste-aggregation?orgId=1&kiosk" style="width:100%;height:480px;border:1px solid rgba(29,45,71,0.2);border-radius:12px"></iframe>
        </div>
    </div>
</x-layouts.app>
