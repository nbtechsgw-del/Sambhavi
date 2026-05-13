import { setStatus } from "../../core/ui.js";
import { createPatient, fetchPatients, searchPatientsByKeyword } from "./api.js";
import {
    renderPatientSearchError,
    renderPatientSearchPrompt,
    renderPatientSearchResults,
    renderPatients
} from "./view.js";

export async function loadPatients() {
    const patients = await fetchPatients();
    renderPatients(patients);
}

export async function handlePatientSearch(event) {
    event.preventDefault();
    const keyword = document.getElementById("search-keyword").value.trim();
    if (!keyword) {
        renderPatientSearchPrompt();
        return;
    }

    try {
        const patients = await searchPatientsByKeyword(keyword);
        renderPatientSearchResults(patients);
    } catch (error) {
        renderPatientSearchError(error.message);
    }
}

export async function handlePatientRegistration(event, refreshers) {
    event.preventDefault();
    const form = event.target;
    const payload = Object.fromEntries(new FormData(form).entries());

    try {
        await createPatient(payload);
        form.reset();
        setStatus("patient-form-status", "Patient registered successfully.", "ok");
        await refreshers.loadPatients();
        await refreshers.loadDashboard();
    } catch (error) {
        setStatus("patient-form-status", error.message, "error");
    }
}
