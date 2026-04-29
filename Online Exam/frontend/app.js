const STORAGE_KEY = "onlineExamSystemData";
const API_BASE_URL = window.location.origin && window.location.origin.startsWith("http")
  ? window.location.origin
  : "http://localhost:8082";

const seedData = {
  users: [
    { id: 1, name: "System Admin", email: "admin@exam.com", password: "admin123", role: "admin" },
    { id: 2, name: "Demo Student", email: "student@exam.com", password: "student123", role: "student" }
  ],
  exams: [
    { id: 1, title: "Java Fundamentals", duration: 10 },
    { id: 2, title: "Web Technology Basics", duration: 8 }
  ],
  questions: [
    {
      id: 1,
      examId: 1,
      text: "Which keyword is used to inherit a class in Java?",
      options: { A: "implements", B: "extends", C: "inherits", D: "super" },
      correct: "B"
    },
    {
      id: 2,
      examId: 1,
      text: "Which method is the entry point of a Java program?",
      options: { A: "start()", B: "main()", C: "run()", D: "init()" },
      correct: "B"
    },
    {
      id: 3,
      examId: 2,
      text: "Which language is used for styling web pages?",
      options: { A: "HTML", B: "CSS", C: "SQL", D: "Java" },
      correct: "B"
    }
  ],
  attempts: []
};

let state = loadState();
let currentUser = null;
let activeExam = null;
let activeTimer = null;
let remainingSeconds = 0;

const $ = (selector) => document.querySelector(selector);

function loadState() {
  const saved = localStorage.getItem(STORAGE_KEY);
  return saved ? JSON.parse(saved) : structuredClone(seedData);
}

function saveState() {
  localStorage.setItem(STORAGE_KEY, JSON.stringify(state));
}

function nextId(collection) {
  return collection.length ? Math.max(...collection.map((item) => item.id)) + 1 : 1;
}

$("#loginForm").addEventListener("submit", async (event) => {
  event.preventDefault();
  const email = $("#emailInput").value.trim().toLowerCase();
  const password = $("#passwordInput").value;
  const submitButton = $("#loginForm button[type='submit']");

  submitButton.disabled = true;
  $("#authMessage").textContent = "Signing in...";

  try {
    const user = await loginWithBackend(email, password);
    currentUser = normalizeBackendUser(user, password);
    await syncExamsFromBackend();
    $("#authMessage").textContent = "";
    showDashboard();
  } catch (error) {
    $("#authMessage").textContent = error.message;
  } finally {
    submitButton.disabled = false;
  }
});

$("#logoutButton").addEventListener("click", () => {
  currentUser = null;
  activeExam = null;
  clearInterval(activeTimer);
  $("#authView").classList.remove("hidden");
  $("#dashboardView").classList.add("hidden");
});

function showDashboard() {
  $("#authView").classList.add("hidden");
  $("#dashboardView").classList.remove("hidden");
  $("#welcomeTitle").textContent = `Welcome, ${currentUser.name}`;
  $("#roleLabel").textContent = `${currentUser.role} module`;

  if (currentUser.role === "admin") {
    $("#adminTabs").classList.remove("hidden");
    $("#adminPanel").classList.remove("hidden");
    $("#studentPanel").classList.add("hidden");
    renderAdmin();
  } else {
    $("#adminTabs").classList.add("hidden");
    $("#adminPanel").classList.add("hidden");
    $("#studentPanel").classList.remove("hidden");
    renderStudent();
  }
}

async function loginWithBackend(email, password) {
  const response = await fetch(`${API_BASE_URL}/api/login`, {
    method: "POST",
    headers: {
      "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8"
    },
    body: new URLSearchParams({ email, password }).toString()
  });

  const data = await response.json();
  if (!response.ok) {
    throw new Error(data.message || "Unable to sign in.");
  }

  return data;
}

function normalizeBackendUser(user, password) {
  return {
    ...user,
    password,
    role: user.role.toLowerCase()
  };
}

async function syncExamsFromBackend() {
  const response = await fetch(`${API_BASE_URL}/api/exams`);
  const data = await response.json();

  if (!response.ok) {
    throw new Error(data.details || data.message || "Unable to load exams.");
  }

  state.exams = data.map((exam) => ({
    id: exam.id,
    title: exam.title,
    duration: exam.durationMinutes
  }));
  saveState();
}

document.querySelectorAll(".tab").forEach((button) => {
  button.addEventListener("click", () => {
    document.querySelectorAll(".tab").forEach((tab) => tab.classList.remove("active"));
    document.querySelectorAll(".tab-panel").forEach((panel) => panel.classList.add("hidden"));
    button.classList.add("active");
    $(`#${button.dataset.tab}`).classList.remove("hidden");
    renderAdmin();
  });
});

$("#examForm").addEventListener("submit", (event) => {
  event.preventDefault();
  state.exams.push({
    id: nextId(state.exams),
    title: $("#examTitleInput").value.trim(),
    duration: Number($("#examDurationInput").value)
  });
  saveState();
  event.target.reset();
  $("#examDurationInput").value = 15;
  renderAdmin();
});

