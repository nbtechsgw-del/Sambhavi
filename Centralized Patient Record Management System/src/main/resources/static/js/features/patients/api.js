import { apiFetch } from "../../core/api.js";
import { endpoints } from "../../core/endpoints.js";

export function fetchPatients() {
    return apiFetch(endpoints.patients);
}

export function searchPatientsByKeyword(keyword) {
    return apiFetch(`${endpoints.patients}/search?keyword=${encodeURIComponent(keyword)}`);
}

export function createPatient(payload) {
    return apiFetch(endpoints.patients, {
        method: "POST",
        body: JSON.stringify(payload)
    });
}
