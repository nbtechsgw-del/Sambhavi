import { card, renderList } from "../../core/ui.js";

function billCard(bill) {
    const patientLabel = bill.patient
        ? `${bill.patient.patientCode || bill.patient.id} - ${bill.patient.firstName || ""} ${bill.patient.lastName || ""}`.trim()
        : "Unknown patient";

    return card(`Bill #${bill.id} - ${bill.status}`, [
        `Patient: ${patientLabel}`,
        `Description: ${bill.description || "-"}`,
        `Amount: ${bill.amount || "-"}`,
        `Billing Date: ${bill.billingDate || "-"}`
    ]);
}

export function renderBills(bills) {
    renderList("bill-list", bills.map(billCard), "No bills found.");
}
