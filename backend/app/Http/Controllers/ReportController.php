<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\WastePicker;
use App\Models\WomenTrainingSession;
use App\Models\SchoolWasteBankRecord;
use App\Models\WasteAggregation;
use App\Models\StakeholderCommitment;
use App\Models\EvacuationTask;

class ReportController extends Controller
{
    public function overview(Request $request)
    {
        $from = $request->get('from');
        $to = $request->get('to');
        $lga = $request->get('lga');
        $source = $request->get('source'); // optional: Schools/Women/Dumpsites/Markets

        $pickerQ = WastePicker::query();
        if ($from) $pickerQ->whereDate('created_at', '>=', $from);
        if ($to) $pickerQ->whereDate('created_at', '<=', $to);
        if ($lga) $pickerQ->where('lga', $lga);
        $pickers = $pickerQ->get();

        $womenQ = WomenTrainingSession::query();
        if ($from) $womenQ->whereDate('created_at', '>=', $from);
        if ($to) $womenQ->whereDate('created_at', '<=', $to);
        if ($lga) $womenQ->where('lga', $lga);
        $sessions = $womenQ->get();

        $schoolsQ = SchoolWasteBankRecord::query();
        if ($from) $schoolsQ->whereDate('created_at', '>=', $from);
        if ($to) $schoolsQ->whereDate('created_at', '<=', $to);
        if ($lga) $schoolsQ->where('lga', $lga);
        $schools = $schoolsQ->get();

        $aggQ = WasteAggregation::query();
        if ($from) $aggQ->whereDate('created_at', '>=', $from);
        if ($to) $aggQ->whereDate('created_at', '<=', $to);
        if ($lga) $aggQ->where('lga', $lga);
        $agg = $aggQ->get();

        $commitQ = StakeholderCommitment::query();
        if ($from) $commitQ->whereDate('created_at', '>=', $from);
        if ($to) $commitQ->whereDate('created_at', '<=', $to);
        if ($lga) $commitQ->where('lga', $lga)->orWhere('lga_id', $lga);
        $commits = $commitQ->get();

        $totalAggKg = $agg->sum('total_waste_kg');
        $estRecyclablesKg = $agg->sum(function ($r) { return ($r->total_waste_kg ?? 0) * (($r->recyclable_percentage ?? 0) / 100.0); });
        $activeSchools30 = $schools->groupBy('school_name')->filter(function ($recs) {
            $cut = now()->subDays(30);
            return $recs->max('updated_at') >= $cut;
        })->count();
        $statusCounts = [
            'pending' => $commits->where('status','pending')->count(),
            'ongoing' => $commits->where('status','ongoing')->count(),
            'completed' => $commits->where('status','completed')->count(),
        ];
        $overdue = $commits->filter(function ($c) {
            return $c->due_date && strtolower($c->status) !== 'completed' && $c->due_date < now()->toDateString();
        })->count();

        $topLgas = $agg->groupBy('lga')->map(function ($items) { return $items->sum('total_waste_kg'); })
            ->sortDesc()->take(3)->map(function ($v, $k) { return [ 'lga'=>$k, 'kg'=>$v]; })->values();
        $topSchoolsPlastics = $schools->groupBy('school_name')->map(function ($items) { return $items->sum('plastic_collected_kg'); })
            ->sortDesc()->take(3)->map(function ($v, $k) { return [ 'school'=>$k, 'kg'=>$v]; })->values();

        $evacQ = EvacuationTask::query();
        if ($from) $evacQ->whereDate('created_at', '>=', $from);
        if ($to) $evacQ->whereDate('created_at', '<=', $to);
        if ($lga) $evacQ->where('lga', $lga);
        $evacs = $evacQ->get();
        $evacTotalKg = $evacs->sum('total_kg');
        $evacPending = $evacs->where('status','pending')->count();
        $evacToday = EvacuationTask::whereDate('created_at', now()->toDateString())->count();
        $evacContamAvg = $evacs->filter(fn($t)=>!is_null($t->contamination_score))->avg('contamination_score');

        $out = [
            'kpis' => [
                'evac_pickups_today' => $evacToday,
                'evac_pending_pickups' => $evacPending,
                'evac_total_kg' => round($evacTotalKg, 1),
                'evac_avg_contamination' => round($evacContamAvg ?? 0, 1),
            ],
            'insights' => [
                'top_lgas_by_waste' => $topLgas,
                'top_schools_plastics' => $topSchoolsPlastics,
            ],
        ];
        if ($source) {
            // optional scope: if user selects "Schools" or "Women" or other source in mobile app, we can tailor KPIs
        }
        return response()->json($out);
    }
}
