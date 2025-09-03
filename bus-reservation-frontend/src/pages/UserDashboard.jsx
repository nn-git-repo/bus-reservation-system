import { useEffect, useState } from "react";
import { Container, Table, Button } from "react-bootstrap";
import API from "../api/axiosConfig";

export default function UserDashboard() {
  const [bookings, setBookings] = useState([]);

  const loadBookings = async () => {
    try {
      const { data } = await API.get("/bookings/my");
      console.log("Bookings from API:", data); // check API response
      setBookings(data || []);
    } catch (err) {
      console.error("Failed to load bookings:", err);
      setBookings([]);
    }
  };

  useEffect(() => {
    loadBookings();
  }, []);

  const cancelBooking = async (id) => {
    if (!confirm("Cancel booking?")) return;
    try {
      await API.post(`/bookings/${id}/cancel`);
      alert("Cancelled (refund processed if applicable)");
      loadBookings();
    } catch (err) {
      console.error(err);
      alert("Cancel failed");
    }
  };

  return (
    <Container className="mt-4">
      <h4>My Bookings</h4>
      {bookings.length === 0 ? (
        <p>No bookings found.</p>
      ) : (
        <Table striped bordered hover responsive>
          <thead>
            <tr>
              <th>ID</th>
              <th>Trip</th>
              <th>Seats</th>
              <th>Status</th>
              <th>PNR</th>
              <th>Total Amount</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {bookings.map((b) => (
              <tr key={b.id}>
                <td>{b.id}</td>
                <td>
                  {b.trip?.route?.source} → {b.trip?.route?.destination} <br />
                  Bus: {b.trip?.bus?.busNumber} <br />
                  Departure: {new Date(b.trip?.departureTime).toLocaleString()} <br />
                  Arrival: {new Date(b.trip?.arrivalTime).toLocaleString()}
                </td>
                <td>{(b.seats || []).join(", ")}</td>
                <td>{b.status}</td>
                <td>{b.pnr}</td>
                <td>₹{b.totalAmount}</td>
                <td>
                  <Button
                    size="sm"
                    className="me-2"
                    onClick={() => window.open(`/ticket/${b.id}`)}
                  >
                    View
                  </Button>
                  {b.status !== "CANCELLED" && (
                    <Button
                      size="sm"
                      variant="danger"
                      onClick={() => cancelBooking(b.id)}
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
