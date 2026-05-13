import { card, renderList } from "../../core/ui.js";

function appointmentCard(appointment) {
    const patientLabel = appointment.patient
        ? `${appointment.patient.patientCode || appointment.patient.id} - ${appointment.patient.firstName || ""} ${appointment.patient.lastName || ""}`.trim()
        : "Unknown patient";

    return card(`Appointment #${appointment.id} - ${appointment.status}`, [
        `Patient: ${patientLabel}`,
        `Doctor: ${appointment.doctorName || "-"}`,
        `Department: ${appointment.department || "-"}`,
        `Date: ${appointment.appointmentDateTime || "-"}`,
        `Notes: ${appointment.notes || "-"}`
    ]);
}

export function renderAppointments(appointments) {
    renderList("appointment-list", appointments.map(appointmentCard), "No appointments found.");
}
