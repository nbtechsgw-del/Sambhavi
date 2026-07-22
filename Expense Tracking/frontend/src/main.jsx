import React, { useEffect, useMemo, useState } from "react";
import { createRoot } from "react-dom/client";
import {
  BarChart3,
  CalendarDays,
  Download,
  Edit3,
  Eye,
  EyeOff,
  Filter,
  LogOut,
  Moon,
  Plus,
  RefreshCw,
  Search,
  Sun,
  Trash2,
  WalletCards,
} from "lucide-react";
import {
  Bar,
  BarChart,
  CartesianGrid,
  Cell,
  Pie,
  PieChart,
  ResponsiveContainer,
  Tooltip,
  XAxis,
  YAxis,
} from "recharts";
import "./styles.css";

const API = import.meta.env.VITE_API_URL || "http://localhost:8080/api";
const money = new Intl.NumberFormat("en-IN", { style: "currency", currency: "INR" });
const today = new Date().toISOString().slice(0, 10);
const firstOfMonth = `${today.slice(0, 8)}01`;

function apiClient(token) {
  async function request(path, options = {}) {
    const response = await fetch(`${API}${path}`, {
      ...options,
      headers: {
        "Content-Type": "application/json",
        ...(token ? { "X-Auth-Token": token } : {}),
        ...(options.headers || {}),
      },
    });
    if (!response.ok) {
      const body = await response.json().catch(() => ({}));
      throw new Error(body.message || "Request failed");
    }
    if (response.status === 204) return null;
    const text = await response.text();
    return text ? JSON.parse(text) : null;
  }
  return {
    get: (path) => request(path),
    post: (path, body) => request(path, { method: "POST", body: JSON.stringify(body) }),
    put: (path, body) => request(path, { method: "PUT", body: JSON.stringify(body) }),
    del: (path) => request(path, { method: "DELETE" }),
  };
}

function App() {
  const [session, setSession] = useState(() => JSON.parse(localStorage.getItem("expense-session") || "null"));
  const [dark, setDark] = useState(() => localStorage.getItem("expense-theme") === "dark");

  useEffect(() => {
    document.documentElement.dataset.theme = dark ? "dark" : "light";
    localStorage.setItem("expense-theme", dark ? "dark" : "light");
  }, [dark]);

  function saveSession(next) {
    setSession(next);
    if (next) localStorage.setItem("expense-session", JSON.stringify(next));
    else localStorage.removeItem("expense-session");
  }

  if (!session) {
    return <AuthScreen onAuth={saveSession} dark={dark} setDark={setDark} />;
  }

  return <Workspace session={session} onLogout={() => saveSession(null)} dark={dark} setDark={setDark} />;
}

