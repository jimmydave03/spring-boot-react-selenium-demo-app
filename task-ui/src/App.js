import React, { useEffect, useState } from "react";
import axios from "axios";

const API = "http://localhost:8080/api/tasks";

export default function App() {
  const [tasks, setTasks] = useState([]);
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");

  const loadTasks = async () => {
    const res = await axios.get(API);
    setTasks(res.data);
  };

  useEffect(() => {
    loadTasks();
  }, []);

  const createTask = async (e) => {
    e.preventDefault();
    if (!title.trim()) return;

    await axios.post(API, { title, description });
    setTitle("");
    setDescription("");
    loadTasks();
  };

  const toggleTask = async (id) => {
    await axios.patch(`${API}/${id}/toggle`);
    loadTasks();
  };

  const deleteTask = async (id) => {
    await axios.delete(`${API}/${id}`);
    loadTasks();
  };

  return (
    <div style={{ maxWidth: 700, margin: "40px auto", fontFamily: "Arial" }}>
      <h2>✅ Task Manager</h2>

      <form onSubmit={createTask} style={{ marginBottom: 20 }}>
        <input data-testid="task-title"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
          placeholder="Task title"
          style={{ width: "100%", padding: 10, marginBottom: 10 }}
        />

        <textarea data-testid="task-desc"
          value={description}
          onChange={(e) => setDescription(e.target.value)}
          placeholder="Task description"
          style={{ width: "100%", padding: 10, marginBottom: 10 }}
        />

        <button data-testid="create-task-btn" style={{ padding: "10px 18px" }}>Create Task</button>
      </form>

      <h3>Tasks</h3>
      {tasks.length === 0 && <p>No tasks yet.</p>}

      {tasks.map((t) => (
        <div data-testid={`task-item-${t.id}`} key={t.id} style={{
            padding: 12,
            border: "1px solid #ddd",
            marginBottom: 10,
            borderRadius: 8,
            display: "flex",
            justifyContent: "space-between",
            alignItems: "center",
          }}
        >
          <div>
            <div
              style={{
                fontWeight: "bold",
                textDecoration: t.completed ? "line-through" : "none",
              }}
            >
              {t.title}
            </div>
            <div style={{ fontSize: 14, color: "#555" }}>{t.description}</div>
          </div>

          <div style={{ display: "flex", gap: 8 }}>
            <button data-testid={`task-toggle-${t.id}`} onClick={() => toggleTask(t.id)}>
              {t.completed ? "Undo" : "Done"}
            </button>
            <button data-testid={`task-delete-${t.id}`} onClick={() => deleteTask(t.id)}>Delete</button>
          </div>
        </div>
      ))}
    </div>
  );
}
