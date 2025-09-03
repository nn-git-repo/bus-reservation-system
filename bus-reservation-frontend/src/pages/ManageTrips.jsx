import { useEffect, useState } from "react";
import { Container, Table, Form, Button } from "react-bootstrap";
import API from "../api/axiosConfig";

export default function ManageTrips() {
  const [trips, setTrips] = useState([]);
  const [form, setForm] = useState({
    id: null,
    busId: "",
    routeId: "",
    departureTime: "",
    arrivalTime: "",
    baseFare: 0,
  });

  // 🔹 Load trips
  const load = async () => {
    try {
      const { data } = await API.get("/admin/trips");
      setTrips(data || []);
    } catch {
      setTrips([]);
    }
  };
  useEffect(() => {
    load();
  }, []);

  // 🔹 Add or Update Trip
  const save = async () => {
    try {
      const payload = {
        busId: Number(form.busId),
        routeId: Number(form.routeId),
        departureTime: new Date(form.departureTime).toISOString(),
        arrivalTime: new Date(form.arrivalTime).toISOString(),
        baseFare: Number(form.baseFare),
      };

      if (form.id) {
        // Update existing trip
        await API.put(`/admin/trips/${form.id}`, payload);
      } else {
        // Add new trip
        await API.post("/admin/trips", payload);
      }

      resetForm();
      load();
    } catch (err) {
      console.error("Save failed ❌", err);
      alert("Save failed");
    }
  };

  // 🔹 Edit trip handler
  const edit = (trip) => {
    setForm({
      id: trip.id,
      busId: trip.bus?.id || "",
      routeId: trip.route?.id || "",
      departureTime: trip.departureTime
        ? trip.departureTime.slice(0, 16) // convert ISO → datetime-local
        : "",
      arrivalTime: trip.arrivalTime
        ? trip.arrivalTime.slice(0, 16)
        : "",
      baseFare: trip.baseFare || 0,
    });
  };

  // 🔹 Delete trip handler
  const remove = async (id) => {
    if (!window.confirm("Are you sure you want to delete this trip?")) return;
    try {
      await API.delete(`/admin/trips/${id}`);
      load();
    } catch (err) {
      console.error("Delete failed ❌", err);
      alert("Delete failed");
    }
  };

  // 🔹 Reset form
  const resetForm = () => {
    setForm({
      id: null,
      busId: "",
      routeId: "",
      departureTime: "",
      arrivalTime: "",
      baseFare: 0,
    });
  };

  return (
    <Container className="mt-4">
      <h4>Manage Trips</h4>

      {/* Form Section */}
      <div className="d-flex gap-2 mb-3 flex-wrap">
        <Form.Control
          placeholder="Bus ID"
          value={form.busId}
          onChange={(e) => setForm({ ...form, busId: e.target.value })}
          style={{ maxWidth: 120 }}
        />
        <Form.Control
          placeholder="Route ID"
          value={form.routeId}
          onChange={(e) => setForm({ ...form, routeId: e.target.value })}
          style={{ maxWidth: 120 }}
        />

        <label>Departure Time: </label>
        <Form.Control
          type="datetime-local"
          value={form.departureTime}
          onChange={(e) =>
            setForm({ ...form, departureTime: e.target.value })
          }
          style={{ maxWidth: 220 }}
        />

        <label>Arrival Time: </label>
        
        <Form.Control
          type="datetime-local"
          value={form.arrivalTime}
          onChange={(e) => setForm({ ...form, arrivalTime: e.target.value })}
          style={{ maxWidth: 220 }}
        />
        <Form.Control
          placeholder="Fare"
          value={form.baseFare}
          onChange={(e) => setForm({ ...form, baseFare: e.target.value })}
          style={{ maxWidth: 120 }}
        />

        <Button onClick={save}>
          {form.id ? "Update Trip" : "Add Trip"}
        </Button>
        {form.id && (
          <Button variant="secondary" onClick={resetForm}>
            Cancel
          </Button>
        )}
      </div>

      {/* Trips Table */}
      <Table striped bordered>
        <thead>
          <tr>
            <th>ID</th>
            <th>Route</th>
            <th>Bus</th>
            <th>Departure</th>
            <th>Fare</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {trips.map((t) => (
            <tr key={t.id}>
              <td>{t.id}</td>
              <td>
                {t?.route?.source} → {t?.route?.destination}
              </td>
              <td>{t?.bus?.busNumber || t?.bus?.name}</td>
              <td>{t.departureTime}</td>
              <td>₹{t.baseFare}</td>
              <td>
                <Button
                  size="sm"
                  variant="warning"
                  className="me-2"
                  onClick={() => edit(t)}
                >
                  Edit
                </Button>
                <Button
                  size="sm"
                  variant="danger"
                  onClick={() => remove(t.id)}
                >
                  Delete
                </Button>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>
    </Container>
  );
}