function AuthScreen({ onAuth, dark, setDark }) {
  const [mode, setMode] = useState("login");
  const [showPassword, setShowPassword] = useState(false);
  const [message, setMessage] = useState("");
  const [form, setForm] = useState({ fullName: "", email: "demo@expense.local", password: "password" });
  const client = apiClient();

  async function submit(event) {
    event.preventDefault();
    setMessage("");
    try {
      const payload = mode === "register" ? form : { email: form.email, password: form.password };
      const data = await client.post(`/auth/${mode}`, payload);
      onAuth(data);
    } catch (error) {
      setMessage(error.message);
    }
  }

  async function forgotPassword() {
    setMessage("");
    try {
      const data = await client.post("/auth/forgot-password", { email: form.email });
      setMessage(data.message);
    } catch (error) {
      setMessage(error.message);
    }
  }

  return (
    <main className="auth-shell">
      <section className="auth-panel">
        <button className="icon-button theme-float" onClick={() => setDark(!dark)} title="Toggle theme">
          {dark ? <Sun size={20} /> : <Moon size={20} />}
        </button>
        <div className="brand-row">
          <WalletCards size={34} />
          <div>
            <h1>Expense Tracker</h1>
            <p>Track income, expenses, reports, and monthly balance.</p>
          </div>
        </div>

        <div className="segmented">
          <button className={mode === "login" ? "active" : ""} onClick={() => setMode("login")}>Login</button>
          <button className={mode === "register" ? "active" : ""} onClick={() => setMode("register")}>Register</button>
        </div>

        <form onSubmit={submit} className="form-grid">
          {mode === "register" && (
            <label>
              Full name
              <input value={form.fullName} onChange={(e) => setForm({ ...form, fullName: e.target.value })} required />
            </label>
          )}
          <label>
            Email
            <input type="email" value={form.email} onChange={(e) => setForm({ ...form, email: e.target.value })} required />
          </label>
          <label>
            Password
            <span className="password-field">
              <input type={showPassword ? "text" : "password"} value={form.password} onChange={(e) => setForm({ ...form, password: e.target.value })} minLength={6} required />
              <button type="button" className="icon-button" onClick={() => setShowPassword(!showPassword)} title="Show password">
                {showPassword ? <EyeOff size={18} /> : <Eye size={18} />}
              </button>
            </span>
          </label>
          <button className="primary" type="submit">{mode === "login" ? "Login" : "Create account"}</button>
        </form>

        <div className="auth-actions">
          <button className="text-button" onClick={() => setForm({ ...form, email: "demo@expense.local", password: "password" })}>Use demo login</button>
          <button className="text-button" onClick={forgotPassword}>Forgot password</button>
        </div>
        {message && <p className="message">{message}</p>}
      </section>
    </main>
  );
}

function Workspace({ session, onLogout, dark, setDark }) {
  const client = useMemo(() => apiClient(session.token), [session.token]);
  const [active, setActive] = useState("dashboard");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [dashboard, setDashboard] = useState(null);
  const [categories, setCategories] = useState([]);
  const [transactions, setTransactions] = useState([]);
  const [report, setReport] = useState(null);
  const [filters, setFilters] = useState({ from: firstOfMonth, to: today, categoryId: "", type: "", search: "" });

  async function load() {
    setLoading(true);
    setError("");
    try {
      const params = new URLSearchParams(Object.entries(filters).filter(([, value]) => value));
      const [dash, cats, txs, rep] = await Promise.all([
        client.get("/dashboard"),
        client.get("/categories"),
        client.get(`/transactions?${params}`),
        client.get(`/reports?from=${filters.from}&to=${filters.to}`),
      ]);
      setDashboard(dash);
      setCategories(cats);
      setTransactions(txs);
      setReport(rep);
    } catch (error) {
      setError(error.message);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    load();
  }, [filters.from, filters.to, filters.categoryId, filters.type]);

  async function applySearch(event) {
    event.preventDefault();
    await load();
  }

  async function logout() {
    await client.post("/auth/logout", {}).catch(() => {});
    onLogout();
  }

  return (
    <main className="app-shell">
      <aside className="sidebar">
        <div className="brand-row compact">
          <WalletCards size={28} />
          <div>
            <h1>Expense Tracker</h1>
            <p>{session.user.fullName}</p>
          </div>
        </div>
        <nav>
          {["dashboard", "transactions", "categories", "reports"].map((item) => (
            <button key={item} className={active === item ? "active" : ""} onClick={() => setActive(item)}>
              {item === "dashboard" && <BarChart3 size={18} />}
              {item === "transactions" && <WalletCards size={18} />}
              {item === "categories" && <Filter size={18} />}
              {item === "reports" && <CalendarDays size={18} />}
              <span>{title(item)}</span>
            </button>
          ))}
        </nav>
        <div className="sidebar-actions">
          <button className="icon-button" onClick={() => setDark(!dark)} title="Toggle theme">{dark ? <Sun size={18} /> : <Moon size={18} />}</button>
          <button className="icon-button" onClick={load} title="Refresh"><RefreshCw size={18} /></button>
          <button className="icon-button" onClick={logout} title="Logout"><LogOut size={18} /></button>
        </div>
      </aside>

      <section className="content">
        <header className="topbar">
          <div>
            <p className="eyebrow">{new Date().toLocaleDateString("en-IN", { dateStyle: "full" })}</p>
            <h2>{title(active)}</h2>
          </div>
          <form className="searchbar" onSubmit={applySearch}>
            <Search size={18} />
            <input value={filters.search} onChange={(e) => setFilters({ ...filters, search: e.target.value })} placeholder="Search transactions" />
          </form>
        </header>

        {error && <p className="banner">{error}</p>}
        {loading && <p className="banner">Loading latest finance data...</p>}
        {!loading && active === "dashboard" && <Dashboard dashboard={dashboard} report={report} />}
        {!loading && active === "transactions" && <Transactions client={client} categories={categories} transactions={transactions} filters={filters} setFilters={setFilters} reload={load} />}
        {!loading && active === "categories" && <Categories client={client} categories={categories} reload={load} />}
        {!loading && active === "reports" && <Reports report={report} filters={filters} setFilters={setFilters} transactions={transactions} />}
      </section>
    </main>
  );
}

