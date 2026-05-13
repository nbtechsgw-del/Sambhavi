import { endpoints } from "./endpoints.js";
import { AUTH_STORAGE_KEY, state } from "./state.js";
import { setStatus } from "./ui.js";

export function setDemoUser(username, password) {
    const usernameField = document.getElementById("username");
    const passwordField = document.getElementById("password");
    if (!usernameField || !passwordField) {
        return;
    }

    usernameField.value = username;
    passwordField.value = password;
}

function buildAuthHeader(username, password) {
    return `Basic ${btoa(`${username}:${password}`)}`;
}

function setSession(username, password) {
    state.credentials = { username, password };
    state.authHeader = buildAuthHeader(username, password);
    localStorage.setItem(AUTH_STORAGE_KEY, JSON.stringify(state.credentials));
}

function clearSession() {
    state.credentials = null;
    state.authHeader = null;
    localStorage.removeItem(AUTH_STORAGE_KEY);
}

async function runLoaders(loaders = {}) {
    const tasks = Object.values(loaders).filter((loader) => typeof loader === "function");
    if (!tasks.length) {
        return;
    }

    await Promise.all(tasks.map((loader) => loader()));
}

export function restoreSession() {
    if (state.authHeader) {
        return true;
    }

    const rawSession = localStorage.getItem(AUTH_STORAGE_KEY);
    if (!rawSession) {
        return false;
    }

    try {
        const credentials = JSON.parse(rawSession);
        if (!credentials?.username || !credentials?.password) {
            clearSession();
            return false;
        }

        state.credentials = credentials;
        state.authHeader = buildAuthHeader(credentials.username, credentials.password);
        return true;
    } catch (error) {
        clearSession();
        return false;
    }
}

export function hydrateAuthForm() {
    const usernameField = document.getElementById("username");
    const passwordField = document.getElementById("password");
    if (!usernameField || !passwordField) {
        return;
    }

    if (restoreSession() && state.credentials) {
        usernameField.value = state.credentials.username;
        passwordField.value = state.credentials.password;
    }
}

export async function connectDashboard(event, loaders) {
    event.preventDefault();
    const username = document.getElementById("username").value.trim();
    const password = document.getElementById("password").value;

    setSession(username, password);
    setStatus("auth-status", "Authenticating and loading data...", "idle");

    try {
        await runLoaders(loaders);
        setStatus("auth-status", `Connected as ${username}.`, "ok");
    } catch (error) {
        clearSession();
        setStatus("auth-status", error.message, "error");
    }
}

export async function initializePage(loaders = {}, options = {}) {
    const { requireLogin = false } = options;
    hydrateAuthForm();

    if (!restoreSession()) {
        if (requireLogin) {
            setStatus("auth-status", "Connect the dashboard to load this page.", "idle");
        }
        return false;
    }

    try {
        await runLoaders(loaders);
        if (state.credentials?.username) {
            setStatus("auth-status", `Connected as ${state.credentials.username}.`, "ok");
        }
        return true;
    } catch (error) {
        clearSession();
        setStatus("auth-status", error.message, "error");
        return false;
    }
}

export { endpoints };
