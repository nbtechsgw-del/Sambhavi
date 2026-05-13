import { card, renderList } from "../../core/ui.js";

function patientCard(patient) {
    return card(`${patient.patientCode} - ${patient.firstName} ${patient.lastName}`, [
        `Phone: ${patient.phoneNumber || "-"}`,
        `Email: ${patient.email || "-"}`,
        `Blood Group: ${patient.bloodGroup || "-"}`,
        `Emergency: ${patient.emergencyContactName || "-"} (${patient.emergencyContactPhone || "-"})`
    ]);
}

export function renderPatients(patients) {
    renderList("patient-list", patients.map(patientCard), "No patients found.");
}

export function renderPatientSearchResults(patients) {
    renderList("search-results", patients.map(patientCard), "No matching patients found.");
}

export function renderPatientSearchError(message) {
    renderList("search-results", [card("Search failed", [message])], "");
}

export function renderPatientSearchPrompt() {
    renderList("search-results", [], "Enter a keyword to search.");
}
