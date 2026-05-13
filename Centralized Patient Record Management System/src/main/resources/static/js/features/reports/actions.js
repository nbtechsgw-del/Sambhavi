import { fetchDashboardReport } from "./api.js";
import { renderDashboardReport, renderDashboardStats } from "./view.js";

export async function loadDashboard() {
    const report = await fetchDashboardReport();
    renderDashboardStats(report);
    renderDashboardReport(report);
}
