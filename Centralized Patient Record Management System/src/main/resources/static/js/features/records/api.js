import { apiFetch } from "../../core/api.js";

export function fetchPatientRecords(patientId) {
    return apiFetch(`/api/patients/${patientId}/records`);
}

export function createMedicalRecord(patientId, payload) {
    return apiFetch(`/api/patients/${patientId}/records`, {
        method: "POST",
        body: JSON.stringify(payload)
    });
}