function Dashboard({ dashboard, report }) {
  const bars = Object.entries(report.monthlyExpenses || {}).map(([name, value]) => ({ name, value: Number(value) }));
  return (
    <>
      <section className="stats-grid">
        <Stat label="Total Income" value={dashboard.totalIncome} tone="income" />
        <Stat label="Total Expenses" value={dashboard.totalExpenses} tone="expense" />
        <Stat label="Current Balance" value={dashboard.currentBalance} tone="balance" />
        <Stat label="This Month" value={Number(dashboard.monthIncome) - Number(dashboard.monthExpenses)} tone="month" />
      </section>
      <section className="split-layout">
        <div className="panel">
          <h3>Monthly Expenses</h3>
          <div className="chart-box">
            <ResponsiveContainer width="100%" height="100%">
              <BarChart data={bars}>
                <CartesianGrid strokeDasharray="3 3" vertical={false} />
                <XAxis dataKey="name" />
                <YAxis />
                <Tooltip formatter={(value) => money.format(value)} />
                <Bar dataKey="value" fill="#2d9cdb" radius={[6, 6, 0, 0]} />
              </BarChart>
            </ResponsiveContainer>
          </div>
        </div>
        <RecentTransactions items={dashboard.recentTransactions} />
      </section>
    </>
  );
}

function Stat({ label, value, tone }) {
  return (
    <article className={`stat ${tone}`}>
      <span>{label}</span>
      <strong>{money.format(Number(value || 0))}</strong>
    </article>
  );
}

function RecentTransactions({ items }) {
  return (
    <div className="panel">
      <h3>Recent Transactions</h3>
      <div className="list">
        {items.map((item) => (
          <div className="list-row" key={item.id}>
            <div>
              <strong>{item.description || item.categoryName}</strong>
              <span>{item.categoryName} · {item.transactionDate}</span>
            </div>
            <b className={item.type.toLowerCase()}>{item.type === "INCOME" ? "+" : "-"}{money.format(Number(item.amount))}</b>
          </div>
        ))}
      </div>
    </div>
  );
}

