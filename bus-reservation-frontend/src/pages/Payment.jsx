import { useEffect, useState } from "react";
import { Container, Card, Button, Form } from "react-bootstrap";
import API from "../api/axiosConfig";
import { useNavigate } from "react-router-dom";

export default function Payment(){
  const navigate = useNavigate();
  const [bookingId, setBookingId] = useState("");
  const [amount, setAmount] = useState(0);
  const [mode, setMode] = useState("UPI");

  useEffect(()=>{
    const storedBookingId = sessionStorage.getItem("bookingId");
    const storedAmount = sessionStorage.getItem("amount");

    if (storedBookingId) setBookingId(storedBookingId);
    if (storedAmount) setAmount(Number(storedAmount));

    console.log("BookingId:", storedBookingId);
    console.log("Amount:", storedAmount);
  },[]);

  const checkout = async () => {
    if (!bookingId) { alert("Missing bookingId"); return; }
    try {
      const { data } = await API.post(`/payments/checkout/${bookingId}`, { amount, mode });

      try { 
        await API.post(`/bookings/${bookingId}/confirm`); 
      } catch(e){ /* ignore confirm errors */ }

      alert("Payment successful");
      navigate(`/ticket/${bookingId}`);
    } catch (e) {
      alert(e?.response?.data?.message || "Payment failed");
    }
  };

  return (
    <Container className="d-flex justify-content-center align-items-center" style={{ minHeight: "80vh" }}>
      <Card 
        className="p-4 shadow-lg" 
        style={{ maxWidth: 480, width: "100%", borderRadius: "12px", backgroundColor: "#fdfdfd", border: "1px solid #e0e0e0" }}
      >
        <h3 className="text-center mb-4" style={{ color: "#0d6efd", fontWeight: "600" }}>Payment</h3>
        <Form>
          <Form.Group className="mb-3">
            <Form.Label style={{ fontWeight: "500" }}>Booking ID</Form.Label>
            <Form.Control value={bookingId} readOnly style={{ borderRadius: "8px" }}/>
          </Form.Group>
          <Form.Group className="mb-3">
            <Form.Label style={{ fontWeight: "500" }}>Amount</Form.Label>
            <Form.Control value={amount} readOnly style={{ borderRadius: "8px" }}/>
          </Form.Group>
          <Form.Group className="mb-4">
            <Form.Label style={{ fontWeight: "500" }}>Mode</Form.Label>
            <Form.Select value={mode} onChange={e=>setMode(e.target.value)} style={{ borderRadius: "8px" }}>
              <option value="UPI">UPI</option>
              <option value="CARD">Card</option>
              <option value="NETBANKING">Netbanking</option>
            </Form.Select>
          </Form.Group>
          <Button 
            onClick={checkout} 
            className="w-100" 
            style={{ backgroundColor: "#0d6efd", border: "none", padding: "10px", fontWeight: "500", borderRadius: "8px", fontSize: "1rem" }}
          >
            Pay & Confirm
          </Button>
        </Form>
      </Card>
    </Container>
  );
}
