import { apiFetch } from "../../core/api.js";
import { endpoints } from "../../core/endpoints.js";

export function fetchAppointments() {
    return apiFetch(endpoints.appointments);
}

export function createAppointment(patientId, payload) {
    return apiFetch(`/api/appointments?patientId=${encodeURIComponent(patientId)}`, {
        method: "POST",
        body: JSON.stringify(payload)
    });
}

export function updateAppointmentStatus(appointmentId, status) {
    return apiFetch(`/api/appointments/${appointmentId}/status?status=${encodeURIComponent(status)}`, {
        method: "PUT"
    });
}
