import { handleBillCreate, handleBillStatusUpdate, loadBills } from "./actions.js";

export function bindBillingEvents(refreshers) {
    document.getElementById("bill-form").addEventListener("submit", (event) => handleBillCreate(event, refreshers));
    document.getElementById("bill-status-form").addEventListener("submit", (event) => handleBillStatusUpdate(event, refreshers));
    document.getElementById("refresh-bills").addEventListener("click", refreshers.loadBills);
}

export { loadBills };
