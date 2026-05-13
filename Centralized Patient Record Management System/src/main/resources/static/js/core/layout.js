import { hydrateAuthForm, setDemoUser } from "./auth.js";

export function bindDemoUserChips() {
    document.querySelectorAll(".chip").forEach((button) => {
        button.addEventListener("click", () => {
            setDemoUser(button.dataset.user, button.dataset.pass);
        });
    });
}

export function initializeSharedLayout() {
    hydrateAuthForm();
    bindDemoUserChips();
}
