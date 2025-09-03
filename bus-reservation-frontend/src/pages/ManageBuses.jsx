import { useEffect, useState } from "react";
import { Container, Table, Form, Button, Modal } from "react-bootstrap";
import API from "../api/axiosConfig";

export default function ManageBuses() {
  const [buses, setBuses] = useState([]);
  const [form, setForm] = useState({ busNumber: "", operatorName: "", totalSeats: 40, busType: "AC" });
  const [editingBus, setEditingBus] = useState(null); // for update modal

  // Load buses
  const load = async () => {
    try {
      const { data } = await API.get("/admin/buses");
      setBuses(data || []);
    } catch {
      setBuses([]);
    }
  };

  useEffect(() => {
    load();
  }, []);

  // Add new bus
  const add = async () => {
    try {
      await API.post("/admin/buses", form);
      load();
      setForm({ busNumber: "", operatorName: "", totalSeats: 40, busType: "AC" });
    } catch (err) {
      console.error("Add Bus Error:", err.response?.data || err.message);
      alert("Add failed: " + (err.response?.data?.message || err.message));
    }
  };

  // Delete bus
  const remove = async (id) => {
    if (!confirm("Delete bus?")) return;
    try {
      await API.delete(`/admin/buses/${id}`);
      load();
    } catch {
      alert("Delete failed");
    }
  };

  // Update bus
  const updateBus = async () => {
    try {
      await API.put(`/admin/buses/${editingBus.id}`, editingBus);
      setEditingBus(null);
      load();
    } catch (err) {
      console.error("Update Bus Error:", err.response?.data || err.message);
      alert("Update failed: " + (err.response?.data?.message || err.message));
    }
  };

  return (
    <Container className="mt-4">
      <h4>Manage Buses</h4>
      
      {/* Add Bus Form */}
      <div className="d-flex gap-2 mb-3">
        <Form.Control placeholder="Bus Number" value={form.busNumber} onChange={e => setForm({ ...form, busNumber: e.target.value })} style={{ maxWidth: 200 }} />
        <Form.Control placeholder="Operator" value={form.operatorName} onChange={e => setForm({ ...form, operatorName: e.target.value })} style={{ maxWidth: 200 }} />
        <Form.Control placeholder="Seats" type="number" value={form.totalSeats} onChange={e => setForm({ ...form, totalSeats: Number(e.target.value) })} style={{ maxWidth: 120 }} />
        <Form.Select value={form.busType} onChange={e => setForm({ ...form, busType: e.target.value })} style={{ maxWidth: 140 }}>
          <option>AC</option>
          <option>NON_AC</option>
          <option>SLEEPER</option>
        </Form.Select>
        <Button onClick={add}>Add Bus</Button>
      </div>

      {/* Bus List */}
      <Table striped bordered>
        <thead>
          <tr>
            <th>ID</th>
            <th>Bus Number</th>
            <th>Operator</th>
            <th>Seats</th>
            <th>Type</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          {buses.map(b => (
            <tr key={b.id}>
              <td>{b.id}</td>
              <td>{b.busNumber}</td>
              <td>{b.operatorName}</td>
              <td>{b.totalSeats}</td>
              <td>{b.busType}</td>
              <td className="d-flex gap-1">
                <Button size="sm" variant="warning" onClick={() => setEditingBus(b)}>Update</Button>
                <Button size="sm" variant="danger" onClick={() => remove(b.id)}>Delete</Button>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>

      {/* Update Modal */}
      {editingBus && (
        <Modal show={true} onHide={() => setEditingBus(null)}>
          <Modal.Header closeButton>
            <Modal.Title>Update Bus</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Form>
              <Form.Group className="mb-2">
                <Form.Label>Bus Number</Form.Label>
                <Form.Control value={editingBus.busNumber} onChange={e => setEditingBus({ ...editingBus, busNumber: e.target.value })} />
              </Form.Group>
              <Form.Group className="mb-2">
                <Form.Label>Operator Name</Form.Label>
                <Form.Control value={editingBus.operatorName} onChange={e => setEditingBus({ ...editingBus, operatorName: e.target.value })} />
              </Form.Group>
              <Form.Group className="mb-2">
                <Form.Label>Total Seats</Form.Label>
                <Form.Control type="number" value={editingBus.totalSeats} onChange={e => setEditingBus({ ...editingBus, totalSeats: Number(e.target.value) })} />
              </Form.Group>
              <Form.Group className="mb-2">
                <Form.Label>Bus Type</Form.Label>
                <Form.Select value={editingBus.busType} onChange={e => setEditingBus({ ...editingBus, busType: e.target.value })}>
                  <option>AC</option>
                  <option>NON_AC</option>
                  <option>SLEEPER</option>
                </Form.Select>
              </Form.Group>
            </Form>
          </Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={() => setEditingBus(null)}>Cancel</Button>
            <Button variant="primary" onClick={updateBus}>Save</Button>
          </Modal.Footer>
        </Modal>
      )}
    </Container>
  );
}
