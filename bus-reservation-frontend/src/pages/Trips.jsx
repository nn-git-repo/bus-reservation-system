import { useState } from "react";
import API from "../api/axiosConfig";
import { Container, Row, Col, Button, Form, Card, ListGroup } from "react-bootstrap";
import { useNavigate } from "react-router-dom";

export default function Trips() {
  const [source, setSource] = useState("");
  const [destination, setDestination] = useState("");
  const [sourceSuggestions, setSourceSuggestions] = useState([]);
  const [destSuggestions, setDestSuggestions] = useState([]);
  const [date, setDate] = useState("");
  const [trips, setTrips] = useState([]);
  const navigate = useNavigate();

  // List of cities for autocomplete
  const cities = ["Hyderabad", "Bengaluru", "Chennai", "Mumbai", "Delhi", "Kolkata", "Pune", "Jaipur", "Ahmedabad", "Kochi"];

  // Handle source input change
  const handleSourceChange = (value) => {
    setSource(value);
    setSourceSuggestions(cities.filter(city => city.toLowerCase().startsWith(value.toLowerCase())));
  };

  // Handle destination input change
  const handleDestinationChange = (value) => {
    setDestination(value);
    setDestSuggestions(cities.filter(city => city.toLowerCase().startsWith(value.toLowerCase())));
  };

  const selectSource = (city) => {
    setSource(city);
    setSourceSuggestions([]);
  };

  const selectDestination = (city) => {
    setDestination(city);
    setDestSuggestions([]);
  };

  // Search trips
  const search = async () => {
    if (!source || !destination || !date) {
      alert("Fill source, destination, date");
      return;
    }
    try {
      const { data } = await API.get(`/trips/search?source=${encodeURIComponent(source)}&destination=${encodeURIComponent(destination)}&date=${date}`);
      setTrips(data || []);
    } catch (err) {
      alert("Search failed");
    }
  };

  const selectSeats = (trip) => {
    sessionStorage.setItem("selectedTrip", JSON.stringify(trip));
    navigate(`/seat/${trip.id}`);
  };

  return (
    <Container className="mt-4">
      <h3>Search Trips</h3>
      <Row className="mb-3 g-2">
        <Col md={3}>
          <Form.Control
            value={source}
            onChange={e => handleSourceChange(e.target.value)}
            placeholder="Source"
            autoComplete="off"
          />
          {sourceSuggestions.length > 0 && (
            <ListGroup>
              {sourceSuggestions.map(city => (
                <ListGroup.Item key={city} action onClick={() => selectSource(city)}>
                  {city}
                </ListGroup.Item>
              ))}
            </ListGroup>
          )}
        </Col>

        <Col md={3}>
          <Form.Control
            value={destination}
            onChange={e => handleDestinationChange(e.target.value)}
            placeholder="Destination"
            autoComplete="off"
          />
          {destSuggestions.length > 0 && (
            <ListGroup>
              {destSuggestions.map(city => (
                <ListGroup.Item key={city} action onClick={() => selectDestination(city)}>
                  {city}
                </ListGroup.Item>
              ))}
            </ListGroup>
          )}
        </Col>

        <Col md={3}>
          <Form.Control type="date" value={date} onChange={e => setDate(e.target.value)} />
        </Col>

        <Col md={3}>
          <Button onClick={search}>Search</Button>
        </Col>
      </Row>

      <Row>
        {trips.map(t => (
          <Col md={4} key={t.id}>
            <Card className="mb-3">
              <Card.Body>
                <Card.Title>{t?.route?.source} → {t?.route?.destination}</Card.Title>
                <Card.Text>
                  Departure: {t.departureTime}<br />
                  Fare: ₹{t.baseFare}
                </Card.Text>
                <Button onClick={() => selectSeats(t)}>Select Seats</Button>
              </Card.Body>
            </Card>
          </Col>
        ))}
        {!trips.length && <p className="text-muted">  Welcome! Search you trips here. Enter your departure city and destination, 
            then click "Search" to find available buses. Make sure to double-check your travel date</p>}
      </Row>
    </Container>
  );
}