function Transactions({ client, categories, transactions, filters, setFilters, reload }) {
  const emptyForm = { id: null, categoryId: "", amount: "", type: "EXPENSE", description: "", transactionDate: today, recurring: false };
  const [form, setForm] = useState(emptyForm);
  const visibleCategories = categories.filter((c) => c.type === form.type);

  async function save(event) {
    event.preventDefault();
    const payload = { ...form, categoryId: Number(form.categoryId), amount: Number(form.amount) };
    if (form.id) await client.put(`/transactions/${form.id}`, payload);
    else await client.post("/transactions", payload);
    setForm(emptyForm);
    await reload();
  }

  async function remove(id) {
    await client.del(`/transactions/${id}`);
    await reload();
  }

  function edit(item) {
    setForm({ ...item, amount: String(item.amount), categoryId: String(item.categoryId) });
  }

  function exportCsv() {
    const rows = [["Date", "Type", "Category", "Description", "Amount", "Recurring"], ...transactions.map((t) => [t.transactionDate, t.type, t.categoryName, t.description, t.amount, t.recurring ? "Yes" : "No"])];
    download("transactions.csv", rows);
  }

  return (
    <>
      <section className="filters">
        <label>From<input type="date" value={filters.from} onChange={(e) => setFilters({ ...filters, from: e.target.value })} /></label>
        <label>To<input type="date" value={filters.to} onChange={(e) => setFilters({ ...filters, to: e.target.value })} /></label>
        <label>Type<select value={filters.type} onChange={(e) => setFilters({ ...filters, type: e.target.value })}><option value="">All</option><option>INCOME</option><option>EXPENSE</option></select></label>
        <label>Category<select value={filters.categoryId} onChange={(e) => setFilters({ ...filters, categoryId: e.target.value })}><option value="">All</option>{categories.map((c) => <option key={c.id} value={c.id}>{c.name}</option>)}</select></label>
        <button className="icon-button" onClick={exportCsv} title="Export CSV"><Download size={18} /></button>
      </section>

      <section className="form-panel">
        <h3>{form.id ? "Edit Transaction" : "Add Transaction"}</h3>
        <form className="transaction-form" onSubmit={save}>
          <select value={form.type} onChange={(e) => setForm({ ...form, type: e.target.value, categoryId: "" })}><option>EXPENSE</option><option>INCOME</option></select>
          <select value={form.categoryId} onChange={(e) => setForm({ ...form, categoryId: e.target.value })} required><option value="">Category</option>{visibleCategories.map((c) => <option key={c.id} value={c.id}>{c.name}</option>)}</select>
          <input type="number" step="0.01" min="0.01" placeholder="Amount" value={form.amount} onChange={(e) => setForm({ ...form, amount: e.target.value })} required />
          <input type="date" value={form.transactionDate} onChange={(e) => setForm({ ...form, transactionDate: e.target.value })} required />
          <input placeholder="Description" value={form.description} onChange={(e) => setForm({ ...form, description: e.target.value })} />
          <label className="check"><input type="checkbox" checked={form.recurring} onChange={(e) => setForm({ ...form, recurring: e.target.checked })} /> Recurring</label>
          <button className="primary" type="submit"><Plus size={17} />{form.id ? "Update" : "Add"}</button>
        </form>
      </section>

      <DataTable rows={transactions} onEdit={edit} onDelete={remove} />
    </>
  );
}

function Categories({ client, categories, reload }) {
  const [form, setForm] = useState({ id: null, name: "", type: "EXPENSE" });
  async function save(event) {
    event.preventDefault();
    if (form.id) await client.put(`/categories/${form.id}`, form);
    else await client.post("/categories", form);
    setForm({ id: null, name: "", type: "EXPENSE" });
    await reload();
  }
  async function remove(id) {
    await client.del(`/categories/${id}`);
    await reload();
  }
  return (
    <>
      <section className="form-panel">
        <h3>{form.id ? "Edit Category" : "Create Category"}</h3>
        <form className="category-form" onSubmit={save}>
          <input placeholder="Category name" value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })} required />
          <select value={form.type} onChange={(e) => setForm({ ...form, type: e.target.value })}><option>EXPENSE</option><option>INCOME</option></select>
          <button className="primary" type="submit"><Plus size={17} />Save</button>
        </form>
      </section>
      <section className="category-grid">
        {categories.map((category) => (
          <article className="category-card" key={category.id}>
            <div>
              <strong>{category.name}</strong>
              <span>{category.type}</span>
            </div>
            <div className="row-actions">
              <button className="icon-button" title="Edit" onClick={() => setForm(category)}><Edit3 size={17} /></button>
              <button className="icon-button danger" title="Delete" onClick={() => remove(category.id)}><Trash2 size={17} /></button>
            </div>
          </article>
        ))}
      </section>
    </>
  );
}

