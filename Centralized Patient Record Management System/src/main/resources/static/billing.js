import { connectDashboard, initializePage } from "./js/core/auth.js";
import { initializeSharedLayout } from "./js/core/layout.js";
import { bindBillingEvents, loadBills } from "./js/features/billing/index.js";

function bindPageEvents() {
    bindBillingEvents({ loadBills, loadDashboard: async () => {} });
    document.getElementById("login-form").addEventListener("submit", (event) =>
        connectDashboard(event, { loadBills })
    );
}

async function bootstrap() {
    initializeSharedLayout();
    bindPageEvents();
    await initializePage({ loadBills }, { requireLogin: true });
}

bootstrap();
