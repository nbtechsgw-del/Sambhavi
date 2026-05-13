import { card, renderList } from "../../core/ui.js";

function recordCard(record) {
    return card(`Record #${record.id} - ${record.doctorName || "Doctor not set"}`, [
        `Diagnosis: ${record.diagnosis || "-"}`,
        `Treatment: ${record.treatment || "-"}`,
        `Prescription: ${record.prescription || "-"}`,
        `Lab Report: ${record.labReportUrl || "-"}`,
        `Recorded At: ${record.recordedAt || "-"}`
    ]);
}

export function renderPatientRecords(records) {
    renderList("record-list", records.map(recordCard), "No medical records found.");
}
