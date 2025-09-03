import { useEffect, useState } from "react";
import { Container, Table, Form, Button, Modal } from "react-bootstrap";
import API from "../api/axiosConfig";

export default function ManageRoutes() {
  const [routes, setRoutes] = useState([]);
  const [r, setR] = useState({ source: "", destination: "", distanceKm: "", durationMin: "" });
  const [editingRoute, setEditingRoute] = useState(null);

  const load = async () => {
    try {
      const { data } = await API.get("/admin/routes");
      setRoutes(data || []);
    } catch {
      setRoutes([]);
    }
  };

  useEffect(() => { load(); }, []);

  const add = async () => {
    try {
      await API.post("/admin/routes", {
        ...r,
        distanceKm: Number(r.distanceKm),
        durationMin: Number(r.durationMin)
      });
      setR({ source: "", destination: "", distanceKm: "", durationMin: "" });
      load();
    } catch { alert("Add failed"); }
  };

  const remove = async (id) => {
    if (!confirm("Delete route?")) return;
    try {
      await API.delete(`/admin/routes/${id}`);
      load();
    } catch { alert("Delete failed"); }
  };

  const updateRoute = async () => {
    try {
      await API.put(`/admin/routes/${editingRoute.id}`, {
        ...editingRoute,
        distanceKm: Number(editingRoute.distanceKm),
        durationMin: Number(editingRoute.durationMin)
      });
      setEditingRoute(null);
      load();
    } catch { alert("Update failed"); }
  };

  return (
    <Container className="mt-4">
      <h4>Manage Routes</h4>

      {/* Add Route */}
      <div className="d-flex gap-2 mb-3">
        <Form.Control placeholder="Source" value={r.source} onChange={e => setR({ ...r, source: e.target.value })}/>
        <Form.Control placeholder="Destination" value={r.destination} onChange={e => setR({ ...r, destination: e.target.value })}/>
        <Form.Control placeholder="Distance Km" type="number" value={r.distanceKm} onChange={e => setR({ ...r, distanceKm: e.target.value })} style={{ maxWidth: 130 }}/>
        <Form.Control placeholder="Duration Min" type="number" value={r.durationMin} onChange={e => setR({ ...r, durationMin: e.target.value })} style={{ maxWidth: 140 }}/>
        <Button onClick={add}>Add Route</Button>
      </div>

      {/* Routes Table */}
      <Table striped bordered>
        <thead>
          <tr><th>ID</th><th>Source</th><th>Destination</th><th>Distance</th><th>Duration</th><th>Actions</th></tr>
        </thead>
        <tbody>
          {routes.map(rt => (
            <tr key={rt.id}>
              <td>{rt.id}</td>
              <td>{rt.source}</td>
              <td>{rt.destination}</td>
              <td>{rt.distanceKm}</td>
              <td>{rt.durationMin}</td>
              <td className="d-flex gap-1">
                <Button size="sm" variant="warning" onClick={() => 
                  setEditingRoute({
                    ...rt,
                    distanceKm: rt.distanceKm?.toString() || "",
                    durationMin: rt.durationMin?.toString() || ""
                  })
                }>Update</Button>
                <Button size="sm" variant="danger" onClick={() => remove(rt.id)}>Delete</Button>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>

      {/* Update Modal */}
      {editingRoute && (
        <Modal show={true} onHide={() => setEditingRoute(null)}>
          <Modal.Header closeButton><Modal.Title>Update Route</Modal.Title></Modal.Header>
          <Modal.Body>
            <Form>
              <Form.Group className="mb-2">
                <Form.Label>Source</Form.Label>
                <Form.Control 
                  value={editingRoute.source} 
                  onChange={e => setEditingRoute({ ...editingRoute, source: e.target.value })}
                />
              </Form.Group>

              <Form.Group className="mb-2">
                <Form.Label>Destination</Form.Label>
                <Form.Control 
                  value={editingRoute.destination} 
                  onChange={e => setEditingRoute({ ...editingRoute, destination: e.target.value })}
                />
              </Form.Group>

              <Form.Group className="mb-2">
                <Form.Label>Distance Km</Form.Label>
                <Form.Control 
                  type="number"
                  value={editingRoute.distanceKm} 
                  onChange={e => setEditingRoute({ ...editingRoute, distanceKm: e.target.value })}
                  placeholder="Enter distance in km"
                />
              </Form.Group>

              <Form.Group className="mb-2">
                <Form.Label>Duration </Form.Label>
                <Form.Control 
                  type="number"
                  value={editingRoute.durationMin} 
                  onChange={e => setEditingRoute({ ...editingRoute, durationMin: e.target.value })}
                  placeholder="Enter duration in minutes"
                />
              </Form.Group>
            </Form>
          </Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={() => setEditingRoute(null)}>Cancel</Button>
            <Button variant="primary" onClick={updateRoute}>Save</Button>
          </Modal.Footer>
        </Modal>
      )}
    </Container>
  );
}
