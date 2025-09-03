import { useEffect, useState } from "react";
import { Container, Table, Alert } from "react-bootstrap";
import API from "../api/axiosConfig";

export default function Reports(){
  const [summary,setSummary]=useState(null);
  useEffect(()=> {
    (async ()=> {
      try {
        const { data } = await API.get("/admin/reports/summary");
        setSummary(data);
      } catch {
        setSummary(null);
      }
    })();
  },[]);

  if (!summary) return <Container className="mt-4"><Alert variant="secondary">Reports endpoint not available</Alert></Container>;

  return (
    <Container className="mt-4">
      <h4>Reports</h4>
      <Table bordered>
        <tbody>
          <tr><th>Total Trips</th><td>{summary.totalTrips}</td></tr>
          <tr><th>Total Bookings</th><td>{summary.totalBookings}</td></tr>
          <tr><th>Total Revenue</th><td>₹{summary.totalRevenue}</td></tr>
        </tbody>
      </Table>
    </Container>
  );
}
