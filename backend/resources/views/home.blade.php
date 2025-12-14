@php($title = 'Home')
<x-layouts.app :title="$title">
    <div class="grid">
        <div class="card">
            <div style="font-size:12px;color:rgba(29,45,71,0.7)">Registered Pickers</div>
            <div style="font-size:24px;font-weight:700">128</div>
        </div>
        <div class="card">
            <div style="font-size:12px;color:rgba(29,45,71,0.7)">Women Trained</div>
            <div style="font-size:24px;font-weight:700">54</div>
        </div>
        <div class="card">
            <div style="font-size:12px;color:rgba(29,45,71,0.7)">School Waste Banks</div>
            <div style="font-size:24px;font-weight:700">23</div>
        </div>
        <div class="card">
            <div style="font-size:12px;color:rgba(29,45,71,0.7)">Total Waste</div>
            <div style="font-size:24px;font-weight:700">4.2t</div>
        </div>
    </div>
    <div class="card" style="margin-top:16px">
        <div style="display:flex;gap:12px;flex-wrap:wrap">
            <a class="btn" href="/dashboards">Open Dashboards</a>
            <a class="btn secondary" href="/">Landing</a>
        </div>
    </div>
</x-layouts.app>
