import { setStatus } from "../../core/ui.js";
import { createBill, fetchBills, updateBillPaymentStatus } from "./api.js";
import { renderBills } from "./view.js";

export async function loadBills() {
    const bills = await fetchBills();
    renderBills(bills);
}

export async function handleBillCreate(event, refreshers) {
    event.preventDefault();
    const form = event.target;
    const payload = Object.fromEntries(new FormData(form).entries());
    const patientId = payload.patientId;
    delete payload.patientId;
    payload.amount = Number(payload.amount);

    try {
        await createBill(patientId, payload);
        form.reset();
        setStatus("bill-form-status", "Bill generated successfully.", "ok");
        await refreshers.loadBills();
        await refreshers.loadDashboard();
    } catch (error) {
        setStatus("bill-form-status", error.message, "error");
    }
}

export async function handleBillStatusUpdate(event, refreshers) {
    event.preventDefault();
    const formData = Object.fromEntries(new FormData(event.target).entries());

    try {
        await updateBillPaymentStatus(formData.billId, formData.status);
        setStatus("bill-form-status", "Payment status updated.", "ok");
        await refreshers.loadBills();
        await refreshers.loadDashboard();
    } catch (error) {
        setStatus("bill-form-status", error.message, "error");
    }
}
