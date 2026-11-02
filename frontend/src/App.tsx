import React, { useEffect, useState } from "react";
import "./App.css";

type Greeting = { id: number; name: string };
type Summary = { service: string; database: string; totalGreetings: number };

const apiBase = (process.env.REACT_APP_API_URL || "/api").replace(/\/$/, "");

async function getJson<T>(path: string): Promise<T> {
  const response = await fetch(`${apiBase}${path}`);
  if (!response.ok) throw new Error(`API trả về HTTP ${response.status}`);
  return response.json();
}

function App() {
  const [greetings, setGreetings] = useState<Greeting[]>([]);
  const [summary, setSummary] = useState<Summary | null>(null);
  const [selected, setSelected] = useState<Greeting | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    let active = true;
    Promise.all([getJson<Greeting[]>("/greetings"), getJson<Summary>("/summary")])
      .then(([items, overview]) => {
        if (!active) return;
        setGreetings(items);
        setSummary(overview);
        setSelected(items[0] || null);
      })
      .catch((reason: Error) => {
        if (active) setError(reason.message || "Không kết nối được API");
      })
      .finally(() => {
        if (active) setLoading(false);
      });
    return () => { active = false; };
  }, []);

  return (
    <div className="app-shell">
      <header className="topbar">
        <div className="brand"><span className="brand-mark">D</span><span>Deploy Lab</span></div>
        <span className="topbar-note">React · Spring Boot · MariaDB</span>
      </header>

      <main className="page-content">
        <section className="hero">
          <div>
            <p className="eyebrow">CI / CD PRACTICE</p>
            <h1>Một thay đổi.<br /><span>Hai nơi triển khai.</span></h1>
            <p className="hero-copy">Giao diện trên GitHub Pages, API và dữ liệu trên Ubuntu VM. Chọn một bản ghi để kiểm tra đường đi từ trình duyệt đến MariaDB.</p>
          </div>
          <div className="hero-aside" aria-label="Sơ đồ triển khai">
            <div><span>01</span><strong>GitHub Pages</strong><small>Frontend</small></div>
            <div><span>02</span><strong>Ubuntu VM</strong><small>Backend API</small></div>
            <div><span>03</span><strong>MariaDB</strong><small>Dữ liệu</small></div>
          </div>
        </section>

        <section className="workspace" aria-label="Kiểm tra dữ liệu">
          <div className="section-heading">
            <div><p className="eyebrow">LIVE DATA</p><h2>Dữ liệu đang phục vụ</h2></div>
            <span className={`status ${error ? "status-error" : loading ? "status-loading" : "status-ok"}`} role="status">
              {error ? "API lỗi" : loading ? "Đang kết nối" : "API hoạt động"}
            </span>
          </div>

          {loading && <p className="feedback">Đang tải dữ liệu từ API...</p>}
          {error && <div className="feedback feedback-error" role="alert">Không tải được dữ liệu: {error}. Kiểm tra địa chỉ API và CORS.</div>}
          {!loading && !error && <div className="data-layout">
            <div className="record-panel">
              <div className="panel-heading"><span>BẢN GHI MARIADB</span><span>{summary?.totalGreetings ?? 0} mục</span></div>
              {greetings.length === 0 ? <p className="empty">Chưa có bản ghi nào.</p> : greetings.map(item =>
                <button className={`record ${selected?.id === item.id ? "record-active" : ""}`} key={item.id} onClick={() => setSelected(item)} type="button">
                  <span className="record-id">{String(item.id).padStart(2, "0")}</span><span>{item.name}</span><span className="record-arrow">↗</span>
                </button>
              )}
            </div>
            <div className="detail-panel">
              <p className="detail-label">BẢN GHI ĐANG CHỌN</p>
              <strong>{selected?.name || "Chưa chọn"}</strong>
              <p>{selected ? `ID ${selected.id} được đọc từ ${summary?.database || "MariaDB"} qua ${summary?.service || "API"}.` : "Chọn một bản ghi ở danh sách bên cạnh."}</p>
              <div className="detail-line"><span>API endpoint</span><code>/greetings/{selected?.id || ":id"}</code></div>
            </div>
          </div>}
        </section>
      </main>
      <footer>Deploy Lab <span>Thử nghiệm triển khai frontend và backend</span></footer>
    </div>
  );
}

export default App;
