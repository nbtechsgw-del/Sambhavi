import {
    handleAppointmentCreate,
    handleAppointmentStatusUpdate,
    loadAppointments
} from "./actions.js";

export function bindAppointmentEvents(refreshers) {
    document.getElementById("appointment-form").addEventListener("submit", (event) => handleAppointmentCreate(event, refreshers));
    document.getElementById("appointment-status-form").addEventListener("submit", (event) => handleAppointmentStatusUpdate(event, refreshers));
    document.getElementById("refresh-appointments").addEventListener("click", refreshers.loadAppointments);
}

export { loadAppointments };
