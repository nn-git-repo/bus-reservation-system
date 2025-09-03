import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import API from "../api/axiosConfig";
import { Container, Card, Button, Table } from "react-bootstrap";

export default function Checkout(){
  const navigate = useNavigate();
  const [trip, setTrip] = useState(null);
  const [seats,setSeats] = useState([]);
  const [booking,setBooking] = useState(null);

  useEffect(()=>{
    const t = sessionStorage.getItem("selectedTrip");
    if (t) setTrip(JSON.parse(t));
    const s = JSON.parse(sessionStorage.getItem("selectedSeats") || "[]");
    setSeats(s);
  },[]);

  const total = (trip?.baseFare || trip?.fare || 0) * seats.length;

  const holdSeats = async () => {
    try {
      const tripId = Number(sessionStorage.getItem("selectedTripId"));
      const payload = { tripId, seats, seatCount: seats.length }; 
      const { data } = await API.post("/bookings/hold", payload);

      const bookingId = data?.id || data?.bookingId || data?.booking_id;
      if (!bookingId) throw new Error("No booking id returned");

      // ✅ Save bookingId + total amount in sessionStorage
      sessionStorage.setItem("bookingId", bookingId);
      sessionStorage.setItem("amount", total);

      setBooking(data);
      alert("Seats held. Proceed to payment.");
      navigate("/payment");
    } catch (e) {
      alert(e?.response?.data?.message || e.message || "Hold failed");
    }
  };

  if (!trip) return <Container className="mt-4"><p>Loading...</p></Container>;

  return (
    <Container className="mt-4">
      <h4>Checkout</h4>
      <Card className="p-3" style={{maxWidth:640}}>
        <Table borderless>
          <tbody>
            <tr><th>Route</th><td>{trip?.route?.source} → {trip?.route?.destination}</td></tr>
            <tr><th>Departure</th><td>{trip?.departureTime}</td></tr>
            <tr><th>Seats</th><td>{seats.join(", ")}</td></tr>
            <tr><th>Total</th><td>₹{total}</td></tr>
          </tbody>
        </Table>
        <Button onClick={holdSeats}>Hold & Continue to Payment</Button>
      </Card>
    </Container>
  );
}
