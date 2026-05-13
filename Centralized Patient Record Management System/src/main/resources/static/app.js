import { connectDashboard, initializePage } from "./js/core/auth.js";
import { initializeSharedLayout } from "./js/core/layout.js";
import { loadDashboard } from "./js/features/reports/index.js";

function bindHomeEvents() {
    document.getElementById("login-form").addEventListener("submit", (event) =>
        connectDashboard(event, { loadDashboard })
    );
}

async function bootstrap() {
    initializeSharedLayout();
    bindHomeEvents();
    await initializePage({ loadDashboard });
}

bootstrap();