function Reports({ report, filters, setFilters, transactions }) {
  const categoryData = Object.entries(report.categoryExpenses || {}).map(([name, value]) => ({ name, value: Number(value) }));
  const incomeExpense = Object.entries(report.incomeVsExpense || {}).map(([name, value]) => ({ name, value: Number(value) }));
  const colors = ["#2d9cdb", "#f2994a", "#27ae60", "#eb5757", "#9b51e0", "#00a878"];

  function exportCsv() {
    const rows = [["Metric", "Value"], ["Income", report.totalIncome], ["Expenses", report.totalExpenses], ["Balance", report.balance], [], ["Date", "Type", "Category", "Description", "Amount"], ...transactions.map((t) => [t.transactionDate, t.type, t.categoryName, t.description, t.amount])];
    download("expense-report.csv", rows);
  }

  return (
    <>
      <section className="filters">
        <label>From<input type="date" value={filters.from} onChange={(e) => setFilters({ ...filters, from: e.target.value })} /></label>
        <label>To<input type="date" value={filters.to} onChange={(e) => setFilters({ ...filters, to: e.target.value })} /></label>
        <button className="icon-button" onClick={exportCsv} title="Export CSV"><Download size={18} /></button>
      </section>
      <section className="stats-grid">
        <Stat label="Income" value={report.totalIncome} tone="income" />
        <Stat label="Expenses" value={report.totalExpenses} tone="expense" />
        <Stat label="Balance" value={report.balance} tone="balance" />
      </section>
      <section className="split-layout">
        <div className="panel">
          <h3>Category-wise Expenses</h3>
          <div className="chart-box">
            <ResponsiveContainer width="100%" height="100%">
              <PieChart>
                <Pie data={categoryData} dataKey="value" nameKey="name" innerRadius={58} outerRadius={104}>
                  {categoryData.map((_, index) => <Cell key={index} fill={colors[index % colors.length]} />)}
                </Pie>
                <Tooltip formatter={(value) => money.format(value)} />
              </PieChart>
            </ResponsiveContainer>
          </div>
        </div>
        <div className="panel">
          <h3>Income vs Expense</h3>
          <div className="chart-box">
            <ResponsiveContainer width="100%" height="100%">
              <BarChart data={incomeExpense}>
                <CartesianGrid strokeDasharray="3 3" vertical={false} />
                <XAxis dataKey="name" />
                <YAxis />
                <Tooltip formatter={(value) => money.format(value)} />
                <Bar dataKey="value" fill="#27ae60" radius={[6, 6, 0, 0]} />
              </BarChart>
            </ResponsiveContainer>
          </div>
        </div>
      </section>
    </>
  );
}

function DataTable({ rows, onEdit, onDelete }) {
  return (
    <section className="table-wrap">
      <table>
        <thead>
          <tr><th>Date</th><th>Type</th><th>Category</th><th>Description</th><th>Amount</th><th>Recurring</th><th></th></tr>
        </thead>
        <tbody>
          {rows.map((row) => (
            <tr key={row.id}>
              <td>{row.transactionDate}</td>
              <td><span className={`pill ${row.type.toLowerCase()}`}>{row.type}</span></td>
              <td>{row.categoryName}</td>
              <td>{row.description}</td>
              <td className={row.type.toLowerCase()}>{money.format(Number(row.amount))}</td>
              <td>{row.recurring ? "Yes" : "No"}</td>
              <td>
                <div className="row-actions">
                  <button className="icon-button" title="Edit" onClick={() => onEdit(row)}><Edit3 size={17} /></button>
                  <button className="icon-button danger" title="Delete" onClick={() => onDelete(row.id)}><Trash2 size={17} /></button>
                </div>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </section>
  );
}

function download(name, rows) {
  const csv = rows.map((row) => row.map((cell) => `"${String(cell ?? "").replaceAll('"', '""')}"`).join(",")).join("\n");
  const blob = new Blob([csv], { type: "text/csv;charset=utf-8" });
  const url = URL.createObjectURL(blob);
  const link = document.createElement("a");
  link.href = url;
  link.download = name;
  link.click();
  URL.revokeObjectURL(url);
}

function title(value) {
  return value.charAt(0).toUpperCase() + value.slice(1);
}

createRoot(document.getElementById("root")).render(<App />);
