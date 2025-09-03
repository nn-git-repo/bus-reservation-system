import { Routes, Route, Navigate } from "react-router-dom";
import "./App.css";
import Navbar from "./components/Navbar";
import LandingPage from "./pages/LandingPage";
import Login from "./pages/Login";
import Register from "./pages/Register";
import Trips from "./pages/Trips";
import SeatSelection from "./pages/SeatSelection";
import Checkout from "./pages/Checkout";
import Payment from "./pages/Payment";
import Ticket from "./pages/Ticket";
import UserDashboard from "./pages/UserDashboard";
import AdminDashboard from "./pages/AdminDashboard";
import ManageBuses from "./pages/ManageBuses";
import ManageRoutes from "./pages/ManageRoutes";
import ManageTrips from "./pages/ManageTrips";
import AdminReports from "./pages/AdminReports";
import ProtectedRoute from "./components/ProtectedRoute";

export default function App() {
  return (
    <>
      <Navbar />
      <Routes>
        {/* Landing page */}
        <Route path="/" element={<LandingPage />} />

        {/* Public login/register */}
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />

        {/* BusFinder / Trips page */}
        <Route path="/trips" element={<Trips />} />

        {/* Protected Routes */}
        <Route path="/seat/:tripId" element={
          <ProtectedRoute allowed={["USER","ADMIN"]}>
            <SeatSelection />
          </ProtectedRoute>
        }/>
        <Route path="/checkout" element={
          <ProtectedRoute allowed={["USER"]}>
            <Checkout />
          </ProtectedRoute>
        }/>
        <Route path="/payment" element={
          <ProtectedRoute allowed={["USER"]}>
            <Payment />
          </ProtectedRoute>
        }/>
        <Route path="/ticket/:bookingId" element={
          <ProtectedRoute allowed={["USER","ADMIN"]}>
            <Ticket />
          </ProtectedRoute>
        }/>
        <Route path="/user" element={
          <ProtectedRoute allowed={["USER"]}>
            <UserDashboard />
          </ProtectedRoute>
        }/>
        <Route path="/admin" element={
          <ProtectedRoute allowed={["ADMIN"]}>
            <AdminDashboard />
          </ProtectedRoute>
        }/>
        <Route path="/admin/buses" element={
          <ProtectedRoute allowed={["ADMIN"]}>
            <ManageBuses />
          </ProtectedRoute>
        }/>
        <Route path="/admin/routes" element={
          <ProtectedRoute allowed={["ADMIN"]}>
            <ManageRoutes />
          </ProtectedRoute>
        }/>
        <Route path="/admin/trips" element={
          <ProtectedRoute allowed={["ADMIN"]}>
            <ManageTrips />
          </ProtectedRoute>
        }/>
        <Route path="/admin/reports" element={
  <ProtectedRoute allowed={["ADMIN"]}>
    <AdminReports />
  </ProtectedRoute>
}/>


        {/* Catch-all */}
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </>
  );
}
