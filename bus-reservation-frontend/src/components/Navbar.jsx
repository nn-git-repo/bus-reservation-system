import { Link, useNavigate } from "react-router-dom";
import { Navbar, Nav, Container, Button, Dropdown } from "react-bootstrap";

export default function AppNavbar() {
  const navigate = useNavigate();
  const token = localStorage.getItem("token");
  const role = localStorage.getItem("role");

  const logout = () => {
    localStorage.clear();
    navigate("/login");
  };

  return (
    <Navbar bg="dark" variant="dark" expand="lg" sticky="top">
      <Container>
        <Navbar.Brand as={Link} to="/">Busify</Navbar.Brand>
        <Navbar.Toggle />
        <Navbar.Collapse className="justify-content-end">
          <Nav className="me-auto">
            {/* Optional left-side links can go here */}
          </Nav>

          <Nav>
            {!token && (
              <Dropdown align="end">
                <Dropdown.Toggle variant="outline-light" id="login-register-dropdown">
                  Login / Register
                </Dropdown.Toggle>

                <Dropdown.Menu>
                  <Dropdown.Item onClick={() => navigate("/login")}>Login</Dropdown.Item>
                  <Dropdown.Item onClick={() => navigate("/register")}>Register</Dropdown.Item>
                </Dropdown.Menu>
              </Dropdown>
            )}

            {token && <Nav.Link as={Link} to="/trips">BusFinder</Nav.Link>}

            {role === "USER" && <Nav.Link as={Link} to="/user">My Bookings</Nav.Link>}
            {role === "ADMIN" && <Nav.Link as={Link} to="/admin">Admin</Nav.Link>}

            {token && (
              <Button variant="outline-light" size="sm" className="ms-2" onClick={logout}>
                Logout
              </Button>
            )}
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
}
