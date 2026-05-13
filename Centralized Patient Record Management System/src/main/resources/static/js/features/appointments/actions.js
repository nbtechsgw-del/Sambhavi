import { setStatus } from "../../core/ui.js";
import {
    createAppointment,
    fetchAppointments,
    updateAppointmentStatus as saveAppointmentStatus
} from "./api.js";
import { renderAppointments } from "./view.js";

export async function loadAppointments() {
    const appointments = await fetchAppointments();
    renderAppointments(appointments);
}

export async function handleAppointmentCreate(event, refreshers) {
    event.preventDefault();
    const form = event.target;
    const payload = Object.fromEntries(new FormData(form).entries());
    const patientId = payload.patientId;
    delete payload.patientId;

    try {
        await createAppointment(patientId, payload);
        form.reset();
        setStatus("appointment-form-status", "Appointment scheduled successfully.", "ok");
        await refreshers.loadAppointments();
        await refreshers.loadDashboard();
    } catch (error) {
        setStatus("appointment-form-status", error.message, "error");
    }
}

export async function handleAppointmentStatusUpdate(event, refreshers) {
    event.preventDefault();
    const formData = Object.fromEntries(new FormData(event.target).entries());

    try {
        await saveAppointmentStatus(formData.appointmentId, formData.status);
        setStatus("appointment-form-status", "Appointment status updated.", "ok");
        await refreshers.loadAppointments();
        await refreshers.loadDashboard();
    } catch (error) {
        setStatus("appointment-form-status", error.message, "error");
    }
}
