import { apiFetch } from "../../core/api.js";
import { endpoints } from "../../core/endpoints.js";

export function fetchDashboardReport() {
    return apiFetch(endpoints.reports);
}
