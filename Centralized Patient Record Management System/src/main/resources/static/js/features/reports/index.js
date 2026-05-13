import { connectDashboard } from "../../core/auth.js";
import { loadDashboard } from "./actions.js";

export function bindReportEvents(loaders) {
    document.getElementById("login-form").addEventListener("submit", (event) => connectDashboard(event, loaders));
    document.getElementById("refresh-reports").addEventListener("click", loaders.loadDashboard);
}

export { loadDashboard };
