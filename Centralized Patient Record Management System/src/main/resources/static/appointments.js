import { connectDashboard, initializePage } from "./js/core/auth.js";
import { initializeSharedLayout } from "./js/core/layout.js";
import { bindAppointmentEvents, loadAppointments } from "./js/features/appointments/index.js";

function bindPageEvents() {
    bindAppointmentEvents({ loadAppointments, loadDashboard: async () => {} });
    document.getElementById("login-form").addEventListener("submit", (event) =>
        connectDashboard(event, { loadAppointments })
    );
}

async function bootstrap() {
    initializeSharedLayout();
    bindPageEvents();
    await initializePage({ loadAppointments }, { requireLogin: true });
}

bootstrap();
