
import { Container, Button, Row, Col, Card } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import "./LandingPage.css";

export default function LandingPage() {
  const navigate = useNavigate();

  return (
    <>
      {/* Hero Section */}
      <Container className="landing-page">
        <h1>Welcome to Busify!</h1>
        <p>Find your bus easily and travel comfortably.</p>
        
        
      </Container>

      {/* About Us Section */}
      <Container className="my-5">
        <h2>About Us</h2>
        <p>
          Busify is your ultimate online bus ticketing platform. We connect travelers with multiple bus operators, making your journey faster, convenient, and hassle-free. Whether you travel for business or leisure, we ensure a comfortable ride.
        </p>
      </Container>

      {/* Contact Us Section */}
      <Container className="my-5">
        <h2>Contact Us</h2>
        <Row>
          <Col md={6}>
            <Card className="p-3 mb-3">
              <h5>Address</h5>
              <p>123 Jaynagr, Bengaluru, Karnataka</p>
              <p>560041</p>
            </Card>
          </Col>
          <Col md={6}>
            <Card className="p-3 mb-3">
              <h5>Email & Phone</h5>
              <p>Email: support@busify.com</p>
              <p>Phone: +91 9876543210</p>
            </Card>
          </Col>
        </Row>
      </Container>

      {/* Footer */}
      <footer className="text-center py-3 bg-dark text-white">
        &copy; {new Date().getFullYear()} Busify. All rights reserved.
      </footer>
    </>
  );
}
