import { handlePatientRegistration, handlePatientSearch, loadPatients } from "./actions.js";

export function bindPatientEvents(refreshers) {
    document.getElementById("search-form").addEventListener("submit", handlePatientSearch);
    document.getElementById("patient-form").addEventListener("submit", (event) => handlePatientRegistration(event, refreshers));
    document.getElementById("refresh-patients").addEventListener("click", refreshers.loadPatients);
}

export { loadPatients };