$("#questionForm").addEventListener("submit", (event) => {
  event.preventDefault();
  state.questions.push({
    id: nextId(state.questions),
    examId: Number($("#questionExamSelect").value),
    text: $("#questionTextInput").value.trim(),
    options: {
      A: $("#optionAInput").value.trim(),
      B: $("#optionBInput").value.trim(),
      C: $("#optionCInput").value.trim(),
      D: $("#optionDInput").value.trim()
    },
    correct: $("#correctOptionInput").value
  });
  saveState();
  event.target.reset();
  renderAdmin();
});

function renderAdmin() {
  renderExamOptions();
  $("#examList").innerHTML = state.exams.map((exam) => {
    const questionCount = state.questions.filter((question) => question.examId === exam.id).length;
    return `
      <div class="item">
        <div>
          <strong>${exam.title}</strong>
          <p>${exam.duration} minutes, ${questionCount} questions</p>
        </div>
        <span class="badge">Active</span>
      </div>
    `;
  }).join("");

  $("#adminResultsList").innerHTML = state.attempts.length
    ? state.attempts.map(resultCard).join("")
    : `<p class="message">No submissions yet.</p>`;
}

function renderExamOptions() {
  $("#questionExamSelect").innerHTML = state.exams
    .map((exam) => `<option value="${exam.id}">${exam.title}</option>`)
    .join("");
}

function renderStudent() {
  $("#availableExamList").innerHTML = state.exams.map((exam) => {
    const questions = state.questions.filter((question) => question.examId === exam.id);
    return `
      <div class="item">
        <div>
          <strong>${exam.title}</strong>
          <p>${exam.duration} minutes, ${questions.length} questions</p>
        </div>
        <button type="button" onclick="startExam(${exam.id})" ${questions.length === 0 ? "disabled" : ""}>Start</button>
      </div>
    `;
  }).join("");

  const myAttempts = state.attempts.filter((attempt) => attempt.userId === currentUser.id);
  $("#studentResultsList").innerHTML = myAttempts.length
    ? myAttempts.map(resultCard).join("")
    : `<p class="message">No results available.</p>`;
}

function startExam(examId) {
  activeExam = state.exams.find((exam) => exam.id === examId);
  const questions = state.questions.filter((question) => question.examId === examId);
  remainingSeconds = activeExam.duration * 60;

  $("#examAttemptPanel").classList.remove("hidden");
  $("#activeExamTitle").textContent = activeExam.title;
  $("#attemptForm").innerHTML = questions.map((question, index) => `
    <section class="question-card">
      <strong>${index + 1}. ${question.text}</strong>
      <div class="option-list">
        ${Object.entries(question.options).map(([key, value]) => `
          <label>
            <input type="radio" name="question-${question.id}" value="${key}">
            ${key}. ${value}
          </label>
        `).join("")}
      </div>
    </section>
  `).join("") + `<button type="submit">Submit Exam</button>`;

  clearInterval(activeTimer);
  updateTimer();
  activeTimer = setInterval(() => {
    remainingSeconds -= 1;
    updateTimer();
    if (remainingSeconds <= 0) {
      submitAttempt();
    }
  }, 1000);
}

$("#attemptForm").addEventListener("submit", (event) => {
  event.preventDefault();
  submitAttempt();
});

function updateTimer() {
  const minutes = Math.floor(remainingSeconds / 60).toString().padStart(2, "0");
  const seconds = (remainingSeconds % 60).toString().padStart(2, "0");
  $("#timerLabel").textContent = `${minutes}:${seconds}`;
}

function submitAttempt() {
  if (!activeExam) return;

  clearInterval(activeTimer);
  const questions = state.questions.filter((question) => question.examId === activeExam.id);
  const answers = questions.map((question) => {
    const selected = document.querySelector(`input[name="question-${question.id}"]:checked`);
    return {
      questionId: question.id,
      selected: selected ? selected.value : "",
      correct: question.correct
    };
  });
  const score = answers.filter((answer) => answer.selected === answer.correct).length;

  state.attempts.push({
    id: nextId(state.attempts),
    userId: currentUser.id,
    userName: currentUser.name,
    examId: activeExam.id,
    examTitle: activeExam.title,
    total: questions.length,
    score,
    submittedAt: new Date().toLocaleString()
  });

  saveState();
  activeExam = null;
  $("#examAttemptPanel").classList.add("hidden");
  renderStudent();
}

function resultCard(attempt) {
  const percentage = attempt.total ? Math.round((attempt.score / attempt.total) * 100) : 0;
  const scoreClass = percentage >= 50 ? "score-good" : "score-low";
  return `
    <div class="item">
      <div>
        <strong>${attempt.examTitle}</strong>
        <p>${attempt.userName} submitted on ${attempt.submittedAt}</p>
      </div>
      <span class="badge ${scoreClass}">${attempt.score}/${attempt.total} (${percentage}%)</span>
    </div>
  `;
}
