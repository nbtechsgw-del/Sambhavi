import { connectDashboard, initializePage } from "./js/core/auth.js";
import { initializeSharedLayout } from "./js/core/layout.js";
import { bindRecordEvents } from "./js/features/records/index.js";

function bindPageEvents() {
    bindRecordEvents({ loadDashboard: async () => {} });
    document.getElementById("login-form").addEventListener("submit", (event) =>
        connectDashboard(event, {})
    );
}

async function bootstrap() {
    initializeSharedLayout();
    bindPageEvents();
    await initializePage({}, { requireLogin: true });
}

bootstrap();
