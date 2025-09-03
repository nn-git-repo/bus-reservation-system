import { useEffect, useState } from "react";
import API from "../api/axiosConfig";
import { Table, Container, Button } from "react-bootstrap";

export default function AdminReports() {
  const [reports, setReports] = useState([]);
  const [loading, setLoading] = useState(true);

  // Fetch all reports
  const fetchReports = async () => {
    try {
      const { data } = await API.get("/admin/reports"); // /api/v1/admin/reports handled in axiosConfig
      setReports(data);
    } catch (err) {
      console.error("Failed to fetch reports:", err);
      alert("Failed to fetch reports.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchReports();
  }, []);

  // Soft delete / cancel booking
  const cancelBooking = async (id) => {
    if (!window.confirm("Are you sure you want to cancel this booking?")) return;
    try {
      await API.delete(`/admin/reports/${id}`);
      alert("Booking cancelled ✅");
      fetchReports(); // reload table after cancelling
    } catch (err) {
      console.error("Cancel failed ❌", err);
      alert("Cancel failed");
    }
  };

  if (loading) return <Container className="mt-4">Loading reports...</Container>;

  return (
    <Container className="mt-4">
      <h4>Admin Reports</h4>
      {reports.length === 0 ? (
        <p>No bookings found.</p>
      ) : (
        <Table striped bordered hover responsive className="mt-3">
          <thead>
            <tr>
              <th>ID</th>
              <th>Booking Time</th>
              <th>Total Amount</th>
              <th>PNR</th>
              <th>Status</th>
              <th>Hold Expires At</th>
              <th>User Name</th>
              <th>Source</th>
              <th>Destination</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody>
            {reports.map((r) => (
              <tr
                key={r.id}
                style={{ backgroundColor: r.status === "CANCELLED" ? "#f8d7da" : "transparent" }}
              >
                <td>{r.id}</td>
                <td>{r.bookingTime || "-"}</td>
                <td>₹{r.totalAmount}</td>
                <td>{r.pnr || "-"}</td>
                <td>{r.status}</td>
                <td>{r.holdExpiresAt || "-"}</td>
                <td>{r.userName || "-"}</td>
                <td>{r.source || "-"}</td>
                <td>{r.destination || "-"}</td>
                <td>
                  {r.status !== "CANCELLED" && (
                    <Button
                      size="sm"
                      variant="danger"
                      onClick={() => cancelBooking(r.id)}
                    >
                      Cancel
                    </Button>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </Table>
      )}
    </Container>
  );
}
