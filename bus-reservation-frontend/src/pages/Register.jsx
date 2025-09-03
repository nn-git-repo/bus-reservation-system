import { Formik, Form, Field, ErrorMessage } from "formik";
import * as Yup from "yup";
import { Button, Card, Container, Alert, InputGroup } from "react-bootstrap";
import { FaEye, FaEyeSlash } from "react-icons/fa";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import API from "../api/axiosConfig";

export default function Register() {
  const [showPassword, setShowPassword] = useState(false);
  const [success, setSuccess] = useState(false);
  const navigate = useNavigate();

  // Yup validation schema
  const validationSchema = Yup.object({
    name: Yup.string().required("Name is required"),
    email: Yup.string().email("Invalid email").required("Email is required"),
    password: Yup.string()
      .min(6, "Password must be at least 6 characters")
      .required("Password is required"),
    phone: Yup.string().matches(/^\d+$/, "Phone must be numbers only").optional(),
  });

  const handleSubmit = async (values, { setSubmitting }) => {
    try {
      await API.post("/auth/register", values);
      setSuccess(true);
    } catch (err) {
      alert(err?.response?.data?.message || "Register failed");
    } finally {
      setSubmitting(false);
    }
  };

  if (success) {
    return (
      <Container className="d-flex align-items-center justify-content-center" style={{ minHeight: "80vh" }}>
        <Card style={{ width: 380, textAlign: "center" }}>
          <Card.Body>
            <Alert variant="success">Registration successful!</Alert>
            <Button onClick={() => navigate("/login")}>Go to Login</Button>
          </Card.Body>
        </Card>
      </Container>
    );
  }

  return (
    <Container className="d-flex align-items-center justify-content-center" style={{ minHeight: "80vh" }}>
      <Card style={{ width: 380 }}>
        <Card.Body>
          <Card.Title>Create account</Card.Title>
          <Formik
            initialValues={{ name: "", email: "", password: "", phone: "" }}
            validationSchema={validationSchema}
            onSubmit={handleSubmit}
          >
            {({ isSubmitting }) => (
              <Form>
                <div className="mb-2">
                  <Field name="name" placeholder="Name" className="form-control" />
                  <ErrorMessage name="name" component="div" className="text-danger" />
                </div>

                <div className="mb-2">
                  <Field name="email" type="email" placeholder="Email" className="form-control" />
                  <ErrorMessage name="email" component="div" className="text-danger" />
                </div>

                <div className="mb-2">
                  <InputGroup>
                    <Field
                      name="password"
                      type={showPassword ? "text" : "password"}
                      placeholder="Password"
                      className="form-control"
                    />
                    <Button
                      variant="outline-secondary"
                      type="button"
                      onClick={() => setShowPassword(!showPassword)}
                    >
                      {showPassword ? <FaEyeSlash /> : <FaEye />}
                    </Button>
                  </InputGroup>
                  <ErrorMessage name="password" component="div" className="text-danger" />
                </div>

                <div className="mb-3">
                  <Field name="phone" placeholder="Phone" className="form-control" />
                  <ErrorMessage name="phone" component="div" className="text-danger" />
                </div>

                <Button type="submit" disabled={isSubmitting}>
                  Register
                </Button>
              </Form>
            )}
          </Formik>
        </Card.Body>
      </Card>
    </Container>
  );
}
