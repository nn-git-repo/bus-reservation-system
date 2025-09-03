import { Navigate } from "react-router-dom";

/**
 * allowed: array of roles like ["USER"], ["ADMIN"], or empty for any authenticated.
 */
export default function ProtectedRoute({ allowed = [], children }) {
  const token = localStorage.getItem("token");
  const role = localStorage.getItem("role");

  if (!token) return <Navigate to="/login" replace />;
  if (allowed.length && !allowed.includes(role)) return <Navigate to="/" replace />;
  return children;
}
