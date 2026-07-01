function validateLogin() {
    const email = document.getElementById("email").value.trim();
    const password = document.getElementById("password").value.trim();
    if (!email || !password) {
        alert("Please enter email and password.");
        return false;
    }
    const personalCode = document.getElementById("personalCode");
    if (personalCode && !personalCode.value.trim()) {
        alert("Please enter your admin personal code.");
        return false;
    }
    return true;
}

function validateRegister() {
    const name = document.getElementById("name").value.trim();
    const email = document.getElementById("email").value.trim();
    const password = document.getElementById("password").value.trim();
    if (name.length < 3) {
        alert("Name must contain at least 3 characters.");
        return false;
    }
    if (!email.includes("@")) {
        alert("Please enter a valid email address.");
        return false;
    }
    if (password.length < 6) {
        alert("Password must contain at least 6 characters.");
        return false;
    }
    const role = document.getElementById("role");
    const personalCode = document.getElementById("personalCode");
    if (role && role.value === "ADMIN" && personalCode && !personalCode.value.trim()) {
        alert("Personal code is required for admin registration.");
        return false;
    }
    return true;
}

function togglePersonalCode() {
    const role = document.getElementById("role");
    const personalCodeGroup = document.getElementById("personalCodeGroup");
    const personalCode = document.getElementById("personalCode");
    if (!role || !personalCodeGroup || !personalCode) {
        return;
    }
    const isAdmin = role.value === "ADMIN";
    personalCodeGroup.classList.toggle("hidden", !isAdmin);
    personalCode.required = isAdmin;
    if (!isAdmin) {
        personalCode.value = "";
    }
}

document.addEventListener("DOMContentLoaded", togglePersonalCode);

function validateBooking() {
    const travelDate = document.getElementById("travelDate").value;
    const travellers = Number(document.getElementById("travellers").value);
    if (!travelDate) {
        alert("Please select a travel date.");
        return false;
    }
    if (new Date(travelDate) < new Date().setHours(0, 0, 0, 0)) {
        alert("Travel date cannot be in the past.");
        return false;
    }
    if (travellers < 1) {
        alert("Please enter at least one traveller.");
        return false;
    }
    return true;
}

function validateHotelBooking() {
    const checkInDate = document.getElementById("checkInDate").value;
    const checkOutDate = document.getElementById("checkOutDate").value;
    const rooms = Number(document.getElementById("rooms").value);
    const guests = Number(document.getElementById("guests").value);
    const today = new Date().setHours(0, 0, 0, 0);
    if (!checkInDate || !checkOutDate) {
        alert("Please select check-in and check-out dates.");
        return false;
    }
    if (new Date(checkInDate) < today) {
        alert("Check-in date cannot be in the past.");
        return false;
    }
    if (new Date(checkOutDate) <= new Date(checkInDate)) {
        alert("Check-out date must be after check-in date.");
        return false;
    }
    if (rooms < 1 || guests < 1) {
        alert("Please enter valid rooms and guests.");
        return false;
    }
    return true;
}

function addRoomTypeRow() {
    const editor = document.getElementById("roomTypeEditor");
    if (!editor) {
        return;
    }
    const row = document.createElement("div");
    row.className = "room-type-row";
    row.innerHTML = `
        <input name="roomTypeNames" placeholder="Room type" required>
        <input name="roomTypeRooms" type="number" min="0" placeholder="Rooms" required>
        <input name="roomTypePrices" type="number" step="0.01" min="0" placeholder="Price/night" required>
        <button class="btn small danger" type="button" onclick="removeRoomTypeRow(this)">Remove</button>
    `;
    editor.appendChild(row);
}

function removeRoomTypeRow(button) {
    const editor = document.getElementById("roomTypeEditor");
    if (!editor || editor.querySelectorAll(".room-type-row").length <= 1) {
        alert("At least one room type is required.");
        return;
    }
    button.closest(".room-type-row").remove();
}

function updateSelectedRoomSummary() {
    const select = document.getElementById("roomTypeId");
    const price = document.getElementById("selectedRoomPrice");
    const available = document.getElementById("selectedRoomAvailability");
    if (!select || !price || !available) {
        return;
    }
    const option = select.options[select.selectedIndex];
    price.textContent = option.dataset.price ? `Rs. ${option.dataset.price}` : "Rs. 0";
    available.textContent = option.dataset.rooms ? `${option.dataset.rooms} rooms available` : "";
}

document.addEventListener("DOMContentLoaded", updateSelectedRoomSummary);
