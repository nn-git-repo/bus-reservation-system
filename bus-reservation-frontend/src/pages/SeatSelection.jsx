import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import API from "../api/axiosConfig";
import { Container, Card, Button } from "react-bootstrap";

export default function SeatSelection(){
  const { tripId } = useParams();
  const navigate = useNavigate();
  const [trip, setTrip] = useState(null);
  const [taken, setTaken] = useState([]); // taken seat numbers
  const [selected, setSelected] = useState([]);

  useEffect(()=> {
    const sTrip = sessionStorage.getItem("selectedTrip");
    if (sTrip) setTrip(JSON.parse(sTrip));
    // fetch taken seats from backend
    (async ()=> {
      try {
        const { data } = await API.get(`/bookings/${tripId}/taken-seats`);
        setTaken(Array.isArray(data) ? data : []);
      } catch (e) {
        // ignore
      }
    })();
  }, [tripId]);

  if (!trip) return <Container className="mt-4"><p>Loading trip...</p></Container>;

  const capacity = (trip?.bus?.totalSeats) || (trip?.bus?.capacity) || 40;
  const seatNumbers = Array.from({length: capacity}, (_,i)=>i+1);

  const toggle = (n) => {
    if (taken.includes(n)) return;
    setSelected(prev => prev.includes(n) ? prev.filter(x=>x!==n) : [...prev, n]);
  };

  const proceed = () => {
    if (!selected.length) { alert("Select seats"); return; }
    sessionStorage.setItem("selectedSeats", JSON.stringify(selected));
    sessionStorage.setItem("selectedTripId", tripId);
    navigate("/checkout");
  };

  return (
    <Container className="mt-4">
      <h4>Seat Selection — Trip #{tripId}</h4>
      <p>{trip?.route?.source} → {trip?.route?.destination} | {trip?.departureTime}</p>
      <Card className="p-3 mb-3">
        <div className="seat-grid">
          {seatNumbers.map(n=>{
            const isTaken = taken.includes(String(n)) || taken.includes(n);
            const isSel = selected.includes(n);
            const cls = "seat " + (isTaken ? "booked" : isSel ? "selected" : "");
            return <div key={n} className={cls} onClick={()=>toggle(n)}>{n}</div>;
          })}
        </div>
      </Card>
      <p>Selected: {selected.join(", ") || "-"}</p>
      <Button onClick={proceed}>Proceed</Button>
    </Container>
  );
}
