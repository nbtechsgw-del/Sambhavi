import { connectDashboard, initializePage } from "./js/core/auth.js";
import { initializeSharedLayout } from "./js/core/layout.js";
import { bindReportEvents, loadDashboard } from "./js/features/reports/index.js";

function bindPageEvents() {
    bindReportEvents({ loadDashboard });
}

async function bootstrap() {
    initializeSharedLayout();
    bindPageEvents();
    await initializePage({ loadDashboard }, { requireLogin: true });
}

bootstrap();
