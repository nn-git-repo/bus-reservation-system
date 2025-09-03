import { Container, Card, Button, Row, Col } from "react-bootstrap";
import { useNavigate } from "react-router-dom";

export default function AdminDashboard() {
  const nav = useNavigate();

  const cardStyle = {
    minWidth: "150px",
    textAlign: "center",
    cursor: "pointer",
    transition: "transform 0.2s, box-shadow 0.2s",
  };

  const handleHover = (e) => {
    e.currentTarget.style.transform = "scale(1.05)";
    e.currentTarget.style.boxShadow = "0 8px 20px rgba(0,0,0,0.2)";
  };

  const handleLeave = (e) => {
    e.currentTarget.style.transform = "scale(1)";
    e.currentTarget.style.boxShadow = "0 4px 10px rgba(0,0,0,0.1)";
  };

  return (
    <Container className="mt-5">
      <h3 className="mb-4 text-center">Admin Dashboard</h3>

      <Row className="g-4 justify-content-center">
        {[
          { title: "Buses", path: "/admin/buses" },
          { title: "Routes", path: "/admin/routes" },
          { title: "Trips", path: "/admin/trips" },
          { title: "Reports", path: "/admin/reports" },
        ].map((item, idx) => (
          <Col key={idx} xs={12} sm={6} md={3}>
            <Card
              className="p-3 shadow-sm"
              style={cardStyle}
              onMouseEnter={handleHover}
              onMouseLeave={handleLeave}
            >
              <h5 className="mb-3">{item.title}</h5>
              <Button variant="primary" onClick={() => nav(item.path)}>
                {item.title === "Reports" ? "View" : "Manage"}
              </Button>
            </Card>
          </Col>
        ))}
      </Row>
    </Container>
  );
}
