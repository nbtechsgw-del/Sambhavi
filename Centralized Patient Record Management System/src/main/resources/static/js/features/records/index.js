import { handleRecordSave, loadPatientRecords } from "./actions.js";

export function bindRecordEvents(refreshers) {
    document.getElementById("record-form").addEventListener("submit", (event) => handleRecordSave(event, refreshers));
    document.getElementById("load-records").addEventListener("click", () => loadPatientRecords());
}

export { loadPatientRecords };
