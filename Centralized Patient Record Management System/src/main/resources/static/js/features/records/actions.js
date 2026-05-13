import { setStatus } from "../../core/ui.js";
import { createMedicalRecord, fetchPatientRecords } from "./api.js";
import { renderPatientRecords } from "./view.js";

function getSelectedPatientId() {
    return document.querySelector("#record-form input[name='patientId']").value;
}

export async function loadPatientRecords(patientIdFromButton) {
    const patientId = patientIdFromButton || getSelectedPatientId();
    if (!patientId) {
        setStatus("record-form-status", "Enter a patient ID first.", "error");
        return;
    }

    try {
        const records = await fetchPatientRecords(patientId);
        renderPatientRecords(records);
        setStatus("record-form-status", `Loaded records for patient ${patientId}.`, "ok");
    } catch (error) {
        setStatus("record-form-status", error.message, "error");
    }
}

export async function handleRecordSave(event, refreshers) {
    event.preventDefault();
    const formData = Object.fromEntries(new FormData(event.target).entries());
    const patientId = formData.patientId;
    delete formData.patientId;

    try {
        await createMedicalRecord(patientId, formData);
        setStatus("record-form-status", "Medical record saved.", "ok");
        await loadPatientRecords(patientId);
        await refreshers.loadDashboard();
    } catch (error) {
        setStatus("record-form-status", error.message, "error");
    }
}
