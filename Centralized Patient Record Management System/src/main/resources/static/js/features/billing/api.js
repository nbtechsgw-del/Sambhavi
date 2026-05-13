import { apiFetch } from "../../core/api.js";
import { endpoints } from "../../core/endpoints.js";

export function fetchBills() {
    return apiFetch(endpoints.bills);
}

export function createBill(patientId, payload) {
    return apiFetch(`/api/bills?patientId=${encodeURIComponent(patientId)}`, {
        method: "POST",
        body: JSON.stringify(payload)
    });
}

export function updateBillPaymentStatus(billId, status) {
    return apiFetch(`/api/bills/${billId}/payment?status=${encodeURIComponent(status)}`, {
        method: "PUT"
    });
}
