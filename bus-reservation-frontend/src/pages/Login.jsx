import { useState } from "react";
import { Container, Card, Form, Button, Row, Col } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import API from "../api/axiosConfig";

export default function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const submit = async (e) => {
    e.preventDefault();
    try {
      setLoading(true);
      const { data } = await API.post("/auth/login", { email, password });
      const token = data.token || data.accessToken || data.jwt || data.access_token;
      const role = (data.user && data.user.role) || data.role || data.userRole || data.roleName;
      if (!token) throw new Error("No token returned");
      localStorage.setItem("token", token);
      localStorage.setItem("role", role || "USER");
      if ((role || "USER") === "ADMIN") navigate("/admin");
      else navigate("/user");
    } catch (err) {
      alert(err?.response?.data?.message || err.message || "Login failed");
    } finally {
      setLoading(false);
    }
  };

  return (
    <Container className="d-flex align-items-center justify-content-center" style={{ minHeight: "80vh" }}>
      <Card style={{ width: 420 }}>
        <Card.Body>
          <Card.Title className="mb-3">Login</Card.Title>
          <Form onSubmit={submit}>
            <Form.Group className="mb-2">
              <Form.Label>Email</Form.Label>
              <Form.Control type="email" value={email} onChange={e => setEmail(e.target.value)} required />
            </Form.Group>
            <Form.Group className="mb-2">
              <Form.Label>Password</Form.Label>
              <Form.Control type="password" value={password} onChange={e => setPassword(e.target.value)} required />
            </Form.Group>
            <Row className="mt-3">
              <Col>
                <Button type="submit" disabled={loading} className="w-100">
                  {loading ? "Signing in..." : "Login"}
                </Button>
              </Col>
              <Col>
                <Button variant="secondary" className="w-100" onClick={() => navigate("/register")}>
                  Register
                </Button>
              </Col>
            </Row>
          </Form>
        </Card.Body>
      </Card>
    </Container>
  );
}
