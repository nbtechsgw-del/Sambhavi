import { connectDashboard, initializePage } from "./js/core/auth.js";
import { initializeSharedLayout } from "./js/core/layout.js";
import { bindPatientEvents, loadPatients } from "./js/features/patients/index.js";

function bindPageEvents() {
    bindPatientEvents({ loadPatients, loadDashboard: async () => {} });
    document.getElementById("login-form").addEventListener("submit", (event) =>
        connectDashboard(event, { loadPatients })
    );
}

async function bootstrap() {
    initializeSharedLayout();
    bindPageEvents();
    await initializePage({ loadPatients }, { requireLogin: true });
}

bootstrap();
