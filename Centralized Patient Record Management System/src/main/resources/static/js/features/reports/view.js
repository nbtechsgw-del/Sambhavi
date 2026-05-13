import { card, renderList } from "../../core/ui.js";

export function renderDashboardStats(report) {
    document.getElementById("stat-patients").textContent = report.totalPatients ?? 0;
    document.getElementById("stat-records").textContent = report.totalMedicalRecords ?? 0;
    document.getElementById("stat-appointments").textContent = report.totalAppointments ?? 0;
    document.getElementById("stat-pending-bills").textContent = report.pendingBills ?? 0;
}

export function renderDashboardReport(report) {
    renderList(
        "report-list",
        Object.entries(report).map(([key, value]) => card(key, [`Value: ${value}`])),
        "No report data available."
    );
}
